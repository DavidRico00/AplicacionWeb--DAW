package controllers;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Comentario;
import models.Producto;
import models.Usuario;

@MultipartConfig
@WebServlet(name = "ProductoController", urlPatterns = {"/nuevoproducto/*", "/producto/*"})
public class ProductoController extends HttpServlet {

    @PersistenceContext(unitName = "PortalVentasPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private static final Logger log = Logger.getLogger(controllers.PrincipalController.class.getName());

    private final String HOST = "localhost";
    //private final String HOST = "192.168.1.161";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = "";
        String servlet = request.getServletPath(), info = request.getPathInfo();
        HttpSession session = request.getSession();

        switch (servlet) {

            case "/producto" -> {
                long id = Long.parseLong(request.getParameter("id"));

                Producto prod = em.find(Producto.class, id);
                request.setAttribute("id", session.getAttribute("id"));
                request.setAttribute("producto", prod);

                vista = "verproducto";
            }

            case "/nuevoproducto" -> {
                if (session.getAttribute("id") == null) {
                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");
                    break;
                }

                request.setAttribute("id", session.getAttribute("id"));
                vista = "nuevoproducto";
            }

            default -> {
                vista = "error";
            }
        }

        if (!vista.equals("")) {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vista + ".jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = "";
        String servlet = request.getServletPath(), info = request.getPathInfo();
        HttpSession session = request.getSession();

        switch (servlet) {

            case "/nuevoproducto" -> {

                if (session.getAttribute("id") == null) {
                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");
                    return;
                }

                if (info.equals("/save")) {
                    String titulo = request.getParameter("titulo");
                    String descripcion = request.getParameter("descripcion");
                    Part imgPart = request.getPart("imagen");
                    String rutaImg;

                    if (titulo.isEmpty() || descripcion.isEmpty() || imgPart == null) {
                        throw new NullPointerException();
                    }

                    Usuario user = em.find(Usuario.class, (long) session.getAttribute("id"));

                    int numProds = user.getSizeOfProductos();
                    numProds++;

                    rutaImg = "img" + File.separator + "productos" + File.separator + user.getId() + "_" + numProds + ".jpg";

                    Producto prod = new Producto(titulo, descripcion, rutaImg, user);

                    try {
                        utx.begin();
                        em.persist(prod);

                        List<Producto> prods = user.getProductos();
                        prods.add(prod);

                        em.merge(user);

                        String relativePathFolder = "" + File.separator + "img" + File.separator + "productos";
                        String absolutePathFolder = getServletContext().getRealPath(relativePathFolder);
                        File f = new File(absolutePathFolder + File.separator + user.getId() + "_" + numProds + ".jpg");
                        OutputStream fos = new FileOutputStream(f);
                        InputStream filecontent = imgPart.getInputStream();
                        int read = 0;
                        final byte[] bytes = new byte[1024];
                        while ((read = filecontent.read(bytes)) != -1) {
                            fos.write(bytes, 0, read);
                        }

                        fos.close();
                        filecontent.close();

                        utx.commit();

                    } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | SecurityException ex) {
                        Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                        request.setAttribute("msg", "Error antes rollback: " + ex.getMessage());
                        vista = "error";
                        try {
                            utx.rollback();
                        } catch (IllegalStateException | SecurityException | SystemException ex1) {
                            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex1);
                            request.setAttribute("msg", "Error despues del rollback: " + ex.getMessage());
                            vista = "error";
                        }
                    }

                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");

                } else {
                    vista = "error";
                }
            }

            case "/producto" -> {
                if (session.getAttribute("id") == null) {
                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");
                    return;
                }

                if (info.equals("/addcomment")) {

                    long idProd = Long.parseLong(request.getParameter("id"));
                    String texto = request.getParameter("comentario");

                    Producto prod = em.find(Producto.class, idProd);

                    List<Comentario> comments = prod.getComentarios();
                    Comentario comentario = new Comentario(texto, prod);
                    comments.add(comentario);
                    prod.setComentarios(comments);

                    try {
                        utx.begin();

                        em.persist(comentario);
                        em.merge(prod);

                        utx.commit();

                    } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                        Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            utx.rollback();
                        } catch (IllegalStateException | SecurityException | SystemException ex1) {
                            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }

                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/producto?id=" + idProd);

                } else {
                    vista = "error";
                }
            }

            default -> {
                vista = "error";
            }
        }

        if (!vista.equals("")) {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vista + ".jsp");
            rd.forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

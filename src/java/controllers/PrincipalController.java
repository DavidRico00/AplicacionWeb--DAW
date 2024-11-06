package controllers;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
import models.Producto;
import models.Usuario;

@MultipartConfig
@WebServlet(name = "PrincipalController", urlPatterns = {"/inicio", "/perfil/*", "/nuevoproducto/*"})
public class PrincipalController extends HttpServlet {

    @PersistenceContext(unitName = "PortalVentasPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private static final Logger log = Logger.getLogger(controllers.LogRegController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista;
        String servlet = request.getServletPath();
        HttpSession session;

        switch (servlet) {
            case "/inicio" -> {
                TypedQuery<Producto> query = em.createNamedQuery("Producto.findAll", Producto.class);
                List<Producto> lista = query.getResultList();
                request.setAttribute("products", lista);

                session = request.getSession();
                if (session.getAttribute("id") != null) {
                    request.setAttribute("id", session.getAttribute("id"));
                }

                vista = "inicio";
            }

            case "/nuevoproducto" -> {
                session = request.getSession();
                if (session.getAttribute("id") != null) {
                    request.setAttribute("id", session.getAttribute("id"));
                }

                vista = "nuevoproducto";
            }

            default -> {
                vista = "error";
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vista + ".jsp");
        rd.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = "";
        String servlet = request.getServletPath(), info = request.getPathInfo();
        HttpSession session;
        final String HOST = "localhost";

        switch (servlet) {
            case "/nuevoproducto" -> {
                if (info.equals("/save")) {
                    String titulo = request.getParameter("titulo");
                    String descripcion = request.getParameter("descripcion");
                    Part imgPart = request.getPart("imagen");
                    String rutaImg = "";

                    if (titulo.isEmpty() || descripcion.isEmpty() || imgPart == null) {
                        throw new NullPointerException();
                    }

                    session = request.getSession();
                    long id = (long) session.getAttribute("id");

                    TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class);
                    query.setParameter("id", id);
                    Usuario user = query.getSingleResult();

                    int numProds = user.getSizeOfProductos();
                    numProds++;

                    String relativePathFolder = "" + File.separator + "img" + File.separator + "productos/";
                    
                    rutaImg = relativePathFolder + getFileName(imgPart) + numProds + ".jpg";
                    Producto prod = new Producto(titulo, descripcion, rutaImg, user);
                    
                    try {
                        utx.begin();
                        em.persist(prod);
                        utx.commit();

                    } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                        Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            utx.rollback();
                        } catch (IllegalStateException | SecurityException | SystemException ex1) {
                            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }

                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");
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

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String fileName = null;

        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                break;
            }
        }
        if (fileName != null && fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        
        return fileName;
    }
}

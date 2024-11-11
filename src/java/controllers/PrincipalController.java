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
import models.Comentario;
import models.Producto;
import models.Usuario;

@MultipartConfig
@WebServlet(name = "PrincipalController", urlPatterns = {"/inicio", "/perfil/*", "/nuevoproducto/*", "/producto/*"})
public class PrincipalController extends HttpServlet {

    @PersistenceContext(unitName = "PortalVentasPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private static final Logger log = Logger.getLogger(controllers.LogRegController.class.getName());

    private final String HOST = "localhost";
    //private final String HOST = "192.168.1.161";

    //response.sendRedirect("http://" + HOST + ":8080/PortalVentas/");
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista;
        String servlet = request.getServletPath(), info = request.getPathInfo();
        HttpSession session;

        switch (servlet) {
            case "/inicio" -> {
                String palabra = request.getParameter("query");
                List<Producto> lista;

                if (palabra != null) {
                    TypedQuery<Producto> query = em.createNamedQuery("Producto.findByWord", Producto.class);
                    query.setParameter("palabra", "%" + palabra + "%");
                    lista = query.getResultList();
                } else {
                    TypedQuery<Producto> query = em.createNamedQuery("Producto.findAll", Producto.class);
                    lista = query.getResultList();
                }
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
                    vista = "nuevoproducto";

                } else {
                    vista = "error";
                }
            }

            case "/perfil" -> {
                session = request.getSession();

                if (session.getAttribute("id") != null) {
                    TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findById", Usuario.class);
                    query.setParameter("id", (long) session.getAttribute("id"));
                    List<Usuario> lista = query.getResultList();

                    request.setAttribute("id", session.getAttribute("id"));
                    request.setAttribute("user", lista.get(0));

                    if (session.getAttribute("msg") != null) {
                        request.setAttribute("msg", session.getAttribute("msg"));
                        session.removeAttribute("msg");
                    }

                    vista = "perfil";

                } else {
                    vista = "error";
                }
            }

            case "/producto" -> {
                long id = Long.parseLong(request.getParameter("id"));

                Producto prod = em.find(Producto.class, id);

                session = request.getSession();
                request.setAttribute("id", session.getAttribute("id"));
                request.setAttribute("producto", prod);

                vista = "verproducto";
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
        HttpSession session;

        switch (servlet) {
            case "/nuevoproducto" -> {
                if (info.equals("/save")) {
                    String titulo = request.getParameter("titulo");
                    String descripcion = request.getParameter("descripcion");
                    Part imgPart = request.getPart("imagen");
                    String rutaImg;

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

                    rutaImg = "img" + File.separator + "productos" + File.separator + getFileName(imgPart) + "_" + user.getId() + "_" + numProds + ".jpg";

                    Producto prod = new Producto(titulo, descripcion, rutaImg, user);

                    try {
                        utx.begin();
                        em.persist(prod);

                        String relativePathFolder = "" + File.separator + "img" + File.separator + "productos";
                        String absolutePathFolder = getServletContext().getRealPath(relativePathFolder);
                        File f = new File(absolutePathFolder + File.separator + getFileName(imgPart) + "_" + user.getId() + "_" + numProds + ".jpg");
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

            case "/perfil" -> {
                if (info.equals("/save")) {
                    String nombre = request.getParameter("nombre");
                    String email = request.getParameter("email");

                    if (nombre.isEmpty() || email.isEmpty()) {
                        throw new NullPointerException();
                    }

                    session = request.getSession();
                    long id = (long) session.getAttribute("id");
                    Usuario user = em.find(Usuario.class, id);

                    user.setName(nombre);
                    user.setEmail(email);
                    try {

                        utx.begin();
                        em.merge(user);
                        utx.commit();

                    } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                        Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            utx.rollback();
                        } catch (IllegalStateException | SecurityException | SystemException ex1) {
                            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }

                    session.setAttribute("msg", "Usuario actualizado con exito");
                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/perfil");

                } else {
                    vista = "error";
                }
            }

            case "/producto" -> {
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

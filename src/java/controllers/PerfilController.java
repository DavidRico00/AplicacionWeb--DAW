package controllers;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Comentario;
import models.Producto;
import models.Usuario;

@WebServlet(name = "PerfilController", urlPatterns = {"/perfil/*"})
public class PerfilController extends HttpServlet {

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
        String vista;
        String servlet = request.getServletPath(), info = request.getPathInfo();
        HttpSession session;

        switch (servlet) {
            
            case "/perfil" -> {
                session = request.getSession();
                request.setAttribute("id", session.getAttribute("id"));

                Usuario user = em.find(Usuario.class, (long) session.getAttribute("id"));

                if (session.getAttribute("msg") != null) {
                    request.setAttribute("msg", session.getAttribute("msg"));
                    session.removeAttribute("msg");
                }

                if (info != null && info.equals("/productos")) {
                    request.setAttribute("productos", user.getProductos());

                    vista = "misproductos";

                } else if (info != null && info.equals("/productos/modificar")) {

                    long idProd = Long.parseLong(request.getParameter("idProd"));
                    Producto prod = em.find(Producto.class, idProd);

                    request.setAttribute("producto", prod);

                    vista = "modificarproducto";

                } else {
                    request.setAttribute("user", user);
                    vista = "perfil";
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = "";
        String servlet = request.getServletPath(), info = request.getPathInfo();
        HttpSession session;

        switch (servlet) {
            
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

                } else if (info.equals("/productos/delete")) {

                    long idProd = Long.parseLong(request.getParameter("idProd"));
                    Producto prod = em.find(Producto.class, idProd);

                    session = request.getSession();
                    Usuario user = em.find(Usuario.class, (long) session.getAttribute("id"));
                    System.out.println("ProdID  " + prod.getId());

                    try {
                        utx.begin();
                        if (!em.contains(prod)) {
                            prod = em.merge(prod);
                        }

                        TypedQuery<Comentario> query = em.createNamedQuery("Comentario.findAllByIdProd", Comentario.class);
                        query.setParameter("idProd", prod);
                        List<Comentario> listaCom = query.getResultList();
                        for (Comentario com : listaCom) {
                            em.remove(com);
                        }

                        em.remove(prod);
                        user.getProductos().remove(prod);
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

                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/perfil/productos");

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

}

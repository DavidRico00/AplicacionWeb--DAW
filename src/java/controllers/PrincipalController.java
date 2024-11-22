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
import models.Producto;
import models.Usuario;

@WebServlet(name = "PrincipalController", urlPatterns = {"/login/*", "/register/*", "/inicio", "/error"})
public class PrincipalController extends HttpServlet {

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
        HttpSession session;

        switch (servlet) {

            case "/login" -> {
                if (info!=null && info.equals("/logout")) {
                    session = request.getSession();
                    session.removeAttribute("id");
                    session.invalidate();

                    response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");

                } else {
                    vista = "login";
                }
            }

            case "/register" -> {
                vista = "register";
            }

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

            case "/login" -> {
                if (info.equals("/check")) {
                    String email = request.getParameter("email");
                    String pw = request.getParameter("password");

                    try {
                        if (email.isEmpty() || pw.isEmpty()) {
                            throw new NullPointerException();
                        }

                        Usuario user = validarLogin(email, pw);

                        if (user != null) {
                            session = request.getSession();
                            session.setAttribute("id", user.getId());

                            response.sendRedirect("http://" + HOST + ":8080/PortalVentas/inicio");

                        } else {
                            request.setAttribute("msg", "Error: email o contraseña erroneos");
                            vista = "login";
                        }

                    } catch (IOException | NullPointerException e) {
                        request.setAttribute("msg", "Error: " + e.getMessage());
                        vista = "error";
                    }

                }
            }

            case "/register" -> {
                if (info.equals("/save")) {
                    String name = request.getParameter("name");
                    String email = request.getParameter("email");
                    String pw = request.getParameter("password");

                    try {
                        if (name.isEmpty() || email.isEmpty() || pw.isEmpty()) {
                            throw new NullPointerException();
                        }

                        Usuario user = new Usuario(name, email, pw);
                        if (save(user)) {
                            response.sendRedirect("http://" + HOST + ":8080/PortalVentas/login");
                        } else {
                            request.setAttribute("msg", "Warning: Ya existe un usuario con ese email");
                            vista = "register";
                        }

                    } catch (IOException | NullPointerException e) {
                        request.setAttribute("msg", "Error: " + e.getMessage());
                        vista = "error";
                    }
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

    private boolean save(Usuario user) {
        boolean bool = false;

        try {
            utx.begin();

            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmail", Usuario.class);
            query.setParameter("email", user.getEmail());

            if (query.getResultList().isEmpty()) {
                em.persist(user);
                log.log(Level.INFO, "New user saved");
                utx.commit();
                bool = true;

            } else {
                utx.rollback();
                log.log(Level.INFO, "Un usuario se ha intentado registrar con un correo ya registrado");
            }

        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException e) {
            log.log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }

        return bool;
    }

    private Usuario validarLogin(String email, String pass) {
        Usuario user = null;

        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmailAndPassword", Usuario.class);
            query.setParameter("email", email);
            query.setParameter("password", pass);
            List<Usuario> lista = query.getResultList();

            if (!lista.isEmpty()) {
                user = lista.get(0);
            } else {
                log.log(Level.INFO, "Un usuario ha hecho login erroneamente");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }

        return user;
    }

}

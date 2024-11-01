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
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.security.MessageDigest;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import models.Usuario;

@WebServlet(name = "LogRegController", urlPatterns = {"/login/*", "/register/*"})
public class LogRegController extends HttpServlet {

    @PersistenceContext(unitName = "PortalVentasPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private static final Logger log = Logger.getLogger(controllers.LogRegController.class.getName());

    private final String direccion = "http://localhost:8080/PortalVentas";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista, servlet = request.getServletPath();

        switch (servlet) {
            case "/login" -> {
                vista = "login";
            }

            case "/register" -> {
                vista = "register";
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

        switch (servlet) {
            case "/login" -> {
                if (info.equals("/check")) {
                    String email = request.getParameter("email");
                    String pw = request.getParameter("password");

                    try {
                        if (email.isEmpty() || pw.isEmpty()) {
                            throw new NullPointerException();
                        }

                        long loginID = validarLogin(email, pw);

                        if (loginID != -1) {
                            response.sendRedirect(direccion + "/inicio");
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

                        Usuario user = new Usuario(name, email, pw, false);
                        if (save(user)) {
                            response.sendRedirect(direccion + "/login");
                        } else {
                            request.setAttribute("msg", "Warning: Ya existe un usuario con ese email");
                            vista = "register";
                        }

                    } catch (IOException | NullPointerException e) {
                        request.setAttribute("msg", e.getMessage());
                        vista = "error";
                    }
                }
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

    private long validarLogin(String email, String pass) {
        long id = -1;

        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmailAndPassword", Usuario.class);
            query.setParameter("email", email);
            query.setParameter("password", pass);
            List<Usuario> lista = query.getResultList();

            if (!lista.isEmpty()) {
                id = lista.get(0).getId();
            } else {
                log.log(Level.INFO, "Un usuario ha hecho login erroneamente");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }

        return id;
    }
}

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
    
    private String redireccion = "http://localhost:8080";
    
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
        String vista, servlet = request.getServletPath();
        String info = request.getPathInfo();

        switch (servlet) {
            case "/login" -> {
                if (info.equals("/check")) {
                    String email = request.getParameter("email");
                    String pw = request.getParameter("password");
                    
                    try {
                        if (email.isEmpty() || pw.isEmpty()) {
                            throw new NullPointerException();
                        }

                        byte[] pass = cifra(pw);

                        long loginID = validarLogin(email, pass);
                        
                        if(loginID != -1){
                            response.sendRedirect(redireccion+"/PortalVentas/principal");
                        } else {
                            
                        }

                    } catch (Exception e) {
                        request.setAttribute("msg", "Error: datos no válidos");
                        vista = "error";
                        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vista + ".jsp");
                        rd.forward(request, response);
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

                        byte[] pass = cifra(pw);

                        Usuario user = new Usuario(name, email, pass, false);
                        if (save(user)) {
                            vista = "preInicio/login";
                        } else {
                            vista = "preInicio/register";
                            request.setAttribute("msg", "Ya existe un usuario con ese email");
                        }
                        
                        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vista + ".jsp");
                        rd.forward(request, response);

                    } catch (Exception e) {
                        request.setAttribute("msg", "Error: datos no válidos");
                        vista = "error";
                        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/" + vista + ".jsp");
                        rd.forward(request, response);
                    }
                }
            }

        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    private boolean save(Usuario user) {
        boolean bool = false;

        try {

            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmail", Usuario.class);
            query.setParameter("email", user.getEmail());

            if (query.getResultList().isEmpty()) {
                utx.begin();
                em.persist(user);
                log.log(Level.INFO, "New user saved");
                bool = true;
                utx.commit();
            } else {
                log.log(Level.INFO, "Un usuario se ha intentado registrar con un correo ya registrado");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }

        return bool;
    }

    private long validarLogin(String email, byte[] pass) {
        long id = -1;
        
        try{
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmailAndPassword", Usuario.class);
            query.setParameter("email", email);
            query.setParameter("password", pass);
            List<Usuario> lista = query.getResultList();
            
            if (!lista.isEmpty()) {
                id = lista.get(0).getId();
            } else {
                log.log(Level.INFO, "Un usuario ha hecho login erroneamente");
            }
            
        } catch (Exception e){
            log.log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
        
        return id;
    }
    
    private byte[] cifra(String sinCifrar) throws Exception {
        final byte[] bytes = sinCifrar.getBytes("UTF-8");
        final Cipher aes = obtieneCipher(true);
        final byte[] cifrado = aes.doFinal(bytes);
        return cifrado;
    }

    private String descifra(byte[] cifrado) throws Exception {
        final Cipher aes = obtieneCipher(false);
        final byte[] bytes = aes.doFinal(cifrado);
        final String sinCifrar = new String(bytes, "UTF-8");
        return sinCifrar;
    }

    private Cipher obtieneCipher(boolean paraCifrar) throws Exception {
        final String frase = "FraseLargaConDiferentesLetrasNumerosYCaracteresEspeciales_áÁéÉíÍóÓúÚüÜñÑ1234567890!#%$&()=%_NO_USAR_ESTA_FRASE!_";
        final MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(frase.getBytes("UTF-8"));
        final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

        final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        if (paraCifrar) {
            aes.init(Cipher.ENCRYPT_MODE, key);
        } else {
            aes.init(Cipher.DECRYPT_MODE, key);
        }

        return aes;
    }

}

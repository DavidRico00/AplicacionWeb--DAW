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
import jakarta.transaction.UserTransaction;
import java.util.List;
import java.util.logging.Logger;
import models.Producto;

@WebServlet(name = "PrincipalController", urlPatterns = {"/inicio"})
public class PrincipalController extends HttpServlet {

    @PersistenceContext(unitName = "PortalVentasPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;
    private static final Logger log = Logger.getLogger(controllers.LogRegController.class.getName());

    private final String direccion = "http://localhost:8080/PortalVentas";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vista = "", servlet = request.getServletPath();
        HttpSession session;

        switch (servlet) {
            case "/inicio" -> {
                TypedQuery<Producto> query = em.createNamedQuery("Producto.findAll", Producto.class);
                List<Producto> lista = query.getResultList();
                request.setAttribute("products", lista);
                
                session = request.getSession();
                if(session.getAttribute("id") != null)
                    request.setAttribute("id", session.getAttribute("id"));
                
                vista = "inicio";
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

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

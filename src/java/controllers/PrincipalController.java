/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
@WebServlet(name = "PrincipalController", urlPatterns = {"/inicio"})
public class PrincipalController extends HttpServlet {

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
            case "/inicio" -> {
                vista = "principal";
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

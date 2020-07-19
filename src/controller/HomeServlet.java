package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static util.Constants.*;
/**
 * Deze controller is verantwoordelijk voor het doorsturen van de request naar
 * de HOME pagina. Dit is de startpagina van de website.
 * @author Thomas Van Poucke
 */
@WebServlet("/home")
public class HomeServlet extends ExtendableServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	forward(request, response, JSP_HOME);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}

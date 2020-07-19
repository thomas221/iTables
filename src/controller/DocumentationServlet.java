package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het doorsturen van de gebruiker naar
 * het documentatie gedeelte van de website. Daarin krijgt de gebruiker meer
 * uitleg over hoe hij moet werken met de website.
 * @author Thomas Van Poucke
 */
@WebServlet("/documentation")
public class DocumentationServlet extends ExtendableServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentationServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	forward(request, response, JSP_DOCUMENTATION);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}

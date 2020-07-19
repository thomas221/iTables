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
 * het contact gedeelte van de website. Daarin kan de gebruiker contact opnemen
 * met de eigenaar van de website. Bijvoorbeeld voor vragen of bij problemen.

 * @author Thomas Van Poucke
 */
@WebServlet("/contact")
public class ContactServlet extends ExtendableServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContactServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	forward(request, response, JSP_CONTACT);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}

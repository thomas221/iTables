package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static util.Constants.*;

/**
 * Deze controller zorgt er voor dat fouten goed afgehandeld worden. De
 * gebruiker wordt doorgestuurd naar een pagina met een foutmelding.
 * @author Thomas Van Poucke
 */
@WebServlet("/error")
public class ErrorServlet extends ExtendableServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ErrorServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String paramRedirect = request.getParameter(P_REDIRECT);
	if (paramRedirect != null && paramRedirect.equals(TRUE)) {
	    redirect(request, response, SERVLET_ERROR);
	} else {
	    forward(request, response, JSP_ERROR);
	}
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}

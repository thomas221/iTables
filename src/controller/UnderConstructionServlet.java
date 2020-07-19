package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static util.Constants.*;

/**
 * Deze controller zorgt er voor dat requests naar servlets of pagina's die nog onder constructie zijn goed afgehandeld worden.
 * De gebruiker wordt doorgestuurd naar een pagina met de mededeling dat het gevraagde nog onder constructie is.
 * @author Thomas Van Poucke
 */
@WebServlet("/under_construction")
public class UnderConstructionServlet extends ExtendableServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnderConstructionServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	forward(request, response, JSP_UNDER_CONSTRUCTION);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}

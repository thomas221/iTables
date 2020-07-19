package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het afbreken van de sessie van de
 * gebruiker. De gebruiker wordt daarna doorgestuurd naar de start-pagina van de
 * website.
 * @author Thomas Van Poucke
 */
@WebServlet("/my_configuration/stop")
public class MyConfigurationStopServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationStopServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Sessie invalideren
		request.getSession().invalidate();

		// Redirect naar de start-pagina
		redirect(request, response, SERVLET_START);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

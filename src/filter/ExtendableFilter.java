package filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.configuration.Configuration;
import util.AlertMessageType;
import static util.Constants.*;

public abstract class ExtendableFilter {

	protected void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	/**
	 * Stuur de gebruiker door naar opgegeven url. De gebruiker moet een nieuwe
	 * request sturen naar die url.
	 * 
	 * @param request
	 *            Request van de gebruiker.
	 * @param response
	 *            Terug te sturen response.
	 * @param url
	 *            URL waarnaar de request doorgestuurd wordt.
	 * @throws ServletException
	 *             Algemene Exception die aangeeft dat de Servlet een Exception
	 *             opgegooid heeft bij het afhandelen van de request.
	 * @throws IOException
	 *             Geeft aan dat er een I/O fout gebeurd is.
	 */
	protected void redirect(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		response.sendRedirect(response.encodeRedirectURL(url));
	}

	protected void redirect(HttpServletRequest request, HttpServletResponse response, String url, AlertMessageType alertMessageType, String message) throws ServletException, IOException {
		ArrayList<String> messages = new ArrayList<String>();
		messages.add(message);

		request.setAttribute(FLASH + P_ALERT_MESSAGE_TYPE, alertMessageType);
		request.setAttribute(FLASH + P_ALERT_MESSAGES, messages);

		response.sendRedirect(url);
	}
	
	/**
	 * Methode die controleert of er een geldige firewallconfiguratie in de
	 * sessie zit.
	 * 
	 * @param request
	 *            Request waaruit de sessie wordt opgevraagd die de configuratie
	 *            zou moeten bevatten.
	 * @return Geeft aan of er een geldige configuratie aanwezig is of niet.
	 */
	protected static boolean isConfiguratieGeldig(HttpServletRequest request) {
		Configuration configuration = (Configuration) request.getSession().getAttribute(P_CONFIGURATION);

		if (configuration != null && configuration instanceof Configuration) {
			return true;
		} else {
			return false;
		}
	}

}

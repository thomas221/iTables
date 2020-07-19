package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.FileException;
import exception.ParseException;
import model.configuration.Configuration;
import model.logfile.LogMessageType;
import util.AlertMessageType;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het afhandelen van requests voor
 * voorbeeld configuraties. GET-requests worden afgehandeld door de gebruiker
 * door te sturen naar een pagina waarin hij 1 van de beschikbare
 * voorbeeldconfiguraties kan kiezen. POST-requests worden afgehandeld zodat de
 * geselecteerde voorbeeld configuratie geladen wordt als configuratie om mee te
 * werken.
 * @author Thomas Van Poucke
 */
@WebServlet("/start/example")
@MultipartConfig
public class StartExampleConfigurationServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartExampleConfigurationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute(P_LIST_EXAMPLE_CONFIGURATIONS, getExampleConfigurationFilenames(request, response));

		forward(request, response, JSP_EXAMPLE);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String filename = request.getParameter(P_EXAMPLE_FILENAME);

			if (filename != null && filename.length() > 0) {
				// Voorbeeldconfiguratie aanmaken
				String content = readConfigurationFromClassPath(EXAMPLE_CONFIGURATION_PATH + filename + EXAMPLE_CONFIGURATION_FILENAME_EXTENSION);
				Configuration configuration = new Configuration(content);
				
				Configuration initialConfiguration = new Configuration(content);
				
				//model.importeer_exporteer wordt ook bijgehouden om later iptables bestand te kunnen exporteren
				request.getSession().setAttribute(EXPORT_RULESET, initialConfiguration.getRuleSet());
				
				request.getSession(true).setAttribute(P_CONFIGURATION, configuration);

				// Voorbeeldconfiguratie als initiele configuratie
				// opslaan
				request.getSession().setAttribute(P_INITIAL_CONFIGURATION, initialConfiguration);

				// Logbericht toevoegen
				log(request, LogMessageType.START_EXAMPLE_CONFIGURATION, filename);

				// Redirect naar mijn configuratie pagina
				redirect(request, response, SERVLET_MY_CONFIGURATION, AlertMessageType.SUCCESS, T_CONFIGURATION_EXAMPLE_SUCCESS);
			} else {
				request.setAttribute(P_LIST_EXAMPLE_CONFIGURATIONS, getExampleConfigurationFilenames(request, response));
				forward(request, response, JSP_EXAMPLE, AlertMessageType.DANGER, T_NO_EXAMPLE_CONFIGURATION_SELECTED);
			}
		} catch (FileException e) {
			request.setAttribute(P_LIST_EXAMPLE_CONFIGURATIONS, getExampleConfigurationFilenames(request, response));
			forward(request, response, JSP_EXAMPLE, AlertMessageType.DANGER, e.getErrors(), e.getParameters());
		} catch (ParseException e) {
			request.setAttribute(P_LIST_EXAMPLE_CONFIGURATIONS, getExampleConfigurationFilenames(request, response));
			forward(request, response, JSP_EXAMPLE, AlertMessageType.DANGER, e.getErrors(), e.getParameters());
		} catch(exception.UnsupportedProtocolException e){
			forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UPLOAD_UNSUPPORTED_PROTOCOL);
		} catch (exception.UnsupportedDefaultActionException e){
			request.setAttribute(P_LIST_EXAMPLE_CONFIGURATIONS, getExampleConfigurationFilenames(request, response));
			forward(request, response, JSP_EXAMPLE, AlertMessageType.DANGER, T_UNSUPPORTED_DEFAULT_ACTION);
		}
	}

	/**
	 * Geeft een ArrayList terug van de namen van alle beschikbare voorbeeld
	 * configuraties als strings.
	 * 
	 * @param request
	 *            Af te handelen request.
	 * @param response
	 *            Terug te sturen response.
	 * @return Lijst van de namen van alle beschikbare voorbeeld configuraties
	 *         als strings.
	 * @throws ServletException
	 *             Algemene Exception die door een Servlet kan opgegooid worden
	 *             bij een onverwachte fout.
	 * @throws IOException
	 *             Wordt opgegooid als er een fout gebeurd bij het inlezen van
	 *             de namen van de voorbeeld configuraties.
	 */
	private static ArrayList<String> getExampleConfigurationFilenames(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Set<String> filepaths = null;

		ArrayList<String> filenames = new ArrayList<String>();

		filepaths = request.getServletContext().getResourcePaths(EXAMPLE_CONFIGURATION_PATH_FROM_WEB_INF);

		for (Object object : filepaths.toArray()) {
			String filepath = (String) object;

			String filename = filepath.split("/")[4];
			String filenameWithoutExtension = filename.split("\\.")[0];

			filenames.add(filenameWithoutExtension);
		}

		return filenames;
	}

}

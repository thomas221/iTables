package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.configuration.Configuration;
import model.logfile.LogFile;
import model.logfile.LogMessage;
import model.logfile.LogMessageType;

import org.apache.commons.io.IOUtils;

import exception.FileException;
import util.AlertMessageType;
import static util.Constants.*;

/**
 * Deze klasse breidt HttpServlet klassen uit door extra hulpmethoden aan te
 * bieden. Deze hulpmethoden vereenvoudigen terugkomende taken, zoals het
 * doorsturen van de request naar een bepaalde pagina.
 * @author Thomas Van Poucke
 */
public abstract class ExtendableServlet extends HttpServlet {

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

	/**
	 * Stuur de gebruiker door naar opgegeven url, maar geef meerdere berichten
	 * mee die op de betreffende pagina getoond moeten worden. De gebruiker moet
	 * een nieuwe request sturen naar die url.
	 * 
	 * @param request
	 *            Request van de gebruiker.
	 * @param response
	 *            Terug te sturen response.
	 * @param url
	 *            URL waarnaar de request doorgestuurd wordt.
	 * @param alertMessageType
	 *            Soort berichten die getoond moeten worden. (succes bericht,
	 *            waarshuwing, fout bericht of informatief)
	 * @param messages
	 *            Berichten die moeten getoond worden op de pagina.
	 * @throws ServletException
	 *             Algemene Exception die aangeeft dat de Servlet een Exception
	 *             opgegooid heeft bij het afhandelen van de request.
	 * @throws IOException
	 *             Geeft aan dat er een I/O fout gebeurd is.
	 */
	protected void redirect(HttpServletRequest request, HttpServletResponse response, String url, AlertMessageType alertMessageType, ArrayList<String> messages) throws ServletException, IOException {

		request.setAttribute(FLASH + P_ALERT_MESSAGE_TYPE, alertMessageType);
		request.setAttribute(FLASH + P_ALERT_MESSAGES, messages);

		response.sendRedirect(response.encodeRedirectURL(url));
	}

	/**
	 * 
	 * Stuur de gebruiker door naar opgegeven url, maar geef een bericht mee die
	 * op de betreffende pagina getoond moet worden. De gebruiker moet een
	 * nieuwe request sturen naar die url.
	 * 
	 * @param request
	 *            Request van de gebruiker.
	 * @param response
	 *            Terug te sturen response.
	 * @param url
	 *            URL waarnaar de request doorgestuurd wordt.
	 * @param alertMessageType
	 *            Soort bericht dat getoond moet worden. (succes bericht,
	 *            waarshuwing, fout bericht of informatief)
	 * @param message
	 *            Bericht dat getoond moet worden op de pagina.
	 * @throws ServletException
	 *             Algemene Exception die aangeeft dat de Servlet een Exception
	 *             opgegooid heeft bij het afhandelen van de request.
	 * @throws IOException
	 *             Geeft aan dat er een I/O fout gebeurd is.
	 */
	protected void redirect(HttpServletRequest request, HttpServletResponse response, String url, AlertMessageType alertMessageType, String message) throws ServletException, IOException {

		ArrayList<String> messages = new ArrayList<String>();
		messages.add(message);

		request.setAttribute(FLASH + P_ALERT_MESSAGE_TYPE, alertMessageType);
		request.setAttribute(FLASH + P_ALERT_MESSAGES, messages);

		response.sendRedirect(response.encodeRedirectURL(url));
	}

	/**
	 * Stuur de request door naar opgegeven url.
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
	protected void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(response.encodeURL(url));
		dispatcher.forward(request, response);
	}

	/**
	 * Stuur de request door naar opgegeven url met opgegeven foutbericht en
	 * fouttype.
	 * 
	 * @param request
	 *            Request van de gebruiker.
	 * @param response
	 *            Terug te sturen response.
	 * @param url
	 *            URL waarnaar de request doorgestuurd wordt.
	 * @param alertMessageType
	 *            Soort bericht dat getoond moet worden. (success bericht,
	 *            waarschuwing, fout bericht of informatief)
	 * @param message
	 *            Bericht dat getoond moet worden op de pagina.
	 * @throws ServletException
	 *             Algemene Exception die aangeeft dat de Servlet een Exception
	 *             opgegooid heeft bij het afhandelen van de request.
	 * @throws IOException
	 *             Geeft aan dat er een I/O fout gebeurd is.
	 */
	protected void forward(HttpServletRequest request, HttpServletResponse response, String url, AlertMessageType alertMessageType, String message) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(response.encodeURL(url));

		ArrayList<String> messages = new ArrayList<String>();
		messages.add(message);

		request.setAttribute(P_ALERT_MESSAGE_TYPE, alertMessageType);
		request.setAttribute(P_ALERT_MESSAGES, messages);

		dispatcher.forward(request, response);
	}

	/**
	 * Stuur de request door naar opgegeven url, maar geef meerdere berichten
	 * mee die op de betreffende pagina getoond moeten worden.
	 * 
	 * @param request
	 *            Request van de gebruiker.
	 * @param response
	 *            Terug te sturen response.
	 * @param url
	 *            URL waarnaar de request doorgestuurd wordt.
	 * @param alertMessageType
	 *            Soort berichten die getoond moeten worden. (succes bericht,
	 *            waarshuwing, fout bericht of informatief)
	 * @param messages
	 *            Berichten die moeten getoond worden op de pagina.
	 * @throws ServletException
	 *             Algemene Exception die aangeeft dat de Servlet een Exception
	 *             opgegooid heeft bij het afhandelen van de request.
	 * @throws IOException
	 *             Geeft aan dat er een I/O fout gebeurd is.
	 */
	protected void forward(HttpServletRequest request, HttpServletResponse response, String url, AlertMessageType alertMessageType, ArrayList<String> messages) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(response.encodeURL(url));

		request.setAttribute(P_ALERT_MESSAGE_TYPE, alertMessageType);
		request.setAttribute(P_ALERT_MESSAGES, messages);

		dispatcher.forward(request, response);
	}

	/**
	 * Stuur de request door naar opgegeven url, maar geef een bericht mee die
	 * op de betreffende pagina getoond moet worden.
	 * 
	 * @param request
	 *            Request van de gebruiker.
	 * @param response
	 *            Terug te sturen response.
	 * @param url
	 *            URL waarnaar de request doorgestuurd wordt.
	 * @param alertMessageType
	 *            Soort bericht dat getoond moet worden. (succes bericht,
	 *            waarshuwing, fout bericht of informatief)
	 * @param messages
	 *            Berichten die getoond moeten worden op de pagina. Meegeven als
	 *            een lijst van type String.
	 * @throws ServletException
	 *             Algemene Exception die aangeeft dat de Servlet een Exception
	 *             opgegooid heeft bij het afhandelen van de request.
	 * @throws IOException
	 *             Geeft aan dat er een I/O fout gebeurd is.
	 */
	protected void forward(HttpServletRequest request, HttpServletResponse response, String url, AlertMessageType alertMessageType, ArrayList<String> messages, ArrayList<String> parameters)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(response.encodeURL(url));

		request.setAttribute(P_ALERT_MESSAGE_TYPE, alertMessageType);
		request.setAttribute(P_ALERT_MESSAGES, messages);
		request.setAttribute(P_ALERT_PARAMETERS, parameters);

		dispatcher.forward(request, response);
	}

	/**
	 * Lees de native IPTABLES configuratie die op de opgegeven locatie staat in
	 * en geef deze terug als een string.
	 * 
	 * @param filePath
	 *            Locatie van de gewenste native IPTABLES configuratie bestand.
	 * @return Native IPTABLES configuratie als string.
	 * @throws FileException
	 *             Wordt opgegooid als er een fout gebeurd bij het proberen
	 *             lezen van bestand met opgegeven locatie als string.
	 */
	protected String readConfigurationFromClassPath(String filePath) throws FileException {
		try {
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer, Charset.defaultCharset());
			String content = writer.toString();
			input.close();
			return content;
		} catch (IOException e) {
			throw new FileException(T_FILE_NOT_FOUND);
		}
	}

	/**
	 * Geeft de model.configuration.Configuration terug die hoort bij de huidige sessie.
	 * @param request Request van de client.
	 * @return model.configuration.Configuration die hoort bij de huidige sessie.
	 */
	protected Configuration getConfiguration(HttpServletRequest request) {
		if (request.getSession() != null && request.getSession().getAttribute(P_CONFIGURATION) != null && request.getSession().getAttribute(P_CONFIGURATION) instanceof Configuration) {
			return (Configuration) request.getSession().getAttribute(P_CONFIGURATION);
		} else {
			return null;
		}
	}

	/**
	 * Geeft de configuratie terug waarmee de gebruiker initieel gestart was. Dus zonder de wijzigingen daarna.
	 * @param request Request van de client.
	 * @return model.configuration.Configuration zoals die was initieel - dus zonder de wijzigingen van de gebruiker.
	 */
	protected Configuration getInitialConfiguration(HttpServletRequest request) {
		if (request.getSession() != null && request.getSession().getAttribute(P_INITIAL_CONFIGURATION) != null && request.getSession().getAttribute(P_INITIAL_CONFIGURATION) instanceof Configuration) {
			return (Configuration) request.getSession().getAttribute(P_INITIAL_CONFIGURATION);
		} else {
			return null;
		}
	}

	/**
	 * Methode die een nieuw log bericht schrijft naar het logboek.
	 * @param request Request van de gebruiker.
	 * @param type Soort bericht.
	 * @param message Gewenst log bericht.
	 */
	protected void log(HttpServletRequest request, LogMessageType type, String message) {
		HttpSession session = request.getSession(true);
		LogFile logFile = null;

		if (session != null && session.getAttribute(P_LOGFILE) != null && session.getAttribute(P_LOGFILE) instanceof LogFile) {
			logFile = (LogFile) session.getAttribute(P_LOGFILE);
		} else if (session != null && session.getAttribute(P_LOGFILE) == null) {
			logFile = new LogFile();
		}

		logFile.log(type, message, getMessage(request, type.getI18n()));
		session.setAttribute(P_LOGFILE, logFile);
	}

	/**
	 * Geeft een ArrayList terug met alle log berichten.
	 * @param request Request van de client.
	 * @return ArrayList met alle log berichten.
	 */
	protected ArrayList<LogMessage> getLogs(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		LogFile logFile = null;

		if (session != null && session.getAttribute(P_LOGFILE) != null && session.getAttribute(P_LOGFILE) instanceof LogFile) {
			logFile = (LogFile) session.getAttribute(P_LOGFILE);

			return logFile.getLogs();
		} else {
			return null;
		}
	}

	/**
	 * Print alle parameter van de request naar de standaard uitvoer.
	 * @param request Request van de client.
	 * @throws ServletException Fout door de servlet bij ophalen van alle parameters van request.
	 * @throws IOException Fout bij uitvoeren naar de standaard uitvoer.
	 */
	protected void printParameterMap(HttpServletRequest request) throws ServletException, IOException {
		Enumeration<String> paramNames = request.getParameterNames();

		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			System.out.println(paramName + " = " + request.getParameter(paramName));
		}
	}

	/**
	 * Verkrijg tekst uit i18n bestand met opgegeven sleutel.
	 * @param request Request van de client.
	 * @param key Sleutel van de gewenste tekst uit het i18n bestand.
	 * @return Tekst die hoort bij opgegeven sleutel in het i18n bestand.
	 */
	protected String getMessage(HttpServletRequest request, String key) {
		Locale locale = request.getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.text", locale);
		return bundle.getString(key);
	}
}

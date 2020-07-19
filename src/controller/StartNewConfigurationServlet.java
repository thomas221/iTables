package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.configuration.Configuration;
import model.logfile.LogMessageType;
import exception.FileException;
import exception.ParseException;
import util.AlertMessageType;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het doorsturen van de request naar
 * de pagina voor het starten met een nieuwe (lege) configuratie.
 */
@WebServlet("/start/new")
@MultipartConfig
public class StartNewConfigurationServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartNewConfigurationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Lege configuratie aanmaken
			String content = readConfigurationFromClassPath(FILENAME_EMPTY_CONFIGURATION);
			Configuration configuration = new Configuration(content);
			
			Configuration initialConfiguration = new Configuration(content);
			request.getSession(true).setAttribute(P_CONFIGURATION, configuration);
			
			//model.importeer_exporteer wordt ook bijgehouden om later iptables bestand te kunnen exporteren
			request.getSession().setAttribute(EXPORT_RULESET, initialConfiguration.getRuleSet());

			// Direct lege configuratie als initiele configuratie opslaan
			request.getSession().setAttribute(P_INITIAL_CONFIGURATION, initialConfiguration);

			// Logbericht toevoegen
			log(request, LogMessageType.START_EMPTY_CONFIGURATION, null);

			// Redirect naar mijn configuratie pagina
			redirect(request, response, SERVLET_MY_CONFIGURATION, AlertMessageType.SUCCESS, T_CONFIGURATION_NEW_SUCCESS);
		} catch (ParseException | FileException e) {
			forward(request, response, JSP_START, AlertMessageType.DANGER, e.getErrors(), e.getParameters());
		} catch(exception.UnsupportedProtocolException e){
			forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UPLOAD_UNSUPPORTED_PROTOCOL);
		} catch (exception.UnsupportedDefaultActionException e){
			forward(request, response, JSP_START, AlertMessageType.DANGER, T_UNSUPPORTED_DEFAULT_ACTION);
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

package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.configuration.Configuration;
import model.importeer_exporteer.RuleSet;
import model.logfile.LogMessageType;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.FileUploadIOException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import exception.ParseException;
import exception.UnsupportedDefaultActionException;
import util.AlertMessageType;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het afhandelen van request om een
 * eigen configuratie te uploaden. Bij een GET-request wordt de gebruiker
 * doorgestuurd naar een pagina waarop hij een configuratie bestand op zijn
 * eigen computer kan uploaden om er mee te werken. Bij een POST-request wordt
 * de met de request meegegeven configuratie bestand geupload naar de website.
 */
@WebServlet("/start/upload")
public class UploadConfigurationServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadConfigurationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forward(request, response, JSP_UPLOAD);
	}

	/**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			try {
				ServletFileUpload upload = new ServletFileUpload();
				//stel maximum file size in (in bytes)
				upload.setFileSizeMax(FILE_SIZE_MAX);
				FileItemIterator iter = upload.getItemIterator(request);

				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					String filename = item.getName();
					InputStream stream = item.openStream();
					if (!item.isFormField() && name.equals(P_UPLOAD_FILENAME)) {
						String content = Streams.asString(stream);
						if (content != null && content.length() > 0) {
							String contentType = item.getContentType();
							if (contentType.equals(CONTENT_PLAIN_TEXT) || contentType.equals(CONTENT_APPLICATION_OCTET_STREAM)) {
								try {
									// Geuploade configuratie aanmaken
									Configuration configuration = new Configuration(content);
									
									Configuration initialConfiguration = new Configuration(content);
									
									request.getSession(true).setAttribute(P_CONFIGURATION, configuration);
									
									//model.importeer_exporteer wordt ook bijgehouden om later iptables bestand te kunnen exporteren
									RuleSet ruleSet = initialConfiguration.getRuleSet();
									request.getSession().setAttribute(EXPORT_RULESET, ruleSet);

									// Direct geuploade configuratie als
									// initiele configuratie opslaan
									request.getSession().setAttribute(P_INITIAL_CONFIGURATION, initialConfiguration);
									
									//ga na of ingelezen configuratie opties bevat die niet door iTables ondersteund worden
									ArrayList<String> messages = new ArrayList<String>();
									messages.add(T_CONFIGURATION_UPLOAD_SUCCESS);
									String logMessage = "";
									logMessage += filename+ " ge&uuml;pload. ";
									
									if(!ruleSet.getOptionsSupported()){
										messages.add(T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_OPTIONS);
										logMessage += "Let op: de configuratie bevat regels met iptables opties die niet ondersteund worden door iTables. Daardoor is niet gegarandeerd dat de ge&iuml;mporteerde configuratie de zelfde is als de oorspronkelijke configuratie. ";
									}
									if(ruleSet.getContainsNotSupportedTable()){
										messages.add(T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_TABLES);
										logMessage += "Let op: de configuratie bevat niet-lege tables die niet ondersteund worden door iTables. iTables ondersteunt enkel de FILTER, MANGLE en RAW tables. Regel van overige tables worden niet ingelezen. ";
									}
									if(!ruleSet.getActionsSupported()){
										messages.add(T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_UNSUPPORTED_ACTION);
										logMessage += "Let op: sommige regels bevatten acties die niet ondersteund worden. Alleen de acties DROP en ACCEPT worden ondersteund. Regels met andere acties worden niet mee ingelezen. ";
									}
									// Logbericht toevoegen
									log(request, LogMessageType.START_UPLOADED_CONFIGURATION, logMessage);

									// Redirect naar mijn configuratie pagina
									if(messages.size() == 1){
										redirect(request, response, SERVLET_MY_CONFIGURATION, AlertMessageType.SUCCESS, messages.get(0));										
									}else{
										redirect(request, response, SERVLET_MY_CONFIGURATION, AlertMessageType.WARNING, messages);
									}
									
									
									/*if(ruleSet.isSupported()){
										// Logbericht toevoegen
										log(request, LogMessageType.START_UPLOADED_CONFIGURATION, filename+ " ge&uuml;pload.");
	
										// Redirect naar mijn configuratie pagina
										redirect(request, response, SERVLET_MY_CONFIGURATION, AlertMessageType.SUCCESS, T_CONFIGURATION_UPLOAD_SUCCESS);
									}else{
										// Logbericht toevoegen
										log(request, LogMessageType.START_UPLOADED_CONFIGURATION, filename+" ge&uuml;pload. Let op: de configuratie bevat iptables opties die niet ondersteund worden door iTables. Daardoor is niet gegarandeerd dat de ingelezen configuratie de zelfde is als de oorspronkelijke configuratie.");
	
										// Redirect naar mijn configuratie pagina
										redirect(request, response, SERVLET_MY_CONFIGURATION, AlertMessageType.WARNING, T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_OPTIONS);
									}*/
								} catch (exception.ParseException e) {
									request.setAttribute("isI18NKey",e.getErrorsAreI18Nkeys());
									forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, e.getErrors(), e.getParameters());
								} catch(exception.UnsupportedProtocolException e){
									forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UPLOAD_UNSUPPORTED_PROTOCOL);
								} catch (exception.UnsupportedDefaultActionException e){
									forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UNSUPPORTED_DEFAULT_ACTION);
								}
							} else {
								forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UPLOAD_WRONG_TYPE);
							}
						} else {
							forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UPLOAD_EMPTY_FILE);
						}
					}
					stream.close();
				}
			} catch (FileUploadIOException e){
				Throwable cause = e.getCause();
				if(cause instanceof FileSizeLimitExceededException){
					forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_FILE_TO_LARGE);
				}else{
					forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, e.getMessage());
				}
			} catch (FileUploadException e) {
				forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, e.getMessage());
			}
		} else {
			forward(request, response, JSP_UPLOAD, AlertMessageType.DANGER, T_UPLOAD_NOT_MULTIPART);
		}
	}

	/**
	 * Geeft de extensie terug van opgegeven bestandsnaam.
	 * 
	 * @param filename
	 *            De bestandsnaam.
	 * @return Extensie van de bestandsnaam als string.
	 */
	public String getFileExtension(String filename) {
		if (filename != null) {
			String[] fileParts = filename.split("\\.");

			if (fileParts != null && fileParts.length > 1) {
				String extension = fileParts[fileParts.length - 1];
				return extension;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}

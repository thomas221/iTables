package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.configuration.Chain;
import model.configuration.Configuration;
import model.configuration.ConflictStatus;
import model.configuration.CorrelationGroup;
import model.configuration.RedundancySolution;
import model.configuration.Rule;
import model.configuration.Segment;
import model.configuration.SetOfGroups;
import model.configuration.Space;
import model.configuration.Table;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import exception.CustomException;
import util.AlertMessageType;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het afhandelen van requests om de
 * huidige sessie te exporteren. Met exporteren wordt bedoeld dat de gebruiker
 * de (eventueel gewijzigde) IPTABLES configuratie kan downloaden naar zijn
 * computer.
 */
@WebServlet("/my_configuration/export")
public class MyConfigurationExportServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationExportServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// handel ajax request af om te bepalen of modal met waarschuwing moet
		// getoond worden
		if (request.getParameter(P_PORT_NO_PROTOCOL) != null) {
			determineIfWarningNeeded(request, response);
		} else {

			// handel verzoek om downloaden iptables configuratie bestand of
			// Excel bestand met sessie af
			if (request.getParameter(P_FILE) != null && request.getParameter(P_FILE).equals(V_IPTABLES)) {
				exportConfigurationFile(request, response);
			} else {
				exportLogFileAsExcel(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * Geeft response de opgegeven headers. Dit zodat de gebruiker het bestand
	 * met opgegeven bestandsnaam aangeboden krijgt om te downloaden.
	 * 
	 * @param response
	 *            Response om terug te sturen naar de client.
	 * @param contentType
	 *            MIME-type van het content-gedeelte van de response.
	 * @param bestandsnaam
	 *            Bestandsnaam van bestand dat door de response aangeboden wordt
	 *            om te downloaden.
	 */
	private static void SetResponseHeaders(HttpServletResponse response, String contentType, String bestandsnaam) {
		response.setContentType(contentType);
		response.setHeader(H_EXPIRES, HV_EXPIRES);
		response.setHeader(H_CACHE_CONTROL, HV_CACHE_CONTROL);
		response.setHeader(H_PRAGMA, HV_PRAGMA);
		response.addHeader(H_CONTENT_DISPOSITION, HV_CONTENT_DISPOSITION + bestandsnaam);
	}

	/**
	 * Geeft de bestandnaam van het Excel-bestand met de geexporteerde sessie
	 * informatie.
	 * 
	 * @return Bestandsnaam van het Excel-bestand met de geexporteerde sessie
	 *         informatie.
	 */
	private static String getExcelExportFilename() {
		return LOGFILE_FILENAME + LOGFILE_EXPORT_FILE_TYPE_EXCEL;
	}

	/**
	 * Geeft de bestandsnaam van het iptables-bestand met de geexporteerde
	 * configuratie.
	 * 
	 * @return Bestandsnaam van het iptables bestand met de geexporteerde
	 *         configuratie.
	 */
	private static String getIptablesExportFilename() {
		return LOGFILE_FILENAME + CONFIGURATION_EXPORT_FILE_TYPE_IPTABLES;
	}

	/**
	 * Methode die Excel-bestand genereert met de geexporteerde sessie
	 * informatie. Dit bestand wordt aangeboden aan de gebruiker.
	 * 
	 * @param request
	 *            Request van de client.
	 * @param response
	 *            Response van de server.
	 * @throws ServletException
	 *             Exception opgegooid door de Servlet.
	 * @throws IOException
	 *             Exception bij benaderen van template bestand.
	 */
	private void exportLogFileAsExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			//bepaal redundante regels - om te tonen in Excel bestand
			ArrayList<Table> tablesWithRedundancy = new ArrayList<Table>();
			List<Table> tables = getConfiguration(request).getTables();
			for(int i=0;i<tables.size();i++){
				Table table = tables.get(i);
				List<Chain> chains = table.getChains();
				for(int j=0;j<chains.size();j++){
					Chain chain = chains.get(j);
					Space space = chain.getSpace();
					SetOfGroups setOfGroups = space.createCorrelationGroups();
					ArrayList<CorrelationGroup> groups = setOfGroups.getCorrelationGroups();
					
					for(int k=0;k<groups.size();k++){
						CorrelationGroup group = groups.get(k);
						RedundancySolution redundancySolution = space.determineRedundantRules(group);
						ArrayList<Integer> redundantRuleNrs = redundancySolution.getRedundantRuleNrs();
						/* Voor test doeleinden:
						 * for(int m=0;m<redundantRuleNrs.size();m++){
						 * 	  System.out.println("Redundant: "+m+redundantRuleNrs.get(m));
						 * }
						 */
						
						//indien er redundante regels zijn in deze table en chain: maak table en chain aan voor redundante regels
						if(redundantRuleNrs.size() > 0){
							//maak table aan indien deze niet bestaat
							boolean redundantTableExists = false;
							Table theTable = null;
							for(Table aTable: tablesWithRedundancy){
								if(aTable.getNameUppercase().equals(table.getNameUppercase())){ //geval dat tabel reeds in lijst van tabellen met redundante regels zit
									redundantTableExists = true;
									theTable = aTable;
								}
							}
							
							if(!redundantTableExists){ //geval dat tabel nog niet in lijst van tabellen met redundante regels zit
								theTable = new Table(table.getName());
								tablesWithRedundancy.add(theTable);
							}
							
							//maak chain aan indien deze niet bestaat
							boolean redundantChainExists = false;
							Chain theChain = null;
							for(Chain achain: theTable.getChains()){
								if(achain.getName().toUpperCase().equals(chain.getName().toUpperCase())){ //geval dat chain reeds in lijt van chains met redundante regels zit
									redundantChainExists = true;
									theChain = achain;
								}
							}
							if(!redundantChainExists){ //geval dat chain nog niet in lijst chains met redundante regels zit
								theChain = new Chain(chain.getName(),Chain.PolicyAction.ACCEPT);
								theTable.addChain(theChain);
							}
							
							//voeg redundante regels toe aan table en chain voor redundante regels
							for(int l=0;l<redundantRuleNrs.size();l++){
								int ruleNr = redundantRuleNrs.get(l);
								Rule originalRule = chain.getRule(ruleNr);
								Rule rule = new Rule(ruleNr,originalRule.getProtocol(),originalRule.getSourceIP(),originalRule.getSourcePort(),originalRule.getDestinationIP(),originalRule.getDestinationPort(),originalRule.getAction());
								theChain.addRule(rule);
								rule.setRuleNr(ruleNr); //omdat regelnummer veranderd is na toevoegen aan chain
							}
							
						}						
					}
					
				}
			}
			
			//check of er al een ConflictStatus object in de sessie zit om de opgeloste conflicten bij te houden. Zo nee maak nieuw ConflictStatus object aan.
			ConflictStatus conflictStatus = (ConflictStatus)request.getSession().getAttribute("ConflictStatus");
			if(conflictStatus == null){
				conflictStatus = new ConflictStatus();
				request.getSession().setAttribute("ConflictStatus", conflictStatus);
			}
			
			
			SetResponseHeaders(response, CONTENT_APPLICATION_EXCEL, getExcelExportFilename());

			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put(LOGILE_VARIABLE_LOGS, getLogs(request));
			beans.put(LOGFILE_VARIABLE_TABLES_INITIEEL, getInitialConfiguration(request).getTables());
			beans.put(LOGFILE_VARIABLE_TABLES_FINAAL, getConfiguration(request).getTables());
			beans.put(LOGFILE_VARIABLE_TABLES_ANOMALIES, getConfiguration(request).getTables());
			beans.put("conflictStatus", conflictStatus);
			beans.put("tablesWithRedundancy", tablesWithRedundancy);

			ServletContext context = getServletContext();
			InputStream inp = context.getResourceAsStream(I_LOGFILE_EXPORT_FILE_TEMPLATE_PATH);

			XLSTransformer transformer = new XLSTransformer();
			Workbook workbook;
			workbook = transformer.transformXLS(inp, beans);
			workbook.write(response.getOutputStream());
			inp.close();
		} catch (ParsePropertyException | InvalidFormatException  | CustomException e) {
			redirect(request, response, SERVLET_CONFIGURATION_LOGFILE, AlertMessageType.DANGER, T_LOGFILE_EXPORT_FAILED);
		}
	}

	/**
	 * Methode die iptables bestand met de huidige configuratie genereert. Dit
	 * bestand wordt aangeboden aan de gebruiker.
	 * 
	 * @param request
	 *            Request van de client.
	 * @param response
	 *            Response van de server.
	 * @throws ServletException
	 *             Exception opgegooid door de Servlet.
	 * @throws IOException
	 *             Fout bij schrijven naar response bericht door PrintWriter.
	 */
	private static void exportConfigurationFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SetResponseHeaders(response, CONTENT_TEXT, getIptablesExportFilename());
		model.importeer_exporteer.RuleSet ruleSet = (model.importeer_exporteer.RuleSet) request.getSession().getAttribute(EXPORT_RULESET);
		String iptablesText = ruleSet.getExportRules();
		PrintWriter output = response.getWriter();
		output.write(iptablesText);
		output.flush();
		output.close();

	}

	private void determineIfWarningNeeded(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(CONTENT_APPLICATION_JSON);
		response.setCharacterEncoding(UTF_8);
		PrintWriter writer = response.getWriter();

		Configuration configuration = getConfiguration(request);

		try {
			if (configuration != null) {
				boolean showWarning = configuration.containsRuleWithPortWithoutProtocol();
				if (showWarning) {
					writer.write("{\"showWarning\":\"yes\"}");
				} else {
					writer.write("{\"showWarning\":\"no\"}");
				}
			} else {
				response.setStatus(400);
				writer.write(TEXT_ONGELDIGE_SITUATIE_OPGETREDEN);
			}
		} finally {
			writer.flush();
			writer.close();
		}

	}
}

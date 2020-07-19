package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exception.CustomException;
import exception.FoutiefParametertypeException;
import util.AlertMessageType;
import model.configuration.Chain;
import model.configuration.Chain.PolicyAction;
import model.configuration.Configuration;
import model.configuration.IPPattern;
import model.configuration.PortPattern;
import model.configuration.Rule;
import model.configuration.Space;
import model.importeer_exporteer.Chain.Policy;
import model.importeer_exporteer.RuleSet;
import model.logfile.LogMessageType;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het afhandelen van alle requests voor de firewall regels pagina.
 * De firewall regels pagina is de pagina waar alle firewall regels getoond worden, en waar regels bewerkt, verplaatst, toegevoegd,
 * of verwijderd kunnen worden. De default policy kan ook aangepast worden op deze pagina.
 * Daarnaast is er op deze pagina de mogelijkheid om de packetFirstMatch functionaliteit uit te testen. (op de pagina onder de naam 'bepaal matchende regel').
 * Deze controller handelt alle requests af van alles dat net opgenoemd is.
 * 
 * 
 */
@WebServlet("/my_configuration/rules")
public class MyConfigurationRulesServlet extends ExtendableServlet {

	/**
	 * Tekst die aangeeft dat de firewall regel niet kon worden gevonden.
	 */
	public static final String T_RULE_NOT_FOUND = "De firewallregel kon niet worden gevonden.";
	/**
	 * Tekst die aangeeft dat een ongeldige bewerkingsactie is aangetroffen.
	 */
	public static final String T_BAD_EDIT_ACTION_FOUND = "Ongeldige bewerkingsactie aangetroffen.";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationRulesServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter(P_ACTION);

		if (action != null && action.equals(V_ACTION_GET_ACTIONS)) {
			response.setContentType(CONTENT_APPLICATION_JSON);
			response.setCharacterEncoding(UTF_8);

			JSONArray array = new JSONArray();

			JSONObject allow = new JSONObject();
			allow.put("value", 1);
			allow.put("text", "accept");

			array.add(allow);

			JSONObject deny = new JSONObject();
			deny.put("value", 0);
			deny.put("text", "drop");

			array.add(deny);

			response.getWriter().print(array);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			String tableName = (request.getParameter(P_TABLE) != null) ? request.getParameter(P_TABLE) : (String) request.getSession().getAttribute(P_TABLE);
			String chainName = (request.getParameter(P_CHAIN) != null) ? request.getParameter(P_CHAIN) : (String) request.getSession().getAttribute(P_CHAIN);

			Configuration configuration = getConfiguration(request);

			if (configuration != null && tableName != null && chainName != null) {
				request.getSession().setAttribute(P_TABLE, tableName);
				request.getSession().setAttribute(P_CHAIN, chainName);

				ArrayList<Rule> rules = configuration.getTable(tableName).getChain(chainName).getRules();

				request.setAttribute(P_RULES, rules);
			}

			forward(request, response, JSP_MY_CONFIGURATION_RULES);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter(P_ACTION);

		printParameterMap(request);

		if (action != null) {
			switch (action) {
				case V_ACTION_ADD_RULE:
					addRule(request, response);
					break;
				case V_ACTION_EDIT_RULE:
					editRuleAjax(request, response);
					break;
				case V_ACTION_EDIT_RULE_WITH_MODAL:
					editRuleModal(request, response);
					break;
				case V_ACTION_DELETE_RULE:
					deleteRule(request, response);
					break;
				case V_ACTION_EDIT_RULE_ORDER:
					editRuleOrder(request, response);
					break;
				case V_ACTION_EDIT_POLICY:
					editPolicy(request, response);
					break;
				case V_ACTION_EDIT_PROTOCOL_WITH_MODAL:
					editProtocolModal(request, response);
					break;
				case V_ACTION_MATCH:
					firstMatch(request, response);
					break;
				default:
					break;
				}
		}
		
		
	}
	
	/**
	 * Handelt AJAX request af komende van het formulier op de firewall regels pagina voor het bepalen van de matchende regel. Daarvoor wordt de packetFirstMatch() methode gebruikt uit de klasse Space.
	 * @param request Request van client.
	 * @param response Response van server.
	 * @throws ServletException Exception door Servlet.
	 * @throws IOException Fout bij I/O operatie.
	 */
	protected void firstMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//volgende handelt requests af ten gevolge van het duwen op de match knop.
		String tableName = (request.getParameter(P_TABLE) != null) ? request.getParameter(P_TABLE) : (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (request.getParameter(P_CHAIN) != null) ? request.getParameter(P_CHAIN) : (String) request.getSession().getAttribute(P_CHAIN);

		response.setContentType(CONTENT_APPLICATION_JSON);
		response.setCharacterEncoding(UTF_8);
		PrintWriter writer = response.getWriter();

		Configuration configuration = (Configuration) request.getSession().getAttribute(P_CONFIGURATION);
		
		try {
			if (configuration != null && tableName != null && chainName != null) {
				//volgende handelt requests af ten gevolge van het duwen op de match knop.
				String matchProtocol = request.getParameter("match_protocol");
				String matchBronIP1 = request.getParameter("match_bron_IP1");
				String matchBronIP2 = request.getParameter("match_bron_IP2");
				String matchBronIP3 = request.getParameter("match_bron_IP3");
				String matchBronIP4 = request.getParameter("match_bron_IP4");
				String matchBronpoort = request.getParameter("match_bronpoort");
				String matchDoelIP1 = request.getParameter("match_doel_IP1");
				String matchDoelIP2 = request.getParameter("match_doel_IP2");
				String matchDoelIP3 = request.getParameter("match_doel_IP3");
				String matchDoelIP4 = request.getParameter("match_doel_IP4");
				String matchDoelpoort = request.getParameter("match_doelpoort");
				if(matchProtocol != null && matchBronIP1 != null && matchBronIP2 != null && matchBronIP3 != null && matchBronIP4 != null && matchBronpoort != null && matchDoelIP1 != null && matchDoelIP2 != null && matchDoelIP3 != null && matchDoelIP4 != null && matchDoelpoort != null){
					
						//verkrijg protocol		
						int protocol = Integer.parseInt(matchProtocol);
						
						//verkrijg bron IP
						int bronIP1 = Integer.parseInt(matchBronIP1);
						int bronIP2 = Integer.parseInt(matchBronIP2);
						int bronIP3 = Integer.parseInt(matchBronIP3);
						int bronIP4 = Integer.parseInt(matchBronIP4);
						
						//verkrijg bron poort
						int bronpoort = Integer.parseInt(matchBronpoort);
						
						//verkrijg doel IP
						int doelIP1 = Integer.parseInt(matchDoelIP1);
						int doelIP2 = Integer.parseInt(matchDoelIP2);
						int doelIP3 = Integer.parseInt(matchDoelIP3);
						int doelIP4 = Integer.parseInt(matchDoelIP4);
						
						//verkrijg doel poort
						int doelpoort = Integer.parseInt(matchDoelpoort);
						
						Space space = configuration.getTable(tableName).getChain(chainName).getSpace();
						int matchendeRegel = space.packetFirstMatch(protocol, bronIP1, bronIP2, bronIP3, bronIP4, bronpoort, doelIP1, doelIP2, doelIP3, doelIP4, doelpoort);
					
						writer.write("{\"matchendeRegel\":\""+matchendeRegel+"\"}");
				} else {
					response.setStatus(400);
					writer.write(TEXT_ONGELDIGE_SITUATIE_OPGETREDEN);
				}
			} else {
				response.setStatus(400);
				writer.write(TEXT_ONGELDIGE_SITUATIE_OPGETREDEN);
			}
		} catch (CustomException e) {
			response.setStatus(400);
			writer.write(TEXT_ONGELDIGE_SITUATIE_OPGETREDEN);
		} catch (NumberFormatException e) {
			response.setStatus(400);
			writer.write(TEXT_ONGELDIGE_SITUATIE_OPGETREDEN);
		} finally {
			writer.flush();
			writer.close();
		}
	}

	/**
	 * Pas volgorde van regels aan. Regel met positie 'fromPosition' wordt opgeschoven naar 'toPosition'. 'fromPosition' en 'toPosition' zijn
	 * getallen en zijn als parameters meegegeven bij de request.
	 * @param request Request van client.
	 * @param response Response van server.
	 * @throws ServletException Exception door Servlet.
	 * @throws IOException Fout bij I/O operatie.
	 */
	protected void editRuleOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		if (configuration != null && tableName != null && chainName != null) {
			try {
				int positionFrom = Integer.parseInt(request.getParameter("fromPosition")); //positie, begint te tellen vanaf 1
				int positionTo = Integer.parseInt(request.getParameter("toPosition")); //positie, begint te tellen vanaf 1

				Chain chain = configuration.getTable(tableName).getChain(chainName);

				chain.repositionRule(positionFrom, positionTo);
				
				//doe deze wijziging ook in ruleSet voor export
				model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
				model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
				model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
				//wijzig volgorde van regels
				if(positionFrom < positionTo){
					int i = positionFrom;
					while(i < positionTo){
						exportChain.repositionRule(i, i+1);
						i++;
					}
				}
				if(positionFrom > positionTo){
					int i = positionFrom;
					while(i > positionTo){
						exportChain.repositionRule(i, i-1);
						i--;
					}
				}

				log(request, LogMessageType.EDIT_FIREWALL_RULE, "Firewallregel op positie " + positionFrom + "  verplaatst naar positie " + positionTo + " van " + tableName.toLowerCase()
						+ " table en " + chainName.toLowerCase() + " chain");

				redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.SUCCESS, "Firewallregel op positie " + positionFrom + " werd succesvol verplaatst naar positie " + positionTo
						+ ".");
			} catch (NumberFormatException e) {
				redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.DANGER, T_INVALID_SITUATION);
			}
		} else {
			redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.DANGER, T_INVALID_SITUATION);
		}
	}

	/**
	 * Voegt regel achteraan toe aan de configuratie.
	 * @param request Request van de client.
	 * @param response Response van de server.
	 * @throws ServletException Exception door Servlet.
	 * @throws IOException I/O exception.
	 */
	protected void addRule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		if (configuration != null && tableName != null && chainName != null) {
			try {
				Chain chain = configuration.getTable(tableName).getChain(chainName);
				//verkrijg protocol
				
				int protocol = Integer.parseInt(request.getParameter("modal-add-rule-protocol-select"));
				if(protocol == -3){
					protocol = Integer.parseInt(request.getParameter("modal-add-rule-protocol-getal"));
				}
				//verkrijg actie
				int actionInt = Integer.parseInt(request.getParameter("modal-add-rule-action"));

				boolean action = false;

				if (actionInt == 1) {
					action = true;
				}
				//verkrijg bron-ip
				String bronIPString = request.getParameter("modal-add-rule-source-ip");
				//indien masker niet opgegeven is deze 32 en moet deze aan string toegevoegd worden
				if(!bronIPString.equals("*") && !bronIPString.contains("/")){
					bronIPString += "/32";
				}
				//verkrijg doel-ip
				String doelIPString = request.getParameter("modal-add-rule-destination-ip");
				//indien masker niet opgegeven is deze 32 en moet deze aan string toegevoegd worden
				if(!doelIPString.equals("*") && !doelIPString.contains("/")){
					doelIPString += "/32";
				}
				boolean isShowscope = false;
				String showscope = request.getParameter("modal-add-rule-showscope");
				if(showscope != null && showscope.equals("on")){
					isShowscope = true;
				}else{
					isShowscope = false;
				}
				
				Rule rule = null;
				//indien protocol niet TCP of UDP : geef geen poorten op
				if(protocol != 6 && protocol != 17){
					rule = new Rule(-1, protocol, new IPPattern(bronIPString), new PortPattern(-1,-1), new IPPattern(
							doelIPString), new PortPattern(-1,-1), action);

				}else{
					rule = new Rule(-1, protocol, new IPPattern(bronIPString), new PortPattern(request.getParameter("modal-add-rule-source-port")), new IPPattern(
						doelIPString), new PortPattern(request.getParameter("modal-add-rule-destination-port")), action);
				}
				
				//doe deze wijziging ook in ruleSet voor export
				model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
				model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
				model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
				model.importeer_exporteer.Rule exportRule = rule.export(chain);
				
				if (isShowscope){ //voeg regel toe zodat er geen overlappingen zijn met regels voor de positie van de nieuwe regel.
					//bepaal die positie
					ArrayList<Rule> rules = chain.getRules();
					Space space = new Space(rules);
					int position = space.showScope(rule);
					chain.addRule(rule, position);	
					exportChain.add(position, exportRule);
				}else{ //voeg regel toe achteraan
					chain.addRule(rule);
					exportChain.add(exportRule);
				}
				

				log(request, LogMessageType.ADD_FIREWALL_RULE, "Firewallregel (" + rule + ") " + " toegevoegd aan " + tableName.toLowerCase() + " table en " + chainName.toLowerCase() + " chain");

				redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.SUCCESS, T_ADD_RULE_SUCCESS);
			} catch (CustomException e) {
				redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.DANGER, e.getMessage());
			}
		} else {
			redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.DANGER, T_INVALID_SITUATION);
		}
	}

	/**
	 * Verwijdert regel uit configuratie ten gevolgen van een ajax request om regel te verwijderen.
	 * @param request Request van client.
	 * @param response Response van server.
	 * @throws ServletException Exception van Servlet.
	 * @throws IOException I/O exception.
	 */
	protected void deleteRuleAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		response.setContentType(CONTENT_PLAIN_TEXT);
		response.setCharacterEncoding(UTF_8);

		if (configuration != null && tableName != null && chainName != null) {
			Chain chain = configuration.getTable(tableName).getChain(chainName);

			int ruleNumber = Integer.parseInt(request.getParameter(P_MODAL_DELETE_RULE_INDEX));

			Rule deletedRule = chain.deleteRule(ruleNumber);
			
			//doe deze wijziging ook in ruleSet voor export
			model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
			model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
			model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
			exportChain.remove(ruleNumber-1);

			log(request, LogMessageType.DELETE_FIREWALL_RULE, "Firewallregel (" + deletedRule + ") " + " verwijderd van " + tableName.toLowerCase() + " table en " + chainName.toLowerCase() + " chain");

			response.setStatus(200);
			response.getWriter().write(T_DELETE_RULE_SUCCESS);
		} else {
			response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
			response.getWriter().write(T_DELETE_RULE_FAILED);
		}

		response.getWriter().close();
	}

	/**
	 * Pas een regel aan ten gevolge van een ajax request.
	 * @param request Request van de client.
	 * @param response Response van de server.
	 * @throws ServletException Exception van de Servlet.
	 * @throws IOException I/O Exception.
	 */
	protected void editRuleAjax(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		response.setContentType(CONTENT_PLAIN_TEXT);
		response.setCharacterEncoding(UTF_8);

		if (configuration != null && tableName != null && chainName != null) {
			Chain chain = configuration.getTable(tableName).getChain(chainName);

			//int ruleNumber = Integer.parseInt(request.getParameter(P_EDITABLE_PK));
			int ruleNumber = Integer.parseInt(request.getParameter("rownr"));
			String fieldName = request.getParameter(P_EDITABLE_NAME);
			String fieldValue = request.getParameter(P_EDITABLE_VALUE);

			if (fieldName != null && fieldValue != null) {

				Rule rule = chain.getRule(ruleNumber);
				if (rule != null) {
					switch (fieldName) {
						/*case V_ACTION_EDIT_RULE_PROTOCOL:
							int oudProtocol = rule.getProtocol();
	
							int nieuwProtocol = Integer.parseInt(fieldValue);
	
							rule.setProtocol(nieuwProtocol);
							
							//indien nieuw protocol niet TCP of UDP is dan is bronpoort en doelpoort '*'0
							if(nieuwProtocol != 6 && nieuwProtocol != 17){
								rule.setSourcePort(new PortPattern(-1,-1));
								rule.setDestinationPort(new PortPattern(-1,-1));
							}
	
							log(request, LogMessageType.EDIT_FIREWALL_RULE, "Protocol bijgewerkt van " + oudProtocol + " naar " + nieuwProtocol + " van firewallregel (" + rule + ") " + " van "
									+ tableName.toLowerCase() + " table en " + chainName.toLowerCase() + " chain");
	
							response.setStatus(RESPONSE_STATUS_CODE_GOOD_REQUEST);
							response.getWriter().write(getMessage(request, T_EDIT_RULE_SUCCESS));
							break;*/
						case V_ACTION_EDIT_RULE_SOURCE_IP:
							try {
								String oldValue = rule.getSourceIP().toString();
								//indien geen masker opgegeven is masker 32
								if(!fieldValue.equals("*") && !fieldValue.contains("/")){
									fieldValue += "/32";
								}
								rule.setSourceIP(fieldValue);
	
								log(request, LogMessageType.EDIT_FIREWALL_RULE, "Bron IP bijgewerkt van " + oldValue + " naar " + rule.getSourceIP() + " van firewallregel (" + rule + ") " + " uit de "+ chainName.toLowerCase() + " chain van de "
										+ tableName.toLowerCase() + " table." );
	
								response.setStatus(RESPONSE_STATUS_CODE_GOOD_REQUEST);
								response.getWriter().write(getMessage(request, T_EDIT_RULE_SUCCESS));
							} catch (CustomException e) {
								response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
								response.getWriter().write(getMessage(request, e.getMessage()));
							}
							break;
						case V_ACTION_EDIT_RULE_SOURCE_PORT:
							try {
								String oldValue = rule.getSourcePort().toString();
	
								rule.setSourcePort(fieldValue);
	
								log(request, LogMessageType.EDIT_FIREWALL_RULE, "Bronpoort bijgewerkt van " + oldValue + " naar " + rule.getSourcePort() + " van firewallregel (" + rule + ") " + " uit de "+ chainName.toLowerCase() + " chain van de "
										+ tableName.toLowerCase() + " table." );
	
								response.setStatus(RESPONSE_STATUS_CODE_GOOD_REQUEST);
								response.getWriter().write(getMessage(request, T_EDIT_RULE_SUCCESS));
							} catch (CustomException e) {
								response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
								response.getWriter().write(getMessage(request, e.getMessage()));
							}
	
							break;
						case V_ACTION_EDIT_RULE_DESTINATION_IP:
							try {
								String oldValue = rule.getDestinationIP().toString();
								//indien geen masker opgegeven is masker 32
								if(!fieldValue.equals("*") && !fieldValue.contains("/")){
									fieldValue += "/32";
								}
	
								rule.setDestinationIP(fieldValue);
	
								log(request, LogMessageType.EDIT_FIREWALL_RULE, "Doel IP bijgewerkt van " + oldValue + " naar " + rule.getDestinationIP() + " van firewallregel (" + rule + ") " + " uit de "+ chainName.toLowerCase() + " chain van de "
										+ tableName.toLowerCase() + " table." );
	
								response.setStatus(RESPONSE_STATUS_CODE_GOOD_REQUEST);
								response.getWriter().write(T_EDIT_RULE_SUCCESS);
							} catch (CustomException e) {
								response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
								response.getWriter().write(getMessage(request, e.getMessage()));
							}
							break;
						case V_ACTION_EDIT_RULE_DESTINATION_PORT:
							try {
								String oldValue = rule.getDestinationPort().toString();
	
								rule.setDestinationPort(fieldValue);
	
								log(request, LogMessageType.EDIT_FIREWALL_RULE, "Doelpoort bijgewerkt van " + oldValue + " naar " + rule.getDestinationPort() + " van firewallregel (" + rule + ") "
										+ " uit de "+ chainName.toLowerCase() + " chain van de " + tableName.toLowerCase() + " table." );
	
								response.setStatus(RESPONSE_STATUS_CODE_GOOD_REQUEST);
								response.getWriter().write(getMessage(request, T_EDIT_RULE_SUCCESS));
							} catch (CustomException e) {
								response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
								response.getWriter().write(getMessage(request, e.getMessage()));
							}  
	
							break;
						case V_ACTION_EDIT_RULE_ACTION:
							String oudeAction = rule.getActionString();
	
							if (Integer.parseInt(fieldValue) == 1) {
								rule.setAction(true);
							} else {
								rule.setAction(false);
							}
	
							log(request, LogMessageType.EDIT_FIREWALL_RULE, "Actie bijgewerkt van " + oudeAction + " naar " + rule.getActionString() + " van firewallregel (" + rule + ") " + " uit de "+ chainName.toLowerCase() + " chain van de "
									+ tableName.toLowerCase() + " table." );
	
							response.setStatus(RESPONSE_STATUS_CODE_GOOD_REQUEST);
							response.getWriter().write(getMessage(request, T_EDIT_RULE_SUCCESS));
							break;
						default:
							response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
							response.getWriter().write(getMessage(request, T_BAD_EDIT_ACTION_FOUND));
							break;
					}
					
					//doe deze wijziging ook in ruleSet voor export
					model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
					model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
					model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
					model.importeer_exporteer.Rule exportRule = rule.export(chain);
					exportChain.remove(ruleNumber-1);
					exportChain.add(ruleNumber-1, exportRule);
					
					
				} else {
					response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
					response.getWriter().write(getMessage(request, T_RULE_NOT_FOUND));
				}
			} else {
				response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
				response.getWriter().write(getMessage(request, T_BAD_EDIT_ACTION_FOUND));
			}
		} else {
			response.setStatus(RESPONSE_STATUS_CODE_BAD_REQUEST);
			response.getWriter().write(getMessage(request, T_EDIT_RULE_FAILED));
		}
		
		

		response.getWriter().close();
	}

	/**
	 * Pas een regel aan ten gevolge van een 'gewone' request. De request kan het gevolg zijn van het wijzigen van een regel op een pop-up voor
	 * het wijzigen van een regel.
	 * @param request Request van de client.
	 * @param response Response van de server.
	 * @throws ServletException Exception van de Servlet.
	 * @throws IOException I/O Exception.
	 */
	protected void editRuleModal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		response.setContentType(CONTENT_PLAIN_TEXT);
		response.setCharacterEncoding(UTF_8);

		if (configuration != null && tableName != null && chainName != null) {
			Chain chain = configuration.getTable(tableName).getChain(chainName);

			int ruleNumber = Integer.parseInt(request.getParameter("modal-change-rule-index"));

				Rule rule = chain.getRule(ruleNumber);

				
				if (rule != null) {
					//waarden van regel voor wijziging
					int oldProtocol = rule.getProtocol();
					String oldSourceIP = rule.getSourceIP().toString();
					String oldSourcePort = rule.getSourcePort().toString();
					String oldDestinationIP = rule.getDestinationIP().toString();
					String oldDestinationPort = rule.getDestinationPort().toString();
					String oldAction = rule.getActionString();
					//edit protocol
						int nieuwProtocol = Integer.parseInt(request.getParameter("modal-change-rule-protocol-choose"));
						if(nieuwProtocol == -3){
							nieuwProtocol = Integer.parseInt(request.getParameter("modal-change-rule-protocol-getal"));
						}

						rule.setProtocol(nieuwProtocol);
						
						
					//edit source IP
						try {
							//verkrijg bron-ip
							String bronIPString = request.getParameter("modal-change-rule-source-ip");
							//indien masker niet opgegeven is deze 32 en moet deze aan string toegevoegd worden
							if(!bronIPString.equals("*") && !bronIPString.contains("/")){
								bronIPString += "/32";
							}
							rule.setSourceIP(bronIPString);

						} catch (CustomException e) {
							log(request, LogMessageType.EDIT_FIREWALL_RULE, "Wijzigen van regel mislukt. Nieuwe source IP heeft een ongeldige waarde.");
							redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.WARNING, "my_configuration.edit_rule.wrong_input.source_ip");
							return;
						}
					
					//indien protocol niet TCP of UDP is zal bronpoort en doelpoort ANY moeten zijn
					if(nieuwProtocol != 6 && nieuwProtocol != 17){
						rule.setSourcePort(new PortPattern(-1,-1));
						rule.setDestinationPort(new PortPattern(-1,-1));
					}else{	
					//edit source port
						try { 
							rule.setSourcePort(request.getParameter("modal-change-rule-source-port"));
						} catch (CustomException e) {
							log(request, LogMessageType.EDIT_FIREWALL_RULE, "Wijzigen van regel mislukt. Nieuwe source port heeft een ongeldige waarde.");
							redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.WARNING, "my_configuration.edit_rule.wrong_input.source_port");
							return;
						}
						
						//edit destination port
						try {
							rule.setDestinationPort(request.getParameter("modal-change-rule-destination-port"));
						} catch (CustomException e) {
							log(request, LogMessageType.EDIT_FIREWALL_RULE, "Wijzigen van regel mislukt. Nieuwe destination port heeft een ongeldige waarde.");
							redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.WARNING, "my_configuration.edit_rule.wrong_input.destination_port");
							return;
						}
					}

						
					//edit destination IP
						try {
							//verkrijg doel-ip
							String doelIPString = request.getParameter("modal-change-rule-destination-ip");
							//indien masker niet opgegeven is deze 32 en moet deze aan string toegevoegd worden
							if(!doelIPString.equals("*") && !doelIPString.contains("/")){
								doelIPString += "/32";
							}
							rule.setDestinationIP(doelIPString);

						} catch (CustomException e) {
							log(request, LogMessageType.EDIT_FIREWALL_RULE, "Wijzigen van regel mislukt. Nieuwe destination IP heeft een ongeldige waarde.");
							redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.WARNING, "my_configuration.edit_rule.wrong_input.destination_ip");
							return;
						}
					
						

					    //edit action
						if (Integer.parseInt(request.getParameter("modal-change-rule-action")) == 1) {
							rule.setAction(true);
						} else {
							rule.setAction(false);
						}
						
						String oldProtocolString = ""+oldProtocol;
						if(oldProtocol == -1){
							oldProtocolString = "*";
						}else if(oldProtocol == 1){
							oldProtocolString = "ICMP";
						}else if(oldProtocol == 6){
							oldProtocolString = "TCP";
						}else if(oldProtocol == 17){
							oldProtocolString = "UDP";
						}
						
						String newProtocolString = ""+rule.getProtocol();
						if(rule.getProtocol() == -1){
							newProtocolString = "*";
						}else if(rule.getProtocol() == 1){
							newProtocolString = "ICMP";
						}else if(rule.getProtocol() == 6){
							newProtocolString = "TCP";
						}else if(rule.getProtocol() == 17){
							newProtocolString = "UDP";
						}
						
						
						
						//doe deze wijziging ook in ruleSet voor export
						model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
						model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
						model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
						model.importeer_exporteer.Rule exportRule = rule.export(chain);
						exportChain.remove(ruleNumber-1);
						exportChain.add(ruleNumber-1, exportRule);

						log(request, LogMessageType.EDIT_FIREWALL_RULE, "Regel "+rule.getRuleNr()+" van table "+tableName.toLowerCase()+" van chain "+chainName.toLowerCase()+" bijgewerkt van ("+oldProtocolString+","+oldSourceIP+","+oldSourcePort+","+oldDestinationIP+","+oldDestinationPort+","+oldAction+") naar ("+newProtocolString+","+rule.getSourceIP().toString()+","+rule.getSourcePort().toString()+","+rule.getDestinationIP().toString()+","+rule.getDestinationPort().toString()+","+rule.getActionString()+")");

						redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.SUCCESS, T_EDIT_RULE_SUCCESS);
					
				} else {
					
					log(request, LogMessageType.EDIT_FIREWALL_RULE, "Wijzigen van regel mislukt omdat regel niet bestaat.");
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.WARNING, T_EDIT_RULE_FAILED);

				}
			} else {
				
				log(request, LogMessageType.EDIT_FIREWALL_RULE, "Configuration, table of chain bestaat niet. Daardoor kan de regel niet gewijzigd worden.");
				redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.WARNING, T_EDIT_RULE_FAILED);
			}
	}

	/**
	 * Verwijdert regel uit configuratie.
	 * @param request Request van client.
	 * @param response Response van server.
	 * @throws ServletException Exception van Servlet.
	 * @throws IOException I/O Exception.
	 */
	protected void deleteRule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		if (configuration != null && tableName != null && chainName != null) {
			Chain chain = configuration.getTable(tableName).getChain(chainName);

			int ruleNumber = Integer.parseInt(request.getParameter(P_MODAL_DELETE_RULE_INDEX));

			Rule deletedRule = chain.deleteRule(ruleNumber);
			
			//doe deze wijziging ook in ruleSet voor export
			model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
			model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
			model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
			exportChain.remove(ruleNumber-1);

			log(request, LogMessageType.DELETE_FIREWALL_RULE,
					"Firewallregel " + ruleNumber + " (" + deletedRule + ") " + " verwijderd uit " + tableName.toLowerCase() + " table en " + chainName.toLowerCase() + " chain");

			//indien verzoek om regel te verwijderen komt vanuit de anomalies pagina, dan moet terug naar de anomalies pagina gegaan worden
			if(request.getParameter("modal-delete-rule-context-page").equals("anomalies_page")){
				redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.SUCCESS, T_DELETE_RULE_SUCCESS);
			}else{
				redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.SUCCESS, T_DELETE_RULE_SUCCESS);
			}
			
		} else {
			redirect(request, response, JSP_MY_CONFIGURATION_RULES, AlertMessageType.DANGER, T_DELETE_RULE_FAILED);
		}
	}
	
	/**
	 * Handel request af om policy van huidige chain nieuwe waarde te geven.
	 * @param request Request van client.
	 * @param response Response van server.
	 * @throws ServletException Exception van Servlet.
	 * @throws IOException I/O Exception.
	 */
	protected void editPolicy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);
		
		response.setContentType(CONTENT_APPLICATION_JSON);
		response.setCharacterEncoding(UTF_8);
		PrintWriter writer = response.getWriter();

		if (configuration != null && tableName != null && chainName != null) {
			Chain chain = configuration.getTable(tableName).getChain(chainName);
			String oldPolicyString = chain.getPolicy().getName();
			
			//doe deze wijziging ook in ruleSet voor export.  
			model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
			model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
			model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
			
			String newPolicyInt = request.getParameter("select-default-policy");
			
			
			
			
			
			if(newPolicyInt.equals("1")){
				chain.setPolicy(PolicyAction.ACCEPT);
				exportChain.setDefaultPolicy(Policy.ACCEPT);
				log(request, LogMessageType.SET_DEFAULT_POLICY, "De default policy van chain " + chainName + " uit tabel " + tableName + " is gewijzigd van " + oldPolicyString + " naar ACCEPT.");				
				//redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.SUCCESS, T_SET_DEFAULT_POLICY_SUCCESS_ACCEPT);
				writer.write("{\"policy\":\"ACCEPT\"}");
				

			}else if(newPolicyInt.equals("0")){
				chain.setPolicy(PolicyAction.DROP);
				exportChain.setDefaultPolicy(Policy.DROP);
				log(request, LogMessageType.SET_DEFAULT_POLICY, "De default policy van chain " + chainName + " uit tabel " + tableName + " is gewijzigd van " + oldPolicyString + " naar DROP.");				
				//redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.SUCCESS, T_SET_DEFAULT_POLICY_SUCCESS_DROP);
				writer.write("{\"policy\":\"DROP\"}");
			}else{
				//redirect(request, response, JSP_MY_CONFIGURATION_RULES, AlertMessageType.DANGER, T_SET_DEFAULT_POLICY_FAILED);
			}

		}else{
			//redirect(request, response, JSP_MY_CONFIGURATION_RULES, AlertMessageType.DANGER, T_SET_DEFAULT_POLICY_FAILED);
		}
		writer.flush();
		writer.close();
	}
	
	
	/**
	 * Handel request af om protocol te wijzigen. De request komt van de modal om een protocol in te voeren.
	 * @param request Request van client.
	 * @param response Response van server.
	 * @throws ServletException Exception van Servlet.
	 * @throws IOException I/O Exception.
	 */
	protected void editProtocolModal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (String) request.getSession().getAttribute(P_CHAIN);

		Configuration configuration = getConfiguration(request);

		if (configuration != null && tableName != null && chainName != null) {
			try{
				Chain chain = configuration.getTable(tableName).getChain(chainName);
				
				int ruleNr = Integer.parseInt(request.getParameter("modal-choose-protocol-rulenr"));
				Rule rule = chain.getRule(ruleNr);
				int oldProtocol = rule.getProtocol();
				int newProtocol = Integer.parseInt(request.getParameter("modal-choose-protocol-option"));
				if(newProtocol == -3){
					newProtocol = Integer.parseInt(request.getParameter("modal-choose-protocol-getal"));
				}
				rule.setProtocol(newProtocol);
				
				String oldProtocolString = ""+oldProtocol;
				if(oldProtocol == -1){
					oldProtocolString = "*";
				}else if(oldProtocol == 1){
					oldProtocolString = "ICMP";
				}else if(oldProtocol == 6){
					oldProtocolString = "TCP";
				}else if(oldProtocol == 17){
					oldProtocolString = "UDP";
				}
				
				String newProtocolString = ""+newProtocol;
				if(newProtocol == -1){
					newProtocolString = "*";
				}else if(newProtocol == 1){
					newProtocolString = "ICMP";
				}else if(newProtocol == 6){
					newProtocolString = "TCP";
				}else if(newProtocol == 17){
					newProtocolString = "UDP";
				}
				
				
				//doe deze wijziging ook in ruleSet voor export
				model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
				model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
				model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
				model.importeer_exporteer.Rule exportRule = rule.export(chain);
				exportChain.remove(ruleNr-1);
				exportChain.add(ruleNr-1, exportRule);
				
				log(request, LogMessageType.EDIT_FIREWALL_RULE, "Protocol bijgewerkt van " + oldProtocolString + " naar " + newProtocolString + " van firewallregel (" + rule + ") " + " uit de "
						 + chainName.toLowerCase() + " chain van de "+ tableName.toLowerCase() + " table.");
				
				response.setContentType(CONTENT_APPLICATION_JSON);
				response.setCharacterEncoding(UTF_8);
				PrintWriter writer = response.getWriter();
				
				writer.write("{\"newProtocol\":\""+newProtocol+"\"}");
				
				writer.flush();
				writer.close();
				
				//redirect(request, response, SERVLET_CONFIGURATION_RULES, AlertMessageType.SUCCESS, T_SET_PROTOCOL_MODAL_SUCCESS);
			}catch(NumberFormatException nfe){
				redirect(request, response, JSP_MY_CONFIGURATION_RULES, AlertMessageType.DANGER, T_SET_PROTOCOL_MODAL_FAILED);
			}
		}else{
			redirect(request, response, JSP_MY_CONFIGURATION_RULES, AlertMessageType.DANGER, T_SET_PROTOCOL_MODAL_FAILED);
		}
	}
}

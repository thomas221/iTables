package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import util.AlertMessageType;
import exception.CustomException;
import model.configuration.Chain;
import model.configuration.Configuration;
import model.configuration.CorrelationGroup;
import model.configuration.RedundancySolution;
import model.configuration.SetOfGroups;
import model.configuration.Space;
import model.importeer_exporteer.RuleSet;
import model.logfile.LogMessageType;
import static util.Constants.*;

/**
 * Servlet verantwoordelijk voor het geven van de redundante regels. De servlet
 * handelt AJAX requests af. De servlet geeft alle regelnummers terug van de
 * regels uit de huidige configuratie die redundant zijn.
 * @author Thomas Van Poucke
 */
@WebServlet("/my_configuration/show_redundancy")
public class MyConfigurationShowRedundancyServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationShowRedundancyServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (request.getParameter(P_TABLE) != null) ? request.getParameter(P_TABLE) : (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (request.getParameter(P_CHAIN) != null) ? request.getParameter(P_CHAIN) : (String) request.getSession().getAttribute(P_CHAIN);
		
		response.setContentType(CONTENT_APPLICATION_JSON);
		response.setCharacterEncoding(UTF_8);
		PrintWriter writer = response.getWriter();
		
		Configuration configuration = getConfiguration(request);
		
		try {
			if (configuration != null && tableName != null && chainName != null) {
				Space space = configuration.getTable(tableName).getChain(chainName).getSpace();

				JSONObject content = space.getRedundantRulesAsJSON();

				writer.write(content.toJSONString());
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (request.getParameter(P_TABLE) != null) ? request.getParameter(P_TABLE) : (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (request.getParameter(P_CHAIN) != null) ? request.getParameter(P_CHAIN) : (String) request.getSession().getAttribute(P_CHAIN);
		
		Configuration configuration = getConfiguration(request);
		
		try{
			if (configuration != null && tableName != null && chainName != null) {
				Chain chain = configuration.getTable(tableName).getChain(chainName);
				Space space = chain.getSpace();
				SetOfGroups setOfGroups = space.createCorrelationGroups();
		
				for (CorrelationGroup group : setOfGroups.getCorrelationGroups()) {
					RedundancySolution redundancySolution = space.determineRedundantRules(group);
					ArrayList<Integer> redundantRuleNrs = redundancySolution.getRedundantRuleNrs();
					
					ArrayList<Integer> redundantRuleIndexes = new ArrayList<Integer>();
					for(int i=0;i<redundantRuleNrs.size();i++){
						redundantRuleIndexes.add(redundantRuleNrs.get(i)-1);
					}
					//verwijder redundante regels
					chain.deleteRules(redundantRuleIndexes);
					
					//doe deze wijziging ook in ruleSet voor export
					model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
					model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
					model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
					exportChain.removeAll(redundantRuleNrs);
				}

				log(request, LogMessageType.REMOVE_REDUNDANT_RULES, "Alle redundante regels zijn verwijderd uit de "+chainName+" chain van de "+tableName+" table.");
				redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.SUCCESS, "redundant.solution.SUCCES");
			}else{
				log(request, LogMessageType.REMOVE_REDUNDANT_RULES, "Onverwachte fout gebeurd bij het verwijderen van redundante regels uit de "+chainName+" chain van de "+tableName+" table.");								
				redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.DANGER, "redundant.solution.ERROR");
			}
		} catch (CustomException ce) {
			log(request, LogMessageType.REMOVE_REDUNDANT_RULES, "Onverwachte fout gebeurd bij het verwijderen van redundante regels uit de "+chainName+" chain van de "+tableName+" table.");								
			redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.DANGER, "redundant.solution.ERROR");
		}
	}

}

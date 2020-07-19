package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.CustomException;
import util.AlertMessageType;
import model.configuration.Configuration;
import model.configuration.ConflictSolution;
import model.configuration.ConflictStatus;
import model.configuration.CorrelationGroup;
import model.configuration.Rule;
import model.configuration.Segment;
import model.configuration.SetOfGroups;
import model.configuration.Space;
import model.importeer_exporteer.RuleSet;
import model.logfile.LogMessageType;
import static util.Constants.*;

/**
 * Deze servlet handelt verzoeken af om policy conflicten op te lossen in een
 * firewall configuratie. De servlet ontvangt AJAX requests. Daarop wordt de
 * regels van de firewall configuratie zo opnieuw geordend zodat voldaan is aan
 * de meegegeven action constraints. De Space klasse wordt daarna opnieuw
 * gemaakt en de gebruiker krijgt de pagina configuration_rules opnieuw te zien
 * met de nieuwe configuratie.
 */
@WebServlet("/my_configuration/solve_conflicts")
public class MyConfigurationSolveConflictsServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationSolveConflictsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (request.getParameter(P_TABLE) != null) ? request.getParameter(P_TABLE) : (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (request.getParameter(P_CHAIN) != null) ? request.getParameter(P_CHAIN) : (String) request.getSession().getAttribute(P_CHAIN);

		// Configuratie ophalen
		Configuration configuration = (Configuration) request.getSession().getAttribute(P_CONFIGURATION);

		if (configuration != null && tableName != null && chainName != null) {
			try {
				Space space = configuration.getTable(tableName).getChain(chainName).getSpace();
				SetOfGroups setOfGroups = space.createCorrelationGroups();
				CorrelationGroup group = setOfGroups.getGroup(Integer.parseInt(request.getParameter("correlation_group_number")) - 1);

				// bepaal action constraint uit ingevuld formulier
				ArrayList<Boolean> actionConstraint = new ArrayList<Boolean>();
				
				//verkrijg ConflictStatus uit sessie
				ConflictStatus conflictStatus = (ConflictStatus)request.getSession().getAttribute("ConflictStatus");

				// ga action constraint na voor elk conflicterend segment
				for (Segment segment : group.getSegments()) {	
					if (segment.isOverlapping() && segment.getConflicting()) {
						boolean actionSegment = segment.getRule(0).getAction();;
						// verkijg actie uit formulier
						String actie = request.getParameter("action_" + segment.getSegmentNumber());
						// zet actie in actionConstraint
						if (actie.equals("allow")) {
							actionConstraint.add(true);
						  // Houd bij wat de voorkeur actie is van het conflicterend segment
							if (actionSegment)
								conflictStatus.setStatus(segment, ConflictStatus.Status.APPROVED_YES);
							else
								conflictStatus.setStatus(segment, ConflictStatus.Status.APPROVED_NO);
						} else {
							actionConstraint.add(false);	
							// Houd bij wat de voorkeur actie is van het conflicterend segment
							if (actionSegment)
								conflictStatus.setStatus(segment, ConflictStatus.Status.APPROVED_NO);
							else
								conflictStatus.setStatus(segment, ConflictStatus.Status.APPROVED_YES);
						}
					}
				}
				
				
				 /* --- code zoals het was ---
				// ga action constraint na voor elk conflicterend segment
				for (Segment segment : group.getSegments()) {
					if (segment.isOverlapping() && segment.getConflicting()) {
						// verkijg actie uit formulier
						String actie = request.getParameter("action_" + segment.getSegmentNumber());
						// zet actie in actionConstraint
						if (actie.equals("allow")) {
							actionConstraint.add(true);
							//Hou bij dat conflicterend segment opgelost is met een voorkeur van allow
							conflictStatus.setStatus(segment, ConflictStatus.Status.APPROVED_YES);
						} else {
							actionConstraint.add(false);
							//Hou bij dat conflicterend segment opgelost is met een voorkeur van deny
							conflictStatus.setStatus(segment, ConflictStatus.Status.APPROVED_NO);
						}
					}
				}
         */
				
				// Conflicten oplossen
				ConflictSolution conflictSolution = space.tryConflictSolution(group, actionConstraint, false);
				

				// Status van oplossing opvragen
				ConflictSolution.Status status = conflictSolution.getStatus();

				// Logbestand bijwerken

				switch (status) {
				case NO_STATUS_YET:
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "onverwachte fout opgetreden");
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.DANGER, "conflict.solution.NO_STATUS_YET");
					break;
				case NO_REORDERING_REQUIRED:
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "geen herordening vereist");
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.INFO, "conflict.solution.NO_REORDERING_REQUIRED");
					break;
				case NO_SOLUTION_EXISTS:
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "geen oplossing mogelijk");					
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.INFO, "conflict.solution.NO_SOLUTION_EXISTS");
					break;
				case SOLUTION_FOUND:
					ArrayList<Rule> rules = space.getRules();
					//verkrijg RuleSet voor exporteren
					model.importeer_exporteer.RuleSet exportRuleSet = (RuleSet)request.getSession().getAttribute(EXPORT_RULESET);
					conflictSolution.commitSolutionInRulesAndExportRules(rules, exportRuleSet, tableName, chainName);
					
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "regels werden herordend");					
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.SUCCESS, "conflict.solution.SOLUTION_FOUND");
					break;
				case ERROR_IN_INPUT:
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "onverwachte fout opgetreden");					
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.DANGER, "conflict.solution.ERROR_IN_INPUT");
					break;
				case TO_COMPLEX:
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "het berekenen van een geschikte herordening is te complex");					
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.INFO, "conflict.solution.TO_COMPLEX");
					break;
				default:
					log(request, LogMessageType.SEGMENT_VOORKEURSACTIES_WIJZIGEN, "Table: " + request.getSession().getAttribute(P_TABLE) + "; chain: " + request.getSession().getAttribute(P_CHAIN) + "; correlatiegroep: " + group.getGroupNumber() + "; resultaat: " + "onverwachte fout opgetreden");					
					
					redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.INFO, "conflict.solution.BAD_CASE");
					break;
				}
			} catch (CustomException e) {
				redirect(request, response, SERVLET_CONFIGURATION_ANOMALIES, AlertMessageType.DANGER, e.getMessage());
			}
		} else {
			forward(request, response, JSP_MY_CONFIGURATION_ANOMALIES);
		}
	}

}

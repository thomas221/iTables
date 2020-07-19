package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.configuration.Configuration;
import model.configuration.ConflictStatus;
import model.configuration.CorrelationGroup;
import model.configuration.Rule;
import model.configuration.Segment;
import model.configuration.SetOfGroups;
import model.configuration.Space;
import util.AlertMessageType;
import exception.CustomException;
import static util.Constants.*;

/**
* @author Thomas Van Poucke
 */
@WebServlet("/my_configuration/anomalies")
public class MyConfigurationAnomaliesServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationAnomaliesServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = (request.getParameter(P_TABLE) != null) ? request.getParameter(P_TABLE) : (String) request.getSession().getAttribute(P_TABLE);
		String chainName = (request.getParameter(P_CHAIN) != null) ? request.getParameter(P_CHAIN) : (String) request.getSession().getAttribute(P_CHAIN);

		// Configuratie ophalen
		Configuration configuration = (Configuration) request.getSession().getAttribute(P_CONFIGURATION);

		if (configuration != null && tableName != null && chainName != null) {
			try {
				request.getSession().setAttribute(P_TABLE, tableName);
				request.getSession().setAttribute(P_CHAIN, chainName);

				ArrayList<Rule> rules = configuration.getTable(tableName).getChain(chainName).getRules();

				request.setAttribute(P_RULES, rules);

				Space space = new Space(rules);

				request.setAttribute(P_SPACE, space);

				ArrayList<Segment> segments = space.getSegments();
				SetOfGroups correlationGroups = space.createCorrelationGroups();

				request.setAttribute(P_SEGMENTS, segments);
				request.setAttribute(P_CORRELATION_GROUPS, correlationGroups);
				
				//check of er al een ConflictStatus object in de sessie zit om de opgeloste conflicten bij te houden. Zo nee maak nieuw ConflictStatus object aan.
				ConflictStatus conflictStatus = (ConflictStatus)request.getSession().getAttribute("ConflictStatus");
				if(conflictStatus == null){
					conflictStatus = new ConflictStatus();
					request.getSession().setAttribute("ConflictStatus", conflictStatus);
				}else{
					//pas isSolved attribuut aan van elke groep
					for (CorrelationGroup group : correlationGroups.getCorrelationGroups()){
						
						//een correlation group met conflicten is reeds opgelost als elk van zijn conflicterende segmenten reeds opgelost zijn.
						if(group.isConflicting()){
							boolean isSolved = true;
							for(Segment segment : group.getSegments()){
								if(segment.isOverlapping() && segment.getConflicting()){
									if (!(conflictStatus.getStatus(segment) == ConflictStatus.Status.APPROVED_YES)) {
										isSolved = false;
									}
								}
							}
							group.setIsSolved(isSolved);
						}
						
						
						 /* --- oude code ---
						//een correlation group met conflicten is reeds opgelost als elk van zijn conflicterende segmenten reeds opgelost zijn.
						if(group.isConflicting()){
							boolean isSolved = true;
							for(Segment segment : group.getSegments()){
								if(segment.isOverlapping() && segment.getConflicting()){
									if(!(conflictStatus.getStatus(segment) == ConflictStatus.Status.APPROVED_YES) && !(conflictStatus.getStatus(segment) == ConflictStatus.Status.APPROVED_NO)){
										isSolved = false;
									}
								}
							}
							group.setIsSolved(isSolved);
						}
						 */
			
						else{
							//correlation groups zonder conflicten zijn uiteraard niet opgelost
							group.setIsSolved(false);
						}
						
					}
				}
				
				

				forward(request, response, JSP_MY_CONFIGURATION_ANOMALIES);
			} catch (CustomException ce) {
				forward(request, response, JSP_MY_CONFIGURATION_ANOMALIES, AlertMessageType.DANGER, T_CHAIN_OR_TABLE_DOES_NOT_EXIST);
			}
		} else {
			forward(request, response, JSP_MY_CONFIGURATION_ANOMALIES);
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

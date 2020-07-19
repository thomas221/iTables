package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import exception.CustomException;
import model.configuration.Configuration;
import model.configuration.Space;
import static util.Constants.*;

/**
 * Deze controller is verantwoordelijk voor het afhandelen van AJAX requests
 * voor het tonen van de inhoud van een segment. Daarop stuurt de controller een
 * response terug die een modal is met de informatie.
 */
@WebServlet("/my_configuration/segment_content")
public class MyConfigurationSegmentContentServlet extends ExtendableServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyConfigurationSegmentContentServlet() {
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

		Configuration configuration = (Configuration) request.getSession().getAttribute(P_CONFIGURATION);
		
		try {
			if (configuration != null && tableName != null && chainName != null) {
				Space space = configuration.getTable(tableName).getChain(chainName).getSpace();
				int segmentNumber = Integer.parseInt(request.getParameter(P_SEGMENT_NUMBER));
				
				int compact = Integer.parseInt(request.getParameter(P_COMPACT));
				JSONObject content = space.getSegmentContentAsJSON(segmentNumber, compact);

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
		// Niet antwoorden op post-request
	}

}

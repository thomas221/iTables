package util;

import static util.Constants.*;

/**
 * Hulp klasse voor in JSP pagina's.
 */
public class CustomJSPTags {

	/**
	 * Geef opgegeven actie terug als de weer te geven string in de JSP pagina.
	 * 
	 * @param action
	 *            Opgegeven actie.
	 * @return String die weergegeven moet worden in JSP pagina naargelang de
	 *         actie.
	 */
	public static String actionToString(boolean action) {
		if (action) {
			return CONFIGURATION_ACTION_ACCEPT;
		} else {
			return CONFIGURATION_ACTION_REJECT;
		}
	}

	/**
	 * Geef benaming van CSS klasse terug naargelang de opgegeven acie. Zo wordt
	 * de CSS klasse aangepast aan de actie.
	 * 
	 * @param action
	 *            Opgegeven actie.
	 * @return Benaming van een CSS klasse naargelang de actie.
	 */
	public static String actionToCssTrClass(boolean action) {
		if (action) {
			return CONFIGURATION_ACTION_ACCEPT_CSS_CLASS_TR;
		} else {
			return CONFIGURATION_ACTION_REJECT_CSS_CLASS_TR;
		}
	}

	public static String actionToCssLabelClass(boolean action) {
		if (action) {
			return CONFIGURATION_ACTION_ACCEPT_CSS_CLASS_LABEL;
		} else {
			return CONFIGURATION_ACTION_REJECT_CSS_CLASS_LABEL;
		}
	}

	public static String getCssClassPanelConflictingCorrelationGroup(boolean conflicting, boolean isSolved) {
		if (conflicting && !isSolved) {
			return "panel-danger";
		} else {
			return "panel-success";
		}
	}
	
	public static String getVersionLayoutFiles() {
		return VERSION_LAYOUT_FILES;
	}

	public static boolean isBeta() {
		return BETA;
	}

	public static boolean isDebug() {
		return DEBUG;
	}

}

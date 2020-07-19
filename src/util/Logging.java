package util;

import static util.Constants.I_NAME_ROOTLOGGER;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hulpklasse voor het loggen van berichten. Deze biedt een klassevariabele aan
 * van het type Logger waarmee berichten gelogd kunnen worden.
 * 
 * 
 */
public class Logging {
	/**
	 * Klasse attribuut dat de logger bijhoudt.
	 */
    public static Logger logger = LogManager.getLogger(I_NAME_ROOTLOGGER);
}

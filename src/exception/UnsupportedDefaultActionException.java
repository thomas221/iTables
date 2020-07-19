package exception;

/**
 * Chain heeft default action die niet ondersteund wordt. Enkel ACCEPT en DROP zijn toegelaten.
 * @author Thomas
 *
 */
public class UnsupportedDefaultActionException extends Exception{		

	/**
	 * Cre&euml;er exception voor geval dat een chain een default action heeft die niet ondersteund wordt. Enkel ACCEPT en DROP zijn toegelaten.
	 * @param foutmelding Foutmelding tekst.
	 */
	public UnsupportedDefaultActionException(String foutmelding) {
		super(foutmelding);
	}
}

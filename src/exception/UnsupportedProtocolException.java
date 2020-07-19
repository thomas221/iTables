package exception;

/**
 * Protocol wordt niet ondersteund.
 * @author Thomas Van Poucke
 *
 */
public class UnsupportedProtocolException extends Exception{
	
	/**
	 * Creeer exception voor geval dat een protocol niet ondersteund wordt.
	 * @param foutmelding Foutmelding tekst.
	 */
	public UnsupportedProtocolException(String foutmelding) {
		super(foutmelding);
	}
}

package exception;

/**
 * Exception die opgegooid kan worden als de poort buiten het toegelaten bereik valt.
 */
public class PoortBuitenBereikException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Constructor voor het opgooien van een Exception als de poort buiten het toegelaten bereik valt.
	 * @param foutmelding De foutmelding.
	 */
	public PoortBuitenBereikException(String foutmelding) {
		super(foutmelding);
	}

}

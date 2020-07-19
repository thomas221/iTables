package exception;

/**
 * Exception die opgegooid kan worden als een bestand niet blijkt te bestaan.
 */
public class NietBestaandBestandException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Constructor voor het aanmaken van een exception als een bestand niet blijkt te bestaan.
	 * @param foutmelding Een foutmelding.
	 */
	public NietBestaandBestandException(String foutmelding) {
		super(foutmelding);
	}

}

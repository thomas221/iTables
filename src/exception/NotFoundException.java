package exception;

/**
 * Exception die opgegooid kan worden als iets niet gevonden is.
 */
public class NotFoundException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Constructor voor het creeren van een exception als iets niet gevonden is.
	 * @param foutmelding Een foutmelding.
	 */
	public NotFoundException(String foutmelding) {
		super(foutmelding);
	}

}

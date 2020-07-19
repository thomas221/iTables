package exception;

/**
 * Exception die opgegooid kan worden als een parameter niet blijkt te bestaan.
 */
public class NietBestaandeParameterException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Constructor voor het creeren van een exception als een parameter niet blijkt te bestaan.
	 * @param foutmelding Een foutmelding.
	 */
	public NietBestaandeParameterException(String foutmelding) {
		super(foutmelding);
	}

}

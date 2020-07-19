package exception;

/**
 * Exception die opgegooid kan worden als een parameter van een fout type is.
 */
public class FoutiefParametertypeException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Constructor voor het opgooien van een exception als een parameter van een fout type is.
	 * @param foutmelding Een foutmelding.
	 */
	public FoutiefParametertypeException(String foutmelding) {
		super(foutmelding);
	}

}

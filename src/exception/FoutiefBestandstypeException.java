package exception;

/**
 * Exception die opgegooid kan worden als een bestand van een fout type is.
 */
public class FoutiefBestandstypeException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Constructor voor een exception als een bestand van een fout type is.
	 * @param foutmelding Een foutmelding.
	 */
	public FoutiefBestandstypeException(String foutmelding) {
		super(foutmelding);
	}

}

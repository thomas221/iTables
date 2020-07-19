package exception;

/**
 * Exception die opgegooid kan worden als een parameter null blijkt te zijn.
 */
public class NullParameterException extends Exception {
    private static final long serialVersionUID = -3963671124495296209L;

    /**
     * Constructor voor het aanmaken van een exception als een parameter null blijkt te zijn.
     * @param foutmelding Een foutmelding.
     */
	public NullParameterException(String foutmelding) {
		super(foutmelding);
	}

}

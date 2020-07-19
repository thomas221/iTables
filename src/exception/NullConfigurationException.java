package exception;

/**
 * Exception die opgegooid kan worden als een configuratie leeg blijkt te zijn.
 */
public class NullConfigurationException extends Exception {
    private static final long serialVersionUID = 6105378111658308851L;

    /**
     * Constructor voor het maken van een exception als een configuratie leeg blijkt te zijn.
     * @param foutmelding Een foutmelding.
     */
	public NullConfigurationException(String foutmelding) {
		super(foutmelding);
	}

}

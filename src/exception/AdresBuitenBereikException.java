package exception;

/**
 * Exception voor gevallen dat het adres buiten het bereik valt.
 */
public class AdresBuitenBereikException extends Exception {
	private static final long serialVersionUID = -4460900952048768731L;

	/**
	 * Creeer exception voor geval dat het adres buiten het bereik valt.
	 * @param foutmelding Foutmelding tekst.
	 */
	public AdresBuitenBereikException(String foutmelding) {
		super(foutmelding);
	}

}

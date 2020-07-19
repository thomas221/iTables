package exception;

/**
 * Wordt opgegooid als er een fout is gebeurd bij het parsen.
 *
 */
public class ParseException extends ExtendableException {

	/**
	 * Maak een Exception die aangeeft dat er een fout is gebeurd bij het parsen.
	 * @param error Opgegeven fout.
	 * @param parameter Opgegeven parameter.
	 */
    public ParseException(String error, String parameter) {
	super(error, parameter);
    }

}

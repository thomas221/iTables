package exception;

/**
 * Geeft aan dat er een Exception gebeurd is bij het werken met een bestand.
 */
public class FileException extends ExtendableException {
    
	/**
	 * Maak FileException aan met opgegeven fout als string.
	 * @param error De gebeurde fout.
	 */
    public FileException(String error) {
	super(error);
    }

    /**
     * Maak FileException aan met opgegeven fout en parameter.
     * @param error Opgegeven fout.
     * @param parameter Opgegeven parameter.
     */
    public FileException(String error, String parameter) {
	super(error, parameter);
    }

}

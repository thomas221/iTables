package exception;

/**
 * Aangepaste Exception die kan opgegooid worden in de webapplicatie.
 */
public class CustomException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception e;

	/**
	 * Publieke constructor die een foutmelding meegeeft.
	 * 
	 * @param foutmelding
	 *            De foutmelding van de exceptie.
	 */
	public CustomException(String foutmelding) {
		super(foutmelding);
	}

	/**
	 * Publieke constructor die een foutmelding en een andere Exception meegeeft. Die andere exception kan bijvoorbeeld informatiever zijn over de fout die opgegooid wordt.
	 * @param foutmelding De foutmelding van de exception.
	 * @param e Een andere exception, deze kan evt. informatiever zijn over de fout die opgegooid wordt.
	 */
	public CustomException(String foutmelding, Exception e) {
		super(foutmelding);
		this.e = e;
	}

	/**
	 * Geef informatie terug over de oorzaak van de Exception. Daarvoor wordt de informatie opgeroepen van de Exception attribuut e. Als deze aanwezig is zal deze informatiever zijn.
	 * @return Informatie over de oorzaak van de Exception.
	 */
	public String getOorzaak() {
		if (e != null) {
			return e.getMessage();
		} else {
			return "";
		}
	}
}

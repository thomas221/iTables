package util;

/**
 * Enumeratie klasse die het soort bericht kan weergeven.
 */
public enum AlertMessageType {
	/**
	 * Geeft aan dat het om een succes bericht gaat.
	 */
    SUCCESS(1), 
    /**
     * Geeft aan dat het om een informatie bericht gaat.
     */
     INFO(2), 
     /**
      * Geeft aan dat het om een waarschuwingsbericht gaat.
     */
     WARNING(3), 
     /**
      * Geeft aan dat het bericht een of ander gevaar aangeeft.
      */
     DANGER(4);

    /**
     * Index getal dat type bericht aangeeft.
     */
    private int index;

    /**
     * Maak bericht type dat overeenstemt met opgegeven waarde voor parameter index.
     * @param index Getal dat type bericht aangeeft.
     */
    private AlertMessageType(int index) {
	this.index = index;
    }

    /**
     * Geef het index getal terug dat overeenstemt met dit type bericht.
     * @return Index getal dat hoort bij dit type bericht.
     */
    public int getIndex() {
	return index;
    }

}

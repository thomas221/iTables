/**
 * 
 */
package model.configuration;

import exception.CustomException;

import static util.Constants.*;

/**
 * Stelt een poort patroon voor in een firewall regel. Als gekeken wordt of een
 * bronpoort of doelpoort matcht aan een regel, dan wordt de bronpoort en
 * doelpoort vergeleken met dit patroon.
 * 
 * @author Thomas Van Poucke
 * 
 */
public class PortPattern {

	/**
	 * ondergrens vanaf; -1 indien refersToallPorts true
	 */
	private int lowerBound = 0;
	/**
	 * bovengrens; tot en met; -1 indien refersToallPorts true
	 */
	private int upperBound = 0;
	/**
	 * Indien deze true is matcht het poort patroon met alle mogelijke poorten.
	 */
	private boolean refersToAllPorts = false;

	/**
	 * Constructor die attributen initiele waarden geeft. Indien lowerBound of
	 * upperBound -1 zal elke poort matchen met het patroon.
	 * 
	 * @param lowerBound
	 *            Waarde voor lowerBound. Indien -1 zal elke poort matchen met
	 *            het patroon.
	 * @param upperBound
	 *            Waarde voor upperBound. Indien -1 zal elke poort matchen met
	 *            het patroon.
	 */
	public PortPattern(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		if (lowerBound == -1 || upperBound == -1) {
			setRefersToAllPorts(true);
		}
	}

	/*
	 * Constructor die op basis van string PortPattern aanmaakt. Deze String
	 * moet van de vorm "poort" of "beginpoort:eindpoort" zijn - Indien poort,
	 * beginpoort of eindpoort -1 zal elke poort matchen met het patroon. Ook
	 * als de waarde "*" of "ANY" is zal elke poort matchen met het patroon.
	 * 
	 * @param port Poort als String. Deze String moet van de vorm "poort" of
	 * "beginpoort:eindpoort" zijn. Als de waarde "*" of "ANY" is zal elke poort
	 * matchen met het patroon.
	 * 
	 * @throws CustomException Als er iets fout loopt bij het maken van het
	 * PortPattern object op basis van de String. De String moet van de vorm
	 * "poort" of "beginpoort:eindpoort" zijn.
	 */

	/**
	 * Constructor die op basis van een String een PortPattern aanmaakt. Deze
	 * String moet een enkel poortnummer betreffen ("poort") of van de vorm
	 * "beginpoort:eindpoort" zijn - Indien poort, beginpoort of eindpoort
	 * gelijk is aan -1 zal elke poort matchen met het patroon. Ook als de
	 * waarde "*" of "ANY" is zal elke poort matchen met het patroon.
	 * 
	 * @param port
	 *            Poort als String. Deze String moet een enkel poortnummer
	 *            betreffen ("poort") of van de vorm "beginpoort:eindpoort"
	 *            zijn. Als de waarde "*" of "ANY" is zal elke poort matchen met
	 *            het patroon.
	 * @throws CustomException
	 *             Als er iets fout loopt bij het maken van het PortPattern
	 *             object op basis van de String. De String moet van de vorm
	 *             "poort" of "beginpoort:eindpoort" zijn.
	 */
	public PortPattern(String port) throws CustomException {
		String[] delen = port.trim().split(":");
		try {
			if (delen.length == 1) { // bv. poort 80, het gaat om een enkele
										// poort

				// ga na of het om elke poort gaat, dus een poort patroon
				// waaraan alle poorten matchen
				if (delen[0].equals("*") || delen[0].equals("ANY")) {
					setRefersToAllPorts(true);
				} else {
					// begin poort is hier gelijk aan eind poort
					int lowerBound = Integer.parseInt(delen[0]);

					// controleer of ondergrens en bovengrens tussen 0 en 65535
					// liggen. Hier ondergrens = bovengrens.
					if (0 <= lowerBound && lowerBound <= 65535 && delen[0].charAt(0) != '-' && !(lowerBound > 0 && delen[0].charAt(0) == '0') && !(lowerBound == 0 && !delen[0].equals("0"))) {
						this.lowerBound = lowerBound;
						this.upperBound = lowerBound; // hier lowerBound =
														// upperBound, want
														// slechts 1 getal als
														// poort opgegeven
					} else {
						throw new CustomException(T_INVALID_PORT_PATTERN);
					}
				}

				// geval dat zowel begin poort als eind poort opgegeven is
			} else if (delen.length == 2) {
				// zet ondergrens en bovengrens in variabele
				int lowerBound = Integer.parseInt(delen[0]);
				int upperBound = Integer.parseInt(delen[1]);

				// controleer of ondergrens en bovengrens tussen 0 en 65535
				// liggen, en dat ondergrens <= bovengrens
				boolean lowerBoundCorrect = 0 <= lowerBound && lowerBound <= 65535 && delen[0].charAt(0) != '-' && !(lowerBound > 0 && delen[0].charAt(0) == '0') && !(lowerBound == 0 && !delen[0].equals("0"));
				boolean upperBoundCorrect = 0 <= upperBound && upperBound <= 65535 && delen[1].charAt(0) != '-' && !(upperBound > 0 && delen[1].charAt(0) == '0') && !(upperBound == 0 && !delen[1].equals("0"));
				if (lowerBoundCorrect && upperBoundCorrect && lowerBound <= upperBound) {
						this.lowerBound = lowerBound;
						this.upperBound = upperBound;
				} else {
					throw new CustomException(T_INVALID_PORT_PATTERN);
				}

				// geval dat ':' meer dan 1 keer voor komt in poort patroon -
				// dit is niet toegelaten
			} else {
				throw new CustomException(T_INVALID_PORT_PATTERN);
			}

		} catch (NumberFormatException nfe) {
			throw new CustomException(T_INVALID_PORT_PATTERN);
		}
	}

	/**
	 * Geeft de ondergrens van het poort patroon terug. -1 indien elke poort
	 * matcht aan het patroon.
	 * 
	 * @return De ondergrens van het poort patroon. -1 indien elke poort matcht
	 *         met het patroon.
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * Stelt de waarde van lowerBound in. Indien de meegegeven waarde -1 is zal
	 * elke poort matchen aan het patroon.
	 * 
	 * @param lowerBound
	 *            De gewenste ondergrens van het poort patroon. Indien -1 zal
	 *            elke poort matchen aan het patroon.
	 */
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
		// volgende code dient om onverwachte resultaten na refactoren te
		// voorkomen - voor het refactoren werd voor het aanduiden van alle
		// poorten de waarde -1 gebruikt voor lowerBound.
		if (lowerBound == -1) {
			setRefersToAllPorts(true);
		}
	}

	/**
	 * Geeft de bovengrens van het poort patroon terug. -1 indien elke poort
	 * matcht aan het patroon.
	 * 
	 * @return De bovengrens van het poort patroon. -1 indien elke poort matcht
	 *         met het patroon.
	 */
	public int getUpperBound() {
		return upperBound;
	}

	/**
	 * Stelt de waarde van de bovengrens in. Indien de meegegeven waarde -1 is
	 * zal elke poort matchen aan het patroon.
	 * 
	 * @param upperBound
	 *            De gewenste bovengrens van het poort patroon. Indien -1 zal
	 *            elke poort matchen aan het patroon.
	 */
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
		if (upperBound == -1) {
			setRefersToAllPorts(true);
		}
	}

	/**
	 * Geeft <code>true</code> terug als elke poort matcht aan het patroon.
	 * 
	 * @return <code>true</code> als elke poort matcht aan het patroon.
	 */
	public boolean isRefersToAllPorts() {
		return refersToAllPorts;
	}

	/**
	 * Stelt in dat elke poort matcht aan het patroon indien parameter
	 * <code>true</code> is. Bovengrens en ondergrens worden dan ook -1.
	 * Attribuut refersToAllPorts wordt <code>true</code>. Indien de parameter
	 * <code>false</code> is zal het attribuut refersToAllPorts
	 * <code>false</code> worden. LET OP! bovengrens en ondergrens blijven dan
	 * ongewijzigd, en moeten dus nog gewijzigd worden.
	 * 
	 * @param refersToAllPorts
	 *            the refersToAllPorts to set
	 */
	public void setRefersToAllPorts(boolean refersToAllPorts) {
		this.refersToAllPorts = refersToAllPorts;
		if (refersToAllPorts) {
			// Dit doe ik omdat er anders misschien nog ergens methoden zijn die
			// nog van voor het refactoren testen of lowerbound -1 is om te
			// controleren of alle poorten matchen
			this.lowerBound = -1;
			this.upperBound = -1;
		}
	}

	/**
	 * Vergelijkt twee poortpatronen en gaat na of ze gelijkwaardig zijn.
	 * 
	 * @param portPattern1
	 *            De eerste van de twee te vergelijken poort patronen.
	 * @param portPattern2
	 *            De tweede van de twee te vergelijken poort patronen.
	 * @return <code>true</code> dan en slechts dan als de twee te vergelijken
	 *         poort patronen gelijkwaardig zijn.
	 */
	public static boolean isEquivalent(PortPattern portPattern1, PortPattern portPattern2) {
		boolean equal = false;
		if (portPattern1.getLowerBound() == -1 && portPattern2.getLowerBound() == -1) { // ga
																						// na
																						// of
																						// beide
																						// poort
																						// patronen
																						// matchen
																						// met
																						// alle
																						// poorten
			equal = true;
		} // andere gevallen
		if (portPattern1.getLowerBound() == portPattern2.getLowerBound() && portPattern1.getUpperBound() == portPattern2.getUpperBound()) {
			equal = true;
		}
		return equal;
	}

	/**
	 * Geeft het poort patroon terug als een <code>String</code>. Indien
	 * ondergrens niet gelijk aan bovengrens is dit van de vorm
	 * "ondergrens:bovengrens". Indien ondergrens gelijk aan bovengrens, dan is
	 * deze van de vorm "ondergrens". Indien elke poort matcht aan het patroon
	 * wordt "*" teruggegeven.
	 * 
	 * @return Poort patroon als <code>String</code>.
	 */
	public String toString() {
		if (isRefersToAllPorts()) {
			return "*";
		} else if(lowerBound == -2 || upperBound == -2){
			return "n.v.t.";
		}else{
			if (lowerBound == upperBound) {
				return "" + lowerBound;
			} else {
				return lowerBound + ":" + upperBound;
			}
		}

	}
}

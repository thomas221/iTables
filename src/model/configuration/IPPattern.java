/**
 * 
 */
package model.configuration;

import exception.*;

import static util.Constants.*;

/**
 * 
 * Deze klasse stelt een IP patroon voor.
 * 
 * @author Thomas Van Poucke
 * 
 */
public class IPPattern {
	/**
	 * Eerste getal van de 4 getallen die een ip-adres in tekst formaat heeft.
	 * Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	private int number1;
	/**
	 * Tweede getal van de 4 getallen die een ip-adres in tekst formaat heeft.
	 * Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	private int number2;
	/**
	 * Derde getal van de 4 getallen die een ip-adres in tekst formaat heeft.
	 * Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	private int number3;
	/**
	 * Vierde getal van de 4 getallen die een ip-adres in tekst formaat heeft.
	 * Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	private int number4;
	/**
	 * Masker dat aangeeft hoeveel van de eerste bits vastliggen. De overige
	 * bits mogen in het netwerk pakket elke waarde hebben. Dit moet een getal
	 * tussen 0 en 32 (inclusief) zijn.
	 */
	private int mask;

	/**
	 * Constructor die attributen intitiele waarden geeft.
	 * 
	 * @param number1
	 *            Eerste getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 * @param number2
	 *            Tweede getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft.Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 * @param number3
	 *            Derde getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 * @param number4
	 *            Vierde getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 * @param mask
	 *            Masker dat aangeeft hoeveel van de eerste bits vastliggen. De
	 *            overige bits mogen in het netwerk pakket elke waarde hebben.
	 *            Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	public IPPattern(int number1, int number2, int number3, int number4, int mask) {
		this.number1 = number1;
		this.number2 = number2;
		this.number3 = number3;
		this.number4 = number4;
		this.mask = mask;
	}

	/**
	 * Constructor die IPPattern maakt adhv van String die ip-patroon in
	 * CIDR-formaat heeft. * of ANY betekent dat elk ip adres matcht aan het
	 * patroon, dus equivalent aan 0.0.0.0/0.
	 * @param cidrAddress String die een ip-patroon in CIDR-formaat heeft.
	 * @throws CustomException Wordt opgegooid indien meegegeven tekst geen geldig IP patroon in CIDR-notatie heeft.
	 */
	public IPPattern(String cidrAddress) throws CustomException {
		cidrAddress = cidrAddress.trim();
		if (cidrAddress.equals("*") || cidrAddress.equals("ANY")) {
			this.number1 = 0;
			this.number2 = 0;
			this.number3 = 0;
			this.number4 = 0;
			this.mask = 0;
		} else {
			try {
				// zet gedeelte met adres in variabele
				String[] ipPatternStringArray = cidrAddress.split("/",-1);
				String address = ipPatternStringArray[0];
				// parse masker
				if (ipPatternStringArray.length == 2) { // er is een masker
					// opgegeven
					// zet gedeelte met masker in variabele
					String maskString = ipPatternStringArray[1];
					if(maskString.equals("")){
						throw new CustomException(I_INVALID_IP_PATTERN);
					}else{
						int mask = (int) Integer.parseInt(maskString);
						// controleer of masker getal tussen 0 en 32
						boolean maskCorrect = 0 <= mask && mask <= 32 && maskString.charAt(0) != '-' && !(mask == 0 && !maskString.equals("0")) && !(mask > 0 && maskString.charAt(0) == '0');
						if (maskCorrect) {
							// stel masker van IPPattern in
							this.mask = mask;
						} else {
							throw new CustomException(I_INVALID_IP_PATTERN);
						}
					}
					// er is geen masker opgegeven
				} else if (ipPatternStringArray.length == 1) {
					this.mask = 32;
				} else {
					// er zijn meerdere forward slashen in het adres patroon -
					// adres patroon is ongeldig
					throw new CustomException(I_INVALID_IP_PATTERN);
				}
				// het adres moet 4 delen hebben (nl. 4 getallen), gescheiden
				// door punten
				//daarnaast indien 1 van de vier getallen start met '-' teken: gooi CustomException
				String[] addressNumbersStringArray = address.split("\\.");
				if (addressNumbersStringArray.length == 4 && addressNumbersStringArray[0].charAt(0) != '-' && addressNumbersStringArray[1].charAt(0) != '-' && addressNumbersStringArray[2].charAt(0) != '-' && addressNumbersStringArray[3].charAt(0) != '-') {
					// zet de vier getallen van het adres in 4 variabelen
					int number1 = (int) Integer.parseInt(addressNumbersStringArray[0]);
					int number2 = (int) Integer.parseInt(addressNumbersStringArray[1]);
					int number3 = (int) Integer.parseInt(addressNumbersStringArray[2]);
					int number4 = (int) Integer.parseInt(addressNumbersStringArray[3]);

					// controleer of vier getallen binnen het bereik van 0 tem
					// 255 vallen
					boolean IP1Correct = 0 <= number1 && number1 <= 255 && !(number1 > 0 && addressNumbersStringArray[0].charAt(0) == '0') && !(number1 == 0 && !addressNumbersStringArray[0].equals("0"));
					boolean IP2Correct = 0 <= number2 && number2 <= 255 && !(number2 > 0 && addressNumbersStringArray[1].charAt(0) == '0') && !(number2 == 0 && !addressNumbersStringArray[1].equals("0"));
					boolean IP3Correct = 0 <= number3 && number3 <= 255 && !(number3 > 0 && addressNumbersStringArray[2].charAt(0) == '0') && !(number3 == 0 && !addressNumbersStringArray[2].equals("0"));
					boolean IP4Correct = 0 <= number4 && number4 <= 255 && !(number4 > 0 && addressNumbersStringArray[3].charAt(0) == '0') && !(number4 == 0 && !addressNumbersStringArray[3].equals("0"));
					if (IP1Correct && IP2Correct && IP3Correct && IP4Correct) {
						// zet de vier getallen van het adres in 4 variabelen
						this.number1 = number1;
						this.number2 = number2;
						this.number3 = number3;
						this.number4 = number4;
					} else {
						throw new CustomException(I_INVALID_IP_PATTERN);
					}
				} else {
					throw new CustomException(I_INVALID_IP_PATTERN);
				}
			} catch (ArrayIndexOutOfBoundsException obe) {
				throw new CustomException(I_INVALID_IP_PATTERN);
			} catch (NumberFormatException nfe) {
				throw new CustomException(I_INVALID_IP_PATTERN);
			}
		}

	}

	/**
	 * Ga na of twee IP patronen equivalent zijn.
	 * 
	 * @param ipPattern1
	 *            Een van de twee IP patronen waarvan gecontroleerd wordt of ze
	 *            equivalent zijn.
	 * @param ipPattern2
	 *            Een van de twee IP patronen waarvan gecontroleerd wordt of ze
	 *            equivalent zijn.
	 * @return True dan en slechts dan als de twee IP patronen equivalent zijn.
	 */
	public static boolean isEquivalent(IPPattern ipPattern1, IPPattern ipPattern2) {
		boolean equal = false;
		if (ipPattern1.getMask() == ipPattern2.getMask()) { // twee equivalente
			// IP patronen
			// hebben een zelfde
			// getal voor het
			// masker
			// twee equivalente IP patronen moet voor de x aantal eerste bits
			// overeenkomen, met x het masker getal
			boolean[] ipPattern1Bits = ipPattern1.toBoolean();
			boolean[] ipPattern2Bits = ipPattern2.toBoolean();
			boolean test = true;
			for (int i = 0; i < ipPattern1.getMask(); i++) {
				if (ipPattern1Bits[i] != ipPattern2Bits[i]) {
					test = false;
				}
			}
			if (test == true) {
				equal = true;
			}
		}
		return equal;
	}

	/**
	 * Zet IP patroon om in zijn bits.
	 * 
	 * @return IP patroon als een reeks bits, teruggegeven als een array van
	 *         boolean waarden. (true -> 1, false -> 0)
	 */
	public boolean[] toBoolean() {
		boolean[] bits = new boolean[32];
		int var = number1;
		for (int i = 7; i >= 0; i--) {
			bits[i] = var % 2 == 1;
			var /= 2;
		}
		var = number2;
		for (int i = 15; i >= 8; i--) {
			bits[i] = var % 2 == 1;
			var /= 2;
		}
		var = number3;
		for (int i = 23; i >= 16; i--) {
			bits[i] = var % 2 == 1;
			var /= 2;
		}
		var = number4;
		for (int i = 31; i >= 24; i--) {
			bits[i] = var % 2 == 1;
			var /= 2;
		}
		return bits;
	}

	/**
	 * @return Eerste getal van de 4 getallen die een ip-adres in tekst formaat
	 *         heeft. Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	public int getNumber1() {
		return number1;
	}

	/**
	 * @param number1
	 *            Eerste getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 */
	public void setNumber1(int number1) {
		this.number1 = number1;

	}

	/**
	 * @return Tweede getal van de 4 getallen die een ip-adres in tekst formaat
	 *         heeft. Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	public int getNumber2() {
		return number2;
	}

	/**
	 * @param number2
	 *            Tweede getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 */
	public void setNumber2(int number2) {
		this.number2 = number2;
	}

	/**
	 * @return Derde getal van de 4 getallen die een ip-adres in tekst formaat
	 *         heeft. Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	public int getNumber3() {
		return number3;
	}

	/**
	 * @param number3
	 *            Derde getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 */
	public void setNumber3(int number3) {
		this.number3 = number3;
	}

	/**
	 * @return Vierde getal van de 4 getallen die een ip-adres in tekst formaat
	 *         heeft. Dit moet een getal tussen -2 en 255 (inclusief) zijn.
	 */
	public int getNumber4() {
		return number4;
	}

	/**
	 * @param number4
	 *            Vierde getal van de 4 getallen die een ip-adres in tekst
	 *            formaat heeft. Dit moet een getal tussen -2 en 255 (inclusief)
	 *            zijn.
	 */
	public void setNumber4(int number4) {
		this.number4 = number4;
	}

	/**
	 * @return Masker dat aangeeft hoeveel van de eerste bits vastliggen. De
	 *         overige bits mogen in het netwerk pakket elke waarde hebben. Dit
	 *         moet een getal tussen 0 en 32 (inclusief) zijn.
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * @param mask
	 *            Masker dat aangeeft hoeveel van de eerste bits vastliggen. De
	 *            overige bits mogen in het netwerk pakket elke waarde hebben.
	 *            Dit moet een getal tussen 0 en 32 (inclusief) zijn.
	 */
	public void setMask(int mask) {
		this.mask = mask;
	}

	/**
	 * Geef IP patroon als string in CIDR-notatie terug.
	 * 
	 * @return IP patroon als string in CIDR-notatie. Bv. "10.20.2.3/8"
	 */
	public String toString() {
		return number1 + "." + number2 + "." + number3 + "." + number4 + "/" + mask;
	}

	/**
	 * Geeft true terug als dit patroon overeenkomt met elk IP-adres.
	 * @return true als dit patroon overeenkomt met elk IP-adres.
	 */
	public boolean isWildcardIP() {
		return (number1 == 0 && number2 == 0 && number3 == 0 && number4 == 0 && mask == 0);
	}
}

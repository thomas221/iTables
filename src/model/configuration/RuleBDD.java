package model.configuration;

import static util.Constants.PROTOCOL_TCP;
import static util.Constants.PROTOCOL_UDP;

/**
 * De klasse<code> RuleBDD </code> bevat de waarden van een regel van een
 * opsomming van de 5 dimensionale ruimte van een BDD. </br> Een regel is hier
 * een opsommingsregel en niet een firewall regel.
 * 
 * @author Ron Melger
 */
public class RuleBDD {
	/**
	 * Bevat het protocol van de BDD regel.
	 */
	private int protocol;
	/**
	 * Bevat het Eerste getal van bron IP adres van de BDD regel.
	 */
	private int sourceIP_1;
	/**
	 * Bevat het Tweede getal van bron IP adres van de BDD regel.
	 */
	private int sourceIP_2;
	/**
	 * Bevat het Derde getal van bron IP adres van de BDD regel.
	 */
	private int sourceIP_3;
	/**
	 * Bevat het Vierde getal van bron IP adres van de BDD regel.
	 */
	private int sourceIP_4;
	/**
	 * Bevat het Getal dat de bron poort aangeeft van de BDD regel.
	 */
	private int sourcePort;
	/**
	 * Bevat het Eerste getal van doel IP adres van de BDD regel.
	 */
	private int destinationIP_1;
	/**
	 * Bevat het Tweede getal van doel IP adres van de BDD regel.
	 */
	private int destinationIP_2;
	/**
	 * Bevat het Derde getal van doel IP adres van de BDD regel.
	 */
	private int destinationIP_3;
	/**
	 * Bevat het Vierde getal van doel IP adres van de BDD regel.
	 */
	private int destinationIP_4;
	/**
	 * Bevat de waarde van het doel poort van de BDD regel.
	 */
	private int destinationPort;
	/**
	 * Gereserveerd voor extra informatie als een compacte weergave wordt
	 * gegeven.
	 */
	private String infoString;

	/**
	 * Initialiseert een nieuw <code>RuleBDD</code> object.
	 */
	public RuleBDD() {
	}

	// hier allemaal get- en set Methoden

	/**
	 * Geeft het protocol als een <code>int</code>.
	 * 
	 * @return de waarde van het attribuut protocol
	 */
	public int getProtocol() {
		return protocol;
	}

	/**
	 * Geef protocol terug als een <code>String</code>.
	 * 
	 * @return Het protocol als String.
	 */
	public String getProtocolString() 
	{
		if (protocol == 6)
			return "TCP";
		else if (protocol == 17)
			return "UDP";
		else if (protocol == 1)
			return "ICMP";
		else
			return protocol + "";
	}
	
	// oude code
	 /*
	public String getProtocolString() {
		return Protocol.getProtocol(protocol).getString();
	}
	 */

	/**
	 * Geeft het eerste deel van het bron IP-adres als een <code>int</code>.
	 * 
	 * @return de waarde van het eerste deel van het bron IP-adres
	 */
	public int getSourceIP_1() {
		return sourceIP_1;
	}

	/**
	 * Geeft het tweede deel van het bron IP-adres als een <code>int</code>.
	 * 
	 * @return de waarde van het tweede deel van het bron IP-adres
	 */
	public int getSourceIP_2() {
		return sourceIP_2;
	}

	/**
	 * Geeft het derde deel van het bron IP-adres als een <code>int</code>.
	 * 
	 * @return de waarde van het derde deel van het bron IP-adres
	 */
	public int getSourceIP_3() {
		return sourceIP_3;
	}

	/**
	 * Geeft het vierde deel van het bron IP-adres als een <code>int</code>.
	 * 
	 * @return de waarde van het vierde deel van het bron IP-adres
	 */
	public int getSourceIP_4() {
		return sourceIP_4;
	}

	/**
	 * Geef bron IP adres terug als een String.
	 * 
	 * @return Bron IP adres als String.
	 */
	public String getSourceIPString() {
		return sourceIP_1 + "." + sourceIP_2 + "." + sourceIP_3 + "." + sourceIP_4;
	}

	/**
	 * Geeft de bronpoort als een <code>int</code>.
	 * 
	 * @return de waarde van bronpoort
	 */
	public int getSourcePort() {
		return sourcePort;
	}

	/**
	 * Geeft het eerste deel van het bestemmings IP-adres als een
	 * <code>int</code>.
	 * 
	 * @return de waarde van het eerste deel van het bestemmings IP-adres
	 */
	public int getDestinationIP_1() {
		return destinationIP_1;
	}

	/**
	 * Geeft het tweede deel van het bestemmings IP-adres als een
	 * <code>int</code>.
	 * 
	 * @return de waarde van het tweede deel van het bestemmings IP-adres
	 */
	public int getDestinationIP_2() {
		return destinationIP_2;
	}

	/**
	 * Geeft het derde deel van het bestemmings IP-adres als een
	 * <code>int</code>.
	 * 
	 * @return de waarde van het derde deel van het bestemmings IP-adres
	 */
	public int getDestinationIP_3() {
		return destinationIP_3;
	}

	/**
	 * Geeft het vierde deel van het bestemmings IP-adres als een
	 * <code>int</code>.
	 * 
	 * @return de waarde van het vierde deel van het bestemmings IP-adres
	 */
	public int getDestinationIP_4() {
		return destinationIP_4;
	}

	/**
	 * Geeft bestemming IP adres terug als een String.
	 * 
	 * @return Bestemming IP adres als String.
	 */
	public String getDestinationIPString() {
		return destinationIP_1 + "." + destinationIP_2 + "." + destinationIP_3 + "." + destinationIP_4;
	}

	/**
	 * Geeft de bestemmingspoort als een <code>int</code>.
	 * 
	 * @return de waarde van bestemmingspoort
	 */
	public int getDestinationPort() {
		return destinationPort;
	}

	/**
	 * Geeft de extra informatie als een String. Dit is 'A' of 'L'.
	 * 
	 * @return de extra informatie als String.
	 */
	public String getInfoString() {
		return infoString;
	}
	
	/**
	 * Geeft de extra informatie terug als 'begin' of 'einde'. 'A' komt overeen met 'begin'. 'L' komt overeen met 'einde'.
	 * @return de extra informatie als 'begin' of 'einde'.
	 */
	public String getInfoStringAsBeginOrEinde(){
		if(infoString.equals("A")){
			return "begin";
		}else if(infoString.equals("L")){
			return "einde";
		}else{
			return "";
		}
	}

	// hier de set methoden
	/**
	 * Stelt de waarde van het protocol in.
	 * 
	 * @param protocol
	 *            de waarde van het protocol
	 */
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	/**
	 * Stelt de waarde van het eerste deel van het bron IP-adres in.
	 * 
	 * @param sourceIP_1
	 *            de waarde van het eerste deel van het bron IP-adres
	 */
	public void setSourceIP_1(int sourceIP_1) {
		this.sourceIP_1 = sourceIP_1;
	}

	/**
	 * Stelt de waarde van het tweede deel van het bron IP-adres in.
	 * 
	 * @param sourceIP_2
	 *            de waarde van het tweede deel van het bron IP-adres
	 */
	public void setSourceIP_2(int sourceIP_2) {
		this.sourceIP_2 = sourceIP_2;
	}

	/**
	 * Stelt de waarde van het derde deel van het bron IP-adres in.
	 * 
	 * @param sourceIP_3
	 *            de waarde van het derde deel van het bron IP-adres
	 */
	public void setSourceIP_3(int sourceIP_3) {
		this.sourceIP_3 = sourceIP_3;
	}

	/**
	 * Stelt de waarde van het vierde deel van het bron IP-adres in.
	 * 
	 * @param sourceIP_4
	 *            de waarde van het vierde deel van het bron IP-adres
	 */
	public void setSourceIP_4(int sourceIP_4) {
		this.sourceIP_4 = sourceIP_4;
	}

	/**
	 * Stelt de waarde van de bronpoort in.
	 * 
	 * @param sourcePort
	 *            de waarde van de bronpoort
	 */
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	/**
	 * Stelt de waarde van het eerste deel van het bestemmings IP-adres in.
	 * 
	 * @param destinationIP_1
	 *            de waarde van het eerste deel van het bestemmings IP-adres
	 */
	public void setDestinationIP_1(int destinationIP_1) {
		this.destinationIP_1 = destinationIP_1;
	}

	/**
	 * Stelt de waarde van het tweede deel van het bestemmings IP-adres in.
	 * 
	 * @param destinationIP_2
	 *            de waarde van het tweede deel van het bestemmings IP-adres
	 */
	public void setDestinationIP_2(int destinationIP_2) {
		this.destinationIP_2 = destinationIP_2;
	}

	/**
	 * Stelt de waarde van het derde deel van het bestemmings IP-adres in.
	 * 
	 * @param destinationIP_3
	 *            de waarde van het derde deel van het bestemmings IP-adres
	 */
	public void setDestinationIP_3(int destinationIP_3) {
		this.destinationIP_3 = destinationIP_3;
	}

	/**
	 * Stelt de waarde van het vierde deel van het bestemmings IP-adres in.
	 * 
	 * @param destinationIP_4
	 *            de waarde van het vierde deel van het bestemmings IP-adres
	 */
	public void setDestinationIP_4(int destinationIP_4) {
		this.destinationIP_4 = destinationIP_4;
	}

	/**
	 * Stelt de waarde van de bestemmingspoort in.
	 * 
	 * @param destinationPort
	 *            de waarde van de bestemmingspoort
	 */
	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}

	/**
	 * Stelt de waarde van de infoString in. Dit betreft extra informatie die
	 * gewenst is bij een compacte opsommming.
	 * 
	 * @param infoString
	 *            se waarde van de infoString
	 */
	public void setInfoString(String infoString) {
		this.infoString = infoString;
	}

}

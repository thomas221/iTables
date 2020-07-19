package model.configuration;

import exception.CustomException;

//code aangepast aan IP netmask 23 mei 2013; // aangepast aan range in poortnummers 26 mei 2013 //aangepast aan IPPattern en PortPattern op 15-9-2013
/**
 * Klasse die een regel voorstelt in een firewall configuratie. Een regel bevat
 * een protocol, bron ip patroon, bron poort patroon, doel ip patroon en doel
 * poort patroon. Indien een pakket voldoet aan die criteria, dan zal het pakket
 * 'matchen' aan die regel (indien het niet met een eerdere regel heeft
 * gematcht).
 * 
 * @author Thomas
 * 
 */
public class Rule implements Comparable<Rule> {
	/**
	 * Houdt protocol bij van de regel.
	 */
	private int protocol;
	/**
	 * Houdt bron IP patroon bij.
	 */
	private IPPattern sourceIP;
	/**
	 * Houdt bron poort patroon bij.
	 */
	private PortPattern sourcePort;
	/**
	 * Houdt doel IP patroon bij.
	 */
	private IPPattern destinationIP;
	/**
	 * Houdt doel poort patroon bij.
	 */
	private PortPattern destinationPort;
	/**
	 * Houdt de actie van de regel bij. <code>true</code> betekent dat het
	 * pakket doorgelaten wordt, <code>false</code> betekent dat het pakket
	 * tegengehouden wordt.
	 */
	private boolean action;
	/**
	 * Houdt het regelnummer bij van de regel in de configuratie. De eerste
	 * regel heeft ruleNr 1, de tweede regel heeft ruleNr 2, enzovoort.
	 */
	public int ruleNr;

	/**
	 * Constructor die alle attributen initiele waarden geeft.
	 * 
	 * @param ruleNr
	 *            Het nummer van de Rule.
	 * @param protocol
	 *            Het protocol.
	 * @param sourceIP
	 *            Het bron IP adres patroon.
	 * @param sourcePort
	 *            Het bron poort patroon.
	 * @param destinationIP
	 *            Het doel IP adres patroon.
	 * @param destinationPort
	 *            Het doel poort patroon.
	 * @param action
	 *            De actie. True voor doorlaten, false voor tegenhouden.
	 */
	public Rule(int ruleNr, int protocol, IPPattern sourceIP, PortPattern sourcePort, IPPattern destinationIP, PortPattern destinationPort, boolean action) {
		this.ruleNr = ruleNr;
		this.sourceIP = sourceIP;
		this.destinationIP = destinationIP;
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.action = action;
		setProtocol(protocol);
	}

	/**
	 * Geeft een String terug met alle informatie over deze regel.
	 * @return String met alle informatie over deze regel.
	 */
	public String toString() {
		//verkrijg String representatie van protocol
		int protocol = getProtocol();
		String protocolString = ""+protocol;
		if(protocol == 6){
			protocolString = "TCP";
		}else if(protocol == 17){
			protocolString = "UDP";
		}else if(protocol == 1){
			protocolString = "ICMP";
		}else if(protocol == -1){
			protocolString = "*";
		}
		
		if(protocol == 6 || protocol == 17){
		
			return "index: " + getRuleNr() + "; protocol: " + protocolString + "; bron IP: " + getSourceIP().toString() + "; bronpoort: " + getSourcePort().toString() + "; doel IP: "
					+ getDestinationIP().toString() + "; doelpoort: " + getDestinationPort().toString() + "; actie: " + getActionString();
		}else{
			return "index: " + getRuleNr() + "; protocol: " + protocolString + "; bron IP: " + getSourceIP().toString() + "; doel IP: "
					+ getDestinationIP().toString() + "; actie: " + getActionString();
		}
	}

	// hier allemaal get- en set Methoden
	/**
	 * Geeft het regelnummer terug van de firewall regel.
	 * 
	 * @return regelnummer van de firewall regel.
	 */
	public int getRuleNr() {
		return ruleNr;
	}

	/**
	 * Geeft ruleNr van de regel de meegegeven waarde.
	 * 
	 * @param ruleNr
	 *            nieuwe waarde van het regelnummer.
	 */
	public void setRuleNr(int ruleNr) {
		this.ruleNr = ruleNr;
	}

	/**
	 * Geeft het protocol terug van de regel.
	 * 
	 * @return protocol van regel.
	 */
	public int getProtocol() {
		return protocol;
	}

	/**
	 * Geeft bron IP patroon terug van de regel.
	 * 
	 * @return Bron IP patroon van de regel.
	 */
	public IPPattern getSourceIP() {
		return sourceIP;
	}

	public String getSourceIPWithAnyAsStar() {
		if (sourceIP.isWildcardIP()) {
			return "*";
		} else {
			return sourceIP.toString();
		}
	}

	/**
	 * Geeft doel IP patroon terug van de regel.
	 * 
	 * @return Doel IP patroon van de regel.
	 */
	public IPPattern getDestinationIP() {
		return destinationIP;
	}
	
	public String getDestinationIPWithAnyAsStar() {
		if (destinationIP.isWildcardIP()) {
			return "*";
		} else {
			return destinationIP.toString();
		}
	}

	/**
	 * Geeft doel poort patroon terug van de regel.
	 * 
	 * @return Doel poort patroon van de regel.
	 */
	public PortPattern getDestinationPort() {
		return destinationPort;
	}

	/**
	 * Geeft bron poort patroon terug van de regel.
	 * 
	 * @return Bron poort patroon van de regel.
	 */
	public PortPattern getSourcePort() {
		return sourcePort;
	}

	/**
	 * Geeft de actie terug van de regel. <code>true</code> betekent doorlaten,
	 * <code>false</code> tegenhouden.
	 * 
	 * @return de actie van de regel. <code>true</code> betekent doorlaten,
	 *         <code>false</code> tegenhouden.
	 */
	public boolean getAction() {
		return action;
	}

	/**
	 * Geeft de actie terug van de regel. <code>1</code> betekent doorlaten,
	 * <code>0</code> tegenhouden.
	 * @return De actie van de regel.<code>1</code> betekent doorlaten, <code>0</code> tegenhouden.
	 */
	public int getActionInt() {
		if (action) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Geeft de actie van de regel als String. 'allow' betekent doorlaten,
	 * 'deny' tegenhouden.
	 * @return De actie van de regel als String. 'allow' betekent doorlaten, 'deny' tegenhouden.
	 */
	public String getActionString() {
		if (action) {
			return "accept";
		} else {
			return "drop";
		}
	}

	/**
	 * Geeft het protocol de opgegeven waarde. Indien dit protocol geen bronpoort of doelpoort ondersteund worden bronpoort en doelpoort op * gezet.
	 * 
	 * @param protocol
	 *            De nieuwe waarde voor het protocol.
	 */
	public void setProtocol(int protocol) {
		//zet poorten op de niet gespecificeerde waarde indien protocol TCP of UDP
		if (protocol != 6 && protocol != 17){
			this.destinationPort = new PortPattern(-1,-1);
			this.sourcePort = new PortPattern(-1,-1);
		}
		/*
		//zet poorten op ANY indien protocol ingesteld wordt op TCP of UDP maar de poorten niet gespecificeerd zijn
		if ((protocol == 6 || protocol == 17) && (this.destinationPort.getLowerBound() == -2 || this.destinationPort.getUpperBound() == -2|| this.sourcePort.getLowerBound() == -2 || this.sourcePort.getUpperBound() == -2)){
			this.destinationPort = new PortPattern(-1,-1);
			this.sourcePort = new PortPattern(-1,-1);
		}*/
		this.protocol = protocol;
	}

	/**
	 * Geeft het bron IP patroon de opgegeven waarde.
	 * 
	 * @param sourceIP
	 *            De nieuwe waarde voor het bron IP patroon.
	 */
	public void setSourceIP(IPPattern sourceIP) {
		this.sourceIP = sourceIP;
	}

	/**
	 * Geeft bron IP van deze regel een nieuwe waarde. De opgegeven String wordt als IPPattern geparst en ingesteld als sourceIP attribuut.
	 * @param sourceIP String die IP patroon in CIDR-notatie bevat.
	 * @throws CustomException Fout bij parsen van String als IPPattern. 
	 */
	public void setSourceIP(String sourceIP) throws CustomException {
		IPPattern ipPattern = new IPPattern(sourceIP);
		this.sourceIP = ipPattern;
	}

	/**
	 * Geeft het doel IP patroon de opgegeven waarde.
	 * 
	 * @param destinationIP
	 *            De nieuwe waarde voor het doel IP patroon.
	 */
	public void setDestinationIP(IPPattern destinationIP) {
		this.destinationIP = destinationIP;
	}

	/**
	 * Geeft doel IP van deze regel een nieuwe waarde. De opgegeven String wordt als IPPattern geparst en ingesteld als destinationIP attribuut.
	 * @param destinationIP String die IP patroon in CIDR-notatie bevat.
	 * @throws CustomException Fout bij parsen van String als IPPattern. 
	 */
	public void setDestinationIP(String destinationIP) throws CustomException {
		IPPattern ipPattern = new IPPattern(destinationIP);
		this.destinationIP = ipPattern;
	}

	/**
	 * Geeft het doel poort patroon de opgegeven waarde.
	 * 
	 * @param destinationPort
	 *            De niewe waarde voor het doel poort patroon.
	 */
	public void setDestinationPort(PortPattern destinationPort) {
		this.destinationPort = destinationPort;
	}

	/**
	 * Geeft doel poort van deze regel een nieuwe waarde. De opgegeven String wordt als doel poort geparst en insgesteld als destinationPort attribuut.
	 * @param destinationPort Poort als String. Deze String moet een enkel poortnummer
	 *            betreffen ("poort") of van de vorm "beginpoort:eindpoort"
	 *            zijn. Als de waarde "*" of "ANY" is zal elke poort matchen met
	 *            het patroon.
	 * @throws CustomException Fout bij parsen van String als poort.
	 */
	public void setDestinationPort(String destinationPort) throws CustomException {
		PortPattern portPattern = new PortPattern(destinationPort);
		this.setDestinationPort(portPattern);
	}

	/**
	 * Geeft het bron poort patroon de opgegeven waarde.
	 * 
	 * @param sourcePort
	 *            De nieuwe waarde voor het bron poort patroon.
	 */
	public void setSourcePort(PortPattern sourcePort) {
		this.sourcePort = sourcePort;
	}

	public void setSourcePort(String sourcePort) throws CustomException {
		PortPattern portPattern = new PortPattern(sourcePort);
		this.setSourcePort(portPattern);
	}

	/**
	 * Geeft de actie van de regel de opgegeven waarde. <code>true</code> voor
	 * doorlaten, <code>false</code> voor tegenhouden.
	 * 
	 * @param action
	 *            De nieuwe waarde voor de actie. <code>true</code> voor
	 *            doorlaten, <code>false</code voor tegenhouden.
	 */
	public void setAction(boolean action) {
		this.action = action;
	}

	/**
	 * Geeft -1 terug als deze regel voor opgegeven regel komt. Geeft 0 terug
	 * als deze regel de zelfde regel nummer heeft (wat niet kan in de zelfde
	 * chain) Geeft 1 terug als deze regel na opgegeven regel komt.
	 * 
	 * @param thatRule
	 *            De regel waarmee de positie van deze regel vergeleken moet
	 *            worden.
	 */
	public int compareTo(Rule thatRule) {
		if (this.ruleNr < thatRule.ruleNr)
			return -1;
		else if (this.ruleNr == thatRule.ruleNr)
			return 0;
		else
			return 1;
	}
	
	/**
	 * Geeft true terug indien de regel protocol ANY bevat en wel een poort. Dit is niet mogelijk in iptables.
	 * De export functie zal daardoor deze regel in 2 iptables regels omzetten: 1 voor tcp en 1 voor udp.
	 * @return true indien de regel geen protocol bevat en wel een poort.
	 */
	public boolean isPortWithoutProtocol(){
		if(protocol == -1 && (!sourcePort.isRefersToAllPorts() || !destinationPort.isRefersToAllPorts())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Exporteer regel naar model.importeer_exporteer regel. Deze model.importeer_exporteer regel
	 * kan dan gebruikt worden bij het exporteren van de regel naar iptables formaat.
	 * @param chain Chain waar de regel in zit.
	 * @return Een overeenkomstige model.importeer_exporteer regel.
	 */
	public model.importeer_exporteer.Rule export(model.configuration.Chain chain){
		//maak iptables commando voor deze regel
		String command = "-A "+chain.getName().toUpperCase()+" ";
		if(!sourceIP.isWildcardIP()){
			command += "-s "; 
			if(sourceIP.getNumber1() >= 0){
				command += sourceIP.getNumber1()+".";
			}else{
				command += "0.";
			}
			if(sourceIP.getNumber2() >= 0){
				command += sourceIP.getNumber2()+".";
			}else{
				command += "0.";
			}
			if(sourceIP.getNumber3() >= 0){
				command += sourceIP.getNumber3()+".";
			}else{
				command += "0.";
			}
			if(sourceIP.getNumber4() >= 0){
				command += sourceIP.getNumber4()+"/";
			}else{
				command += "0/";
			}
			command += sourceIP.getMask()+" ";
		}
		if(!destinationIP.isWildcardIP()){
			command += "-d "; 
			if(destinationIP.getNumber1() >= 0){
				command += destinationIP.getNumber1()+".";
			}else{
				command += "0.";
			}
			if(destinationIP.getNumber2() >= 0){
				command += destinationIP.getNumber2()+".";
			}else{
				command += "0.";
			}
			if(destinationIP.getNumber3() >= 0){
				command += destinationIP.getNumber3()+".";
			}else{
				command += "0.";
			}
			if(destinationIP.getNumber4() >= 0){
				command += destinationIP.getNumber4()+"/";
			}else{
				command += "0/";
			}
			command += destinationIP.getMask()+" ";
		}
		if(protocol != -1){
			command += "-p ";
			if(protocol == 6){
				command += "tcp ";
			}else if(protocol == 17){
				command += "udp ";
			}else if(protocol == 1){
				command += "icmp ";
			}else{
				command += protocol+" ";
			}
			
		}
		if(protocol == 6 || protocol == 17){
			if(!sourcePort.isRefersToAllPorts()){
				command += "--sport ";
				command += sourcePort.toString()+" ";
			}
			if(!destinationPort.isRefersToAllPorts()){
				command += "--dport ";
				command += destinationPort.toString()+" ";
			}
		}
		command += "-j ";
		if(action){
			command += "ACCEPT";
		}else{
			command += "DROP";
		}
		//maak model.importeer_exporteer regel met dit commando
		return model.importeer_exporteer.Rule.buildFromCommand(command);
		
	}
}

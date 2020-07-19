package test.model;

//code aangepast voor netmask 23 mei 2013; // aangepast aan range in poortnummers 26 mei 2013
import java.util.ArrayList;

import model.configuration.IPPattern;
import model.configuration.PortPattern;
import model.configuration.Rule;

/**
 * Hulp test klasse gebruikt om een test configuratie te genereren.
 *
 */
public class DemoRules {
	/**
	 * Lijst van regels van deze test configuratie.
	 */
    private ArrayList<Rule> ruleList;

    // constructor
    /**
     * Constructor die initiele waarde van configuratie laadt met nul regels.
     */
    public DemoRules() {
	mijnInit();
    }

    /**
     * Initialiseer lijst met regels als lijst met nul regels.
     */
    private void mijnInit() {
	ruleList = new ArrayList<Rule>();
	ruleList.clear();
    }

    /**
     * Geef lijst van regels terug met gewenste waarde voor het testen.
     * @return Lijst van regels waarmee testen uitgevoerd kunnen worden.
     */
    public ArrayList<Rule> getDemoRules() // code gecontroleerd 9 mei 2013
    {
	// ingelezen rules worden omgezet naar Java representatie in objecten
	// Rule // hier conversie van XML naar Java
	// regel 1
	int ruleNr = 1;
	int protocol = 17;
	int sourceIP_1 = 10;
	int sourceIP_2 = 1;
	int sourceIP_3 = 2;
	int sourceIP_4 = 0; // aangepast voor netmask
	int sourceMask = 24;
	int sourcePort_from = -1; // aangepast voor range in poortadressen
	int sourcePort_until = -1;
	int destinationIP_1 = 172;
	int destinationIP_2 = 32;
	int destinationIP_3 = 1;
	int destinationIP_4 = 0; // aangepast voor netmask
	int destinationMask = 24;
	int destinationPort_from = 53;
	int destinationPort_until = 53;
	boolean action = false;

	IPPattern bronIP;
	PortPattern bronPort;
	IPPattern doelIP;
	PortPattern doelPort;
	Rule rule;

	bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
	bronPort = new PortPattern(sourcePort_from, sourcePort_until);
	doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
	doelPort = new PortPattern(destinationPort_from, destinationPort_until);
	rule = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
	// voeg regel toe
	ruleList.add(rule);

	// regel 2
	ruleNr = 2;
	protocol = 17;
	sourceIP_1 = 10;
	sourceIP_2 = 1;
	sourceIP_3 = 0;
	sourceIP_4 = 0;
	sourceMask = 16;
	sourcePort_from = -1;
	sourcePort_until = -1;
	destinationIP_1 = 172;
	destinationIP_2 = 32;
	destinationIP_3 = 1;
	destinationIP_4 = 0;
	destinationMask = 24;
	destinationPort_from = 53;
	destinationPort_until = 53;
	action = false;

	bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
	bronPort = new PortPattern(sourcePort_from, sourcePort_until);
	doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
	doelPort = new PortPattern(destinationPort_from, destinationPort_until);
	rule = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
	
	// voeg regel toe
	ruleList.add(rule);

	// regel 3
	ruleNr = 3;
	protocol = 6;// TCP = 6
	sourceIP_1 = 10;
	sourceIP_2 = 1;
	sourceIP_3 = 0;
	sourceIP_4 = 0;
	sourceMask = 16;
	sourcePort_from = -1;
	sourcePort_until = -1;
	destinationIP_1 = 192;
	destinationIP_2 = 168;
	destinationIP_3 = 0;
	destinationIP_4 = 0;
	destinationMask = 16;
	destinationPort_from = 25;
	destinationPort_until = 25;
	action = true;

	bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
	bronPort = new PortPattern(sourcePort_from, sourcePort_until);
	doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
	doelPort = new PortPattern(destinationPort_from, destinationPort_until);
	rule = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
	
		// voeg regel toe
	ruleList.add(rule);

	// regel 4
	ruleNr = 4;
	protocol = 6; // TCP = 6
	sourceIP_1 = 10;
	sourceIP_2 = 1;
	sourceIP_3 = 1;
	sourceIP_4 = 0;
	sourceMask = 24;
	sourcePort_from = -1;
	sourcePort_until = -1;
	destinationIP_1 = 192;
	destinationIP_2 = 168;
	destinationIP_3 = 1;
	destinationIP_4 = 0;
	destinationMask = 24;
	destinationPort_from = 25;
	destinationPort_until = 25;
	action = false;

	bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
	bronPort = new PortPattern(sourcePort_from, sourcePort_until);
	doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
	doelPort = new PortPattern(destinationPort_from, destinationPort_until);
	rule = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);

	// voeg regel toe
	ruleList.add(rule);

	// regel 5
	ruleNr = 5;
	protocol = -1;
	sourceIP_1 = 10;
	sourceIP_2 = 1;
	sourceIP_3 = 1;
	sourceIP_4 = 0;
	sourceMask = 24;
	sourcePort_from = -1;
	sourcePort_until = -1;
	destinationIP_1 = 0;
	destinationIP_2 = 0;
	destinationIP_3 = 0;
	destinationIP_4 = 0;
	destinationMask = 0;
	destinationPort_from = -1;
	destinationPort_until = -1;
	action = true;

	bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
	bronPort = new PortPattern(sourcePort_from, sourcePort_until);
	doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
	doelPort = new PortPattern(destinationPort_from, destinationPort_until);
	rule = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
	
	// voeg regel toe
	ruleList.add(rule);

	// regel 6
	ruleNr = 6;
	protocol = -1;
	sourceIP_1 = 10;
	sourceIP_2 = 1;
	sourceIP_3 = 1;
	sourceIP_4 = 0;
	sourceMask = 24;
	sourcePort_from = 7;
	sourcePort_until = 11;
	destinationIP_1 = 0;
	destinationIP_2 = 0;
	destinationIP_3 = 0;
	destinationIP_4 = 0;
	destinationMask = 0;
	destinationPort_from = 1;
	destinationPort_until = 2;
	action = true;

	bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
	bronPort = new PortPattern(sourcePort_from, sourcePort_until);
	doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
	doelPort = new PortPattern(destinationPort_from, destinationPort_until);
	rule = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
	
	// voeg regel toe
	// ruleList.add(rule);

	return ruleList;
    }
}

package test.model;

import java.util.ArrayList;

import model.configuration.IPPattern;
import model.configuration.PortPattern;
import model.configuration.Rule;

public class DemoRulesGroup {
	/**
	 * Lijst van regels waarmee testen uitgevoerd kunnen worden. Dit test geval
	 * bevat Correlatiegroepen.
	 */
	private ArrayList<Rule> ruleList;

	// private Rule[] ruleList;

	// constructor
	/**
	 * Constructor die een configuratie zonder regels initialiseert.
	 */
	public DemoRulesGroup() {
		myInit();
	}

	/**
	 * Initialiseer lijst van regels als een lijst met geen regels.
	 */
	private void myInit() {
		ruleList = new ArrayList<Rule>();
		ruleList.clear();
		// ruleList = new Rule [36];

	}

	/**
	 * Geef lijst van regels terug waarop testen uitgevoerd kunnen worden.
	 * 
	 * @return Een lijst met demo firewall rules.
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

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		// voeg dummy rule toe
//		Rule rule_0 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		Rule rule_1 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);

		// voeg regel toe

		// ruleList.add(rule_0);
		ruleList.add(rule_1);

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

		Rule rule_2 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_2);

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

		Rule rule_3 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_3);

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

		Rule rule_4 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_4);

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

		Rule rule_5 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_5);

		// regel 6
		ruleNr = 6;
		protocol = 17;
		sourceIP_1 = 20;
		sourceIP_2 = 1;
		sourceIP_3 = 2;
		sourceIP_4 = 0;
		sourceMask = 24;
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

		Rule rule_6 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_6);

		// regel 7
		ruleNr = 7;
		protocol = 17;
		sourceIP_1 = 20;
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

		Rule rule_7 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_7);

		// regel 8
		ruleNr = 8;
		protocol = 6;// TCP = 6
		sourceIP_1 = 20;
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

		Rule rule_8 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_8);

		// regel 9
		ruleNr = 9;
		protocol = 6; // TCP = 6
		sourceIP_1 = 20;
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

		Rule rule_9 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_9);

		// regel 10
		ruleNr = 10;
		protocol = -1;
		sourceIP_1 = 20;
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

		Rule rule_10 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_10);

		// regel 11
		ruleNr = 11;
		protocol = 17;
		sourceIP_1 = 30;
		sourceIP_2 = 1;
		sourceIP_3 = 2;
		sourceIP_4 = 0;
		sourceMask = 24;
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

		Rule rule_11 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_11);

		// regel 12
		ruleNr = 12;
		protocol = 17;
		sourceIP_1 = 30;
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

		Rule rule_12 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_12);

		// regel 13
		ruleNr = 13;
		protocol = 6;// TCP = 6
		sourceIP_1 = 30;
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

		Rule rule_13 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_13);

		// regel 14
		ruleNr = 14;
		protocol = 6; // TCP = 6
		sourceIP_1 = 30;
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

		Rule rule_14 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_14);

		// regel 15
		ruleNr = 15;
		protocol = -1;
		sourceIP_1 = 30;
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

		Rule rule_15 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_15);

		// regel 16
		ruleNr = 16;
		protocol = 17;
		sourceIP_1 = 40;
		sourceIP_2 = 1;
		sourceIP_3 = 2;
		sourceIP_4 = 0;
		sourceMask = 24;
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

		Rule rule_16 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_16);

		// regel 17
		ruleNr = 17;
		protocol = 17;
		sourceIP_1 = 40;
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

		Rule rule_17 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_17);

		// regel 18
		ruleNr = 18;
		protocol = 6;// TCP = 6
		sourceIP_1 = 40;
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

		Rule rule_18 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_18);

		// regel 19
		ruleNr = 19;
		protocol = 6; // TCP = 6
		sourceIP_1 = 40;
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

		Rule rule_19 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_19);

		// regel 20
		ruleNr = 20;
		protocol = -1;
		sourceIP_1 = 40;
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

		Rule rule_20 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_20);

		// regel 21
		ruleNr = 21;
		protocol = 17;
		sourceIP_1 = 50;
		sourceIP_2 = 1;
		sourceIP_3 = 2;
		sourceIP_4 = 0;
		sourceMask = 24;
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

		Rule rule_21 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_21);

		// regel 22
		ruleNr = 22;
		protocol = 17;
		sourceIP_1 = 50;
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

		Rule rule_22 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_22);

		// regel 23
		ruleNr = 23;
		protocol = 6;// TCP = 6
		sourceIP_1 = 50;
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

		Rule rule_23 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_23);

		// regel 24
		ruleNr = 24;
		protocol = 6; // TCP = 6
		sourceIP_1 = 50;
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

		Rule rule_24 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_24);

		// regel 25
		ruleNr = 25;
		protocol = -1;
		sourceIP_1 = 50;
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

		Rule rule_25 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		// voeg regel toe
		ruleList.add(rule_25);

		// regel 26
		ruleNr = 26;
		protocol = 6; // TCP
		sourceIP_1 = 10;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 220;
		destinationIP_2 = 210;
		destinationIP_3 = 10;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 8;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_26 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_26);

		// regel 27
		ruleNr = 27;
		protocol = 6; // TCP
		sourceIP_1 = 20;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 220;
		destinationIP_2 = 210;
		destinationIP_3 = 10;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 8;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_27 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_27);

		// regel 28
		ruleNr = 28;
		protocol = 6; // TCP
		sourceIP_1 = 30;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 220;
		destinationIP_2 = 210;
		destinationIP_3 = 10;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 8;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_28 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_28);

		// regel 29
		ruleNr = 29;
		protocol = 6; // TCP
		sourceIP_1 = 40;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 220;
		destinationIP_2 = 210;
		destinationIP_3 = 10;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 8;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_29 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_29);

		// regel 30
		ruleNr = 30;
		protocol = 6; // TCP
		sourceIP_1 = 50;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 220;
		destinationIP_2 = 210;
		destinationIP_3 = 10;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 8;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_30 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_30);

		// regel 31
		ruleNr = 31;
		protocol = 6; // TCP
		sourceIP_1 = 10;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 172;
		destinationIP_2 = 32;
		destinationIP_3 = 1;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 9;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_31 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_31);

		// regel 32
		ruleNr = 32;
		protocol = 6; // TCP
		sourceIP_1 = 20;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 172;
		destinationIP_2 = 32;
		destinationIP_3 = 1;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 9;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_32 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_32);

		// regel 33
		ruleNr = 33;
		protocol = 6; // TCP
		sourceIP_1 = 30;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 172;
		destinationIP_2 = 32;
		destinationIP_3 = 1;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 9;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_33 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_33);

		// regel 34
		ruleNr = 34;
		protocol = 6; // TCP
		sourceIP_1 = 40;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 172;
		destinationIP_2 = 32;
		destinationIP_3 = 1;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 9;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_34 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_34);

		// regel 35
		ruleNr = 35;
		protocol = 6; // TCP
		sourceIP_1 = 50;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 24;
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 172;
		destinationIP_2 = 32;
		destinationIP_3 = 1;
		destinationIP_4 = 8;
		destinationMask = 32;
		destinationPort_from = 5;
		destinationPort_until = 9;
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_35 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_35);

		// regel 36
		ruleNr = 36;
		protocol = -1;
		sourceIP_1 = 10;
		sourceIP_2 = 1;
		sourceIP_3 = 1;
		sourceIP_4 = 0;
		sourceMask = 0;// aangepast
		sourcePort_from = -1;
		sourcePort_until = -1;
		destinationIP_1 = 0;
		destinationIP_2 = 0;
		destinationIP_3 = 0;
		destinationIP_4 = 0;
		destinationMask = 0;
		destinationPort_from = -1; // aangepast
		destinationPort_until = -1; // aangepast
		action = true;

		bronIP = new IPPattern(sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask);
		bronPort = new PortPattern(sourcePort_from, sourcePort_until);
		doelIP = new IPPattern(destinationIP_1, destinationIP_2, destinationIP_3, destinationIP_4, destinationMask);
		doelPort = new PortPattern(destinationPort_from, destinationPort_until);

		Rule rule_36 = new Rule(ruleNr, protocol, bronIP, bronPort, doelIP, doelPort, action);
		ruleList.add(rule_36);

		return ruleList;
	}

}

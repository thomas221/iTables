/**
 * @package jIPtables
 * @copyright Copyright (C) 2011 jIPtables. All rights reserved.
 * @license GNU/GPL, see COPYING file
 * @author "Daniel Zozin <zdenial@gmx.com>"
 * 
 *         This file is part of jIPtables.
 *         jIPtables is free software: you can redistribute it
 *         and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *         jIPtables is distributed in the hope that it will be
 *         useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *         GNU General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with jIPtables. If not, see
 *         <http://www.gnu.org/licenses/>.
 * 
 */

package model.importeer_exporteer;

import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.CustomException;
import exception.ParseException;
//import model.configuration.Actie;
import model.configuration.IPPattern;
import model.configuration.PortPattern;
import static util.Constants.*;

/**
 * A rule for the firewall. You can specify the generic rule options, a protocol
 * matcher(-p option), a target (-j option) and additional matcher(-m option)
 * 
 */
public class Rule extends Command implements Cloneable {

	private static final Pattern counterPatten = Pattern.compile("\\[(\\d*):(\\d*)\\]");

	// FIXME: Accept arguments with escaped double quotes
	private static final Pattern argumentPattern = Pattern.compile("(!?)[\\s]*([-]{1,2}[-a-zA-Z0-9]*)([\\s|\"]*)([\\w|0-9|.|?|=|&|+|\\u002D|:|,|\\\\|/]*)(\\s|$)");
	//ORIGINEEL: 	private static final Pattern argumentPattern = Pattern.compile("(!?)[\\s]*([-]{1,2}[-a-zA-Z0-9]*)([\\s|\"]*)([\\w|.|?|=|&|+|\\s|:|,|\\\\|/]*)([\\s|\"]|$)");

	/**
	 * The chainName field can be written only by the rule itself and by other
	 * classes in the same package
	 */
	protected String chainname = "";
	/**
	 * The number of packets
	 */
	private long packets;
	/**
	 * The number of bytes
	 */
	private long bytes;

	/**
	 * Create an empty rule
	 */
	public Rule() {
	}

	/**
	 * @return A rule created from the passed command
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed command is null or is empty
	 */
	public static Rule buildFromCommand(String ruleCommand) {
		if (ruleCommand == null || ruleCommand.isEmpty())
			throw new IllegalArgumentException("Invalid rule command");

		Rule rule = new Rule();
		rule.initRule(ruleCommand);
		return rule;
	}

	/**
	 * Initialize the passed rule with the string command
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed ruleCommand is invalid
	 */
	private void initRule(String ruleCommand) {
		if (ruleCommand == null || ruleCommand.isEmpty())
			throw new IllegalArgumentException("Invalid rule command");

		parseArguments(ruleCommand);
		parseDataCounters(ruleCommand);
	}

	private void parseArguments(String ruleCommand) {
		Matcher argumentMatcher = argumentPattern.matcher(ruleCommand);

		while (argumentMatcher.find()) {
			String argumentName = argumentMatcher.group(2).trim();
			String argumentValue = argumentMatcher.group(4).trim();
			boolean isNegated = (argumentMatcher.group(1) != null && !argumentMatcher.group(1).isEmpty());

			if (isChainNameArgument(argumentName))
				chainname = argumentValue;
			else
				setOption(argumentName, argumentValue, isNegated);
		}
	}

	private static boolean isChainNameArgument(String argumentName) {
		return "-A".equals(argumentName) || "--append".equals(argumentName);
	}

	private void parseDataCounters(String ruleCommand) {
		Matcher counterMatcher = counterPatten.matcher(ruleCommand);
		if (counterMatcher.find()) {
			setPacketsNum(Long.parseLong(counterMatcher.group(1)));
			setBytesNum(Long.parseLong(counterMatcher.group(2)));
		}
	}

	/**
	 * @return The name of the associated chain
	 */
	public String getChainName() {
		return chainname;
	}
	
	/**
	 * Geeft attribuut chainname de opgegeven waarde.
	 * @param chainName Nieuwe waarde voor chainname.
	 */
	public void setChainName(String chainName) {
		chainname = chainName;
	}

	/**
	 * Set the number of packets that have matched this rule
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed number of packets is lesser of 0
	 * 
	 */
	public void setPacketsNum(long packets) {
		if (packets < 0)
			throw new IllegalArgumentException("The packets number cannot be less than 0, " + packets + " given");
		this.packets = packets;
	}

	/**
	 * Set the number of bytes that have matched this rule
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed number of bytes is lesser of 0
	 */
	public void setBytesNum(long bytes) {
		if (bytes < 0)
			throw new IllegalArgumentException("The bytes number cannot be less than 0, " + bytes + " given");
		this.bytes = bytes;
	}

	/**
	 * @return The number of packets that have matched this rule
	 */
	public long getPacketsNum() {
		return packets;
	}

	/**
	 * @return The number of bytes that have matched this rule
	 */
	public long getBytesNum() {
		return bytes;
	}

	@Override
	public String getCommand() {
		return super.getCommand(); // + getDataCounter();
	}
	
	/**
	 * Geeft false terug als de regel opties bevat die niet ondersteund worden door iTables.
	 * @return false indien regel opties bevat die niet ondersteund worden door iTables.
	 */
	@Override
	public boolean isSupported() {	
		return super.isSupported();
	}
	
	/**
	 * Geeft true terug in de actie van de regel ondersteund wordt. Alleen ACCEPT en DROP worden ondersteund.
	 * @return true indien actie van de regel ondersteund wordt.
	 */
	public boolean actionSupported(){
		String actie = this.getOption("-j");

		if (actie.equals("ACCEPT") || actie.equals("DROP")) {
			return true;
		}else{
			return false;
		}
	}

	private String getDataCounter() {
		if (getPacketsNum() > 0 || getBytesNum() > 0)
			return " -c " + getPacketsNum() + " " + getBytesNum();
		return "";
	}

	/**
	 * @return A copy of this rule
	 */
	@Override
	public Rule clone() {
		return buildFromCommand(getCommand());
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Geef het geparste protocol terug. Null indien bestand niet-ondersteund protocol bevat.
	 * 
	 * @return Het geparste protocol. -4 als het geparste protocol een ongeldige waarde heeft.
	 */
	public int getProtocol() {
		String protocol = this.getOption("-p");

		if(protocol != null){
			if (protocol.equalsIgnoreCase("tcp")) {
				return 6;
			} else if (protocol.equalsIgnoreCase("udp")) {
				return 17;
			} else if(protocol.equals("")){
				return -1;
			}else if(protocol.equalsIgnoreCase("ip")){
				return 0;
			}else if(protocol.equalsIgnoreCase("hopopt")){
				return 0;
			}else if(protocol.equalsIgnoreCase("icmp")){
				return 1;
			}else if(protocol.equalsIgnoreCase("igmp")){
				return 2;
			}else if(protocol.equalsIgnoreCase("ggp")){
				return 3;
			}else if(protocol.equalsIgnoreCase("ipencap")){
				return 4;
			}else if(protocol.equalsIgnoreCase("st")){
				return 5;
			}else if(protocol.equalsIgnoreCase("cbt")){
				return 7;
			}else if(protocol.equalsIgnoreCase("egp")){
				return 8;
			}else if(protocol.equalsIgnoreCase("igp")){
				return 9;
			}else if(protocol.equalsIgnoreCase("pup")){
				return 12;
			}else if(protocol.equalsIgnoreCase("argus")){
				return 13;
			}else if(protocol.equalsIgnoreCase("emcon")){
				return 14;
			}else if(protocol.equalsIgnoreCase("xnet")){
				return 15;
			}else if(protocol.equalsIgnoreCase("chaos")){
				return 16;
			}else if(protocol.equalsIgnoreCase("mux")){
				return 18;
			}else if(protocol.equalsIgnoreCase("dcn-meas")){
				return 19;
			}else if(protocol.equalsIgnoreCase("hmp")){
				return 20;
			}else if(protocol.equalsIgnoreCase("prm")){
				return 21;
			}else if(protocol.equalsIgnoreCase("xns-idp")){
				return 22;
			}else if(protocol.equalsIgnoreCase("trunk-1")){
				return 23;
			}else if(protocol.equalsIgnoreCase("trunk-2")){
				return 24;
			}else if(protocol.equalsIgnoreCase("leaf-1")){
				return 25;
			}else if(protocol.equalsIgnoreCase("leaf-2")){
				return 26;
			}else if(protocol.equalsIgnoreCase("rdp")){
				return 27;
			}else if(protocol.equalsIgnoreCase("irtp")){
				return 28;
			}else if(protocol.equalsIgnoreCase("iso-tp4")){
				return 29;
			}else if(protocol.equalsIgnoreCase("netblt")){
				return 30;
			}else if(protocol.equalsIgnoreCase("mfe-nsp")){
				return 31;
			}else if(protocol.equalsIgnoreCase("merit-inp")){
				return 32;
			}else if(protocol.equalsIgnoreCase("dccp")){
				return 33;
			}else if(protocol.equalsIgnoreCase("3pc")){
				return 34;
			}else if(protocol.equalsIgnoreCase("idpr")){
				return 35;
			}else if(protocol.equalsIgnoreCase("xtp")){
				return 36;
			}else if(protocol.equalsIgnoreCase("ddp")){
				return 37;
			}else if(protocol.equalsIgnoreCase("idpr-cmtp")){
				return 38;
			}else if(protocol.equalsIgnoreCase("tp++")){
				return 39;
			}else if(protocol.equalsIgnoreCase("il")){
				return 40;
			}else if(protocol.equalsIgnoreCase("ipv6")){
				return 41;
			}else if(protocol.equalsIgnoreCase("sdrp")){
				return 42;
			}else if(protocol.equalsIgnoreCase("ipv6-route")){
				return 43;
			}else if(protocol.equalsIgnoreCase("ipv6-frag")){
				return 44;
			}else if(protocol.equalsIgnoreCase("idrp")){
				return 45;
			}else if(protocol.equalsIgnoreCase("rsvp")){
				return 46;
			}else if(protocol.equalsIgnoreCase("gre")){
				return 47;
			}else if(protocol.equalsIgnoreCase("dsr")){
				return 48; 
			}else if(protocol.equalsIgnoreCase("bna")){
				return 49;	
			}else if(protocol.equalsIgnoreCase("esp")){
				return 50;
			}else if(protocol.equalsIgnoreCase("ah")){
				return 51;
			}else if(protocol.equalsIgnoreCase("i-nlsp")){
				return 52;
			}else if(protocol.equalsIgnoreCase("swipe")){
				return 53;
			}else if(protocol.equalsIgnoreCase("narp")){
				return 54;
			}else if(protocol.equalsIgnoreCase("mobile")){
				return 55;
			}else if(protocol.equalsIgnoreCase("tlsp")){
				return 56;
			}else if(protocol.equalsIgnoreCase("skip")){
				return 57;
			}else if(protocol.equalsIgnoreCase("ipv6-icmp")){
				return 58;
			}else if(protocol.equalsIgnoreCase("ipv6-nonxt")){
				return 59;
			}else if(protocol.equalsIgnoreCase("ipv6-opts")){
				return 60;
			}else if(protocol.equalsIgnoreCase("cftp")){
				return 62;
			}else if(protocol.equalsIgnoreCase("sat-expak")){
				return 64;
			}else if(protocol.equalsIgnoreCase("kryptolan")){
				return 65;
			}else if(protocol.equalsIgnoreCase("rvd")){
				return 66;
			}else if(protocol.equalsIgnoreCase("ippc")){
				return 67;
			}else if(protocol.equalsIgnoreCase("sat-mon")){
				return 69;
			}else if(protocol.equalsIgnoreCase("visa")){
				return 70;
			}else if(protocol.equalsIgnoreCase("ipcv")){
				return 71;
			}else if(protocol.equalsIgnoreCase("cpnx")){
				return 72;
			}else if(protocol.equalsIgnoreCase("rspf")){
				return 73;
			}else if(protocol.equalsIgnoreCase("wsn")){
				return 74;
			}else if(protocol.equalsIgnoreCase("pvp")){
				return 75;
			}else if(protocol.equalsIgnoreCase("br-sat-mon")){
				return 76;
			}else if(protocol.equalsIgnoreCase("nun-nd")){
				return 77;
			}else if(protocol.equalsIgnoreCase("wb-mon")){
				return 78;
			}else if(protocol.equalsIgnoreCase("wb-expak")){
				return 79;
			}else if(protocol.equalsIgnoreCase("iso-ip")){
				return 80;
			}else if(protocol.equalsIgnoreCase("vmtp")){
				return 81;
			}else if(protocol.equalsIgnoreCase("secure-vmtp")){
				return 82;
			}else if(protocol.equalsIgnoreCase("vines")){
				return 83;
			}else if(protocol.equalsIgnoreCase("ttp")){
				return 84;
			}else if(protocol.equalsIgnoreCase("iptm")){
				return 84;
			}else if(protocol.equalsIgnoreCase("nsfnet-igp")){
				return 85;
			}else if(protocol.equalsIgnoreCase("dgp")){
				return 86;
			}else if(protocol.equalsIgnoreCase("tcf")){
				return 87;
			}else if(protocol.equalsIgnoreCase("eigrp")){
				return 88;
			}else if(protocol.equalsIgnoreCase("ospf")){
				return 89;
			}else if(protocol.equalsIgnoreCase("ospfigp")){
				return 89;
			}else if(protocol.equalsIgnoreCase("sprite-rpc")){
				return 90;
			}else if(protocol.equalsIgnoreCase("larp")){
				return 91;
			}else if(protocol.equalsIgnoreCase("mtp")){
				return 92;
			}else if(protocol.equalsIgnoreCase("ax.25")){
				return 93;
			}else if(protocol.equalsIgnoreCase("ipip")){
				return 94;
			}else if(protocol.equalsIgnoreCase("micp")){
				return 95;
			}else if(protocol.equalsIgnoreCase("scc-sp")){
				return 96;
			}else if(protocol.equalsIgnoreCase("etherip")){
				return 97;
			}else if(protocol.equalsIgnoreCase("encap")){
				return 98;
			}else if(protocol.equalsIgnoreCase("#")){
				return 99;
			}else if(protocol.equalsIgnoreCase("gmtp")){
				return 100;
			}else if(protocol.equalsIgnoreCase("ifmp")){
				return 101;
			}else if(protocol.equalsIgnoreCase("pnni")){
				return 102;
			}else if(protocol.equalsIgnoreCase("pim")){
				return 103;
			}else if(protocol.equalsIgnoreCase("aris")){
				return 104;
			}else if(protocol.equalsIgnoreCase("scps")){
				return 105;
			}else if(protocol.equalsIgnoreCase("qnx")){
				return 106;
			}else if(protocol.equalsIgnoreCase("a/n")){
				return 107;
			}else if(protocol.equalsIgnoreCase("ipcomp")){
				return 108;
			}else if(protocol.equalsIgnoreCase("snp")){
				return 109;
			}else if(protocol.equalsIgnoreCase("compaq-peer")){
				return 110;
			}else if(protocol.equalsIgnoreCase("ipx-in-ip")){
				return 111;
			}else if(protocol.equalsIgnoreCase("vrrp")){
				return 112;
			}else if(protocol.equalsIgnoreCase("pgm")){
				return 113;
			}else if(protocol.equalsIgnoreCase("12tp")){
				return 115;
			}else if(protocol.equalsIgnoreCase("ddx")){
				return 116;
			}else if(protocol.equalsIgnoreCase("iatp")){
				return 117;
			}else if(protocol.equalsIgnoreCase("stp")){
				return 118;
			}else if(protocol.equalsIgnoreCase("srp")){
				return 119;
			}else if(protocol.equalsIgnoreCase("uti")){
				return 120;
			}else if(protocol.equalsIgnoreCase("smp")){
				return 121;
			}else if(protocol.equalsIgnoreCase("sm")){
				return 122;
			}else if(protocol.equalsIgnoreCase("ptp")){
				return 123;
			}else if(protocol.equalsIgnoreCase("isis")){
				return 124;
			}else if(protocol.equalsIgnoreCase("fire")){
				return 125;
			}else if(protocol.equalsIgnoreCase("crtp")){
				return 126;
			}else if(protocol.equalsIgnoreCase("crudp")){
				return 127;
			}else if(protocol.equalsIgnoreCase("sscopmce")){
				return 128;
			}else if(protocol.equalsIgnoreCase("iplt")){
				return 129;
			}else if(protocol.equalsIgnoreCase("sps")){
				return 130;
			}else if(protocol.equalsIgnoreCase("pipe")){
				return 131;
			}else if(protocol.equalsIgnoreCase("sctp")){
				return 132;
			}else if(protocol.equalsIgnoreCase("fc")){
				return 133;
			}else if(protocol.equalsIgnoreCase("rsvp-e2e-ignore")){
				return 134;
			}else if(protocol.equalsIgnoreCase("mobility-header")){
				return 135;
			}else if(protocol.equalsIgnoreCase("udplite")){
				return 136;
			}else if(protocol.equalsIgnoreCase("mpls-in-ip")){
				return 137;
			}else if(protocol.equalsIgnoreCase("manet")){
				return 138;
			}else if(protocol.equalsIgnoreCase("hip")){
				return 139;
			}else if(protocol.equalsIgnoreCase("shim6")){
				return 140;
			}else if(protocol.equalsIgnoreCase("wesp")){
				return 141;
			}else if(protocol.equalsIgnoreCase("rohc")){
				return 142;
			} else {
				try{
					return Integer.parseInt(protocol);
				}catch(NumberFormatException nfe){
					return -4;
				}
			}
		}else{
			return -4;
		}
	}

	/**
	 * Geef het geparste bron ip adres terug.
	 * 
	 * @return Het geparste bron ip adres.
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurd bij het parsen van
	 *             het bron ip adres.
	 */
	public IPPattern getBronIP() throws CustomException {
		String bronIPString = this.getOption("-s");

		if (bronIPString != null && bronIPString.length() > 0) {
			// zet String in IPPattern object
			IPPattern bronIp = new IPPattern(bronIPString);
			return bronIp;
			// indien geen bron ip adres opgegeven is
		} else if (bronIPString == "") {
			return new IPPattern("0.0.0.0/0");
		} else {
			return null;
		}
	}

	/**
	 * Geef het geparste doel ip adres terug.
	 * 
	 * @return Het geparste doel ip adres.
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurd bij het parsen van
	 *             het doel ip adres.
	 */
	public IPPattern getDoelIP() throws CustomException {
		String doelIPString = this.getOption("-d");

		if (doelIPString != null && doelIPString.length() > 0) {
			// zet String in IPPattern object
			IPPattern doelIp = new IPPattern(doelIPString);
			return doelIp;
			// indien geen doel ip adres opgegeven is
		} else if (doelIPString == "") {
			return new IPPattern("0.0.0.0/0");
		} else {
			// geval geen doel ip adres in regel
			return null;
		}
	}

	/**
	 * Geef bronpoort terug van de regel.
	 * 
	 * @return De bronpoort.
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurt bij het parsen van
	 *             de bronpoort.
	 */
	public PortPattern getBronpoort() throws CustomException {
		String bronpoortString = this.getOption("--sport");

		if (bronpoortString != null && bronpoortString.length() > 0) {
			PortPattern bronpoort = new PortPattern(bronpoortString);
			return bronpoort;
		} else if (bronpoortString == "") {
			// indien de bron poort niet opgegeven is zal bronpoortString gelijk
			// zijn aan "".
			return new PortPattern("*");

		} else {
			return null;
		}
	}

	/**
	 * Geef doelpoort terug van de regel.
	 * 
	 * @return De doelpoort.
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurt bij het parsen van
	 *             de bronpoort.
	 */
	public PortPattern getDoelpoort() throws CustomException {
		String doelPoortString = this.getOption("--dport");

		if (doelPoortString != null && doelPoortString.length() > 0) {
			PortPattern doelpoort = new PortPattern(doelPoortString);
			return doelpoort;
		} else if (doelPoortString == "") {
			// indien de doel poort niet opgegeven is zal doelPoortString gelijk
			// zijn aan "".
			return new PortPattern("*");
		} else {
			return null;
		}
	}

	/**
	 * Geef de geparste actie terug als boolean. Enkel toelaten=true en
	 * weigeren=false worden ondersteund.
	 * 
	 * @return De geparste actie. Enkel toelaten=true en weigeren=false worden
	 *         ondersteund.
	 * @throws ParseException
	 *             Wordt opgegooid indien er een fout gebeurd is bij het parsen
	 *             van de actie.
	 */
	public boolean getActie() throws ParseException {
		String actie = this.getOption("-j");

		if (actie != null && actie.length() > 0) {
			if (actie.equals("ACCEPT")) {
				return true;
			} else if (actie.equals("DROP") || actie.equals("REJECT")) {
				return false;
			} else {
				throw new ParseException(T_RULE_BAD_ACTION, toString());
			}
		} else {
			throw new ParseException(T_RULE_NO_ACTION, toString());
		}
	}

}

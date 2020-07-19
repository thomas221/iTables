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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.CustomException;
import exception.UnsupportedDefaultActionException;

/**
 * An iptables chain that contains the corresponding rules
 * 
 */
public class Chain extends Command implements Cloneable, List<Rule> {

	protected static final Pattern chainPattern = Pattern.compile(":[\\s]*([^ ]*)[\\s]*([^ ]*)]*[\\s]*(\\[(\\d*):(\\d*)\\])*");

	private static final Policy PREDEFINED_POLICY = Policy.ACCEPT;

	public enum Policy {
		ACCEPT, DROP
	}

	private final String chainName;
	private long packetsNum;
	private long bytesNum;
	private Policy defaultPolicy = PREDEFINED_POLICY;

	private final List<Rule> rules;

	/**
	 * Create a chain with the specified name
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed name is null or is empty
	 */
	public Chain(String name) {
		if (name == null)
			throw new IllegalArgumentException("Invalid chain name");
		this.chainName = name;
		rules = new ArrayList<Rule>();
	}

	/**
	 * Try to parse an iptables chain definition
	 * 
	 * @throws ParsingException
	 *             If some parsing error occurs
	 * @throws NullPointerException
	 *             If the passed chain is null
	 */
	protected static Chain parse(String chain) throws ParsingException,UnsupportedDefaultActionException {
		if (chain == null)
			throw new NullPointerException();

		// System.out.println(chain);

		Matcher chainMatcher = chainPattern.matcher(chain);

		if (!chainMatcher.find())
			throw new ParsingException("Invalid chain format");

		return buildParsedChain(chainMatcher);
	}

	private static Chain buildParsedChain(Matcher chainMatcher) throws UnsupportedDefaultActionException{
		Chain newChain = new Chain(chainMatcher.group(1));

		newChain.setDefaultPolicy(parseDefaultPolicy(chainMatcher));
		//newChain.setPacketsNum(parsePacketsNum(chainMatcher));
		//newChain.setBytesNum(parseBytesNum(chainMatcher));

		return newChain;
	}

	private static Policy parseDefaultPolicy(Matcher chainMatcher) throws UnsupportedDefaultActionException{
		if ("ACCEPT".equalsIgnoreCase(chainMatcher.group(2)))
			return Policy.ACCEPT;
		else if ("DROP".equalsIgnoreCase(chainMatcher.group(2)))
			return Policy.DROP;
		else
			throw new UnsupportedDefaultActionException("Chain heeft een default action die niet ondersteund wordt. Enkel ACCEPT en DROP worden ondersteund.");		
	}

	private static long parsePacketsNum(Matcher chainMatcher) {
		return Long.parseLong(chainMatcher.group(4));
	}

	private static long parseBytesNum(Matcher chainMatcher) {
		return Long.parseLong(chainMatcher.group(5));
	}

	/**
	 * Set the default policy of the chain
	 * 
	 * @param defaultPolicy
	 *            The default policy
	 * @throws NullPointerException
	 *             If the passed policy is null
	 */
	public void setDefaultPolicy(Policy defaultPolicy) {
		if (defaultPolicy == null)
			throw new NullPointerException();
		this.defaultPolicy = defaultPolicy;
	}

	/**
	 * Set the number of packets that matches this chain
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed number of packets is lesser of 0
	 */
	public void setPacketsNum(long packets) {
		if (packets < 0)
			throw new IllegalArgumentException("The packets number cannot be less than 0, " + packets + " given");
		this.packetsNum = packets;
	}

	/**
	 * Set the number of bytes that matches this chain
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed number of bytes is lesser of 0
	 */
	public void setBytesNum(long bytes) {
		if (bytes < 0)
			throw new IllegalArgumentException("The bytes number cannot be less than 0, " + bytes + " given");
		this.bytesNum = bytes;
	}

	/**
	 * @return The chain name
	 */
	public String getName() {
		return chainName;
	}

	/**
	 * @return The number of packets that matches this chain
	 */
	public long getPacketsNum() {
		return packetsNum;
	}

	/**
	 * @return The number of bytes that matches this chain
	 */
	public long getBytesNum() {
		return bytesNum;
	}

	/**
	 * @return The default policy of the chain
	 */
	public Policy getDefaultPolicy() {
		return defaultPolicy;
	}

	@Override
	public String getCommand() {
		StringBuilder outCommand = new StringBuilder(":" + chainName + " " + defaultPolicy);

		//appendDataCounters(outCommand, packetsNum, bytesNum);
		outCommand.append('\n');
		try{
			int ruleNr = 1;
			for (Rule rule : rules) {
				//appendDataCounters(outCommand, rule.getPacketsNum(), rule.getBytesNum());
				int protocol = rule.getProtocol();
				if(protocol == 6 || protocol == 17 || ((protocol != 6 && protocol != 17) && rule.getDoelpoort().isRefersToAllPorts() && rule.getBronpoort().isRefersToAllPorts())){
					outCommand.append("# Regel "+ruleNr+"\n");
					outCommand.append("-A " + chainName + rule.getCommand() + "\n");
				}else{
					outCommand.append("# Regel "+ruleNr+". Let op: Regel "+ruleNr+" heeft als protocol zowel TCP als UDP, en een bronpoort of doelpoort. Dit is niet mogelijk in iptables. Daarom worden 2 regels gegenereerd: 1 voor TCP en 1 voor UDP.\n");
					outCommand.append("-A " + chainName + " -p tcp "+rule.getCommand() + "\n");
					outCommand.append("-A " + chainName + " -p udp "+rule.getCommand() + "\n");
				}
				ruleNr++;
			}
			
		}catch(CustomException ce){
			System.out.println(ce.getOorzaak());
			return "";
		}
		return outCommand.toString();
	}

	private void appendDataCounters(StringBuilder outCommand, long packetsNum, long bytesNum) {
		if (packetsNum > -1 && bytesNum > -1)
			outCommand.append(" [" + packetsNum + ":" + bytesNum + "]");
	}

	@Override
	public Chain clone() {
		try {
			Chain c = parse(getCommand());
			for (Rule r : rules)
				c.add(r.clone());
			return c;
		} catch (ParsingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedDefaultActionException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chain other = (Chain) obj;
		if (bytesNum != other.bytesNum)
			return false;
		if (chainName == null) {
			if (other.chainName != null)
				return false;
		} else if (!chainName.equals(other.chainName))
			return false;
		if (defaultPolicy != other.defaultPolicy)
			return false;
		if (packetsNum != other.packetsNum)
			return false;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder("Chain " + chainName + " (policy " + defaultPolicy + " " + packetsNum + " packets, " + bytesNum + " bytes)\n");
		for (Rule r : rules)
			out.append(r.toString() + "\n");
		return out.toString();
	}

	public void add(int index, Rule element) {
		rules.add(index, element);
	}

	public boolean add(Rule e) {
		return rules.add(e);
	}

	public boolean addAll(Collection<? extends Rule> c) {
		return rules.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends Rule> c) {
		return rules.addAll(index, c);
	}

	public void clear() {
		rules.clear();
	}

	public boolean contains(Object o) {
		return rules.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return rules.containsAll(c);
	}

	public Rule get(int index) {
		return rules.get(index);
	}

	public int hashCode() {
		return rules.hashCode();
	}

	public int indexOf(Object o) {
		return rules.indexOf(o);
	}

	public boolean isEmpty() {
		return rules.isEmpty();
	}

	public Iterator<Rule> iterator() {
		return rules.iterator();
	}

	public int lastIndexOf(Object o) {
		return rules.lastIndexOf(o);
	}

	public ListIterator<Rule> listIterator() {
		return rules.listIterator();
	}

	public ListIterator<Rule> listIterator(int index) {
		return rules.listIterator(index);
	}

	public Rule remove(int index) {
		return rules.remove(index);
	}

	public boolean remove(Object o) {
		return rules.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return rules.removeAll(c);
	}
	
	/**
	 * Verwijder alle opgegeven regels. De regel nummers van de te verwijderen regels zijn meegegeven als parameter.
	 * Regelnummers beginnen vanaf 1. Eerste regel heeft regelnummer 1, enzovoort.
	 * @param ruleNrs Regelnummers van te verwijderen regels.
	 */
	public void removeAll(ArrayList<Integer> ruleNrs){
		List<Rule> toRemove = new ArrayList<Rule>();
		for(int i=0;i<ruleNrs.size();i++){
			toRemove.add(rules.get(ruleNrs.get(i) - 1));
		}
		removeAll(toRemove);
	}

	public boolean retainAll(Collection<?> c) {
		return rules.retainAll(c);
	}

	public Rule set(int index, Rule element) {
		return rules.set(index, element);
	}

	public int size() {
		return rules.size();
	}

	public List<Rule> subList(int fromIndex, int toIndex) {
		return rules.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return rules.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return rules.toArray(a);
	}
	
	public void repositionRule(int positionFrom, int positionTo) {
		int indexFrom = positionFrom - 1;
		int indexTo = positionTo - 1;
		// Minstens 2 regels zijn nodig om een herpositionering te kunnen
		// uitvoeren
		if (rules != null && rules.size() >= 2) {
		    Rule ruleFrom = get(indexFrom);
		    Rule ruleTo = get(indexTo);
		    set(indexTo, ruleFrom);
		    set(indexFrom, ruleTo);
		}
	}
}

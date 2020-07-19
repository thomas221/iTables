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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import exception.UnsupportedDefaultActionException;

/**
 * Contains the configuration tables used by iptables
 * 
 */
public class RuleSet implements Iterable<Table> {

	private final Table filterTable;
	private final Table mangleTable;
	private final Table rawTable;
	private final Table not_supportedTable;
	private boolean containsNotSupportedTable = false;
	private boolean actionsSupported = true;
	private boolean optionsSupported = true;

	/**
	 * The supported tables
	 */
	public enum TableType {
		FILTER, MANGLE, RAW, NOT_SUPPORTED;

		/**
		 * @return The table type as string
		 */
		public String getName() {
			switch (this) {
			case FILTER:
				return "filter";
			case MANGLE:
				return "mangle";
			case RAW:
				return "raw";
			default:
				return "not_supported";
			}
		}

		/**
		 * Get the table type from string
		 * 
		 * @return The corresponding table type or null if no type corresponds
		 */
		public static TableType getType(String table) {
			if ("filter".equalsIgnoreCase(table))
				return FILTER;
			else if ("mangle".equalsIgnoreCase(table))
				return MANGLE;
			else if ("raw".equalsIgnoreCase(table))
				return RAW;
			else
				return NOT_SUPPORTED;
		}
	}

	/**
	 * Create an empty RuleSet
	 */
	public RuleSet() {
		filterTable = new Table("filter");
		mangleTable = new Table("mangle");
		rawTable = new Table("raw");
		not_supportedTable = new Table("not_supported");
	}

	/**
	 * Try to parse the input stream as iptables rules
	 * 
	 * @return The parsed RuleSet
	 * @throws ParsingException
	 *             If some parsing error occurs
	 * @throws IOException
	 *             If some I/O error occurs in reading the input stream
	 * @throws NullPointerException
	 *             If the passed stream is null
	 */
	public static RuleSet parse(InputStream ruleStream) throws ParsingException, IOException, UnsupportedDefaultActionException {
		if (ruleStream == null)
			throw new NullPointerException();
		BufferedReader b = new BufferedReader(new InputStreamReader(ruleStream));
		StringBuilder s = new StringBuilder();
		for (String line = b.readLine(); line != null; line = b.readLine())
			s.append(line + "\n");
		return RuleSet.parse(s.toString());
	}

	/**
	 * Try to parse the input as iptables rules
	 * 
	 * @return The parsed RuleSet
	 * @throws ParsingException
	 *             If the operation cannot be completed, for example for
	 *             insufficient unix privileges
	 * @throws NullPointerException
	 *             If the passed rules is null
	 */
	public static RuleSet parse(String rules) throws ParsingException,UnsupportedDefaultActionException {
		if (rules == null)
			throw new NullPointerException();

		RuleSet parsedRules = new RuleSet();
		BufferedReader r = new BufferedReader(new StringReader(rules));
		Table currentTable = parsedRules.filterTable;

		int lineNum = 1;
		String line;
		try {
			while ((line = r.readLine()) != null) {
				if(!line.isEmpty()){ //!isEmptyLine(line) en !line.isEmpty() toegevoegd door Thomas Van Poucke voor accepteren van 'lege' regels
					line = line.trim();
					if (isTableLine(line)){
						currentTable = getCurrentTable(parsedRules, line);
					}	
					else if (isOKForParseLine(line)){
						if(currentTable.getName().equals("not_supported")){ //geval dat een niet ondersteunde table regels bevat die wij niet ondersteunen.
							parsedRules.containsNotSupportedTable = true;
						}else{
							parseLine(line, currentTable, parsedRules);
						}
						
					}						
				}
				lineNum++;
			}
		} catch (ParsingException e) {
			throw new ParsingException(lineNum, e.getParsingMessage());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return parsedRules;

	}
	
	private static boolean isOKForParseLine(String line){
		return line.startsWith("-A") || line.charAt(0) == ':';
	}

	private static boolean isCommentLine(String line) {
		return line.charAt(0) == '#';
	}
	
	private static boolean isCommitLine(String line)	{
		return line.trim().equals("COMMIT");
	}
	
	/**
	 * Methode die true teruggeeft als de regel enkel spaties en een enter bevat.
	 * @param line Te parsen regel.
	 * @return true Indien de regel enkel spaties en een enter bevat.
	 */
	private static boolean isEmptyLine(String line) {
		boolean isEmptyLine = true;
		for(int i=0;i<line.length();i++){
			char character = line.charAt(i);
			if(!Character.isWhitespace(character)){
				isEmptyLine = false;
			}
		}
		return isEmptyLine;
	}

	private static boolean isTableLine(String line) {
		return line.charAt(0) == '*';
	}
	
	private static boolean isNewChainLine(String line) {
		return line.startsWith("-N");
	}

	private static Table getCurrentTable(RuleSet ruleSet, String tableLine) throws ParsingException {
		if ("filter".equals(tableLine.substring(1)))
			return ruleSet.filterTable;
		else if ("mangle".equals(tableLine.substring(1)))
			return ruleSet.mangleTable;
		else if ("raw".equals(tableLine.substring(1)))
			return ruleSet.rawTable;
		else{
			return ruleSet.not_supportedTable;
		}
	}

	/**
	 * @return The table corresponding to the specified table type or table NOT_SUPPORTED if no
	 *         table matches
	 */
	public Table getTable(TableType type) {
		switch (type) {
		case FILTER:
			return filterTable;
		case MANGLE:
			return mangleTable;
		case RAW:
			return rawTable;
		default:
			return not_supportedTable;
		}
	}

	public List<Rule> getRules() {
		
		if (getTable(TableType.FILTER) != null && getTable(TableType.FILTER).getChain("FORWARD") != null && !getTable(TableType.FILTER).getChain("FORWARD").isEmpty()) {
			return getTable(TableType.FILTER).getChain("FORWARD");
		} else {
			return new ArrayList<Rule>();
		}
	}

	/**
	 * @return The available tables
	 */
	public Table[] getTables() {
		Table[] t = new Table[3];
		t[0] = filterTable;
		t[1] = mangleTable;
		t[2] = rawTable;
		return t;
	}

	@Override
	public Iterator<Table> iterator() {
		return Arrays.asList(getTables()).iterator();
	}

	/**
	 * @return The rules in iptables format
	 */
	public String getExportRules() {
		String export = "# iptables configuratie gegenereerd met iTables.\n";
		export += "# iTables is een webapplicatie ontwikkeld door Thomas Van Poucke, Joel Craenhals en Ron Melger voor de bachelorproef aan de Open Universiteit.\n";
		// huidige datum
		Calendar today = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("HH:mm:ss 'op' dd/MM/yyyy");

		export += "# Gegenereerd om " + df.format(today.getTime()) + "\n\n";
		export += filterTable.getCommand() + mangleTable.getCommand() + rawTable.getCommand();
		return export;
	}
	
	/*
	 * Geeft false terug als de configuratie opties bevat die niet ondersteund worden door iTables.
	 * @return false indien configuratie opties bevat die niet ondersteund worden door iTables.
	 */
	/*
	public boolean isSupported() {
		System.out.println("XX");
		for(model.importeer_exporteer.Rule rule:getRules()){
			boolean isSupported = rule.isSupported();
			System.out.println("Rule :"+isSupported);
			if(!isSupported){
				return false;
			}
		}
		return true;
	}
	*/
	/**
	 * Geeft waarde van actionsSupported terug. Deze is true indien er in de configuratie regels zaten die een andere actie hebben
	 * als ALLOW of DROP. Deze regels werden niet mee ingelezen dus niet alle regels zijn mee ingelezen in de configuratie.
	 * @return Waarde van actionsSupported
	 */
	public boolean getActionsSupported() {
		return actionsSupported;
	}
	
	/**
	 * Geeft attribuut actionsSupported de nieuwe opgegeven waarde.
	 * @param actionsSupported Nieuwe waarde voor actionsSupported.
	 */
	public void setActionsSupported(boolean actionsSupported){
		this.actionsSupported = actionsSupported;
	}

	/**
	 * Parse a generic command line
	 */
	private static void parseLine(String rule, Table table, RuleSet parsedRules) throws ParsingException,UnsupportedDefaultActionException {
		if (rule.charAt(0) == ':')
			parseChain(rule, table);
		else
			parseCommand(rule, table, parsedRules);
	}

	/**
	 * Parse a chain command
	 */
	private static void parseChain(String chain, Table table) throws ParsingException,UnsupportedDefaultActionException {
		table.addChain(Chain.parse(chain));
	}

	/**
	 * Parse a generic command (not chain)
	 */
	private static void parseCommand(String rule, Table table, RuleSet parsedRules) throws ParsingException {
		if (!rule.startsWith("-A") && rule.charAt(0) != '[')
			return;

		Rule r = Rule.buildFromCommand(rule);
		if (r == null)
			return;
		String chainName = r.getChainName();
		Chain c = table.getChain(chainName);
		if (c == null){ //chain bestond nog niet - maak deze aan
			c = new Chain(chainName);
			table.addChain(c);
			r.setChainName(chainName);
		}
		
		//indien actie DROP of ALLOW is wordt regel toegevoegd aan chain - andere regels worden niet ingelezen omdat ze niet ondersteund worden
		if(r.actionSupported()){
			//indien regel opties bevat die niet ondersteund worden - bewaar dit in attribuut optionsSupported
			if(!r.isSupported()){
				parsedRules.setOptionsSupported(false);
			}
			//voeg regel toe aan chain
			c.add(r);
		}else{
			parsedRules.setActionsSupported(false);
		}
	}

	@Override
	public String toString() {
		return filterTable.toString() + "\n" + rawTable.toString() + "\n" + mangleTable.toString();
	}

	/**
	 * @return the containsNotSupportedTable
	 */
	public boolean getContainsNotSupportedTable() {
		return containsNotSupportedTable;
	}
	
	/**
	 * @param containsNotSupportedTable the containsNotSupportedTable to set
	 */
	public void setContainsNotSupportedTable(boolean containsNotSupportedTable) {
		this.containsNotSupportedTable = containsNotSupportedTable;
	}

	/**
	 * @return the optionsSupported
	 */
	public boolean getOptionsSupported() {
		return optionsSupported;
	}

	/**
	 * @param optionsSupported the optionsSupported to set
	 */
	public void setOptionsSupported(boolean optionsSupported) {
		this.optionsSupported = optionsSupported;
	}

	
}
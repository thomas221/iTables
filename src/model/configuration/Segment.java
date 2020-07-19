package model.configuration;

import java.util.ArrayList;
import java.util.TreeSet;
import net.sf.javabdd.BDD;

/**
 * De klasse Segment representeert een (sub)ruimte die ontstaat als de regelset
 * wordt opgedeeld. Die opdeling heeft als doelstelling de regelset beter te
 * kunnen analyseren. Het segment is een zo groot mogelijke (sub)ruimte waarbij
 * moet gelden dat voor ieder tweetal (verschillende) segmenten geldt dat de
 * doorsnede leeg is.
 * 
 * 
 */
public class Segment // code gecontroleerd 9 mei 2013
{
	/**
	 * Bevat de inhoud van dit segment, in een BDD datatype.
	 */
	private BDD segmentBDD;
	/**
	 * Bevat de regels die (gedeeltelijk) in dit segment zitten.
	 */
	private ArrayList<Rule> rules;
	/**
	 * Bevat de rule subspaces van de regels die (gedeeltelijk) in dit segment
	 * zitten.
	 */
	private ArrayList<RuleSubspace> ruleSubspace = new ArrayList<RuleSubspace>();
	/**
	 * Bevat het nummer van dit segment. Nummering vanaf nummer 1.
	 */
	private int segmentNumber;
	/**
	 * TreeSet datatype dat de regelnummers bevat van de regels die
	 * (gedeeltelijk) in dit segment zitten.
	 */
	private TreeSet<Integer> treeSet = new TreeSet<Integer>();
	/**
	 * Geeft aan of er meerdere regels tot dit segment horen.
	 */
	private boolean overlapping;
	/**
	 * Geeft aan of segment conflicterend is. Een conflicterend segment heeft zowel regels met actie toelaten als regels met actie tegenhouden.
	 */
	private boolean conflicting; // kan alleen gebruikt worden na overlapping ==
	/** 
	  * Geeft aan of de firewall regels in dit segment allen de actie
	  * accepteer hebben.
	  */
	private boolean allowedSpace;
	/** 
	  * Geeft aan of de firewall regels in dit segment allen de actie
	  * tegenhouden hebben.
	  */
	private boolean deniedSpace;

	/**
	 * Maakt een segment aan op basis van een segment nummer, een BDD datatype,
	 * en een regel. Het segment zal initieel enkel de opgegeven regel bevatten.
	 * De BDD voorstelling zal de meegegeven BDD voorstelling zijn.
	 * 
	 * @param number
	 *            Gewenste segment nummer.
	 * @param space
	 *            BDD voorstelling van het segment.
	 * @param rule
	 *            Regel die initieel in het segment zit.
	 */
	public Segment(int number, BDD space, Rule rule) {
		rules = new ArrayList<Rule>();
		rules.clear();
		segmentBDD = space;
		rules.add(rule);
		// System.out.println("in constructor van Segment met ruleNr : ");
		// System.out.println("in Segment size : " + rules.size());
		this.segmentNumber = number;
		treeSet.add(rule.getRuleNr());
	}

	/**
	 * Maakt een segment aan met het opgegeven segment nummer en BDD ruimte.
	 * 
	 * @param number
	 *            Gewenste nummer van het nieuwe segment.
	 * @param space
	 *            Gewenste BDD ruimte van het nieuwe segment.
	 */
	public Segment(int number, BDD space) {
		rules = new ArrayList<Rule>();
		rules.clear();
		segmentBDD = space;

		// System.out.println("in overloaded constructor van Segment (zonder ruleNr)");
		// System.out.println("in Segment size : " + rules.size());
		this.segmentNumber = number;
	}

	/**
	 * Maakt nieuwe rule subspaces aan die horen bij de regels die in dit
	 * segment samenkomen.
	 * 
	 */
	public void initPropertyAssignment() {
		// maak property array leeg
		ruleSubspace.clear();
		// maak een nieuwe RuleSubspace voor elke Rule
		RuleSubspace subspace;

		for (Rule rule : getRules()) {
			subspace = new RuleSubspace(rule.getRuleNr());
			ruleSubspace.add(subspace);
		}
	}

	/**
	 * Geeft de BDD voorstelling van dit segment terug.
	 * 
	 * @return De BDD voorstelling van dit segment.
	 */
	public BDD getSegmentBDD() {
		return segmentBDD;
	}

	/**
	 * Geeft het aantal rule subspaces terug die horen bij dit segment.
	 * 
	 * @return Het aantal rule subspaces die horen bij dit segment.
	 */
	public int getSizeRuleSubspace() {
		return ruleSubspace.size();
	}

	/**
	 * Geeft het aantal rules terug die horen bij dit segment.
	 * 
	 * @return Het aantal rules die horen bij dit segment.
	 */
	public int getNumberOfRules() {
		return rules.size();
	}

	/**
	 * Geeft regel met opgeven index terug uit de ArrayList van regels die horen
	 * bij dit segment.
	 * 
	 * @param index
	 *            Index van de gewenste regel in de ArrayList van regels die
	 *            horen bij dit segment. De index loopt vanaf 0.
	 * @return De regel op de positie van de opgegeven index.
	 */
	public Rule getRule(int index) {
		return rules.get(index);
	}

	/**
	 * Geeft het nummer van dit segment terug.
	 * 
	 * @return Het nummer van dit segment.
	 */
	public int getSegmentNumber() {
		return segmentNumber;
	}

	/**
	 * Geeft de regel nummers terug van de regels die in dit segment samenkomen.
	 * 
	 * @return Regel nummer van de regels in dit segment.
	 */
	public TreeSet<Integer> getRuleNumbers() {
		return treeSet;
	}

	/**
	 * Vervang de BDD voorstelling van dit segment door opgegeven BDD.
	 * 
	 * @param segmentBDD
	 *            Nieuwe BDD voorstelling van dit segment.
	 */
	public void replaceBDD(BDD segmentBDD) {
		this.segmentBDD = segmentBDD;
	}

	/**
	 * Voegt de opgegeven regel toe aan de ArrayList van regels die horen bij
	 * dit segment.
	 * 
	 * @param rule
	 *            Toe te voegen regel.
	 */
	public void addRule(Rule rule) {
		// System.out.println("in Segment addRule : " + (ruleNr + 1));
		rules.add(rule);
		treeSet.add(rule.getRuleNr());
	}

	/**
	 * Voer informatie van dit segment uit als tekst naar de standaard uitvoer
	 * voor test doeleinden.
	 */
	public void printSegment() {
		int aantalRegels = rules.size();
		System.out.println("Segment nr. : " + getSegmentNumber());
		for (int j = 0; j < aantalRegels; j++) {
			System.out.println("regel : " + (rules.get(j).getRuleNr()));
		}
	}

	/**
	 * Geeft <code>true</code> terug als de opgegeven regel zit in de ArrayList
	 * van regels die horen bij dit segment.
	 * 
	 * @param ruleToTest
	 *            Regel waarvan men wil weten of deze aanwezig is in de
	 *            ArrayList van regels die horen bij dit segment.
	 * @return <code>true</code> als de opgegeven regel aanwezig is in de
	 *         ArrayList van regels die tot het segment hoort.
	 */
	public boolean containsSegmentRule(Rule ruleToTest) {
		for (Rule rule : rules) {
			if (rule.getRuleNr() == ruleToTest.getRuleNr()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Geeft de regels terug die horen bij dit segment als een ArrayList van
	 * regels .
	 * 
	 * @return ArrayList van alle regels die horen bij dit segment.
	 */
	public ArrayList<Rule> getRules() {
		return this.rules;
	}

	/**
	 * Geeft alle rule subspaces terug die horen bij dit segment.
	 * 
	 * @return Alle rule subspaces die horen bij dit segment.
	 */
	private ArrayList<RuleSubspace> getRuleSubspace() {
		return this.ruleSubspace;
	}

	/**
	 * Geeft de rule subspace terug met opgegeven index in de ArrayList van rule
	 * subspaces die horen bij dit segment.
	 * 
	 * @param index
	 *            Index van gewenste rule subspace.
	 * @return Gewenste rule subspace.
	 */
	public RuleSubspace getRuleSubspace(int index) {
		return ruleSubspace.get(index);
	}

	/**
	 * Geeft <code>true</code> terug als de opgegeven regel hoort tot de
	 * opgegeven segment.
	 * 
	 * @param segment
	 *            Opgegeven segment.
	 * @param ruleToTest
	 *            Opgegeven regel.
	 * @return true als de opgegeven regel hoort tot de opgegeven segment.
	 */
	public static boolean containsSegmentRule(Segment segment, Rule ruleToTest) {
		for (Rule rule : segment.getRules()) {
			if (rule.getRuleNr() == ruleToTest.getRuleNr()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * De waarden van de attributen overlapping conflicting allowedSpace en
	 * deniedSpace worden bepaalt en vastgelegd. <br />
	 * Deze zijn daarna met get-Methoden te verkrijgen, zij kunnen geen andere
	 * waarde krijgen en er zijn dan ook geen set-Methoden voor. <br />
	 * Als er een aanpassing is geweest in de regelset dan moet opnieuw een
	 * object van klasse Space worden aangemaakt die op zijn beurt opnieuw
	 * segmenten aanmaakt.
	 */
	public void setAttributes() {
		if (getNumberOfRules() > 1)
			overlapping = true;
		else
			overlapping = false;

		boolean bevatDeniedRules = false;
		boolean bevatAllowedRules = false;
		for (Rule rule : getRules()) {
			if (rule.getAction() == true)
				bevatAllowedRules = true;
			else
				bevatDeniedRules = true;
		}

		if (bevatDeniedRules && bevatAllowedRules)
			conflicting = true;
		else
			conflicting = false;

		if (bevatDeniedRules)
			allowedSpace = false;
		else
			allowedSpace = true;

		if (bevatAllowedRules)
			deniedSpace = false;
		else
			deniedSpace = true;
	}

	/**
	 * Geeft <code>true</code> terug als in segment meer dan 1 regel
	 * bevat.
	 * 
	 * @return true als dit segment meerder regels bevat.
	 */
	public boolean isOverlapping() {
		return overlapping;
	}

	/**
	 * Geeft <code>true</code> terug als in dit segment regels met een
	 * verschillende actie voorkomen. <br />
	 * Opvragen van deze methode is alleen zinvol nadat de methode
	 * isOverlapping() <code>true</code> heeft teruggegeven.
	 * 
	 * @return <code>true</code> als in dit segment regels met een verschillende
	 *         actie voorkomen.
	 */
	public boolean getConflicting() {
		return conflicting;
	}

	/**
	 * Geeft aan of de firewall regels in dit segment allen de eigenschap
	 * accepteer hebben.
	 * 
	 * @return <code>true</code> als er geen firewall regels in dit segment zijn
	 *         met een andere waarde voor action dan accepteer anders
	 *         <code>false/code>
	 */
	public boolean getAllowedSpace() {
		return allowedSpace;
	}

	/**
	 * Geeft aan of de firewall regels in dit segment allen de actie weigeren
	 * hebben.
	 * 
	 * @return <code>true</code> als alle regels horende bij dit segment de
	 *         actie weigeren hebben anders <code>false/code>.
	 */
	public boolean getDeniedSpace() {
		return deniedSpace;
	}

	// voor debug en testdoeleinden
	/**
	 * Print de toewijzing van properties aan rule subspaces van dit segment uit
	 * naar de standaard uitvoer.
	 */
	public void printPropertyAssignments() {
		System.out.println("Segment : " + segmentNumber);
		for (RuleSubspace ruleSubspace : getRuleSubspace()) {
			System.out.print("Rule Nr. & Property : " + ruleSubspace.getNumber() + " ");
			switch (ruleSubspace.getProperty()) {
			case SI:
				System.out.println("SI");
				break;
			case WI:
				System.out.println("WI");
				break;
			case R:
				System.out.println("R");
				break;
			case C:
				System.out.println("C");
				break;
			default:

			}
		}
	}

}
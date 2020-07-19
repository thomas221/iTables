package model.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.CustomJSPTags;
import exception.CustomException;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;
import static util.Constants.*;

/**
 * De klasse Space bevat de geimplementeerde algoritmes die nodig zijn voor het
 * analyseren van een RuleSet van Firewall-regels. Bij veel van die methodes
 * wordt gebruik gemaakt van een efficiente datastructuur: BDD's <br />
 * Slechts enkele methodes zijn static, voor het gebruik van de meeste methodes
 * moet een object van Space worden aangemaakt met als parameter de huidige
 * RuleSet van Firewall regels.
 * 
 * @author Ron Melger
 * 
 */
public class Space {
	/**
	 * De segmenten waarin de firewall regelset is opgedeeld t.b.v. een analyse
	 * van die regelset.
	 */
	private ArrayList<Segment> segments = new ArrayList<Segment>();
	/**
	 * Bevat alle regels waarop de algoritmes betrekking hebben.
	 */
	private ArrayList<Rule> rules;
	/**
	 * Hulp variabele die de machten van twee zal bevatten.
	 */
	private int[] powerOfTwo;
	/**
	 * Bevat BDDFactory, gebruik van BDD's is nodig voor de algoritmes.
	 */
	private BDDFactory fact = JFactory.init(100000, 10000);
	/**
	 * Hulpvariabele. Bevat de BDD representatie die altijd <code>true</code> is
	 * (Tautologie).
	 */
	private BDD True = fact.one();
	/**
	 * Hulpvariabele. Bevat de BDD representatie die altijd <code>false</code>
	 * is.
	 */
	private BDD False = fact.zero();
	/**
	 * Standaard static waarde voor iets dat nog niet gespecificeerd is.
	 */
	public final static int NOT_SPECIFIED = -2;

	/**
	 * De constructor om een ruimte aan te maken. Hierbij worden de
	 * firewallregels meegegeven waarvan de ruimte moet worden aangemaakt. <br />
	 * De segmentering wordt automatisch uitgevoerd.
	 * 
	 * @param rules
	 *            de firewallregels die de ruimte representeren
	 */
	public Space(ArrayList<Rule> rules) throws CustomException {
		this.rules = rules;
		powerOfTwo = new int[16];
		fact.setVarNum(105);

		int nieuweWaarde = 1;
		powerOfTwo[0] = nieuweWaarde;

		for (int i = 1; i < 16; i++) {
			nieuweWaarde = nieuweWaarde * 2;
			powerOfTwo[i] = nieuweWaarde;
		}
		makeSegments();
	}

	/**
	 * Geeft de segmenten van de ruimte terug als een ArrayList.
	 * 
	 * @return de segmenten in de ruimte
	 */
	public ArrayList<Segment> getSegments() {
		return segments;
	}

	/**
	 * Maakt de segmenten van de ruimte die door de firewall regels wordt
	 * bestreken.
	 * 
	 * @throws CustomException
	 *             Treedt op bij het aanmaken van de verschillende BDD's als er
	 *             in een firewallregel een abnormaliteit wordt aangetroffen.
	 */
	private void makeSegments() throws CustomException {
		BDD spaceRule;
		Segment segment;
		Segment nieuwSegment;
		BDD segmentBDD;
		int segmentNummer = 0; // de segmenten krijgen een nummer //
		// 15 juni 2013

		int rulesSize = rules.size();

		boolean extraBreak = false;

		// zoalng er nog regels zijn DO
		for (int ruleIndex = 0; ruleIndex < rulesSize; ruleIndex++) {
			extraBreak = false;

			// zet rule in Java om naar BDD
			spaceRule = ruleToBDD(rules.get(ruleIndex));

			// bepaal hoeveel segmenten er nu zijn
			int aantalSegmenten = segments.size();

			for (int j = 0; j < aantalSegmenten; j++) {
				// neem het volgende segment

				segment = segments.get(j);

				// neem van dit segment het BDD
				segmentBDD = segment.getSegmentBDD();

				// test of de ruimte van de rule gelijk is aan het segment
				if (spaceRule.equals(segmentBDD)) {
					// System.out.println("ruimte rule = ruimte segment");
					// ruimte van de rule is gelijk aan dit segment
					// voeg alleen het regelnummer toe aan dat segment
					// segment.addRule(mijnArrayList.get(ruleNr)); // aangepast
					// 13 juni 2013
					segment.addRule(rules.get(ruleIndex));
					extraBreak = true;
					break;
				} else {
					// tempAND is tijdelijk nodig voor de analyse
					BDD tempAND = spaceRule.apply(segmentBDD, BDDFactory.and);

					// test of het een strikte deelverzameling betreft
					if (tempAND.equals(spaceRule)) {
						// System.out.println("betreft een strikte deelverzameling : ");

						// spaceRule is een strikte deelverzameling van segment,
						// het segment moet opgedeeld worden
						// bepaal nu de ruimte die niet in beide zit
						BDD surplus = spaceRule.apply(segmentBDD, BDDFactory.xor);

						// maak een nieuw segment bestaande uit de BDD maar
						// zonder de Rule
						segmentNummer++;
						nieuwSegment = new Segment(segmentNummer, surplus);

						// copieer de regels uit het oude segment, zo blijven ze
						// op volgorde
						int numberOfRules = segment.getNumberOfRules();
						for (int rule_Index = 0; rule_Index < numberOfRules; rule_Index++) {
							nieuwSegment.addRule(segment.getRule(rule_Index));
						}

						// voeg het nieuwe segment toe aan beheer

						segments.add(nieuwSegment);

						// het segment dat we aan het vergelijken zijn wordt
						// vervangen door de rule
						segment.replaceBDD(spaceRule);
						segment.addRule(rules.get(ruleIndex));

						// klaar met vergelijken van segmenten. beeindig de for
						// loop
						extraBreak = true;
						break;
					} else {
						// test of spaceRule een superset is van segment
						if (tempAND.equals(segmentBDD)) {
							// spaceRule is een superset van segment
							// vervang spaceRule door de ruimte die niet in
							// beide zit
							spaceRule = spaceRule.apply(segmentBDD, BDDFactory.xor);
							segment.addRule(rules.get(ruleIndex));
						} else {
							// test of de doorsnede niet leeg is
							if (!(tempAND.equals(False))) {
								// spaceRule en segment hebben geen lege
								// doorsnede

								// maak een nieuw segment bestaande uit de BDD
								// en de rule
								BDD nieuwBDD = tempAND.apply(segmentBDD, BDDFactory.xor);

								// maak een nieuw segment bestaande uit de BDD
								// maar zonder de Rule
								segmentNummer++;
								nieuwSegment = new Segment(segmentNummer, nieuwBDD);

								// copieer de regels uit het oude segment, zo
								// blijven ze op volgorde
								int numberOfRules = segment.getNumberOfRules();
								for (int rule_Index = 0; rule_Index < numberOfRules; rule_Index++) {
									nieuwSegment.addRule(segment.getRule(rule_Index));
								}

								// voeg het nieuwe segment toe aan beheer
								segments.add(nieuwSegment);

								// segmentBDD = tempAND;
								// vervang de BDD van het segment door de
								// doorsnede
								segment.replaceBDD(tempAND);
								segment.addRule(rules.get(ruleIndex));
								spaceRule = tempAND.apply(spaceRule, BDDFactory.xor);
							}
						}
					}
				}
			}
			if (!extraBreak) {
				// System.out.println("bij Algoritme regel 17 : ");
				segmentNummer++;
				nieuwSegment = new Segment(segmentNummer, spaceRule, rules.get(ruleIndex));
				segments.add(nieuwSegment);
			}
		} // end buitenste for lus - die met ruleNr
			// segmentenBeheer.printSegmentenBeheer();
		int aantalSegmenten = segments.size();
		for (int i = 0; i < aantalSegmenten; i++)
			segments.get(i).setAttributes();

	} // end maakSegmenten

	/**
	 * Methode die voor een firewallregel een representatie in de vorm van een
	 * binaire beslisboom aanmaakt en teruggeeft. De binaire beslisboom is een
	 * representatie van de firewallregel waarin alle velden van de betreffende
	 * regel zijn verwerkt.
	 * 
	 * @param rule
	 *            de firewallregel waarvan we de binaire beslisboom
	 *            representatie wensen
	 * @return een binaire beslisboom die de opgegeven firewallregel
	 *         representeert
	 * 
	 * @throws CustomException
	 *             Treedt op bij het aanmaken van de verschillende BDD's als er
	 *             in een firewallregel een abnormaliteit wordt aangetroffen.
	 */
	private BDD ruleToBDD(Rule rule) throws CustomException // code
	// gecontroleerd 9
	// mei 2013;
	// aangepast aan
	// netmask 23 mei
	// 2013
	{ // aangepast aan range in poortnummers 1 juni 2013
		// aangepast voor packetFirstMatch 13 juni 2013
		int protocol = rule.getProtocol();
		int sourceIP_1 = rule.getSourceIP().getNumber1();
		int sourceIP_2 = rule.getSourceIP().getNumber2();
		int sourceIP_3 = rule.getSourceIP().getNumber3();
		int sourceIP_4 = rule.getSourceIP().getNumber4();
		int sourceMask = rule.getSourceIP().getMask();
		int sourcePortFrom = rule.getSourcePort().getLowerBound();
		int sourcePortUntil = rule.getSourcePort().getUpperBound();
		int destinationIP_1 = rule.getDestinationIP().getNumber1();
		int destinationIP_2 = rule.getDestinationIP().getNumber2();
		int destinationIP_3 = rule.getDestinationIP().getNumber3();
		int destinationIP_4 = rule.getDestinationIP().getNumber4();
		int destinationMask = rule.getDestinationIP().getMask();
		int destinationPortFrom = rule.getDestinationPort().getLowerBound();
		int destinationPortUntil = rule.getDestinationPort().getUpperBound();

		return ruleOrPacketToBDD(protocol, sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask, sourcePortFrom, sourcePortUntil, destinationIP_1, destinationIP_2, destinationIP_3,
				destinationIP_4, destinationMask, destinationPortFrom, destinationPortUntil);

	}

	/**
	 * Methode die gbruikt wordt om van een firewall regel, een fictief
	 * pakketje, of van regels uit een opsomming van een BDD weer een BDD te
	 * maken.
	 * 
	 * @param protocol
	 *            het protocol
	 * @param sourceIP_1
	 *            eerste deel van het bron IP-adres
	 * @param sourceIP_2
	 *            tweede deel van het bron IP-adres
	 * @param sourceIP_3
	 *            derde deel van het bron IP-adres
	 * @param sourceIP_4
	 *            vierde deel van het bron IP-adres
	 * @param sourceMask
	 *            het subnetmasker van de bronpoort (waarde 0..32)
	 * @param sourcePortFrom
	 *            de bronpoort vanaf poortnummer
	 * @param sourcePortUntil
	 *            de bronpoort tot en met poortnummer
	 * @param destinationIP_1
	 *            eerste deel van het bestemmings IP-adres
	 * @param destinationIP_2
	 *            tweede deel van het bestemmings IP-adres
	 * @param destinationIP_3
	 *            derde deel van het bestemmings IP-adres
	 * @param destinationIP_4
	 *            vierde deel van het bestemmings IP-adres
	 * @param destinationMask
	 *            het subnetmasker van de bestemmingspoort (waarde 0..32)
	 * @param destinationPortFrom
	 *            de bestemmingspoort vanaf poortnummer
	 * @param destinationPortUntil
	 *            de bestemmingspoort tot en met poortnummer
	 * @return een BDD gemaakt van de invoervelden Voorwaarde: sourcePort_until
	 *         >= sourcePort_from EN destinationPort_until >=
	 *         destinationPort_from
	 */
	private BDD ruleOrPacketToBDD(int protocol, int sourceIP_1, int sourceIP_2, int sourceIP_3, int sourceIP_4, int sourceMask, int sourcePortFrom, int sourcePortUntil, int destinationIP_1,
			int destinationIP_2, int destinationIP_3, int destinationIP_4, int destinationMask, int destinationPortFrom, int destinationPortUntil) {
		// aangepast voor packetFirstMatch 13 juni 2013

		BDD tempBDD = True;
		BDD bovengrensBDD; // voor range in poortnummers
		BDD ondergrensBDD; // voor range in poortnummers
		BDD rangeBDD; // voor range in poortnummers
		int index;

		// voeg protocol toe aan BDD
		index = 0;
		// verkrijg protocol als int - int kan beter geencodeerd worden in BDD.
		int protocol_als_int = protocol;

		if (protocol_als_int < 0) {
			// doe niets; don't care ; asterisk // stond alleen protocol_als_int
			// == -1 (niets over -2)
		} else {
			for (int i = 7; i >= 0; i--) {
				index++;
				if (protocol_als_int >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					protocol_als_int = protocol_als_int - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
			} // end for
		}
		// voeg Source IP 1 toe nsar BDD
		index = 8;
		// deze code is vermoedelijk af te spitsen zodat je het samen kan nemen
		// met destination IP
		for (int i = 7; i >= 0; i--) {
			if (sourceMask > 0) {
				index++;
				if (sourceIP_1 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					sourceIP_1 = sourceIP_1 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				sourceMask--;
			}
		} // end for

		// voeg Source IP 2 toe nsar BDD
		index = 16;

		for (int i = 7; i >= 0; i--) {
			if (sourceMask > 0) {
				index++;
				if (sourceIP_2 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					sourceIP_2 = sourceIP_2 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				sourceMask--;
			}
		} // end for

		// voeg Source IP 3 toe nsar BDD
		index = 24;

		for (int i = 7; i >= 0; i--) {
			if (sourceMask > 0) {
				index++;
				if (sourceIP_3 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					sourceIP_3 = sourceIP_3 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				sourceMask--;
			}
		} // end for

		// voeg Source IP 4 toe nsar BDD
		index = 32;

		for (int i = 7; i >= 0; i--) {
			if (sourceMask > 0) {
				index++;
				if (sourceIP_4 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					sourceIP_4 = sourceIP_4 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				sourceMask--;
			}
		} // end for

		// voeg Source Port toe aan BDD
		index = 40;
		if (sourcePortFrom == -1) {
			// doe niets; don't care ; asterisk
		} else {
			// test of ondergrens = bovengrens
			if (sourcePortFrom == sourcePortUntil) {
				// 1 poortnummer
				for (int i = 15; i >= 0; i--) {
					index++;
					if (sourcePortFrom >= powerOfTwo[i]) {
						tempBDD = tempBDD.and(fact.ithVar(index));
						sourcePortFrom = sourcePortFrom - powerOfTwo[i];
					} else {
						tempBDD = tempBDD.and(fact.nithVar(index));
					}
				} // end for
			} else // het betreft een range van poorten
			{
				if (sourcePortFrom > 0)
				{
					bovengrensBDD = everythingUnderBoundaryBDD(index, sourcePortUntil);
					ondergrensBDD = everythingUnderBoundaryBDD(index, sourcePortFrom - 1);
					rangeBDD = bovengrensBDD.apply(ondergrensBDD, BDDFactory.xor);
				}
				else
					rangeBDD = everythingUnderBoundaryBDD(index, sourcePortUntil);
				// rangeBDD.printDot();
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
			}
		}
		// voeg Destination IP 1 toe aan BDD
		index = 56;

		for (int i = 7; i >= 0; i--) {
			if (destinationMask > 0) {
				index++;
				if (destinationIP_1 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					destinationIP_1 = destinationIP_1 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				destinationMask--;
			}
		} // end for

		// voeg Destination IP 2 toe aan BDD
		index = 64;

		for (int i = 7; i >= 0; i--) {
			if (destinationMask > 0) {
				index++;
				if (destinationIP_2 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					destinationIP_2 = destinationIP_2 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				destinationMask--;
			}
		} // end for

		// voeg Destination IP 3 toe aan BDD
		index = 72;

		for (int i = 7; i >= 0; i--) {
			if (destinationMask > 0) {
				index++;
				if (destinationIP_3 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					destinationIP_3 = destinationIP_3 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				destinationMask--;
			}
		} // end for

		// voeg Destination IP 4 toe aan BDD
		index = 80;

		for (int i = 7; i >= 0; i--) {
			if (destinationMask > 0) {
				index++;
				if (destinationIP_4 >= powerOfTwo[i]) {
					tempBDD = tempBDD.and(fact.ithVar(index));
					destinationIP_4 = destinationIP_4 - powerOfTwo[i];
				} else {
					tempBDD = tempBDD.and(fact.nithVar(index));
				}
				destinationMask--;
			}
		} // end for

		// voeg Destination Port toe aan BDD
		index = 88;
		if (destinationPortFrom == -1) {
			// doe niets; don't care ; asterisk
		} else {
			// test of ondergrens = bovengrens
			if (destinationPortFrom == destinationPortUntil) {
				for (int i = 15; i >= 0; i--) {
					index++;
					if (destinationPortFrom >= powerOfTwo[i]) {
						tempBDD = tempBDD.and(fact.ithVar(index));
						destinationPortFrom = destinationPortFrom - powerOfTwo[i];
					} else {
						tempBDD = tempBDD.and(fact.nithVar(index));
					}
				} // end for
			} else // het betreft een range van poorten
			{
				if (destinationPortFrom > 0)
				{
					bovengrensBDD = everythingUnderBoundaryBDD(index, destinationPortUntil);
					ondergrensBDD = everythingUnderBoundaryBDD(index, destinationPortFrom - 1);
					rangeBDD = bovengrensBDD.apply(ondergrensBDD, BDDFactory.xor);
				}
				else
					rangeBDD = everythingUnderBoundaryBDD(index, destinationPortUntil);
				// rangeBDD.printDot();
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
			}
		}
		return tempBDD;
	}

	/**
	 * Geeft aan een subrange van variabelen in een BDD, die het poortnummer
	 * representeert, de ruimte vanaf poort 0 tot en met een bovengrens aan
	 * poortnummers.
	 * 
	 * @param index
	 *            de variabele in de BDD, waar het poortnummer is
	 * @param grensPoort
	 *            de bovengrens van de poortnummers
	 * @return een BDD waarvan alleen de variabelen die betrekking hebben op het
	 *         poortnummer tot en met de bovengrens vertegenwoordigd zijn
	 *         Voorwaarde: grensPoort >= 0
	 */
	private BDD everythingUnderBoundaryBDD(int index, int grensPoort) {
		boolean[] grensPoortBinair = new boolean[16];
		BDD grensBDD = False; // staat goed op False

		// bepaal binaire representatie van grens
		for (int i = 15; i >= 0; i--) {
			if (grensPoort >= powerOfTwo[i]) {
				grensPoortBinair[15 - i] = true;
				grensPoort = grensPoort - powerOfTwo[i];
			} else {
				grensPoortBinair[15 - i] = false;
			}
		} // end for

		BDD tussenvormBDD = True;

		for (int bitIndex = 0; bitIndex < 16; bitIndex++) {
			index++;
			if (grensPoortBinair[bitIndex]) // bitwaarde is een 1
			{
				if (bitIndex == 15) {
					grensBDD = grensBDD.apply(tussenvormBDD, BDDFactory.or);
				} else {
					// bitwaarde is een 1 ; // eerst met een 0 toevoegen
					grensBDD = grensBDD.apply(tussenvormBDD.and(fact.nithVar(index)), BDDFactory.or);

					// dan de 1 nemen
					tussenvormBDD = tussenvormBDD.and(fact.ithVar(index));
				}
			} else // bitwaarde is een 0
			{
				// een 0 toevoegen
				tussenvormBDD = tussenvormBDD.and(fact.nithVar(index));
				// bovengrensBDD.printDot();
				if (bitIndex == 15) {// that BDD is consumed and can no longer
					// be used
					grensBDD = grensBDD.apply(tussenvormBDD, BDDFactory.or);
				}
			}
		} // end for

		return grensBDD;
	} // end allesOnderGrensBDD

	/**
	 * Bepaalt aan de hand van een combinatie van invoervelden met welke regel
	 * uit de IPTables ruleset dit fictieve pakketje het eerst een match heeft.
	 * 
	 * @param _protocol
	 *            het protocol
	 * @param _sourceIP_1
	 *            eerste deel van het bron IP-adres
	 * @param _sourceIP_2
	 *            tweede deel van het bron IP-adres
	 * @param _sourceIP_3
	 *            derde deel van het bron IP-adres
	 * @param _sourceIP_4
	 *            vierde deel van het bron IP-adres
	 * @param _sourcePort
	 *            de bronpoort
	 * @param _destinationIP_1
	 *            eerste deel van het bestemmings IP-adres
	 * @param _destinationIP_2
	 *            tweede deel van het bestemmings IP-adres
	 * @param _destinationIP_3
	 *            derde deel van het bestemmings IP-adres
	 * @param _destinationIP_4
	 *            vierde deel van het bestemmings IP-adres
	 * @param _destionationPort
	 *            de bestemmingspoort
	 * @return het nummer van de regel die het eerst matcht
	 * 
	 * @throws CustomException
	 * 
	 */
	public int packetFirstMatch(int _protocol, int _sourceIP_1, int _sourceIP_2, int _sourceIP_3, int _sourceIP_4, int _sourcePort, int _destinationIP_1, int _destinationIP_2,
			int _destinationIP_3, int _destinationIP_4, int _destionationPort) throws CustomException // toegevoegd
	// 13
	// juni
	// 2013
	{
		// als een veld niet meedoet voor de test van first match moet het
		// waarde Ruimte.NIETINGESTELD hebben.
		// als bijv. sourceIP niet in de test meedoet hoeft maar 1 van die
		// subvelden Ruimte.NIETINGESTELD te hebben.
		// ALS geen match is gevonden DAN returnwaarde == -1
		// ALS de input ongeldig is DAN returnwaarde == -2
		// ArrayList<Rule> mijnArrayList = new ArrayList<Rule>(rules);
		// ArrayList<Rule> mijnArrayList = rules;
		int protocol;
		int sourceIP_1;
		int sourceIP_2;
		int sourceIP_3;
		int sourceIP_4;
		int sourceMask; // hier altijd 32
		int sourcePort_from; // zijn hier altijd gelijk
		int sourcePort_until; //
		// private int sourcePort;
		int destinationIP_1;
		int destinationIP_2;
		int destinationIP_3;
		int destinationIP_4;
		int destinationMask;
		int destinationPort_from; // zijn hier altijd gelijk
		int destinationPort_until; //
		// private int destionationPort;
		boolean veldProtocol; // aangeven of deze vergeleken moeten worden
		boolean veldSourceIP; //
		boolean veldSourcePort; //
		boolean veldDestinationIP; //
		boolean veldDestinationPort; //

		// hier een test op correcte input
		boolean inputCorrect = true;
	//	if (_protocol == null || _protocol.getCode() < -2 || _protocol.getCode() == -1 || _protocol.getCode() > 255) // protocol
																														// 0
		if((_protocol >= -2 && _protocol <= 255) == false )
			
			// protocol
			// zal ook wel
			// niet
			// bestaan,
			// maar lijkt
			// geen
			// probleem
			inputCorrect = false;
		if (!(_sourceIP_1 == Space.NOT_SPECIFIED || _sourceIP_2 == Space.NOT_SPECIFIED || _sourceIP_3 == Space.NOT_SPECIFIED || _sourceIP_4 == Space.NOT_SPECIFIED)) {
			if (_sourceIP_1 < -2 || _sourceIP_1 == -1 || _sourceIP_1 > 255)
				inputCorrect = false;
			if (_sourceIP_2 < -2 || _sourceIP_2 == -1 || _sourceIP_2 > 255)
				inputCorrect = false;
			if (_sourceIP_3 < -2 || _sourceIP_3 == -1 || _sourceIP_3 > 255)
				inputCorrect = false;
			if (_sourceIP_4 < -2 || _sourceIP_4 == -1 || _sourceIP_4 > 255)
				inputCorrect = false;
		}
		if (_sourcePort < -2 || _sourcePort == -1 || _sourcePort > 65535)
			inputCorrect = false;

		if (!(_destinationIP_1 == Space.NOT_SPECIFIED || _destinationIP_2 == Space.NOT_SPECIFIED || _destinationIP_3 == Space.NOT_SPECIFIED || _destinationIP_4 == Space.NOT_SPECIFIED)) {
			if (_destinationIP_1 < -2 || _destinationIP_1 == -1 || _destinationIP_1 > 255)
				inputCorrect = false;
			if (_destinationIP_2 < -2 || _destinationIP_2 == -1 || _destinationIP_2 > 255)
				inputCorrect = false;
			if (_destinationIP_3 < -2 || _destinationIP_3 == -1 || _destinationIP_3 > 255)
				inputCorrect = false;
			if (_destinationIP_4 < -2 || _destinationIP_4 == -1 || _destinationIP_4 > 255)
				inputCorrect = false;
		}
		if (_destionationPort < -2 || _destionationPort == -1 || _destionationPort > 65535)
			inputCorrect = false;

		if (!inputCorrect)
			return -2;

		protocol = _protocol;
	//	veldProtocol = protocol.getCode() == Space.NOT_SPECIFIED ? false : true;
		veldProtocol = protocol == -2 ? false : true;

		sourceIP_1 = _sourceIP_1;
		sourceIP_2 = _sourceIP_2;
		sourceIP_3 = _sourceIP_3;
		sourceIP_4 = _sourceIP_4;
		sourceMask = 32;
		veldSourceIP = (sourceIP_1 == Space.NOT_SPECIFIED || sourceIP_2 == Space.NOT_SPECIFIED || sourceIP_3 == Space.NOT_SPECIFIED || sourceIP_4 == Space.NOT_SPECIFIED) ? false : true;

		sourcePort_from = _sourcePort; // is dus gewoon de source poort
		sourcePort_until = sourcePort_from; // geen range
		veldSourcePort = sourcePort_from == Space.NOT_SPECIFIED ? false : true;

		destinationIP_1 = _destinationIP_1;
		destinationIP_2 = _destinationIP_2;
		destinationIP_3 = _destinationIP_3;
		destinationIP_4 = _destinationIP_4;
		destinationMask = 32; // hier n.v.t.
		veldDestinationIP = (destinationIP_1 == Space.NOT_SPECIFIED || destinationIP_2 == Space.NOT_SPECIFIED || destinationIP_3 == Space.NOT_SPECIFIED || destinationIP_4 == Space.NOT_SPECIFIED) ? false
				: true;

		destinationPort_from = _destionationPort;
		destinationPort_until = destinationPort_from;
		veldDestinationPort = destinationPort_from == Space.NOT_SPECIFIED ? false : true;

		// vraag aantal regels op
		// int aantalRegels = mijnArrayList.size();
		int aantalRegels = rules.size();
		int regelNummer = 0;
		BDD pakketjeBDD;
		BDD regelBDD;
		Rule huidigeRule;
		Boolean matchGevonden = false;

		while ((!matchGevonden) && (regelNummer < aantalRegels)) {
			// huidigeRule = mijnArrayList.get(regelNummer);
			huidigeRule = rules.get(regelNummer);

			if (!veldProtocol) // protocol niet ingevuld
			{
				protocol = huidigeRule.getProtocol();
			}
			if (!veldSourceIP) // source IP
			{
				sourceIP_1 = huidigeRule.getSourceIP().getNumber1();
				sourceIP_2 = huidigeRule.getSourceIP().getNumber2();
				sourceIP_3 = huidigeRule.getSourceIP().getNumber3();
				sourceIP_4 = huidigeRule.getSourceIP().getNumber4();
				sourceMask = huidigeRule.getSourceIP().getMask();
			}
			if (!veldSourcePort) {
				sourcePort_from = huidigeRule.getSourcePort().getLowerBound();
				sourcePort_until = huidigeRule.getSourcePort().getUpperBound();
			}
			if (!veldDestinationIP) {
				destinationIP_1 = huidigeRule.getDestinationIP().getNumber1();
				destinationIP_2 = huidigeRule.getDestinationIP().getNumber2();
				destinationIP_3 = huidigeRule.getDestinationIP().getNumber3();
				destinationIP_4 = huidigeRule.getDestinationIP().getNumber4();
				destinationMask = huidigeRule.getDestinationIP().getMask();
			}
			if (!veldDestinationPort) {
				destinationPort_from = huidigeRule.getDestinationPort().getLowerBound();
				destinationPort_until = huidigeRule.getDestinationPort().getUpperBound();
			}

			// maak een BDD van deze mix
			pakketjeBDD = ruleOrPacketToBDD(protocol, sourceIP_1, sourceIP_2, sourceIP_3, sourceIP_4, sourceMask, sourcePort_from, sourcePort_until, destinationIP_1, destinationIP_2, destinationIP_3,
					destinationIP_4, destinationMask, destinationPort_from, destinationPort_until);

			regelBDD = ruleToBDD(huidigeRule);

			// test of pakketjeBDD een subset is van regelBDD
			// neem de doorsnede
			BDD tempAND = regelBDD.apply(pakketjeBDD, BDDFactory.and);
			// en test of dit gelijk is aan pakketjeBDD, dan is het een subset
			if (tempAND.equals(pakketjeBDD)) {
				// een match
				matchGevonden = true;
			}

			regelNummer++;
		} // end while
		if (matchGevonden)
			return regelNummer;
		else
			return -1;
	} // end packetFirstMatch

	/**
	 * Deelt de IPTables Ruleset op in correlatie groepen van segmenten, die
	 * afzonderlijk geanalyseerd kunnen worden.
	 * 
	 * @return Geeft een SetOfGroups object, met de afzonderlijke groepen
	 */
	public SetOfGroups createCorrelationGroups() // throws CustomException
	{
		TreeSet<Integer> rulesGroup;
		CorrelationGroup group;
		Boolean segmentAanGroupToegevoegd;
		ArrayList<Segment> segmenten = getSegments();
		SetOfGroups setOfGroups = new SetOfGroups();
		int aantalSegmenten = segmenten.size();

		Segment segment;
		CorrelationGroup naartoeOverhevelenGroup = new CorrelationGroup(0); // compiler
																			// wenst
																			// initialisatie

		for (int i = 0; i < aantalSegmenten; i++) {
			segmentAanGroupToegevoegd = false;
			// haal segment op
			segment = segmenten.get(i);

			int aantalGroups = setOfGroups.getNumberOfGroups();
			int k = 0;
			while (k < aantalGroups) {
				// haal een group op
				group = setOfGroups.getGroup(k);
				rulesGroup = group.getRuleNumbers();

				// bepaal of doorsnede niet leeg is
				int aantalRules = segment.getNumberOfRules();
				boolean nietLegeDoorsnede = false; // niets gemeen
				for (int j = 0; j < aantalRules; j++) {
					if (rulesGroup.contains(segment.getRule(j).getRuleNr())) {
						nietLegeDoorsnede = true;
					}
				}
				if (nietLegeDoorsnede) {
					if (segmentAanGroupToegevoegd == false) {
						// segment nog niet aan een group toegevoegd
						// leg vast aan welke group is toegevoegd
						naartoeOverhevelenGroup = group;

						// voeg toe aan bestaande group
						group.addSegment(segment);
						segmentAanGroupToegevoegd = true;
						k++;
					} else {
						// segment al aan een group toegevoegd
						int groupSize = group.getSize();
						Segment tempSegment;

						// tweedeGroup overhevelen naar eerstGroup
						for (int l = 0; l < groupSize; l++) {
							tempSegment = group.getSegment(l);
							naartoeOverhevelenGroup.addSegment(tempSegment);
						}

						// oude group verwijderen
						setOfGroups.removeGroup(k);

						// nummering van volgende groepen aanpassen
						aantalGroups = setOfGroups.getNumberOfGroups();
						for (int j = k; j < aantalGroups; j++) {
							setOfGroups.getGroup(j).decGroupNumber();
						}
					}
				} // end if (nietLegeDoorsnede)
				else {
					k++;
				}
			} // end while
			if (!segmentAanGroupToegevoegd) {
				// voeg toe aan een nieuwe lege group
				CorrelationGroup newGroup = new CorrelationGroup(setOfGroups.getMaxGroupNr() + 1);
				newGroup.addSegment(segment);
				setOfGroups.addGroup(newGroup);
			}
		} // end for

		// voor debuggen
		// System.out.println("Alle Groupen gemaakt : " );
		// setOfGroups.printGroups();

		return setOfGroups;
	} // end createCorrelationGroups

	/**
	 * Bepaalt, met behulp van een algoritme dat kenmerken toekent aan
	 * deelruimtes, regels die als redundant kunnen wprden aangemerkt. <br />
	 * Van deelruimtes met eigenschap 'R' (Redundant) worden regels bepaalt die
	 * verwijderd kunnen worden, dit is niet optimaal maar een benadering die
	 * efficient is. <br />
	 * Van deelruimtes met eigenschap 'C' (Correlated) worden door verschillende
	 * combinaties uit te proberen ook regels als redundant aangemerkt. Dit
	 * deelalgoritme heeft tijdcomplexiteit O(n!) en werkt voor een groep t/m 7
	 * regels met kenmerk "C' optimaal. Een groep is hier per action, dus een
	 * groep voor 'denied' en een groep voor 'allowed'. Als zo'n groep meer dan
	 * 7 regels bevat is de kans nog steeds aanwezig dat een redelijke tot
	 * optimale oplossing wordt gevonden.
	 * 
	 * @param group
	 *            de correlation group die geanalyseerd wordt
	 * @return object RedundancySolution dat de gegevens bevat van de analyse
	 * @throws CustomException
	 */
	public RedundancySolution determineRedundantRules(CorrelationGroup group) throws CustomException {
		TreeSet<Integer> rulesSet = new TreeSet<Integer>();
		// process 1: non-overlapping segment
		int aantalSegments = group.getSize();
		for (int i = 0; i < aantalSegments; i++) {
			group.getSegment(i).initPropertyAssignment();
		}

		// ken Strong Irremoveble toe aan subspace van Non-Overlapping segmenten
		Segment segment;
		for (int i = 0; i < aantalSegments; i++) {
			segment = group.getSegment(i);
			if (segment.isOverlapping() == false) {
				// Strong Irremoveble toekennen
				segment.getRuleSubspace(0).setProperty(RuleSubspace.Property.SI);
				// verzamel de nummers van deze Rules
				rulesSet.add(segment.getRuleSubspace(0).getNumber());
			}
		}
		// de overige stukken subspace krijgen Weak Irremoveble
		for (int i = 0; i < aantalSegments; i++) {
			segment = group.getSegment(i);
			int aantalSubspace = segment.getSizeRuleSubspace();
			for (int j = 0; j < aantalSubspace; j++) {
				if (rulesSet.contains(segment.getRuleSubspace(j).getNumber())) {
					if (segment.getRuleSubspace(j).getProperty() != RuleSubspace.Property.SI) {
						segment.getRuleSubspace(j).setProperty(RuleSubspace.Property.WI);
					}
				}
			}
		}
		// process 2: conflicting overlapping segment
		rulesSet.clear();
		for (int k = 0; k < aantalSegments; k++) {
			segment = group.getSegment(k);
			if (segment.isOverlapping() && segment.getConflicting()) {
				// aan de eerste subspace Strong Irremoveble toekennen
				segment.getRuleSubspace(0).setProperty(RuleSubspace.Property.SI);
				// regelnummer onthouden
				rulesSet.add(segment.getRuleSubspace(0).getNumber());

				// aan de overige Removeble toekennen
				int aantalSubspace = segment.getSizeRuleSubspace();
				for (int l = 1; l < aantalSubspace; l++) {
					segment.getRuleSubspace(l).setProperty(RuleSubspace.Property.R);
				}
			}
		}
		// de overige subspaces van de Rule van die eerste subspace
		// krijgen Weak Irremoveble tenzij zij al Strong Irremoveble hebben
		for (int m = 0; m < aantalSegments; m++) {
			segment = group.getSegment(m);
			int aantalSubspace = segment.getSizeRuleSubspace();
			for (int j = 0; j < aantalSubspace; j++) {
				if (rulesSet.contains(segment.getRuleSubspace(j).getNumber())) {
					if (segment.getRuleSubspace(j).getProperty() != RuleSubspace.Property.SI) {
						segment.getRuleSubspace(j).setProperty(RuleSubspace.Property.WI);
					}
				}
			}
		} // end for

		// process 3: non-conflicting overlapping segment
		rulesSet.clear();
		for (int k = 0; k < aantalSegments; k++) {
			segment = group.getSegment(k);
			if (segment.isOverlapping() && (segment.getConflicting() == false)) {
				// kijk of er een subspace is met Weak Irremoveble property
				boolean gevonden_WI = false;
				int aantalSubspace = segment.getSizeRuleSubspace();
				int l = 0;
				while ((l < aantalSubspace) && (gevonden_WI == false)) {
					if (segment.getRuleSubspace(l).getProperty() == RuleSubspace.Property.WI)
						gevonden_WI = true;
					l++;
				}
				if (gevonden_WI == true) {
					for (int m = 0; m < aantalSubspace; m++) {
						if ((segment.getRuleSubspace(m).getProperty() != RuleSubspace.Property.WI) && (segment.getRuleSubspace(m).getProperty() != RuleSubspace.Property.SI))
							segment.getRuleSubspace(m).setProperty(RuleSubspace.Property.R);
					}
				} else {
					for (int n = 0; n < aantalSubspace; n++) {
						segment.getRuleSubspace(n).setProperty(RuleSubspace.Property.C);
					}
				}

			}
		} // end for
			// bepaal welke Rules redundant zijn
			// neem een set van alle betrokken Rule nummers
		TreeSet<Integer> tempSet = new TreeSet<Integer>();
		tempSet = group.getRuleNumbers();

		for (int i = 0; i < aantalSegments; i++) {
			segment = group.getSegment(i);

			int aantalSubSpace = segment.getSizeRuleSubspace();
			for (int j = 0; j < aantalSubSpace; j++) {
				if (segment.getRuleSubspace(j).getProperty() != RuleSubspace.Property.R)
					tempSet.remove(segment.getRuleSubspace(j).getNumber());
			}
		}
		// voor debug en testdoeleinden
		// group.printPropertyAsssignment();

		// analyseer Coorelated
		BDD BDD_voor_D = False;
		BDD BDD_voor_A = False;
		BDD BDD_voor;
		BDD spaceRule;

		TreeSet<Integer> corrIndicesSet_A = new TreeSet<Integer>();
		TreeSet<Integer> corrIndicesSet_D = new TreeSet<Integer>();
		ArrayList<Integer> corrIndicesArray_A = new ArrayList<Integer>();
		ArrayList<Integer> corrIndicesArray_D = new ArrayList<Integer>();
		ArrayList<Integer> correlatedIndices;
		TreeSet<Integer> verwijderSet = new TreeSet<Integer>();

		// neem indexen van betrokken firewall regels -- dus vanaf index 0
		for (int i = 0; i < aantalSegments; i++) {
			segment = group.getSegment(i);
			if (segment.isOverlapping() && !segment.getConflicting()) {
				if (segment.getRuleSubspace(0).getProperty() == RuleSubspace.Property.C) {
					if (segment.getDeniedSpace()) {
						for (Rule rule : segment.getRules()) {
							corrIndicesSet_D.add(rule.getRuleNr() - 1);
							spaceRule = ruleToBDD(rule);
							BDD_voor_D = spaceRule.apply(BDD_voor_D, BDDFactory.or);
						}
					} else // segment betreft allowed Space
					{
						for (Rule rule : segment.getRules()) {
							corrIndicesSet_A.add(rule.getRuleNr() - 1);
							spaceRule = ruleToBDD(rule);
							BDD_voor_A = spaceRule.apply(BDD_voor_A, BDDFactory.or);
						}
					}
				}

			}
		} // end for
		corrIndicesArray_D.addAll(corrIndicesSet_D);
		Collections.sort(corrIndicesArray_D);
		corrIndicesArray_A.addAll(corrIndicesSet_A);
		Collections.sort(corrIndicesArray_A);

		// maak voor 7 firewall regels met eenzelfde action, kan ook werken voor
		// meer regels
		int[][] teVergelijken = { { 0 }, { 1 }, { 2 }, { 3 }, { 4 }, { 5 }, { 6 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 }, { 0, 6 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 1, 6 },
				{ 2, 3 }, { 2, 4 }, { 2, 5 }, { 2, 6 }, { 3, 4 }, { 3, 5 }, { 3, 6 }, { 4, 5 }, { 4, 6 }, { 5, 6 }, { 0, 1, 2 }, { 0, 1, 3 }, { 0, 1, 4 }, { 0, 1, 5 }, { 0, 1, 6 }, { 0, 2, 3 },
				{ 0, 2, 4 }, { 0, 2, 5 }, { 0, 2, 6 }, { 0, 3, 4 }, { 0, 3, 5 }, { 0, 3, 6 }, { 0, 4, 5 }, { 0, 4, 6 }, { 0, 5, 6 }, { 1, 2, 3 }, { 1, 2, 4 }, { 1, 2, 5 }, { 1, 2, 6 }, { 1, 3, 4 },
				{ 1, 3, 5 }, { 1, 3, 6 }, { 1, 4, 5 }, { 1, 4, 6 }, { 1, 5, 6 }, { 2, 3, 4 }, { 2, 3, 5 }, { 2, 3, 6 }, { 2, 4, 5 }, { 2, 4, 6 }, { 2, 5, 6 }, { 3, 4, 5 }, { 3, 4, 6 }, { 3, 5, 6 },
				{ 4, 5, 6 }, { 0, 1, 2, 3 }, { 0, 1, 2, 4 }, { 0, 1, 2, 5 }, { 0, 1, 2, 6 }, { 0, 1, 3, 4 }, { 0, 1, 3, 5 }, { 0, 1, 3, 6 }, { 0, 1, 4, 5 }, { 0, 1, 4, 6 }, { 0, 1, 5, 6 },
				{ 0, 2, 3, 4 }, { 0, 2, 3, 5 }, { 0, 2, 3, 6 }, { 0, 2, 4, 5 }, { 0, 2, 4, 6 }, { 0, 2, 5, 6 }, { 0, 3, 4, 5 }, { 0, 3, 4, 6 }, { 0, 3, 5, 6 }, { 0, 4, 5, 6 }, { 1, 2, 3, 4 },
				{ 1, 2, 3, 5 }, { 1, 2, 3, 6 }, { 1, 2, 4, 5 }, { 1, 2, 4, 6 }, { 1, 2, 5, 6 }, { 1, 3, 4, 5 }, { 1, 3, 4, 6 }, { 1, 3, 5, 6 }, { 1, 4, 5, 6 }, { 2, 3, 4, 5 }, { 2, 3, 4, 6 },
				{ 2, 3, 5, 6 }, { 2, 4, 5, 6 }, { 3, 4, 5, 6 }, { 0, 1, 2, 3, 4 }, { 0, 1, 2, 3, 5 }, { 0, 1, 2, 3, 6 }, { 0, 1, 2, 4, 5 }, { 0, 1, 2, 4, 6 }, { 0, 1, 2, 5, 6 }, { 0, 1, 3, 4, 5 },
				{ 0, 1, 3, 4, 6 }, { 0, 1, 3, 5, 6 }, { 0, 1, 4, 5, 6 }, { 0, 2, 3, 4, 5 }, { 0, 2, 3, 4, 6 }, { 0, 2, 3, 5, 6 }, { 0, 2, 4, 5, 6 }, { 0, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5 },
				{ 1, 2, 3, 4, 6 }, { 1, 2, 3, 5, 6 }, { 1, 2, 4, 5, 6 }, { 1, 3, 4, 5, 6 }, { 2, 3, 4, 5, 6 }, { 0, 1, 2, 3, 4, 5 }, { 0, 1, 2, 3, 4, 6 }, { 0, 1, 2, 3, 5, 6 }, { 0, 1, 2, 4, 5, 6 },
				{ 0, 1, 3, 4, 5, 6 }, { 0, 2, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5, 6 } }; // code
																					// ok
																					// ;
																					// 7
																					// +
		// 21 + 35 + 35 +
		// 21 + 7 = 126

		int aantalRules; // aantal regels met D resp. met A
		boolean doorgaan;
		int aantalTeVergelijken = teVergelijken.length; // aantal setjes van
		// regels
		BDD nieuweBDD;
		int[] tempArray;
		int aantalRulesTeVerg; // aantal per setje

		for (int j = 0; j < 2; j++) // 0 = denied, 1 = allowed
		{
			if (j == 0) {
				correlatedIndices = corrIndicesArray_D;
				BDD_voor = BDD_voor_D;
			} else {
				correlatedIndices = corrIndicesArray_A;
				BDD_voor = BDD_voor_A;
			}
			aantalRules = correlatedIndices.size();
			if (aantalRules > 1)
				doorgaan = true;
			else
				doorgaan = false;

			int arrayTeller = 0; // telt aantal verzamelingen met regels

			while (doorgaan) {
				nieuweBDD = False;
				// volgende array
				tempArray = teVergelijken[arrayTeller];
				aantalRulesTeVerg = tempArray.length;
				if (tempArray[aantalRulesTeVerg - 1] < aantalRules) {
					for (int i = 0; i < aantalRulesTeVerg; i++) {
						nieuweBDD = nieuweBDD.apply(ruleToBDD(rules.get(correlatedIndices.get(tempArray[i]))), BDDFactory.or);
					}
					// System.out.println();
					if (BDD_voor.equals(nieuweBDD)) {
						verwijderSet.clear();
						for (int i = 0; i < aantalRules; i++)
							verwijderSet.add(i);
						// stop de regelindices in verwijderSet

						for (int i = 0; i < aantalRulesTeVerg; i++)
							verwijderSet.remove(tempArray[i]);
						for (int i = 0; i < aantalRules; i++) {
							if (verwijderSet.contains(i)) {
								tempSet.add(correlatedIndices.get(i) + 1); // voeg
								// firewall
								// regelnr
								// toe
							}
						}
						doorgaan = false;
					}
				} // end if
				arrayTeller++;

				if (arrayTeller >= aantalTeVergelijken) {
					doorgaan = false;
				}
			}
		} // end for
		ArrayList<Integer> tempArrayList = new ArrayList<Integer>();
		tempArrayList.addAll(tempSet);
		Collections.sort(tempArrayList);
		RedundancySolution oplossingRedundant = new RedundancySolution();
		int aantal = tempArrayList.size();
		for (int i = 0; i < aantal; i++) {
			oplossingRedundant.addRedundantRuleNrs(tempArrayList.get(i));
		}
		if (aantal > 0)
			oplossingRedundant.setStatus(RedundancySolution.Status.FOUND_REDUNDANT_RULES);
		else
			oplossingRedundant.setStatus(RedundancySolution.Status.THERE_ARE_NO_REDUNDANT_RULES);
		return oplossingRedundant;
	} // end bepaalRedundanteRegels

	/**
	 * Maakt een opsomming van de 5 dimensionale ruimte van een BDD.
	 * 
	 * @param bdd
	 *            de BDD waarvan een opsomming wordt gemaakt
	 * @return een object van type EnumerationBDD met een opsomming van de BDD
	 */
	public EnumerationBDD makeEnumerationBDD(BDD bdd, boolean compact) {
		EnumerationBDD tempOpsommingBDD = new EnumerationBDD();
		final int OVERFLOW_GETAL = 2100000000;
		// String infoString_A = new String("A");
		// String infoString_L = new String("L");
		boolean isOverflow;
		ArrayList<String> inStrings = new ArrayList<String>();
		String stringBDD = bdd.toString();
		int indexHaakOpen;
		int indexHaakSluit;
		int aantalDontCare;
		int teConverterenAantal; // 2 tot de macht aantalDontCare
		boolean dontCareOpGeschiktePos;
		TreeSet<Integer> treeSet = new TreeSet<Integer>();
		String binaryString;

		RuleBDD regelBDD;

		indexHaakOpen = stringBDD.indexOf("<");
		while (indexHaakOpen != -1) {
			indexHaakSluit = stringBDD.indexOf(">");
			inStrings.add(stringBDD.substring(0, indexHaakSluit + 1));
			stringBDD = stringBDD.substring(indexHaakSluit + 1);
			indexHaakOpen = stringBDD.indexOf("<");
		}

		String conjunctionString; // krijgt steeds de volgende string uit
		// inStrings
		String varColonString; // bevat bijvoorbeeld -- 104:
		ArrayList<Integer> savePosities = new ArrayList<Integer>(); // posities
		// don't
		// casre
		String colon = ":";

		boolean[] register = new boolean[105];
		int veldGetal;
		int positieVar;
		int positieBit;
		int aantalStrings = inStrings.size();

		for (int i = 0; i < aantalStrings; i++) {
			savePosities.clear();
			conjunctionString = inStrings.get(i);

			for (int j = 1; j < 105; j++) {
				register[j] = false;
			}

			for (int index = 104; index > 0; index--) {
				varColonString = index + colon;
				positieVar = conjunctionString.lastIndexOf(varColonString);

				if (positieVar != -1) {
					// zoek 0 of 1
					positieBit = (conjunctionString.indexOf(colon, positieVar) + 1);
					// neem 0 of 1
					if (conjunctionString.charAt(positieBit) == '1') {
						register[index] = true;
					}
					// string inkorten
					conjunctionString = conjunctionString.substring(0, positieVar);
				} else {
					// is don't care
					savePosities.add(index);
				}
			}
			// bepaal aantal don't cares
			teConverterenAantal = 1; // als er geen dont cares zijn gebruik ik
			// waarde 1
			aantalDontCare = savePosities.size();
			if (aantalDontCare > 30) {
				isOverflow = true;
			} else
				isOverflow = false;

			if (isOverflow == false) {
				for (int j = 0; j < aantalDontCare; j++) {
					teConverterenAantal = teConverterenAantal * 2;
				}
			} else
				teConverterenAantal = OVERFLOW_GETAL;

			dontCareOpGeschiktePos = false;
			if (compact && aantalDontCare > 1) {
				// stop de posities in een set
				treeSet.clear();
				treeSet.addAll(savePosities);

				int index = 104;
				boolean doorgaan = true;
				while (doorgaan && index > 0) {
					if (treeSet.contains(index)) {
						treeSet.remove(index);
						index--;
					} else
						doorgaan = false;
				}

				index = 88;
				doorgaan = true;
				while (doorgaan && index > 0) {
					if (treeSet.contains(index)) {
						treeSet.remove(index);
						index--;
					} else
						doorgaan = false;
				}
				index = 56;
				doorgaan = true;
				while (doorgaan && index > 0) {
					if (treeSet.contains(index)) {
						treeSet.remove(index);
						index--;
					} else
						doorgaan = false;
				}
				index = 40;
				doorgaan = true;
				while (doorgaan && index > 0) {
					if (treeSet.contains(index)) {
						treeSet.remove(index);
						index--;
					} else
						doorgaan = false;
				}
				index = 8;
				doorgaan = true;
				while (doorgaan && index > 0) {
					if (treeSet.contains(index)) {
						treeSet.remove(index);
						index--;
					} else
						doorgaan = false;
				}
				if (treeSet.isEmpty())
					dontCareOpGeschiktePos = true;
			}

			// System.out.println("aantalDontCare : " + aantalDontCare);
			// System.out.println("dontCareOpGeschiktePos : " +
			// dontCareOpGeschiktePos);
			// System.out.println("teConverterenAantal : " +
			// teConverterenAantal);

			int getal = 0;
			// als er sprake is van overflow dan ALTIJD compacte weergave !
			while (getal < teConverterenAantal) {

				if (getal == (OVERFLOW_GETAL - 1)) {
					for (int index = 0; index < aantalDontCare; index++) // index
					{
						register[savePosities.get(index)] = true;
					}

				} else {
					if (getal > 0) {
						binaryString = Integer.toBinaryString(getal);
						int lengteBinString = binaryString.length();
						for (int index = 0; index < aantalDontCare; index++) // index
						// neemt
						// binaryString
						// beginnend
						// aan
						// de
						// rechterkant
						{
							if ((lengteBinString > index) && (binaryString.charAt(lengteBinString - index - 1) == '1')) {
								register[savePosities.get(index)] = true;
							} else {
								register[savePosities.get(index)] = false;
							}
						}
					}
				}
				// lees register en zet om naar integers in een object
				// 'regelBDD'
				regelBDD = new RuleBDD();

				// protocol -- posities 1..8
				veldGetal = 0;
				for (int k = 1; k <= 8; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[8 - k];
					}
				}
				// voeg toe
				regelBDD.setProtocol(veldGetal);

				// source IP 1 -- posities 9..16
				veldGetal = 0;
				for (int k = 9; k <= 16; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[16 - k];
					}
				}
				// voeg toe
				regelBDD.setSourceIP_1(veldGetal);

				// source IP 2 -- posities 17..24
				veldGetal = 0;
				for (int k = 17; k <= 24; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[24 - k];
					}
				}
				// voeg toe
				regelBDD.setSourceIP_2(veldGetal);

				// source IP 3 -- posities 25..32
				veldGetal = 0;
				for (int k = 25; k <= 32; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[32 - k];
					}
				}
				// voeg toe
				regelBDD.setSourceIP_3(veldGetal);

				// source IP 4 -- posities 33..40
				veldGetal = 0;
				for (int k = 33; k <= 40; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[40 - k];
					}
				}
				// voeg toe
				regelBDD.setSourceIP_4(veldGetal);

				// source port -- posities 41..56
				veldGetal = 0;
				for (int k = 41; k <= 56; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[56 - k];
					}
				}
				// voeg toe
				regelBDD.setSourcePort(veldGetal);

				// destination IP 1 -- posities 57..64
				veldGetal = 0;
				for (int k = 57; k <= 64; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[64 - k];
					}
				}
				// voeg toe
				regelBDD.setDestinationIP_1(veldGetal);

				// destination IP 2 -- posities 65..72
				veldGetal = 0;
				for (int k = 65; k <= 72; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[72 - k];
					}
				}
				// voeg toe
				regelBDD.setDestinationIP_2(veldGetal);

				// destination IP 3 -- posities 73..80
				veldGetal = 0;
				for (int k = 73; k <= 80; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[80 - k];
					}
				}
				// voeg toe
				regelBDD.setDestinationIP_3(veldGetal);

				// destination IP 4 -- posities 81..88
				veldGetal = 0;
				for (int k = 81; k <= 88; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[88 - k];
					}
				}
				// voeg toe
				regelBDD.setDestinationIP_4(veldGetal);

				// destination port -- posities 89..104
				veldGetal = 0;
				for (int k = 89; k <= 104; k++) {
					if (register[k]) {
						veldGetal = veldGetal + powerOfTwo[104 - k];
					}
				}
				// voeg toe
				regelBDD.setDestinationPort(veldGetal);

				if (dontCareOpGeschiktePos || isOverflow) {
					if (getal == 0) {
						regelBDD.setInfoString("A");
						// regelBDD.setInfoString(infoString_A);
					} else {
						// String infoString = "L ";
						String infoString = "L";
						// if (teConverterenAantal == OVERFLOW_GETAL)
						// infoString = infoString + "overflow";
						// else
						// infoString = infoString + teConverterenAantal;
						regelBDD.setInfoString(infoString);
					}
				} else
					regelBDD.setInfoString("");

				tempOpsommingBDD.addRuleBDD(regelBDD);

				// probeer compacte opsomming nog compacter te krijgen
				boolean compacterGeprobeerd = false;
				int aantalRegels = tempOpsommingBDD.getSize();
				// /*
				if ((aantalRegels >= 3) && (compacterGeprobeerd == false)) {
					RuleBDD regel_A = tempOpsommingBDD.getRuleBDD(aantalRegels - 3);
					RuleBDD regel_B = tempOpsommingBDD.getRuleBDD(aantalRegels - 2);
					RuleBDD regel_C = tempOpsommingBDD.getRuleBDD(aantalRegels - 1);

					// A en L en losse regel
					if ((regel_B.getInfoString().equals("L")) && (regel_C.getInfoString().equals("A") == false)) {
						compacterGeprobeerd = true;
						// A en L en losse regel
						// test nu dat er maar een veld verschillend is
						int aantalVerschillend = 0;
						int welkeVerschillend = 0; // 1..5
						// test of protocol eender is
						if (((regel_A.getProtocol() == regel_B.getProtocol()) && (regel_B.getProtocol() == regel_C.getProtocol())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 1;
						}
						if ((is_Zelfde_IPadres(regel_A.getSourceIP_1(), regel_A.getSourceIP_2(), regel_A.getSourceIP_3(), regel_A.getSourceIP_4(), regel_B.getSourceIP_1(), regel_B.getSourceIP_2(),
								regel_B.getSourceIP_3(), regel_B.getSourceIP_4()) && is_Zelfde_IPadres(regel_B.getSourceIP_1(), regel_B.getSourceIP_2(), regel_B.getSourceIP_3(),
								regel_B.getSourceIP_4(), regel_C.getSourceIP_1(), regel_C.getSourceIP_2(), regel_C.getSourceIP_3(), regel_C.getSourceIP_4())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 2;
						}
						if (((regel_A.getSourcePort() == regel_B.getSourcePort()) && (regel_B.getSourcePort() == regel_C.getSourcePort())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 3;
						}
						if ((is_Zelfde_IPadres(regel_A.getDestinationIP_1(), regel_A.getDestinationIP_2(), regel_A.getDestinationIP_3(), regel_A.getDestinationIP_4(), regel_B.getDestinationIP_1(),
								regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4()) && is_Zelfde_IPadres(regel_B.getDestinationIP_1(),
								regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4(), regel_C.getDestinationIP_1(), regel_C.getDestinationIP_2(),
								regel_C.getDestinationIP_3(), regel_C.getDestinationIP_4())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 4;
						}
						if (((regel_A.getDestinationPort() == regel_B.getDestinationPort()) && (regel_B.getDestinationPort() == regel_C.getDestinationPort())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 5;
						}
						if (aantalVerschillend == 1) {
							// test of het opvolgend is in de reeks en voeg
							// samen
							switch (welkeVerschillend) {
							case 1: // protocol
								if (regel_C.getProtocol() == regel_B.getProtocol() + 1) {
									regel_B.setProtocol(regel_C.getProtocol());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
								}
								break;
							case 2: // source IP
								if (is_Opvolgend_IPadres(regel_B.getSourceIP_1(), regel_B.getSourceIP_2(), regel_B.getSourceIP_3(), regel_B.getSourceIP_4(), regel_C.getSourceIP_1(),
										regel_C.getSourceIP_2(), regel_C.getSourceIP_3(), regel_C.getSourceIP_4())) {
									regel_B.setSourceIP_1(regel_C.getSourceIP_1());
									regel_B.setSourceIP_2(regel_C.getSourceIP_2());
									regel_B.setSourceIP_3(regel_C.getSourceIP_3());
									regel_B.setSourceIP_4(regel_C.getSourceIP_4());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
								}
								break;
							case 3: // source port
								if (regel_C.getSourcePort() == regel_B.getSourcePort() + 1) {
									regel_B.setSourcePort(regel_C.getSourcePort());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
								}
								break;
							case 4: // destination IP
								if (is_Opvolgend_IPadres(regel_B.getDestinationIP_1(), regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4(),
										regel_C.getDestinationIP_1(), regel_C.getDestinationIP_2(), regel_C.getDestinationIP_3(), regel_C.getDestinationIP_4())) {
									regel_B.setDestinationIP_1(regel_C.getDestinationIP_1());
									regel_B.setDestinationIP_2(regel_C.getDestinationIP_2());
									regel_B.setDestinationIP_3(regel_C.getDestinationIP_3());
									regel_B.setDestinationIP_4(regel_C.getDestinationIP_4());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
								}
								break;
							case 5: // destination port
								if (regel_C.getDestinationPort() == regel_B.getDestinationPort() + 1) {
									regel_B.setDestinationPort(regel_C.getDestinationPort());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
								}
								break;
							default: {
								// doe niets
							}
							}
						}

					}
				}

				if ((aantalRegels >= 3) && (compacterGeprobeerd == false)) {
					RuleBDD regel_A = tempOpsommingBDD.getRuleBDD(aantalRegels - 3);
					RuleBDD regel_B = tempOpsommingBDD.getRuleBDD(aantalRegels - 2);
					RuleBDD regel_C = tempOpsommingBDD.getRuleBDD(aantalRegels - 1);

					if ((regel_C.getInfoString().equals("L")) && (regel_A.getInfoString().equals("L") == false)) {
						compacterGeprobeerd = true;
						// gewone regel en A L
						// test nu dat er maar een veld verschillend is
						int aantalVerschillend = 0;
						int welkeVerschillend = 0; // 1..5
						// test of protocol eender is
						if (((regel_A.getProtocol() == regel_B.getProtocol()) && (regel_B.getProtocol() == regel_C.getProtocol())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 1;
						}
						if ((is_Zelfde_IPadres(regel_A.getSourceIP_1(), regel_A.getSourceIP_2(), regel_A.getSourceIP_3(), regel_A.getSourceIP_4(), regel_B.getSourceIP_1(), regel_B.getSourceIP_2(),
								regel_B.getSourceIP_3(), regel_B.getSourceIP_4()) && is_Zelfde_IPadres(regel_B.getSourceIP_1(), regel_B.getSourceIP_2(), regel_B.getSourceIP_3(),
								regel_B.getSourceIP_4(), regel_C.getSourceIP_1(), regel_C.getSourceIP_2(), regel_C.getSourceIP_3(), regel_C.getSourceIP_4())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 2;
						}
						if (((regel_A.getSourcePort() == regel_B.getSourcePort()) && (regel_B.getSourcePort() == regel_C.getSourcePort())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 3;
						}
						if ((is_Zelfde_IPadres(regel_A.getDestinationIP_1(), regel_A.getDestinationIP_2(), regel_A.getDestinationIP_3(), regel_A.getDestinationIP_4(), regel_B.getDestinationIP_1(),
								regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4()) && is_Zelfde_IPadres(regel_B.getDestinationIP_1(),
								regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4(), regel_C.getDestinationIP_1(), regel_C.getDestinationIP_2(),
								regel_C.getDestinationIP_3(), regel_C.getDestinationIP_4())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 4;
						}
						if (((regel_A.getDestinationPort() == regel_B.getDestinationPort()) && (regel_B.getDestinationPort() == regel_C.getDestinationPort())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 5;
						}
						if (aantalVerschillend == 1) {
							// test of het opvolgend is in de reeks en voeg
							// samen
							switch (welkeVerschillend) {
							case 1: // protocol
								if (regel_B.getProtocol() == regel_A.getProtocol() + 1) {
									regel_B.setProtocol(regel_A.getProtocol());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 3);
								}
								break;
							case 2: // source IP
								if (is_Opvolgend_IPadres(regel_A.getSourceIP_1(), regel_A.getSourceIP_2(), regel_A.getSourceIP_3(), regel_A.getSourceIP_4(), regel_B.getSourceIP_1(),
										regel_B.getSourceIP_2(), regel_B.getSourceIP_3(), regel_B.getSourceIP_4())) {
									regel_B.setSourceIP_1(regel_A.getSourceIP_1());
									regel_B.setSourceIP_2(regel_A.getSourceIP_2());
									regel_B.setSourceIP_3(regel_A.getSourceIP_3());
									regel_B.setSourceIP_4(regel_A.getSourceIP_4());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 3);
								}
								break;
							case 3: // source port
								if (regel_B.getSourcePort() == regel_A.getSourcePort() + 1) {
									regel_B.setSourcePort(regel_A.getSourcePort());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 3);
								}
								break;
							case 4: // destination IP
								if (is_Opvolgend_IPadres(regel_A.getDestinationIP_1(), regel_A.getDestinationIP_2(), regel_A.getDestinationIP_3(), regel_A.getDestinationIP_4(),
										regel_B.getDestinationIP_1(), regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4())) {
									regel_B.setDestinationIP_1(regel_A.getDestinationIP_1());
									regel_B.setDestinationIP_2(regel_A.getDestinationIP_2());
									regel_B.setDestinationIP_3(regel_A.getDestinationIP_3());
									regel_B.setDestinationIP_4(regel_A.getDestinationIP_4());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 3);
								}
								break;
							case 5: // destination port
								if (regel_B.getDestinationPort() == regel_A.getDestinationPort() + 1) {
									regel_B.setDestinationPort(regel_A.getDestinationPort());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 3);
								}
								break;
							default: {
								// doe niets
							}
							}
						}

					}
				}
				// */

				if ((aantalRegels >= 4) && (compacterGeprobeerd == false)) {
					RuleBDD regel_A = tempOpsommingBDD.getRuleBDD(aantalRegels - 4);
					RuleBDD regel_B = tempOpsommingBDD.getRuleBDD(aantalRegels - 3);
					RuleBDD regel_C = tempOpsommingBDD.getRuleBDD(aantalRegels - 2);
					RuleBDD regel_D = tempOpsommingBDD.getRuleBDD(aantalRegels - 1);

					if ((regel_B.getInfoString().equals("L")) && (regel_D.getInfoString().equals("L"))) {
						compacterGeprobeerd = true;
						// 2 keer achter elkaar A L
						// test nu dat er maar een veld verschillend is
						int aantalVerschillend = 0;
						int welkeVerschillend = 0; // 1..5
						// test of protocol eender is
						if (((regel_A.getProtocol() == regel_C.getProtocol()) && (regel_B.getProtocol() == regel_D.getProtocol())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 1;
						}
						if ((is_Zelfde_IPadres(regel_A.getSourceIP_1(), regel_A.getSourceIP_2(), regel_A.getSourceIP_3(), regel_A.getSourceIP_4(), regel_C.getSourceIP_1(), regel_C.getSourceIP_2(),
								regel_C.getSourceIP_3(), regel_C.getSourceIP_4()) && is_Zelfde_IPadres(regel_B.getSourceIP_1(), regel_B.getSourceIP_2(), regel_B.getSourceIP_3(),
								regel_B.getSourceIP_4(), regel_D.getSourceIP_1(), regel_D.getSourceIP_2(), regel_D.getSourceIP_3(), regel_D.getSourceIP_4())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 2;
						}
						if (((regel_A.getSourcePort() == regel_C.getSourcePort()) && (regel_B.getSourcePort() == regel_D.getSourcePort())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 3;
						}
						if ((is_Zelfde_IPadres(regel_A.getDestinationIP_1(), regel_A.getDestinationIP_2(), regel_A.getDestinationIP_3(), regel_A.getDestinationIP_4(), regel_C.getDestinationIP_1(),
								regel_C.getDestinationIP_2(), regel_C.getDestinationIP_3(), regel_C.getDestinationIP_4()) && is_Zelfde_IPadres(regel_B.getDestinationIP_1(),
								regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4(), regel_D.getDestinationIP_1(), regel_D.getDestinationIP_2(),
								regel_D.getDestinationIP_3(), regel_D.getDestinationIP_4())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 4;
						}
						if (((regel_A.getDestinationPort() == regel_C.getDestinationPort()) && (regel_B.getDestinationPort() == regel_D.getDestinationPort())) == false) {
							aantalVerschillend++;
							welkeVerschillend = 5;
						}
						if (aantalVerschillend == 1) {
							// test of het opvolgend is in de reeks en voeg
							// samen
							switch (welkeVerschillend) {
							case 1: // protocol
								if (regel_C.getProtocol() == regel_B.getProtocol() + 1) {
									regel_B.setProtocol(regel_D.getProtocol());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 2);
								}
								break;
							case 2: // source IP
								if (is_Opvolgend_IPadres(regel_B.getSourceIP_1(), regel_B.getSourceIP_2(), regel_B.getSourceIP_3(), regel_B.getSourceIP_4(), regel_C.getSourceIP_1(),
										regel_C.getSourceIP_2(), regel_C.getSourceIP_3(), regel_C.getSourceIP_4())) {
									regel_B.setSourceIP_1(regel_D.getSourceIP_1());
									regel_B.setSourceIP_2(regel_D.getSourceIP_2());
									regel_B.setSourceIP_3(regel_D.getSourceIP_3());
									regel_B.setSourceIP_4(regel_D.getSourceIP_4());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 2);
								}
								break;
							case 3: // source port
								if (regel_C.getSourcePort() == regel_B.getSourcePort() + 1) {
									regel_B.setSourcePort(regel_D.getSourcePort());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 2);
								}
								break;
							case 4: // destination IP
								if (is_Opvolgend_IPadres(regel_B.getDestinationIP_1(), regel_B.getDestinationIP_2(), regel_B.getDestinationIP_3(), regel_B.getDestinationIP_4(),
										regel_C.getDestinationIP_1(), regel_C.getDestinationIP_2(), regel_C.getDestinationIP_3(), regel_C.getDestinationIP_4())) {
									regel_B.setDestinationIP_1(regel_D.getDestinationIP_1());
									regel_B.setDestinationIP_2(regel_D.getDestinationIP_2());
									regel_B.setDestinationIP_3(regel_D.getDestinationIP_3());
									regel_B.setDestinationIP_4(regel_D.getDestinationIP_4());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 2);
								}
								break;
							case 5: // destination port
								if (regel_C.getDestinationPort() == regel_B.getDestinationPort() + 1) {
									regel_B.setDestinationPort(regel_D.getDestinationPort());
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 1);
									tempOpsommingBDD.removeRuleBDD(aantalRegels - 2);
								}
								break;
							default: {
								// doe niets
							}
							}
						}

					}
				}

				if (dontCareOpGeschiktePos && (getal < (teConverterenAantal - 1)))
					getal = teConverterenAantal - 1;
				else
					getal++;
			}
		} // end for -- aantalStrings

		return tempOpsommingBDD;
	} // end maakOpsommingBDD

	/**
	 * Geeft true terug als de 2 ip-adressen in de parameters de zelfde zijn.
	 * @param A_1 Eerste getal van eerste ip-adres.
	 * @param A_2 Tweede getal van eerste ip-adres.
	 * @param A_3 Derde getal van eerste ip-adres.
	 * @param A_4 Vierde getal van eerste ip-adres.
	 * @param B_1 Eerste getal van tweede ip-adres.
	 * @param B_2 Tweede getal van tweede ip-adres.
	 * @param B_3 Derde getal van tweede ip-adres.
	 * @param B_4 Vierde getal van tweede ip-adres.
	 * @return true als de 2 ip-adressen in de parameters de zelfde zijn.
	 */
	private static boolean is_Zelfde_IPadres(int A_1, int A_2, int A_3, int A_4, int B_1, int B_2, int B_3, int B_4) {
		if ((A_1 == B_1) && (A_2 == B_2) && (A_3 == B_3) && (A_4 == B_4))
			return true;
		else
			return false;
	}

	/**
	 * Geeft true terug als het tweede ip-adres numeriek de opvolger is van het eerste ip-adres.
	 * Bijvoorbeeld 177.45.67.55 is de opvolger van 177.45.67.54.
	 * @param A_1 Eerste getal van eerste ip-adres.
	 * @param A_2 Tweede getal van eerste ip-adres.
	 * @param A_3 Derde getal van eerste ip-adres.
	 * @param A_4 Vierde getal van eerste ip-adres.
	 * @param B_1 Eerste getal van tweede ip-adres.
	 * @param B_2 Tweede getal van tweede ip-adres.
	 * @param B_3 Derde getal van tweede ip-adres.
	 * @param B_4 Vierde getal van tweede ip-adres.
	 * @return true als tweede ip-adres numeriek de opvolger is van het eerste ip-adres.
	 */
	private static boolean is_Opvolgend_IPadres(int A_1, int A_2, int A_3, int A_4, int B_1, int B_2, int B_3, int B_4) {
		A_4++;
		if (A_4 == 256) {
			A_4 = 0;
			A_3++;
		}
		if (A_3 == 256) {
			A_3 = 0;
			A_2++;
		}
		if (A_2 == 256) {
			A_2 = 0;
			A_1++;
		}
		if (A_1 == 256) // kan niet voorkomen
			return false;
		if ((A_1 == B_1) && (A_2 == B_2) && (A_3 == B_3) && (A_4 == B_4))
			return true;
		return false;
	}


	/**
	 * Controleert of een opsomming van de 5 dimensionale ruimte van een BDD
	 * overeenstemt met de ruimte van de oorspronkelijke BDD.
	 * 
	 * @param bronBDD
	 *            de BDD waarvan een opsomming is gemaakt
	 * @param opsommingBDD
	 *            de opsomming van de 5 dimensionale ruimte van een BDD
	 * @return <code>true</code> als de ruimte beschreven door de opsomming in
	 *         regels <br />
	 *         overeenstemt met de ruime van de bron BDD, anders
	 *         <code>false</code>
	 */
	public boolean testEnumerationBDD(BDD bronBDD, EnumerationBDD opsommingBDD) {
		BDD opsomming = False;
		
		BDD tempBDD = True;
		RuleBDD regelBDD;
		RuleBDD regelBDD_L;
		BDD bovengrensBDD;
		BDD ondergrensBDD;
		BDD rangeBDD;
		int index;

		int aantal = opsommingBDD.getSize();
		int aanwijzer = 0;
		while (aanwijzer < aantal) 
		{
			// neem een regel van de opsomming
			regelBDD = opsommingBDD.getRuleBDD(aanwijzer);
			if (regelBDD.getInfoString().equals("A") )
			{
				tempBDD = True;	
				aanwijzer++;
				regelBDD_L = opsommingBDD.getRuleBDD(aanwijzer);		

				// voeg protocol toe aan BDD
				index = 0;
				
				if (regelBDD.getProtocol() == 0)
				{
					rangeBDD = everythingUnderBoundaryBDD_Protocol(index, regelBDD_L.getProtocol()); 
				}
				else
				{
					bovengrensBDD = everythingUnderBoundaryBDD_Protocol(index, regelBDD_L.getProtocol()); 
					ondergrensBDD =	everythingUnderBoundaryBDD_Protocol(index, regelBDD.getProtocol() - 1);
					rangeBDD = bovengrensBDD.apply(ondergrensBDD,BDDFactory.xor); 
				}
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
				
				// voeg source IP adres toe
				index = 8;
				
				if ((regelBDD.getSourceIP_1() == 0) && (regelBDD.getSourceIP_2() == 0) && (regelBDD.getSourceIP_3() == 0) && (regelBDD.getSourceIP_4() == 0))
				{
					rangeBDD = everythingUnderBoundaryBDD_IP(index, regelBDD_L.getSourceIP_1(), regelBDD_L.getSourceIP_2(), regelBDD_L.getSourceIP_3(), regelBDD_L.getSourceIP_4(), false ); 
				}
				else
				{
					bovengrensBDD = everythingUnderBoundaryBDD_IP(index, regelBDD_L.getSourceIP_1(), regelBDD_L.getSourceIP_2(), regelBDD_L.getSourceIP_3(), regelBDD_L.getSourceIP_4(), false ); 
					ondergrensBDD =	everythingUnderBoundaryBDD_IP(index, regelBDD.getSourceIP_1(), regelBDD.getSourceIP_2(), regelBDD.getSourceIP_3(), regelBDD.getSourceIP_4(), true ); 
					rangeBDD = bovengrensBDD.apply(ondergrensBDD, BDDFactory.xor); 
				}
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
				
				// voeg source port toe
				index = 40;
				if (regelBDD.getSourcePort() == 0)
				{
					 rangeBDD = everythingUnderBoundaryBDD(index, regelBDD_L.getSourcePort());
				}
				else
				{
					bovengrensBDD = everythingUnderBoundaryBDD(index, regelBDD_L.getSourcePort());
					ondergrensBDD = everythingUnderBoundaryBDD(index, regelBDD.getSourcePort() - 1);
					rangeBDD = bovengrensBDD.apply(ondergrensBDD, BDDFactory.xor);
				
				}	
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
				
				// voeg destination IP adres toe
				index = 56;
				
				if ((regelBDD.getDestinationIP_1() == 0) && (regelBDD.getDestinationIP_2() == 0) && (regelBDD.getDestinationIP_3() == 0) && (regelBDD.getDestinationIP_4() == 0))
				{
					rangeBDD = everythingUnderBoundaryBDD_IP(index, regelBDD_L.getDestinationIP_1(), regelBDD_L.getDestinationIP_2(), regelBDD_L.getDestinationIP_3(), regelBDD_L.getDestinationIP_4(), false ); 
				}
				else
				{
					bovengrensBDD = everythingUnderBoundaryBDD_IP(index, regelBDD_L.getDestinationIP_1(), regelBDD_L.getDestinationIP_2(), regelBDD_L.getDestinationIP_3(), regelBDD_L.getDestinationIP_4(), false ); 
					ondergrensBDD =	everythingUnderBoundaryBDD_IP(index, regelBDD.getDestinationIP_1(), regelBDD.getDestinationIP_2(), regelBDD.getDestinationIP_3(), regelBDD.getDestinationIP_4(), true ); 
					rangeBDD = bovengrensBDD.apply(ondergrensBDD, BDDFactory.xor); 
				}
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
				
				// voeg source port toe
				index = 88;
				if (regelBDD.getDestinationPort() == 0)
				{
					 rangeBDD = everythingUnderBoundaryBDD(index, regelBDD_L.getDestinationPort());
				}
				else
				{
					bovengrensBDD = everythingUnderBoundaryBDD(index, regelBDD_L.getDestinationPort());
					ondergrensBDD = everythingUnderBoundaryBDD(index, regelBDD.getDestinationPort() - 1);
					rangeBDD = bovengrensBDD.apply(ondergrensBDD, BDDFactory.xor);
				}	
				tempBDD = tempBDD.apply(rangeBDD, BDDFactory.and);
				
			}
			else //if (((regelBDD.getInfoString().equals("L") == false) && (regelBDD.getInfoString().equals("A") == false))) 
			{
				tempBDD = ruleOrPacketToBDD(regelBDD.getProtocol(), regelBDD.getSourceIP_1(), regelBDD.getSourceIP_2(), regelBDD.getSourceIP_3(), regelBDD.getSourceIP_4(), 32,
						regelBDD.getSourcePort(), regelBDD.getSourcePort(), regelBDD.getDestinationIP_1(), regelBDD.getDestinationIP_2(), regelBDD.getDestinationIP_3(), regelBDD.getDestinationIP_4(),
						32, regelBDD.getDestinationPort(), regelBDD.getDestinationPort());	
			}
			opsomming = opsomming.apply(tempBDD, BDDFactory.or);
			aanwijzer++;
		}
		if (opsomming.equals(bronBDD)) {
			return true;
		} else
			return false;
	} // end testOpsommingBDD

	// voor IP adress
	/**
	 * Geeft aan een subrange van variabelen in een BDD, die het IP adres
	 * representeert, de ruimte vanaf IP adres 0 tot en met een bovengrens aan
	 * IP adressen.<br /><br />
	 * Wordt allen gebruikt binnen een testmethode voor de compacte opsommming van een segment.
	 * 
	 * @param index
	 *            de variabele in de BDD, waar het IP adres is
	 * @param ip_1 het eerste deel van de bovengrens van het IP adres
	 * @param ip_2 het tweede deel van de bovengrens van het IP adres
	 * @param ip_3 het derde deel van de bovengrens van het IP adres
	 * @param ip_4 het vierde deel van de bovengrens van het IP adres
	 * 
	 * @param eenEraf <code>true</code> als de grenswaarde nog met een verlaagd moet worden,
	 *        anders <code>falsee</code>
	 * @return een BDD waarvan alleen de variabelen die betrekking hebben op het
	 *         IP adres tot en met de bovengrens vertegenwoordigd zijn
	 *         Voorwaarde: IP adres >= 0
	 */
	private BDD everythingUnderBoundaryBDD_IP(int index, int ip_1, int ip_2, int ip_3, int ip_4, boolean eenEraf) 
	{
		boolean[] ip_all_Binair = new boolean[32];
		boolean[] ip_1_Binair = new boolean[8];
		boolean[] ip_2_Binair = new boolean[8];
		boolean[] ip_3_Binair = new boolean[8];
		boolean[] ip_4_Binair = new boolean[8];
		BDD grensBDD = False; // staat goed op False

		if (eenEraf)
		{
			if (ip_4 > 0)
				ip_4--;
			else if (ip_3 > 0)
			{
				ip_3--;
				ip_4 = 255;
			}
			else if (ip_2 > 0)
			{
				ip_2--;
				ip_3 = 255;
				ip_4 = 255;
			}
			else if (ip_1 > 0)
			{
				ip_1--;
				ip_2 = 255;
				ip_3 = 255;
				ip_4 = 255;
			}
			else
			{// kan niet voorkomen
				
			}
		}
		// voor debug
		//System.out.println();
		//System.out.println("eenEraf : " + eenEraf ); 
		//System.out.println("IP adres : " + ip_1 + " " + ip_2 + " " + ip_3 + " " +ip_4);
		
		// bepaal binaire representatie van grens
		int data;
		boolean[] ip_Binair;
		for (int iteratie = 1; iteratie <= 4; iteratie++)
		{
			if (iteratie == 1)
			{
				data = ip_1;
				ip_Binair = ip_1_Binair;
			}
			else if (iteratie == 2)
			{
				data = ip_2;
				ip_Binair = ip_2_Binair;
			}
			else if (iteratie == 3)
			{
				data = ip_3;	
				ip_Binair = ip_3_Binair;
			}
			else 
			{
				data = ip_4;	
				ip_Binair = ip_4_Binair;
			}
			
			for (int i = 7; i >= 0; i--) 
			{
				if (data >= powerOfTwo[i]) 
				{
					ip_Binair[7 - i] = true;
					data = data - powerOfTwo[i];
				} 
				else 
				{
					ip_Binair[7 - i] = false;
				}
			} // end for
		}

		// plak ze aan elkaar
	
		int aanwijzer;
	
		aanwijzer = 0;
		ip_Binair = ip_1_Binair;
		for (int i = 0; i < 8; i++) 
		{
			ip_all_Binair[aanwijzer + i] = ip_Binair[i];
		}
		aanwijzer = 8;	
		ip_Binair = ip_2_Binair;
		for (int i = 0; i < 8; i++) 
		{
			ip_all_Binair[aanwijzer + i] = ip_Binair[i];
		}
		aanwijzer = 16;	
		ip_Binair = ip_3_Binair;
		for (int i = 0; i < 8; i++) 
		{
			ip_all_Binair[aanwijzer + i] = ip_Binair[i];
		}
		aanwijzer = 24;	
		ip_Binair = ip_4_Binair;
		for (int i = 0; i < 8; i++) 
		{
			ip_all_Binair[aanwijzer + i] = ip_Binair[i];
		}
		
		BDD tussenvormBDD = True;

		 /*
	 
		// voor debug
		System.out.println();
		for (int bitIndex = 0; bitIndex < 32; bitIndex++) 
		{
			
			if (ip_all_Binair[bitIndex]) // bitwaarde is een 1
			{
				 System.out.print("1");
			}
			else
				 System.out.print("0");
		}
		  */
		for (int bitIndex = 0; bitIndex < 32; bitIndex++) 
		{
			index++;
			if (ip_all_Binair[bitIndex]) // bitwaarde is een 1
			{
				if (bitIndex == 31) 
				{
					grensBDD = grensBDD.apply(tussenvormBDD, BDDFactory.or);
				} 
				else 
				{
					// bitwaarde is een 1 ; // eerst met een 0 toevoegen
					grensBDD = grensBDD.apply(tussenvormBDD.and(fact.nithVar(index)), BDDFactory.or);

					// dan de 1 nemen
					tussenvormBDD = tussenvormBDD.and(fact.ithVar(index));
				}
			} 
			else // bitwaarde is een 0
			{
				// een 0 toevoegen
				tussenvormBDD = tussenvormBDD.and(fact.nithVar(index));
				// bovengrensBDD.printDot();
				if (bitIndex == 31) 
				{// that BDD is consumed and can no longer
					// be used
					grensBDD = grensBDD.apply(tussenvormBDD, BDDFactory.or);
				}
			}
		} // end for

		return grensBDD;
	} // end allesOnderGrensBDD
	
	/**
	 * Geeft aan een subrange van variabelen in een BDD, die het Protocol
	 * representeert, de ruimte vanaf Protocol 0 tot en met een bovengrens aan
	 * protocolnummers.
	 * <br /><br />
	 * Wordt allen gebruikt binnen een testmethode voor de compacte opsommming van een segment.
	 * 
	 * @param index
	 *            de variabele in de BDD, waar het Protocol is
	 * @param getalByte
	 *            de bovengrens van de protocollen
	 * @return een BDD waarvan alleen de variabelen die betrekking hebben op het
	 *         Protocol tot en met de bovengrens vertegenwoordigd zijn
	 *         Voorwaarde: getalByte >= 0
	 */
	protected BDD everythingUnderBoundaryBDD_Protocol(int index, int getalByte) {
		boolean[] getalByteBinair = new boolean[8];
		BDD grensBDD = False; // staat goed op False

		// bepaal binaire representatie van grens
		for (int i = 7; i >= 0; i--) {
			if (getalByte >= powerOfTwo[i]) {
				getalByteBinair[7 - i] = true;
				getalByte = getalByte - powerOfTwo[i];
			} else {
				getalByteBinair[7 - i] = false;
			}
		} // end for

		BDD tussenvormBDD = True;

		for (int bitIndex = 0; bitIndex < 8; bitIndex++) {
			index++;
			if (getalByteBinair[bitIndex]) // bitwaarde is een 1
			{
				if (bitIndex == 7) {
					grensBDD = grensBDD.apply(tussenvormBDD, BDDFactory.or);
				} else {
					// bitwaarde is een 1 ; // eerst met een 0 toevoegen
					grensBDD = grensBDD.apply(tussenvormBDD.and(fact.nithVar(index)), BDDFactory.or);

					// dan de 1 nemen
					tussenvormBDD = tussenvormBDD.and(fact.ithVar(index));
				}
			} else // bitwaarde is een 0
			{
				// een 0 toevoegen
				tussenvormBDD = tussenvormBDD.and(fact.nithVar(index));
				// bovengrensBDD.printDot();
				if (bitIndex == 7) {// that BDD is consumed and can no longer
										// be used
					grensBDD = grensBDD.apply(tussenvormBDD, BDDFactory.or);
				}
			}
		} // end for

		return grensBDD;
	} // end allesOnderGrensBDD

	
	/**
	 * Geeft het aantal regels dat een opsomming van de 5 dimensionale ruimte
	 * van een BDD heeft.
	 * 
	 * @param bdd
	 *            de BDD waarvan men de grootte in opsomming wilt verkrijgen
	 * @return het aantal regels van de opsomming
	 */
	public int getNumberOfRulesOfBDD(BDD bdd) {
		return (int) (bdd.satCount() / 2); // var met index 0 wordt niet
		// gebruikt, dus delen door 2
	}

	/**
	 * Geeft een Object ConflictSolution met daarin de informatie van de analyse
	 * naar een herschikking van de firewall regels die betrokken zijn bij
	 * conflictrende segmenten.
	 * 
	 * @param group
	 *            de correlatie groep die geanalyseerd gaat worden voor
	 *            conflicterende segmenten
	 * @param actionConstraint
	 *            de gewenste acties waaraan de firewall regels die het eerst
	 *            voorkomen in eeen segment moeten voldoen
	 * @param extraLangZoeken
	 *            ALS <code>true</code> dan wordt extra lang gezocht (4 maal zo
	 *            lang), dit omdat het algoritme tijdcomplexiteit O(n!) heeft <br />
	 *            ANDERS wordt met een gewone tijdslimiet gezocht
	 * @return een object OplossingConflict
	 */
	public ConflictSolution tryConflictSolution(CorrelationGroup group, ArrayList<Boolean> actionConstraint, boolean extraLangZoeken) { // laatse
		// wijzigingen:
		// 13
		// augustus
		// 2013
		int teller = 0;
		int limietTeller = 20000000;
		if (extraLangZoeken) {
			limietTeller = limietTeller * 4;
		}

		ConflictSolution oplossingConflict = new ConflictSolution(); // voor
		// de
		// uitvoer
		ArrayList<Rule> puzzelArray = new ArrayList<Rule>(); // voor
		// herschikking van
		// betrokken
		// firewall regels
		ArrayList<Segment> conflictSegmenten = new ArrayList<Segment>(); // voor
		// de
		// conflicterende
		// segmenten
		ArrayList<Boolean> actionMatch = new ArrayList<Boolean>();
		TreeSet<Rule> treeSet = new TreeSet<Rule>();

		Segment segment;
		int aantalSegmentenInGroup = group.getSize();
		for (int i = 0; i < aantalSegmentenInGroup; i++) {
			segment = group.getSegment(i);
			if (segment.isOverlapping() && segment.getConflicting()) {
				conflictSegmenten.add(segment);
				actionMatch.add(false); // misschien beter in een array
				treeSet.addAll(segment.getRules()); // treeSet bevat alle
				// relevante rules
			}
		}
		puzzelArray.addAll(treeSet);
		Collections.sort(puzzelArray); // puzzelArray wisselt steeds van
		// volgorde
		int aantal = puzzelArray.size();
		for (int i = 0; i < aantal; i++) {
			oplossingConflict.addInvolvedRuleNr(puzzelArray.get(i).getRuleNr());
		}

		ArrayList<ArrayList<Integer>> actionList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tempActionList;
		for (int i = 0; i < aantal; i++) {
			actionList.add(new ArrayList<Integer>());
		}
		int aantalAction;
		int aantalRules = puzzelArray.size();
		int aanwijzerRule = 0;
		int aanwijzerSegment = 0;
		Boolean vooruit = true;

		int aantalConflictSegmenten = conflictSegmenten.size();
		boolean verderZoeken = true;
		boolean oplossingGevonden = false;

		if (aantalConflictSegmenten != actionConstraint.size()) // invoer fout
		{
			oplossingConflict.setStatus(ConflictSolution.Status.ERROR_IN_INPUT);
			return oplossingConflict;
		}

		while (verderZoeken) // tot alles doorzocht of oplossing gevonden
		{
			if (vooruit) // slaat op rules
			{
				aanwijzerSegment = 0;
				// controleer voor dit en ieder volgend segment waar de rule
				// in voorkomt en actionMatch == false of de action vereenstemt
				while ((vooruit) && (aanwijzerSegment < aantalConflictSegmenten)) {
					// heeft segment al de juiste actie
					if (actionMatch.get(aanwijzerSegment) == true) {
						aanwijzerSegment++;
					} else {
						// kijk of huidige rule in segment voorkomt
						if (conflictSegmenten.get(aanwijzerSegment).getRuleNumbers().contains(puzzelArray.get(aanwijzerRule).getRuleNr())) {
							// controleer of action == actionConstraint
							if (puzzelArray.get(aanwijzerRule).getAction() == actionConstraint.get(aanwijzerSegment)) {
								actionMatch.set(aanwijzerSegment, true);
								actionList.get(aanwijzerRule).add(aanwijzerSegment);
								aanwijzerSegment++;
							} else // regel kan niet op deze positie
							{
								vooruit = false;
							}
						} else // regel komt niet in segment voor
						{
							aanwijzerSegment++;
						}
					}
				} // end while

				if (aanwijzerSegment == aantalConflictSegmenten) {
					aanwijzerRule++;

					if (aanwijzerRule == aantalRules) {
						oplossingGevonden = true;
						verderZoeken = false;
					}
				}
			} // end if
			else // vooruit == false
			{
				tempActionList = actionList.get(aanwijzerRule);
				aantalAction = tempActionList.size();
				for (int i = 0; i < aantalAction; i++) {
					actionMatch.set(tempActionList.get(i), false);
				}
				tempActionList.clear();

				// zoek hoger regelnummer
				int aanwijzer2 = aanwijzerRule;
				aanwijzer2++;
				// sorteer wat volgend op dit veld komt
				Collections.sort(puzzelArray.subList(aanwijzer2, aantalRules));

				// zoek hoger regelnummer
				int huidigRegelNr = puzzelArray.get(aanwijzerRule).getRuleNr();
				boolean hogereRegelGevonden = false;

				while (!hogereRegelGevonden && aanwijzer2 < aantalRules) {
					if (puzzelArray.get(aanwijzer2).getRuleNr() > huidigRegelNr) {
						hogereRegelGevonden = true;
					} else
						aanwijzer2++;
				}
				if (hogereRegelGevonden) {
					// verwissel de twee rules -- blijft op volgorde
					Collections.swap(puzzelArray, aanwijzerRule, aanwijzer2);
					vooruit = true;
				} else {
					// verlaag de aanwijzer
					aanwijzerRule--;

					// vooruit blijft false
					if (aanwijzerRule == -1) // alles geprobeerd geen oplossing
					{
						verderZoeken = false;
					}
					teller++;
					if (teller == limietTeller) {

						oplossingConflict.setStatus(ConflictSolution.Status.TO_COMPLEX);
						return oplossingConflict;
					}
				}
			} // end else

		} // end while
		if (oplossingGevonden) {
			// test of er wel een conflict was
			if (aantalRules == 1) {
				oplossingConflict.setStatus(ConflictSolution.Status.NO_REORDERING_REQUIRED);
			} else // als de volgorde niet oplopend is dan echte oplossing
			{
				boolean oplopend = true;
				for (int i = 1; i < aantalRules; i++) {
					if (puzzelArray.get(i - 1).getRuleNr() >= puzzelArray.get(i).getRuleNr())
						oplopend = false;
				}
				if (oplopend == true) {
					oplossingConflict.setStatus(ConflictSolution.Status.NO_REORDERING_REQUIRED);
				} else {
					oplossingConflict.setStatus(ConflictSolution.Status.SOLUTION_FOUND);
					for (int i = 0; i < aantal; i++) {
						oplossingConflict.addSolutionRuleNr(puzzelArray.get(i).getRuleNr());
					}
				}
			}
		} else {
			oplossingConflict.setStatus(ConflictSolution.Status.NO_SOLUTION_EXISTS);
		}
		return oplossingConflict;
	} // end tryOplossingConflict

	/**
	 * Geeft een DeniedAndAllowedSpace met de totale toegestane en geweigerde
	 * ruimtes die bij de firewall regels horen.
	 * 
	 * @param ruleSet
	 *            de Firewall regels
	 * @return een DeniedAndAllowedSpace met de toegestane en geweigerde ruimtes
	 * @throws CustomException
	 *             Kan optreden bij het manipuleren van de verschillende BDD's.
	 */
	public DeniedAndAllowedSpace getDeniedAndAllowedSpace(ArrayList<Rule> ruleSet) throws CustomException {
		DeniedAndAllowedSpace deniedAndAllowedSpace = new DeniedAndAllowedSpace();
		BDD deniedBDD = False;
		BDD allowedBDD = False;
		BDD spaceRule;
		BDD tempAND;
		BDD surplus;

		for (Rule rule : ruleSet) {
			spaceRule = ruleToBDD(rule);

			// vergelijk met deniedBDD
			tempAND = spaceRule.apply(deniedBDD, BDDFactory.and);
			// neem het deel dat niet in deniedBDD zit
			surplus = spaceRule.apply(tempAND, BDDFactory.xor);
			// vergelijk het overgebleven deel met allowedBDD
			tempAND = surplus.apply(allowedBDD, BDDFactory.and);
			// neem het deel dat niet in allowedBDD zit
			surplus = surplus.apply(tempAND, BDDFactory.xor);

			if (rule.getAction()) {
				// voeg toe aan allowed
				allowedBDD = allowedBDD.apply(surplus, BDDFactory.or);
			} else {
				// voeg toe aan denied
				deniedBDD = deniedBDD.apply(surplus, BDDFactory.or);
			}

		}
		deniedAndAllowedSpace.setDeniedBDD(deniedBDD);
		deniedAndAllowedSpace.setAllowedBDD(allowedBDD);

		return deniedAndAllowedSpace;
	} // end getDeniedAndAllowedSpace

	/**
	 * Print informatie van de opgegeven EnumerationBDD af naar de standaard
	 * uitvoer. <br />
	 * Gebruikt voor test- en debug doeleinden.
	 * 
	 * @param opsommingBDD
	 *            EnumerationBDD waarvan informatie naar de standaard uitvoer
	 *            geprint moet worden.
	 */
	public static void printEnumerationBDD(EnumerationBDD opsommingBDD) {
		System.out.println("in print Opsommming");
		RuleBDD regelBDD;
		int aantal = opsommingBDD.getSize();
		for (int i = 0; i < aantal; i++) {

			regelBDD = opsommingBDD.getRuleBDD(i);
			System.out.print(regelBDD.getProtocol());
			System.out.print("\t");
			System.out.print(regelBDD.getSourceIP_1());
			System.out.print("\t");
			System.out.print(regelBDD.getSourceIP_2());
			System.out.print("\t");
			System.out.print(regelBDD.getSourceIP_3());
			System.out.print("\t");
			System.out.print(regelBDD.getSourceIP_4());
			System.out.print("\t");
			System.out.print(regelBDD.getSourcePort());
			System.out.print("\t");
			System.out.print(regelBDD.getDestinationIP_1());
			System.out.print("\t");
			System.out.print(regelBDD.getDestinationIP_2());
			System.out.print("\t");
			System.out.print(regelBDD.getDestinationIP_3());
			System.out.print("\t");
			System.out.print(regelBDD.getDestinationIP_4());
			System.out.print("\t");
			System.out.print(regelBDD.getDestinationPort());
			System.out.print("\t");
			System.out.print(regelBDD.getInfoString());
			System.out.print("\t");
			System.out.println();
		}
	}

	/**
	 * Geeft aan tot en met welke Rule uit de huidige ruleset de nieuwe Rule
	 * volledig zichtbaar is. Er staat dan geen regel voor die (gedeeltelijk)
	 * overlapt en een andere action heeft. Bijvoorbeeld: als de returnwaarde 4
	 * is, dan zou de nieuwe Rule op de posities 1,2,3,4 of direct na 4 kunnen
	 * worden ingevoegd, zodat de functionaliteit van de nieuwe Rule verzekerd
	 * is.
	 * 
	 * @param nieuweRule
	 *            de nieuwe Rule waarvan de scope bepaald zal worden
	 * @return ALS de RuleSet leeg is 0 ANDERS het nummer van de regel tot waar
	 *         deze zichtbaar is.
	 * @throws CustomException
	 */
	public int showScope(Rule nieuweRule) throws CustomException {
		// geeft tot en met firewall regel returnwaarde
		BDD nieuweRuleBDD = ruleToBDD(nieuweRule);
		boolean nieuweRuleAction = nieuweRule.getAction();
		int aanwijzer = 0;
		int aantalInRuleSet = rules.size();
		Rule rule;

		boolean eindeScope = false;
		BDD ruleBDD;
		BDD tempAND; // voor de doorsnede
		while (aanwijzer < aantalInRuleSet && eindeScope == false) {
			rule = rules.get(aanwijzer);
			ruleBDD = ruleToBDD(rule);

			tempAND = ruleBDD.apply(nieuweRuleBDD, BDDFactory.and);
			// geen lege doorsnede EN verschillende action
			if (!(tempAND.equals(False)) && (nieuweRuleAction != rule.getAction())) {
				eindeScope = true;
			} else
				aanwijzer++;
		}
		return aanwijzer;
	} // end showScope

	// tijdelijk nodig voor JUnit-Tests
	public ArrayList<Rule> getRules() {
		return rules;
	}

	// alleen voor gebruik JUnit testmethodes
	public int matchRuleNr(Segment segment) throws CustomException {
		BDD ruleBDD;
		BDD tempAND;
		BDD segmentBDD = segment.getSegmentBDD();
		int aantalRules = rules.size();
		boolean doorgaan = true;
		int index = 0;
		while (doorgaan) {
			ruleBDD = ruleToBDD(rules.get(index));
			tempAND = ruleBDD.apply(segmentBDD, BDDFactory.and);
			if (!(tempAND.equals(False))) {
				doorgaan = false;
			} else {
				index++;
				if (index >= aantalRules) // geen match
					doorgaan = false;
			}

		}
		if (index >= aantalRules)
			return -1;
		else
			return index;
	}
	
	/**
	 * Geeft de compacte opsomming van een segment terug in een lijst van RuleBDD objecten. 
	 * 
	 * @param segment
	 *            het segment waarvan de opsomming wordt weergegeven
	 * @return Lijst van RuleBDD 's, deze regels zijn een compacte opsomming van de inhoud van het segment.
	 */
	public ArrayList<RuleBDD> getRuleBDDs(Segment segment) {
		// verkrijg BDD van dat segment
		BDD segmentBDD = segment.getSegmentBDD();
		// als inhoud van de BDD te groot is om weer te geven, toon dan volgend
		// bericht		
		// bereken EnumerationBDD - hier gebeurt de eigenlijk berekening
		EnumerationBDD enumerationBDD = makeEnumerationBDD(segmentBDD, true);
		
		ArrayList<RuleBDD> ruleBDDList = new ArrayList<RuleBDD>();
		for (int i = 0; i < enumerationBDD.getSize(); i++) {
			ruleBDDList.add(enumerationBDD.getRuleBDD(i));				
		}
		return ruleBDDList;
		
	}

	/**
	 * Geeft de opsomming van een segment terug als <code>String</code>. Deze
	 * opsomming is dan netjes geformatteerd, inclusief passsend opschrift.
	 * 
	 * @param segment
	 *            het segment waarvan de opsomming wordt weergegeven
	 * @param compact
	 * voor een compacte weergave van de opsomming dient <code>true</code>
	 *            te worden meegegeven anders dient <code>false</code> te worden
	 *            meegegeven.
	 * @return ALS het aantal opsommingsregels kleiner is dan een zekere grens
	 *         wordr die opsomming gegeven als <code>String</code> <br />
	 *         ANDERS wordt een <code>String</code> teruggegeven met de melding
	 *         dat de opsomming te groot is voor weergave. Wel wordt dit aantal
	 *         vermeld en welke regels in het segment samenkomen.
	 */
	public String getSegmentContent(Segment segment, boolean compact) {
		final int GRENSWAARDE = 400;
		// verkrijg BDD van dat segment
		BDD segmentBDD = segment.getSegmentBDD();
		int aantalRegels = getNumberOfRulesOfBDD(segmentBDD);
		// als inhoud van de BDD te groot is om weer te geven, toon dan volgend
		// bericht
		if (aantalRegels > GRENSWAARDE && (compact == false)) {

			String tekstTeGroot = "<html> Inhoud van segment is te groot om weer te geven.";
			tekstTeGroot = tekstTeGroot + "<br /><br />";
			String meer_dan = "";
			int overflow = 2100000000; // 2147483647
			if (aantalRegels > overflow) {
				meer_dan = "meer dan ";
				aantalRegels = overflow;
			}
			tekstTeGroot = tekstTeGroot + "(Segment bevat " + meer_dan + aantalRegels + " opsommingsregels)" + "<br />";
			tekstTeGroot = tekstTeGroot + "Regels betrokken in dit segment van de packet space:" + "<br /><ul>";
			TreeSet<Integer> ruleNrs_a = segment.getRuleNumbers();
			ArrayList<Integer> ruleNrs = new ArrayList<Integer>();
			ruleNrs.addAll(ruleNrs_a);
			int aantal = ruleNrs.size();
			for (int i = 0; i < aantal; i++) {
				tekstTeGroot = tekstTeGroot + "<li>regel " + ruleNrs.get(i) + "</li>";
			}
			tekstTeGroot = tekstTeGroot + "</ul></html>";
			return tekstTeGroot; // "Inhoud van segment is te groot om weer te geven.";
		} else {
			// bereken EnumerationBDD - hier gebeurt de eigenlijk berekening
			EnumerationBDD enumerationBDD = makeEnumerationBDD(segmentBDD, compact);
			String tekst = "<table class=\"table table-striped table-bordered table-hover\">\n";
			tekst += "<caption>Opsomming van inhoud van segment " + segment.getSegmentNumber() + "</caption>";
			tekst += "<thead><tr>";
			tekst += "<th>protocol</th>";
			tekst += "<th>source IP</th>";
			tekst += "<th>source port</th>";
			tekst += "<th>destination IP</th>";
			tekst += "<th>destination port</th>";
			tekst += "<th> </th>"; // voor info bij compacte weergave
			tekst += "</tr></thead>";
			tekst += "<tbody>";
			// voor elke regel (dit zijn speciale regels voor opsomming inhoud,
			// geen firewall regels)
			for (int i = 0; i < enumerationBDD.getSize(); i++) {
				RuleBDD ruleBDD = enumerationBDD.getRuleBDD(i);
				// protocol
				tekst += "<tr><td>" + ruleBDD.getProtocolString() + "</td>";
				// bron IP
				tekst += "<td>" + ruleBDD.getSourceIPString() + "</td>";
				// bron poort
				tekst += "<td>" + ruleBDD.getSourcePort() + "</td>";
				// doel IP
				tekst += "<td>" + ruleBDD.getDestinationIPString() + "</td>";
				// doel poort
				tekst += "<td>" + ruleBDD.getDestinationPort() + "</td>";
				// info
				tekst += "<td><small>" + ruleBDD.getInfoStringAsBeginOrEinde() + "</small></td>";
				tekst += "</tr>";
			}
			tekst += "</tbody>";
			tekst += "</table>";
			return tekst;
		}
	}

	/**
	 * Geeft segment van de ruimte terug met opgegeven segment nummer.
	 * @param segmentNumber Segment nummer van gewenst segment.
	 * @return Segment met dat segmentnummer.
	 */
	protected Segment getSegment(int segmentNumber) {
		ArrayList<Segment> segments = getSegments();

		if (segments != null && segments.size() > 0) {
			if (segmentNumber >= 1 && segmentNumber <= segments.size()) {
				return segments.get(segmentNumber - 1);
			} else {
				throw new IndexOutOfBoundsException("Het segment-nummer valt buiten de array indexen.");
			}
		} else {
			throw new NullPointerException("De space-instantie bevat geen segmenten.");
		}
	}

	/**
	 * Geeft inhoud van segment terug in JSON formaat. De inhoud wordt compact weergegeven als <code>compact</code> 1 is, anders
	 * wordt de inhoud uitgebreid opgesomt.
	 * @param segmentNumber Nummer van segment waarvan men de inhoud wilt.
	 * @param compact Is 1 als men de inhoud op een compacte manier wilt, anders wordt inhoud uitgebreid opgesomt.
	 * @return JSONObject met inhoud van segment.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getSegmentContentAsJSON(int segmentNumber, int compact) {
		BDD segmentBDD = getSegment(segmentNumber).getSegmentBDD();
		int aantalRegels = getNumberOfRulesOfBDD(segmentBDD);
		JSONObject jsonObject = new JSONObject();

		boolean bCompact = false;

		if (compact == 1) {
			bCompact = true;
		}

		jsonObject.put("compact", bCompact);

		if ((aantalRegels > I_GRENSWAARDE_AANTAL_TOONBARE_REGELS_VAN_SEGMENT) && !bCompact) {
			jsonObject.put("toonbaar", false);
		} else {
			jsonObject.put("toonbaar", true);
			JSONArray jsonArray = new JSONArray();

			EnumerationBDD enumerationBDD = makeEnumerationBDD(segmentBDD, bCompact);

			for (RuleBDD ruleBDD : enumerationBDD.getRuleBDDs()) {
				JSONArray jsonArrayRule = new JSONArray();

				jsonArrayRule.add(ruleBDD.getProtocolString());
				jsonArrayRule.add(ruleBDD.getSourceIPString());				
				jsonArrayRule.add(ruleBDD.getSourcePort());
				jsonArrayRule.add(ruleBDD.getDestinationIPString());				
				jsonArrayRule.add(ruleBDD.getDestinationPort());
				jsonArrayRule.add(ruleBDD.getInfoStringAsBeginOrEinde());

				jsonArray.add(jsonArrayRule);
			}

			jsonObject.put("aaData", jsonArray);
		}

		return jsonObject;
	}

	/**
	 * Geeft de redundante regels terug in JSON formaat.
	 * @return JSONObject met informatie over welke regels redundant zijn.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getRedundantRulesAsJSON() {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		SetOfGroups setOfGroups = this.createCorrelationGroups();

		for (CorrelationGroup group : setOfGroups.getCorrelationGroups()) {
			try {
				RedundancySolution redundancySolution = this.determineRedundantRules(group);
				ArrayList<Integer> redundantRuleNrs = redundancySolution.getRedundantRuleNrs();

				for (Integer ruleNr : redundantRuleNrs) {
					// System.out.println("Redundante regel " + ruleNr);
					// Regel opzoeken
					Rule rule = getRules().get(ruleNr - 1);

					JSONArray jsonArrayRule = new JSONArray();

					jsonArrayRule.add(rule.getRuleNr());
					
					//verkrijg protocol als string
					int protocol = rule.getProtocol();
					String protocolString = ""+protocol;
					if(protocol == -1){
						protocolString = "*";
					}else if(protocol == 1){
						protocolString = "ICMP";
					}else if(protocol == 6){
						protocolString = "TCP";
					}else if(protocol == 17){
						protocolString = "UDP";
					}
					jsonArrayRule.add(protocolString);
					jsonArrayRule.add(rule.getSourceIP().toString());
					jsonArrayRule.add(rule.getSourcePort().toString());
					jsonArrayRule.add(rule.getDestinationIP().toString());
					jsonArrayRule.add(rule.getDestinationPort().toString());

					String htmlRuleAction = "<span class='label " + CustomJSPTags.actionToCssLabelClass(rule.getAction()) + "'>" + rule.getActionString() + "</span>";

					jsonArrayRule.add(htmlRuleAction);

					jsonArray.add(jsonArrayRule);
				}

			} catch (CustomException e) {
				e.printStackTrace();
			}

		}

		jsonObject.put("iTotalRecords", jsonArray.size());
		jsonObject.put("aaData", jsonArray);

		return jsonObject;
	}

	/**
	 * Geeft aantal policy conflicten terug in de ruimte.
	 * @return Aantal policy conflicten in de ruimte.
	 */
	public int getConflictCount() {
		int count = 0;
		
		if (getSegments() != null && getSegments().size() > 0) {

			for (Segment segment : getSegments()) {
				if (segment.isOverlapping() && segment.getConflicting()) {
					count++;
				}
			}
			return count;
		}
		
		return count;
	}

} // end Space

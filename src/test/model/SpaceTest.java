package test.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import org.junit.Test;

import exception.CustomException;
import model.configuration.ConflictStatus;
import model.configuration.IPPattern;
import model.configuration.PortPattern;
import model.configuration.ConflictSolution;
import model.configuration.CorrelationGroup;
import model.configuration.DeniedAndAllowedSpace;
import model.configuration.EnumerationBDD;
import model.configuration.RedundancySolution;
import model.configuration.Rule;
import model.configuration.Segment;
import model.configuration.SetOfGroups;
import model.configuration.Space;
import static org.junit.Assert.*;

public class SpaceTest {

	/**
	 * Test methode getSegments van klasse Space.
	 * 
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurd bij segmenteren.
	 */
	@Test
	public void testGetSegments() throws CustomException {

		// voorbeeld uit paper 5 rules
		DemoRules demoRules = new DemoRules();
		ArrayList<Rule> mijnRules = demoRules.getDemoRules();
		Space ruimte = new Space(mijnRules);
		SetOfGroups setOfGroups = ruimte.createCorrelationGroups();
		assertEquals(1, setOfGroups.getNumberOfGroups());
		CorrelationGroup group = setOfGroups.getGroup(0); // er is maar een
															// groep
		ArrayList<Segment> segmenten = new ArrayList<Segment>();

		int aantalSegmenten2 = group.getSize();
		for (int i = 0; i < aantalSegmenten2; i++) {
			segmenten.add(group.getSegment(i));
		}

		// int aantalSegmenten2 = segmenten.size();
		for (int i = 0; i < aantalSegmenten2; i++) {
			Segment segment = segmenten.get(i);
			assertEquals(i + 1, segment.getSegmentNumber()); // hier toevallig
																// zo
			ArrayList<Rule> rules = segment.getRules();
			int aantalRules = rules.size();
			switch (i + 1) {
			case 1:
				assertEquals(2, aantalRules);
				assertEquals(1, rules.get(0).getRuleNr());
				assertEquals(2, rules.get(1).getRuleNr());
				break;
			case 2:
				assertEquals(2, aantalRules);
				assertEquals(2, rules.get(0).getRuleNr());
				assertEquals(5, rules.get(1).getRuleNr());
				break;
			case 3:
				assertEquals(3, aantalRules);
				assertEquals(3, rules.get(0).getRuleNr());
				assertEquals(4, rules.get(1).getRuleNr());
				assertEquals(5, rules.get(2).getRuleNr());
				break;
			case 4:
				assertEquals(2, aantalRules);
				assertEquals(3, rules.get(0).getRuleNr());
				assertEquals(5, rules.get(1).getRuleNr());
				break;
			case 5:
				assertEquals(1, aantalRules);
				assertEquals(2, rules.get(0).getRuleNr());
				break;
			case 6:
				assertEquals(1, aantalRules);
				assertEquals(3, rules.get(0).getRuleNr());
				break;
			case 7:
				assertEquals(1, aantalRules);
				assertEquals(5, rules.get(0).getRuleNr());
				break;
			default:
				// kan niet voorkomen
				assertEquals(-1, aantalRules);
			}
		}

		// 5 groepen -- eerste 25 regels van dit bestand
		// is gelijk aan bestand voor 5 groepen
		DemoRulesGroup demoRulesGroup = new DemoRulesGroup();
		ArrayList<Rule> mijnRules_even = demoRulesGroup.getDemoRules();
		mijnRules.clear();
		for (int i = 0; i < 25; i++) {
			mijnRules.add(mijnRules_even.get(i));
		}

		ruimte = new Space(mijnRules);
		setOfGroups = ruimte.createCorrelationGroups();
		int aantalGroupen = setOfGroups.getNumberOfGroups();
		assertEquals(5, aantalGroupen);
		for (int j = 0; j < aantalGroupen; j++) {
			group = setOfGroups.getGroup(j);
			switch (j + 1) {
			case 1:
				aantalSegmenten2 = group.getSize();
				for (int i = 0; i < aantalSegmenten2; i++) {
					Segment segment = group.getSegment(i);
					ArrayList<Rule> rules = segment.getRules();
					int aantalRules = rules.size();
					switch (i + 1) {
					case 1:
						assertEquals(1, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(1, rules.get(0).getRuleNr());
						assertEquals(2, rules.get(1).getRuleNr());
						break;
					case 2:
						assertEquals(2, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(2, rules.get(0).getRuleNr());
						assertEquals(5, rules.get(1).getRuleNr());
						break;
					case 3:
						assertEquals(3, segment.getSegmentNumber());
						assertEquals(3, aantalRules);
						assertEquals(3, rules.get(0).getRuleNr());
						assertEquals(4, rules.get(1).getRuleNr());
						assertEquals(5, rules.get(2).getRuleNr());
						break;
					case 4:
						assertEquals(4, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(3, rules.get(0).getRuleNr());
						assertEquals(5, rules.get(1).getRuleNr());
						break;
					case 5:
						assertEquals(5, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(2, rules.get(0).getRuleNr());
						break;
					case 6:
						assertEquals(6, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(3, rules.get(0).getRuleNr());
						break;
					case 7:
						assertEquals(7, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(5, rules.get(0).getRuleNr());
						break;
					default:
						// kan niet voorkomen
						assertEquals(-1, aantalRules);
					}
				}
				break;
			case 2:
				aantalSegmenten2 = group.getSize();
				for (int i = 0; i < aantalSegmenten2; i++) {
					Segment segment = group.getSegment(i);
					ArrayList<Rule> rules = segment.getRules();
					int aantalRules = rules.size();
					switch (i + 1) {
					case 1:
						assertEquals(8, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(6, rules.get(0).getRuleNr());
						assertEquals(7, rules.get(1).getRuleNr());
						break;
					case 2:
						assertEquals(9, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(7, rules.get(0).getRuleNr());
						assertEquals(10, rules.get(1).getRuleNr());
						break;
					case 3:
						assertEquals(10, segment.getSegmentNumber());
						assertEquals(3, aantalRules);
						assertEquals(8, rules.get(0).getRuleNr());
						assertEquals(9, rules.get(1).getRuleNr());
						assertEquals(10, rules.get(2).getRuleNr());
						break;
					case 4:
						assertEquals(11, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(8, rules.get(0).getRuleNr());
						assertEquals(10, rules.get(1).getRuleNr());
						break;
					case 5:
						assertEquals(12, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(7, rules.get(0).getRuleNr());
						break;
					case 6:
						assertEquals(13, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(8, rules.get(0).getRuleNr());
						break;
					case 7:
						assertEquals(14, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(10, rules.get(0).getRuleNr());
						break;
					default:
						// kan niet voorkomen
						assertEquals(-1, aantalRules);
					}
				}
				break;
			case 3:
				aantalSegmenten2 = group.getSize();
				for (int i = 0; i < aantalSegmenten2; i++) {
					Segment segment = group.getSegment(i);
					ArrayList<Rule> rules = segment.getRules();
					int aantalRules = rules.size();
					switch (i + 1) {
					case 1:
						assertEquals(15, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(11, rules.get(0).getRuleNr());
						assertEquals(12, rules.get(1).getRuleNr());
						break;
					case 2:
						assertEquals(16, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(12, rules.get(0).getRuleNr());
						assertEquals(15, rules.get(1).getRuleNr());
						break;
					case 3:
						assertEquals(17, segment.getSegmentNumber());
						assertEquals(3, aantalRules);
						assertEquals(13, rules.get(0).getRuleNr());
						assertEquals(14, rules.get(1).getRuleNr());
						assertEquals(15, rules.get(2).getRuleNr());
						break;
					case 4:
						assertEquals(18, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(13, rules.get(0).getRuleNr());
						assertEquals(15, rules.get(1).getRuleNr());
						break;
					case 5:
						assertEquals(19, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(12, rules.get(0).getRuleNr());
						break;
					case 6:
						assertEquals(20, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(13, rules.get(0).getRuleNr());
						break;
					case 7:
						assertEquals(21, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(15, rules.get(0).getRuleNr());
						break;
					default:
						// kan niet voorkomen
						assertEquals(-1, aantalRules);
					}
				}

				break;
			case 4:
				aantalSegmenten2 = group.getSize();
				for (int i = 0; i < aantalSegmenten2; i++) {
					Segment segment = group.getSegment(i);
					ArrayList<Rule> rules = segment.getRules();
					int aantalRules = rules.size();
					switch (i + 1) {
					case 1:
						assertEquals(22, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(16, rules.get(0).getRuleNr());
						assertEquals(17, rules.get(1).getRuleNr());
						break;
					case 2:
						assertEquals(23, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(17, rules.get(0).getRuleNr());
						assertEquals(20, rules.get(1).getRuleNr());
						break;
					case 3:
						assertEquals(24, segment.getSegmentNumber());
						assertEquals(3, aantalRules);
						assertEquals(18, rules.get(0).getRuleNr());
						assertEquals(19, rules.get(1).getRuleNr());
						assertEquals(20, rules.get(2).getRuleNr());
						break;
					case 4:
						assertEquals(25, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(18, rules.get(0).getRuleNr());
						assertEquals(20, rules.get(1).getRuleNr());
						break;
					case 5:
						assertEquals(26, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(17, rules.get(0).getRuleNr());
						break;
					case 6:
						assertEquals(27, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(18, rules.get(0).getRuleNr());
						break;
					case 7:
						assertEquals(28, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(20, rules.get(0).getRuleNr());
						break;
					default:
						// kan niet voorkomen
						assertEquals(-1, aantalRules);
					}
				}
				break;
			case 5:
				aantalSegmenten2 = group.getSize();
				for (int i = 0; i < aantalSegmenten2; i++) {
					Segment segment = group.getSegment(i);
					ArrayList<Rule> rules = segment.getRules();
					int aantalRules = rules.size();
					switch (i + 1) {
					case 1:
						assertEquals(29, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(21, rules.get(0).getRuleNr());
						assertEquals(22, rules.get(1).getRuleNr());
						break;
					case 2:
						assertEquals(30, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(22, rules.get(0).getRuleNr());
						assertEquals(25, rules.get(1).getRuleNr());
						break;
					case 3:
						assertEquals(31, segment.getSegmentNumber());
						assertEquals(3, aantalRules);
						assertEquals(23, rules.get(0).getRuleNr());
						assertEquals(24, rules.get(1).getRuleNr());
						assertEquals(25, rules.get(2).getRuleNr());
						break;
					case 4:
						assertEquals(32, segment.getSegmentNumber());
						assertEquals(2, aantalRules);
						assertEquals(23, rules.get(0).getRuleNr());
						assertEquals(25, rules.get(1).getRuleNr());
						break;
					case 5:
						assertEquals(33, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(22, rules.get(0).getRuleNr());
						break;
					case 6:
						assertEquals(34, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(23, rules.get(0).getRuleNr());
						break;
					case 7:
						assertEquals(35, segment.getSegmentNumber());
						assertEquals(1, aantalRules);
						assertEquals(25, rules.get(0).getRuleNr());
						break;
					default:
						// kan niet voorkomen
						assertEquals(-1, aantalRules);
					}
				}
				break;
			default:
				// kan niet voorkomen
				assertEquals(-1, j + 1);
			}
		}

		DemoRulesGroup demoRules_A = new DemoRulesGroup();
		ArrayList<Rule> mijnRules_A = demoRules_A.getDemoRules();
		Space ruimte_A = new Space(mijnRules_A);

		DemoRulesGroup demoRules_B = new DemoRulesGroup();
		ArrayList<Rule> mijnRules_B1 = demoRules_B.getDemoRules();
		ArrayList<Rule> mijnRules_B = new ArrayList<Rule>();
		int aantal = mijnRules_B1.size();

		for (int i = aantal - 1; i >= 0; i--) {
			mijnRules_B.add(mijnRules_B1.get(i));
		}
		// ruimte_B bevat de regels in omgekeerde volgorde
		Space ruimte_B = new Space(mijnRules_B);

		ArrayList<Segment> segmenten_A = ruimte_A.getSegments();
		ArrayList<Segment> segmenten_B = ruimte_B.getSegments();
		// gelijk aantal segmenten
		assertEquals(segmenten_A.size(), segmenten_B.size());
		int aantalSegmenten = segmenten_A.size();
		for (int i = 0; i < aantalSegmenten; i++) {
			boolean eender = true;
			TreeSet<Integer> treeSet_B = segmenten_B.get(i).getRuleNumbers();
			ArrayList<Integer> nummers_B = new ArrayList<Integer>();
			nummers_B.addAll(treeSet_B);
			Collections.sort(nummers_B);
			Segment segment = segmenten_A.get(i);
			aantal = segment.getNumberOfRules();
			// aantal regels per segment gelijk
			if (aantal != nummers_B.size())
				eender = false;
			else {
				for (int j = 0; j < aantal; j++) {
					// regels op volgorde EN gelijke nummers
					if (segment.getRule(j).getRuleNr() != nummers_B.get(j)) {
						eender = false;
					}
				}
			}
			if (eender == false) {
				int aanwijzer = i + 1;
				boolean verwisseld = false;
				while (aanwijzer < aantalSegmenten && !verwisseld) {
					boolean eender2 = true;
					treeSet_B = segmenten_B.get(aanwijzer).getRuleNumbers();
					nummers_B.clear();
					nummers_B.addAll(treeSet_B);
					Collections.sort(nummers_B);

					// aantal regels per segment gelijk
					if (aantal != nummers_B.size())
						eender2 = false;
					else {
						for (int j = 0; j < aantal; j++) {
							// regels op volgorde EN gelijke nummers
							if (segment.getRule(j).getRuleNr() != nummers_B.get(j)) {
								eender2 = false;
							}
						}
					}

					if (eender2 == true) {
						Collections.swap(segmenten_B, i, aanwijzer);
						verwisseld = true;
					} else {
						aanwijzer++;
					}
				}
			}
		}

		// nog eens allemaal controleren; bedoeld wordt een extra controle
		for (int i = 0; i < aantalSegmenten; i++) {
			assertEquals(segmenten_A.get(i).getRuleNumbers().size(), segmenten_A.get(i).getNumberOfRules());
			assertEquals(segmenten_B.get(i).getRuleNumbers().size(), segmenten_B.get(i).getNumberOfRules());

			TreeSet<Integer> treeSet_B = segmenten_B.get(i).getRuleNumbers();
			ArrayList<Integer> nummers_B = new ArrayList<Integer>();
			nummers_B.addAll(treeSet_B);
			Collections.sort(nummers_B);
			Segment segment = segmenten_A.get(i);
			aantal = segment.getNumberOfRules();
			// aantal regels per segment gelijk
			assertEquals(aantal, nummers_B.size());
			for (int j = 0; j < aantal; j++) {
				// regels op volgorde EN gelijke nummers
				assertEquals((Integer) segment.getRule(j).getRuleNr(), nummers_B.get(j));
			}
		}
	}

	/*
	 * oude code die niet werkt want BDD's uit verschillende objecten kunnen niet vergeleken worden
	 * 
	 * @Test public void testGetSegmenten() { DemoRulesGroup demoRules_A = new
	 * DemoRulesGroup(); ArrayList< Rule > mijnRules_A =
	 * demoRules_A.getDemoRules(); Ruimte ruimte_A = new Ruimte(mijnRules_A);
	 * 
	 * DemoRulesGroup demoRules_B = new DemoRulesGroup(); ArrayList< Rule >
	 * mijnRules_B1 = demoRules_B.getDemoRules(); ArrayList< Rule > mijnRules_B
	 * = new ArrayList<Rule>(); int aantal = mijnRules_B1.size();
	 * 
	 * for (int i = aantal-1; i >= 0; i-- ) {
	 * mijnRules_B.add(mijnRules_B1.get(i)); }
	 * 
	 * Ruimte ruimte_B = new Ruimte(mijnRules_B);
	 * 
	 * ArrayList<Segment> segmenten_A = ruimte_A.getSegmenten();
	 * ArrayList<Segment> segmenten_B = ruimte_B.getSegmenten(); // gelijk
	 * aantal segmenten assertEquals(segmenten_A.size(), segmenten_B.size());
	 * int aantalSegmenten = segmenten_A.size(); for (int i = 0; i <
	 * aantalSegmenten; i++ ) { BDD gezochteBDD =
	 * segmenten_A.get(i).getSegmentBDD(); if (
	 * segmenten_B.get(i).getSegmentBDD().equals( gezochteBDD ) == false ) { int
	 * aanwijzer = i+1; boolean verwisseld = false; while (aanwijzer <
	 * aantalSegmenten && !verwisseld) { if (
	 * segmenten_B.get(aanwijzer).getSegmentBDD().equals( gezochteBDD ) == true
	 * ) { System.out.println("true : " + aanwijzer);
	 * Collections.swap(segmenten_B, i, aanwijzer); verwisseld = true; } else {
	 * aanwijzer++; } } } } for (int i = 0; i < aantalSegmenten; i++ ) { //
	 * BDD's gelijk boolean waarde =
	 * (segmenten_B.get(i).getSegmentBDD().equals(segmenten_A
	 * .get(i).getSegmentBDD() )); assertTrue(waarde); //
	 * assertTrue(segmenten_B.
	 * get(i).getSegmentBDD().equals(segmenten_A.get(i).getSegmentBDD() ));
	 * TreeSet <Integer> treeSet_B = segmenten_B.get(i).getRuleNumbers();
	 * ArrayList<Integer> nummers_B = new ArrayList<Integer>();
	 * nummers_B.addAll(treeSet_B); Collections.sort(nummers_B); Segment segment
	 * = segmenten_A.get(i); aantal = segment.getNumberOfRules(); // aantal
	 * regels per segment gelijk assertEquals(aantal, nummers_B.size()); for
	 * (int j = 0; j < aantal; j++ ) { // regels op volgorde EN gelijke nummers
	 * assertEquals((Integer)segment.getRule(j).getIndex(), nummers_B.get(j) );
	 * } } }
	 */

	/**
	 * Test methode everythingUnderBoundaryBDD van klasse Space.
	 */
	@Test
	public void testAllesOnderGrensBDD() {
		/*
		 * Voorwaarde is reeds dat bovengrens >= ondergrens Ik neem een aantal
		 * ranges -- maak een aantal Rules en ga met Packet first match testen
		 * of die poortnummers inderdaad binnen die range vallen
		 */

		IPPattern bron_1 = new IPPattern(158, 158, 158, 158, 0);
		IPPattern best_1 = new IPPattern(158, 158, 158, 158, 0);
		PortPattern portPatternBron1 = new PortPattern(5, 5);

		// maak Rule voor Range [0..2] -- 000 t/m 010
		PortPattern portPatternBest1 = new PortPattern(0, 2);
		Rule rule1 = new Rule(1, 17, bron_1, portPatternBron1, best_1, portPatternBest1, true);

		// maak Rule voor Range [65007..65008] -- 011 t/m 100
		PortPattern portPatternBest2 = new PortPattern(65007, 65008);
		Rule rule2 = new Rule(2, 17, bron_1, portPatternBron1, best_1, portPatternBest2, true);

		// maak Rule voor Range [3..7] -- 11 t/m 111
		PortPattern portPatternBest3 = new PortPattern(3, 7);
		Rule rule3 = new Rule(3, 17, bron_1, portPatternBron1, best_1, portPatternBest3, true);

		// maak Rule voor Range [8..8008] -- 000 t/m 000
		PortPattern portPatternBest4 = new PortPattern(8, 8008);
		Rule rule4 = new Rule(4, 17, bron_1, portPatternBron1, best_1, portPatternBest4, true);

		// maak Rule voor Range [10005..65002] -- 010 t/m 010
		PortPattern portPatternBest5 = new PortPattern(10005, 65002);
		Rule rule5 = new Rule(5, 17, bron_1, portPatternBron1, best_1, portPatternBest5, true);

		// maak Rule voor dest. poort 8009 --
		PortPattern portPatternBest6 = new PortPattern(8009, 8009);
		Rule rule6 = new Rule(6, 17, bron_1, portPatternBron1, best_1, portPatternBest6, true);

		// maak Rule voor Range [8010..9999] -- 010 t/m 111
		PortPattern portPatternBest7 = new PortPattern(8010, 9999);
		Rule rule7 = new Rule(7, 17, bron_1, portPatternBron1, best_1, portPatternBest7, true);

		// maak een RuleSet
		ArrayList<Rule> mijnRules = new ArrayList<Rule>();
		mijnRules.add(rule1);
		mijnRules.add(rule2);
		mijnRules.add(rule3);
		mijnRules.add(rule4);
		mijnRules.add(rule5);
		mijnRules.add(rule6);
		mijnRules.add(rule7);

		Space space;
		try {
			int firstMatch;
			space = new Space(mijnRules);
			for (int i = 1; i < 65200; i++) {
				firstMatch = space.packetFirstMatch(17, 158, 158, 158, 158, 5, 158, 158, 158, 158, i);

				if (i >= 1 && i <= 2)
					assertEquals(1, firstMatch);
				else if (i >= 3 && i <= 7)
					assertEquals(3, firstMatch);
				else if (i >= 8 && i <= 8008)
					assertEquals(4, firstMatch);
				else if (i >= 8009 && i <= 8009)
					assertEquals(6, firstMatch);
				else if (i >= 8010 && i <= 9999)
					assertEquals(7, firstMatch);
				else if (i >= 10005 && i <= 65002)
					assertEquals(5, firstMatch);
				else if (i >= 65007 && i <= 65008)
					assertEquals(2, firstMatch);
				else {
					assertEquals(-1, firstMatch); // geen match
				}
			}

		} catch (CustomException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test methode packetFirstMatch van klasse Space.
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testPacketFirstMatch() throws CustomException {
		System.out.println("in testPacketFirstMatch ... ");
		// voor voorbeeld 5 regels uit paper
		DemoRules demoRules = new DemoRules();
		ArrayList<Rule> mijnRules = demoRules.getDemoRules();
		Space ruimte = new Space(mijnRules);

		// test UDP, sourve IP en destination poort
		assertEquals(2, ruimte.packetFirstMatch(17, 10, 1, 3, 0, Space.NOT_SPECIFIED, Space.NOT_SPECIFIED, Space.NOT_SPECIFIED, Space.NOT_SPECIFIED, Space.NOT_SPECIFIED, 53));
		// test TCP en dest poort
		assertEquals(5, ruimte.packetFirstMatch(6, -100, Space.NOT_SPECIFIED, -4, 0, Space.NOT_SPECIFIED, 10, -4, Space.NOT_SPECIFIED, 0, 26));
		assertEquals(5, ruimte.packetFirstMatch(17, Space.NOT_SPECIFIED, 0, -2, 0, 22, 172, 32, 2, 0, 53));
		assertEquals(-1, ruimte.packetFirstMatch(17, 10, 1, 0, 0, 22, 172, 32, 2, 0, 53));
		assertEquals(-2, ruimte.packetFirstMatch(17, 10, 1, 1, -1, 22, 172, 32, 2, 0, 53));
		//assertEquals(3, ruimte.packetFirstMatch(-1, 10, 1, 1, 1, 21, 192, 168, 2, 0, Space.NOT_SPECIFIED));
		assertEquals(-2, ruimte.packetFirstMatch(-1, 10, 1, 1, 1, 21, 192, 168, 2, 0, Space.NOT_SPECIFIED));

		// demoregel match voorbeeld rule nr 5 UDP
		assertEquals(5, ruimte.packetFirstMatch(17, 10, 1, Space.NOT_SPECIFIED, 1, 155, 192, 168, 121, 10, 25));

		// demoregel match voorbeeld rule nr 2 UDP
		assertEquals(2, ruimte.packetFirstMatch(17, 10, 1, 22, 1, 155, 172, 32, 1, 10, 53));

		// demoregel match voorbeeld rule nr 3 alleen poortnummer 25
		assertEquals(3, ruimte.packetFirstMatch(6, 10, 1, Space.NOT_SPECIFIED, 1, 155, Space.NOT_SPECIFIED, 168, 121, 10, 25));

		// demoregel match voorbeeld rule nr 2 test alleen op UDP ensource iP
		// (OOPS...matcht rule 5 ---zie destination port)
		assertEquals(5, ruimte.packetFirstMatch(17, 10, 1, 1, 1, 155, Space.NOT_SPECIFIED, 168, 121, 10, 25));

		// regel voor ongeldige invoer; protocol 16 bestaat niet. Aangepast dat je ANY terugkrijgt //test verouderd- protocol 16 wordt nu wel ondersteund
		//assertEquals(-2, ruimte.packetFirstMatch(16, 10, 1, 22, 1, 155, 172, 32, 1, 10, 53));

		// pakketje met ongeldige invoer
		assertEquals(-2, ruimte.packetFirstMatch(17, 300, 1, 22, 1, 155, 172, 32, 1, 10, 53));

		// pakketje met wel degelijk geldige invoer; matcht rule 5
		assertEquals(5, ruimte.packetFirstMatch(17, 10, 1, 1, 1, 155, Space.NOT_SPECIFIED, 3168, -121, 10, 25));

	}

	/**
	 * Test methode createCorrelationGroups van klasse Space.
	 * 
	 * @throws CustomException
	 */
	@Test
	public void testCreateCorrelationGroups() throws CustomException {
		DemoRulesGroup demoRulesObject = new DemoRulesGroup();
		ArrayList<Rule> demoRules = demoRulesObject.getDemoRules();
		ArrayList<Rule> mijnDemoRules_1 = new ArrayList<Rule>();
		// voeg toe in gewenste test volgorde
		mijnDemoRules_1.add(demoRules.get(1));
		mijnDemoRules_1.add(demoRules.get(18));
		mijnDemoRules_1.add(demoRules.get(28));
		mijnDemoRules_1.add(demoRules.get(20));
		mijnDemoRules_1.add(demoRules.get(7));
		mijnDemoRules_1.add(demoRules.get(14));
		mijnDemoRules_1.add(demoRules.get(26));
		mijnDemoRules_1.add(demoRules.get(24));
		mijnDemoRules_1.add(demoRules.get(32));
		mijnDemoRules_1.add(demoRules.get(17));
		mijnDemoRules_1.add(demoRules.get(6));
		mijnDemoRules_1.add(demoRules.get(25));
		mijnDemoRules_1.add(demoRules.get(31));
		mijnDemoRules_1.add(demoRules.get(27));
		mijnDemoRules_1.add(demoRules.get(33));
		mijnDemoRules_1.add(demoRules.get(19));
		mijnDemoRules_1.add(demoRules.get(4));
		mijnDemoRules_1.add(demoRules.get(21));
		mijnDemoRules_1.add(demoRules.get(11));
		mijnDemoRules_1.add(demoRules.get(0));
		mijnDemoRules_1.add(demoRules.get(12));
		mijnDemoRules_1.add(demoRules.get(22));
		mijnDemoRules_1.add(demoRules.get(16));
		mijnDemoRules_1.add(demoRules.get(2));
		mijnDemoRules_1.add(demoRules.get(5));
		mijnDemoRules_1.add(demoRules.get(9));
		mijnDemoRules_1.add(demoRules.get(23));
		mijnDemoRules_1.add(demoRules.get(15));
		mijnDemoRules_1.add(demoRules.get(3));
		mijnDemoRules_1.add(demoRules.get(8));
		mijnDemoRules_1.add(demoRules.get(34));
		mijnDemoRules_1.add(demoRules.get(30));
		mijnDemoRules_1.add(demoRules.get(10));
		mijnDemoRules_1.add(demoRules.get(29));
		mijnDemoRules_1.add(demoRules.get(13)); // alle 35 verschillend
		/*
		 * System.out.println("pos 4 : " + mijnDemoRules_1.get(4).getIndex());
		 * System.out.println("pos 5 : " + mijnDemoRules_1.get(5).getIndex());
		 * System.out.println("pos 6 : " + mijnDemoRules_1.get(6).getIndex());
		 */
		Space ruimte = new Space(mijnDemoRules_1);
		SetOfGroups setOfGroups = ruimte.createCorrelationGroups();

		int aantalGroepen = setOfGroups.getNumberOfGroups();
		assertEquals(5, aantalGroepen);
		int groepvan_1 = 0;
		int groepvan_2 = 0;
		int groepvan_3 = 0;
		int groepvan_meer = 0;

		CorrelationGroup group;
		for (int i = 0; i < aantalGroepen; i++) {
			group = setOfGroups.getGroup(i);
			assertEquals(i + 1, group.getGroupNumber());
			int aantalSegmenten = group.getSize();

			groepvan_1 = 0;
			groepvan_2 = 0;
			groepvan_3 = 0;
			groepvan_meer = 0;

			for (int j = 0; j < aantalSegmenten; j++) {
				switch (group.getSegment(j).getNumberOfRules()) {
				case 1:
					groepvan_1++;
					break;
				case 2:
					groepvan_2++;
					break;
				case 3:
					groepvan_3++;
					break;
				default:
					groepvan_meer++;
				}
			}
			assertEquals(3, groepvan_1);
			assertEquals(5, groepvan_2);
			assertEquals(1, groepvan_3);
			assertEquals(0, groepvan_meer);
		}
		// voor de eerste 25 regels
		ArrayList<Rule> mijnDemoRules_2 = new ArrayList<Rule>();
		// voeg toe in gewenste test volgorde
		mijnDemoRules_2.add(demoRules.get(7));
		mijnDemoRules_2.add(demoRules.get(14));
		mijnDemoRules_2.add(demoRules.get(9));
		mijnDemoRules_2.add(demoRules.get(24));
		mijnDemoRules_2.add(demoRules.get(1));
		mijnDemoRules_2.add(demoRules.get(18));
		mijnDemoRules_2.add(demoRules.get(8));
		mijnDemoRules_2.add(demoRules.get(20));
		mijnDemoRules_2.add(demoRules.get(13));
		mijnDemoRules_2.add(demoRules.get(17));
		mijnDemoRules_2.add(demoRules.get(6));
		mijnDemoRules_2.add(demoRules.get(0));
		mijnDemoRules_2.add(demoRules.get(11));
		mijnDemoRules_2.add(demoRules.get(15));
		mijnDemoRules_2.add(demoRules.get(12));
		mijnDemoRules_2.add(demoRules.get(22));
		mijnDemoRules_2.add(demoRules.get(3));
		mijnDemoRules_2.add(demoRules.get(10));
		mijnDemoRules_2.add(demoRules.get(23));
		mijnDemoRules_2.add(demoRules.get(19));
		mijnDemoRules_2.add(demoRules.get(16));
		mijnDemoRules_2.add(demoRules.get(2));
		mijnDemoRules_2.add(demoRules.get(4));
		mijnDemoRules_2.add(demoRules.get(21));
		mijnDemoRules_2.add(demoRules.get(5)); // alle 25 verschillend
		/*
		 * System.out.println("pos 4 : " + mijnDemoRules_2.get(4).getIndex());
		 * System.out.println("pos 5 : " + mijnDemoRules_2.get(5).getIndex());
		 * System.out.println("pos 6 : " + mijnDemoRules_2.get(6).getIndex());
		 */
		ruimte = new Space(mijnDemoRules_2);
		setOfGroups = ruimte.createCorrelationGroups();

		aantalGroepen = setOfGroups.getNumberOfGroups();
		assertEquals(5, aantalGroepen);

		for (int i = 0; i < aantalGroepen; i++) {
			group = setOfGroups.getGroup(i);
			assertEquals(i + 1, group.getGroupNumber());
			int aantalSegmenten = group.getSize();

			groepvan_1 = 0;
			groepvan_2 = 0;
			groepvan_3 = 0;
			groepvan_meer = 0;

			for (int j = 0; j < aantalSegmenten; j++) {
				switch (group.getSegment(j).getNumberOfRules()) {
				case 1:
					groepvan_1++;
					break;
				case 2:
					groepvan_2++;
					break;
				case 3:
					groepvan_3++;
					break;
				default:
					groepvan_meer++;
				}
			}
			assertEquals(3, groepvan_1);
			assertEquals(3, groepvan_2);
			assertEquals(1, groepvan_3);
			assertEquals(0, groepvan_meer);
		}
	}

	/**
	 * Test methode determineRedundantRules van klasse Space.
	 * 
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurd bij het testen.
	 */
	@Test
	public void testDetermineRedundantRules() throws CustomException {
		DemoRulesGroup demoRulesObject = new DemoRulesGroup();
		DemoRulesGroup demoRulesObject2 = new DemoRulesGroup();
		ArrayList<Rule> demoRules = demoRulesObject.getDemoRules();
		ArrayList<Rule> demoRules2 = demoRulesObject2.getDemoRules();
		ArrayList<Rule> mijnDemoRules_1 = new ArrayList<Rule>();

		// voeg toe in gewenste test volgorde
		mijnDemoRules_1.add(demoRules.get(1));
		mijnDemoRules_1.add(demoRules.get(18));
		mijnDemoRules_1.add(demoRules.get(28));
		mijnDemoRules_1.add(demoRules.get(20));
		mijnDemoRules_1.add(demoRules.get(7));
		mijnDemoRules_1.add(demoRules.get(14));
		mijnDemoRules_1.add(demoRules.get(26));
		mijnDemoRules_1.add(demoRules.get(24));

		mijnDemoRules_1.add(demoRules.get(32));
		mijnDemoRules_1.add(demoRules.get(17));
		mijnDemoRules_1.add(demoRules.get(6));
		mijnDemoRules_1.add(demoRules.get(25));
		mijnDemoRules_1.add(demoRules.get(31));
		mijnDemoRules_1.add(demoRules.get(27));
		mijnDemoRules_1.add(demoRules.get(33));
		mijnDemoRules_1.add(demoRules.get(19));
		mijnDemoRules_1.add(demoRules.get(4));
		mijnDemoRules_1.add(demoRules.get(21));
		mijnDemoRules_1.add(demoRules.get(11));
		mijnDemoRules_1.add(demoRules.get(0));
		mijnDemoRules_1.add(demoRules.get(12));
		mijnDemoRules_1.add(demoRules.get(22));
		mijnDemoRules_1.add(demoRules.get(16));
		mijnDemoRules_1.add(demoRules.get(2));

		mijnDemoRules_1.add(demoRules2.get(31));
		mijnDemoRules_1.add(demoRules2.get(27));
		mijnDemoRules_1.add(demoRules2.get(33));
		mijnDemoRules_1.add(demoRules2.get(19));
		mijnDemoRules_1.add(demoRules2.get(4));
		mijnDemoRules_1.add(demoRules2.get(21));
		mijnDemoRules_1.add(demoRules2.get(11));
		mijnDemoRules_1.add(demoRules2.get(0));
		mijnDemoRules_1.add(demoRules2.get(12));
		mijnDemoRules_1.add(demoRules2.get(22));
		mijnDemoRules_1.add(demoRules2.get(16));
		mijnDemoRules_1.add(demoRules2.get(2));

		// pas de nummering aan
		int aantal = mijnDemoRules_1.size();
		for (int i = 0; i < aantal; i++) {
			mijnDemoRules_1.get(i).setRuleNr(i + 1);
		}

		Space ruimte = new Space(mijnDemoRules_1);
		SetOfGroups setOfGroups = ruimte.createCorrelationGroups();

//		int aantalGroepen = setOfGroups.getNumberOfGroups();
		// assertEquals(1, aantalGroepen);
		CorrelationGroup group = setOfGroups.getGroup(4);
		RedundancySolution oplossingRedundant;
		oplossingRedundant = ruimte.determineRedundantRules(group);
		ArrayList<Integer> redRegels = oplossingRedundant.getRedundantRuleNrs();
		aantal = redRegels.size();
		System.out.println("De redundante regels zijn: ");
		for (int i = 0; i < aantal; i++) {
			System.out.print(" " + redRegels.get(i));
		}
		System.out.println();
		System.out.println();

		DeniedAndAllowedSpace spaceVoor = ruimte.getDeniedAndAllowedSpace(mijnDemoRules_1);
		System.out.println("Voor verwijdering");
		printRuleSet(mijnDemoRules_1);
		oplossingRedundant.commitRemoval(mijnDemoRules_1);
		aantal = mijnDemoRules_1.size();
		for (int i = 0; i < aantal; i++) {
			assertEquals(mijnDemoRules_1.get(i).getRuleNr(), i + 1);
		}
		System.out.println("Na verwijdering");
		printRuleSet(mijnDemoRules_1);
		DeniedAndAllowedSpace spaceNa = ruimte.getDeniedAndAllowedSpace(mijnDemoRules_1);
		assertTrue(spaceVoor.getAllowedBDD().equals(spaceNa.getAllowedBDD()));
		assertTrue(spaceVoor.getDeniedBDD().equals(spaceNa.getDeniedBDD()));

	}

	/**
	 * Test methode makeEnumerationBDD van klasse Space.
	 * 
	 * @throws CustomException
	 *             Wordt opgegooid als er een Exception gebeurd bij deze testen.
	 */
	 /**
	 * @throws CustomException
	 */
	@Test
	public void testMakeEnumerationBDD() throws CustomException {
		// test tevens de klassen: OpsommingBDD, RegelBDD
		// en methodes public boolean testOpsommingBDD(BDD bronBDD, OpsommingBDD
		// opsommingBDD)

		
		 DemoRulesOpsomming demoRules = new DemoRulesOpsomming();
		 ArrayList<Rule> mijnRules = demoRules.getDemoRules(); 
		 Space ruimte = new Space(mijnRules);
		 
		 ArrayList<Segment> segmenten = ruimte.getSegments(); 
		 EnumerationBDD opsommingBDD; 
		 
		 for (Segment segment : segmenten) 
		 { 
			 int aantal = (int) ruimte.getNumberOfRulesOfBDD(segment.getSegmentBDD());
			 System.out.println("Segment : " + segment.getSegmentNumber());
		 
			 if (aantal < 400) 
			 { 
				 System.out.println("aantal regels minder dan 400 : " + aantal);
				 opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), false);
				 assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				 
				 // ook compact
				 System.out.println("aantal regels minder dan 400, maar toch compact : " + aantal);
				 opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), true);
				 assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				// Space.printEnumerationBDD(opsommingBDD); 
			 } 
			 else 
			 {
				 // alleen compact
				 System.out.println("aantal regels hoger dan 400, dus alleen compact : " + aantal);
				 opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), true);
				 assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				// Space.printEnumerationBDD(opsommingBDD); 
				 
			 }
		 
		 } 
		
		DemoRulesGroup demoRulesGroup = new DemoRulesGroup();
		ArrayList<Rule> mijnRules_even = demoRulesGroup.getDemoRules();
		mijnRules = new ArrayList<Rule>();

		for (int i = 0; i < 25; i++) {
			mijnRules.add(mijnRules_even.get(i));
		}

		ruimte = new Space(mijnRules);

		segmenten = ruimte.getSegments();
	
		for (Segment segment : segmenten) {
			int aantal = (int) ruimte.getNumberOfRulesOfBDD(segment.getSegmentBDD());
			System.out.println("Segment : " + segment.getSegmentNumber());

			if (aantal < 400) {
			//	System.out.println("aantal regels verwerkbaar : " + aantal);
				opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), false);
				assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), true);
				assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
			//	Space.printEnumerationBDD(opsommingBDD);
			} 
		}

		// compacte weergave
		System.out.println("Compacte weergave : ");
		boolean compact = true;
		for (Segment segment : segmenten) 
		{
			int aantal = (int) ruimte.getNumberOfRulesOfBDD(segment.getSegmentBDD());
			System.out.println("Segment : " + segment.getSegmentNumber());
			// if ((segment.getSegmentNumber() == 1 )||(segment.getSegmentNumber() == 2 )||(segment.getSegmentNumber() == 3 )||(segment.getSegmentNumber() == 4 ))
			// {
			if (aantal < 400 || compact) 
			{
				System.out.println("aantal regels verwerkbaar : " + aantal);
				opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), true);
				System.out.println("EnumerationBDD aantal: " + opsommingBDD.getSize());
				assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(),opsommingBDD));
				Space.printEnumerationBDD(opsommingBDD);

			} else {
				System.out.println("aantal regels te hoog : " + aantal);
			}
			// }
		}
		// voor demorules
		 DemoRules demoRules1 = new DemoRules();
		 mijnRules = demoRules1.getDemoRules(); 
		 ruimte = new Space(mijnRules);
		 
		 segmenten = ruimte.getSegments(); 
		 
		 for (Segment segment : segmenten) 
		 { 
			 int aantal = (int) ruimte.getNumberOfRulesOfBDD(segment.getSegmentBDD());
			// System.out.println("Segment : " + segment.getSegmentNumber());
		 
			 if (aantal < 400) 
			 { 
			//	 System.out.println("aantal regels minder dan 400 : " + aantal);
				 opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), false);
				 assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				 
				 // ook compact
			//	 System.out.println("aantal regels minder dan 400, maar toch compact : " + aantal);
				 opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), true);
				 assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				// Space.printEnumerationBDD(opsommingBDD); 
			 } 
			 else 
			 {
				 // alleen compact
			//	 System.out.println("aantal regels hoger dan 400, dus alleen compact : " + aantal);
				 opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), true);
				 assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
				// Space.printEnumerationBDD(opsommingBDD); 
				 
			 }
		 
		 } 

	}

	/**
	 * Test methode tryConflictSolution van klasse Space.
	 * 
	 * @throws CustomException
	 *             Wordt opgegooid als er een Exception optreedt bij deze
	 *             testen.
	 */
	@Test
	public void testTryConflictSolution() throws CustomException {
		System.out.println("in testTryOplossingConflict()");
		DemoRulesGroup demoRulesObject = new DemoRulesGroup();
		ArrayList<Rule> demoRules = demoRulesObject.getDemoRules();
		// ArrayList< Rule > mijnDemoRules = new ArrayList<Rule>();
		// System.out.println("aantal demorules " + demoRules.size());
		Space ruimte = new Space(demoRules);
		SetOfGroups setOfGroups = ruimte.createCorrelationGroups();

		int aantalGroepen = setOfGroups.getNumberOfGroups();
		// System.out.println("aantal groepen : " + aantalGroepen);
		assertEquals(1, aantalGroepen);
		CorrelationGroup group = setOfGroups.getGroup(0);
		ArrayList<Boolean> aConstr = new ArrayList<Boolean>();
		ConflictSolution oplossingConflict;

		// opl 1
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.NO_REORDERING_REQUIRED, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 2
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false); // blijft
																				// te
																				// complex
		assertEquals(ConflictSolution.Status.TO_COMPLEX, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);

		// opl 3
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 4
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		if (oplossingConflict.getStatus() == ConflictSolution.Status.SOLUTION_FOUND) {
			ArrayList<Integer> voor = oplossingConflict.getInvolvedRuleNrs();
			int aantal = voor.size();
			System.out.print("voor : ");
			for (int i = 0; i < aantal; i++) {
				System.out.print(" " + voor.get(i));
			}
			System.out.println();
			ArrayList<Integer> na = oplossingConflict.getSolutionRuleNrs();
			aantal = na.size();
			System.out.print("na : ");
			for (int i = 0; i < aantal; i++) {
				System.out.print(" " + na.get(i));
			}
			System.out.println();

			// toon regelset voor
			printRuleSet(demoRules);
			oplossingConflict.commitSolution(demoRules);
			System.out.println("Regelset na doorvoeren :");
			printRuleSet(demoRules);

		}

		// test regelnummers
		int aantal2 = demoRules.size();
		for (int i = 0; i < aantal2; i++) {
			assertEquals(demoRules.get(i).getRuleNr(), i + 1);
		}

		// opl 5
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 6
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 7
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 8
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 9
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false); // kijken
		System.out.println("resultaat : " + oplossingConflict.getStatus());
		assertEquals(ConflictSolution.Status.TO_COMPLEX, oplossingConflict.getStatus()); // tijdelijk
																							// aangepast
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);

		// opl 10
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 11
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false); // kijken
		System.out.println("resultaat test 11: " + oplossingConflict.getStatus());
		assertEquals(ConflictSolution.Status.TO_COMPLEX, oplossingConflict.getStatus()); // tijdelijk
																							// aangepast
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);

		// opl 12
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		// oplossingConflict.oplossingDoorvoeren(demoRules);
		// testRegelnummers(oplossingConflict, demoRules );
		// ruimte = new Ruimte(demoRules);
		// setOfGroups = ruimte.createCorrelationGroups();
		// group = setOfGroups.getGroup(0);
		// oplossingConflict = ruimte.tryOplossingConflict(group, aConstr,
		// false);
		// assertEquals(OplossingConflict.GEEN_HERSCHIKKING_VEREIST,
		// oplossingConflict.getStatus());

		if (oplossingConflict.getStatus() == ConflictSolution.Status.SOLUTION_FOUND) {
			ArrayList<Integer> voor = oplossingConflict.getInvolvedRuleNrs();
			int aantal = voor.size();
			System.out.print("voor : ");
			for (int i = 0; i < aantal; i++) {
				System.out.print(" " + voor.get(i));
			}
			System.out.println();
			ArrayList<Integer> na = oplossingConflict.getSolutionRuleNrs();
			aantal = na.size();
			System.out.print("na : ");
			for (int i = 0; i < aantal; i++) {
				System.out.print(" " + na.get(i));
			}
			System.out.println();

			// toon regelset voor
			printRuleSet(demoRules);
			oplossingConflict.commitSolution(demoRules);
			System.out.println("Regelset na doorvoeren :");
			printRuleSet(demoRules);

		}
		controleerOplossing(group, ruimte, aConstr);

		// opl 13
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);

		// opl 14
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);
		/*
		 * ruimte = new Space(demoRules); setOfGroups =
		 * ruimte.createCorrelationGroups(); group = setOfGroups.getGroup(0);
		 * oplossingConflict = ruimte.tryConflictSolution(group, aConstr,
		 * false); assertEquals(ConflictSolution.Status.NO_REORDERING_REQUIRED,
		 * oplossingConflict.getStatus());
		 */

		// test 15
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false); // kijken
		System.out.println("resultaat test 15: " + oplossingConflict.getStatus());
		assertEquals(ConflictSolution.Status.TO_COMPLEX, oplossingConflict.getStatus()); // tijdelijk
																							// aangepast
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);

		// test 16
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);
		/*
		 * ruimte = new Space(demoRules); setOfGroups =
		 * ruimte.createCorrelationGroups(); group = setOfGroups.getGroup(0);
		 * oplossingConflict = ruimte.tryConflictSolution(group, aConstr,
		 * false); assertEquals(ConflictSolution.Status.NO_REORDERING_REQUIRED,
		 * oplossingConflict.getStatus());
		 */

		// test 17
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);
		/*
		 * ruimte = new Space(demoRules); setOfGroups =
		 * ruimte.createCorrelationGroups(); group = setOfGroups.getGroup(0);
		 * oplossingConflict = ruimte.tryConflictSolution(group, aConstr,
		 * false); assertEquals(ConflictSolution.Status.NO_REORDERING_REQUIRED,
		 * oplossingConflict.getStatus());
		 */

		// test 18
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);
		/*
		 * ruimte = new Space(demoRules); setOfGroups =
		 * ruimte.createCorrelationGroups(); group = setOfGroups.getGroup(0);
		 * oplossingConflict = ruimte.tryConflictSolution(group, aConstr,
		 * false); assertEquals(OplossingConflict.GEEN_HERSCHIKKING_VEREIST,
		 * oplossingConflict.getStatus());
		 */

		// opl 19
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(false);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.SOLUTION_FOUND, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);
		controleerOplossing(group, ruimte, aConstr);
		/*
		 * ruimte = new Space(demoRules); setOfGroups =
		 * ruimte.createCorrelationGroups(); group = setOfGroups.getGroup(0);
		 * oplossingConflict = ruimte.tryConflictSolution(group, aConstr,
		 * false); assertEquals(OplossingConflict.GEEN_HERSCHIKKING_VEREIST,
		 * oplossingConflict.getStatus());
		 */

		// opl 20
		demoRules.clear();
		demoRules = demoRulesObject.getDemoRules();
		ruimte = new Space(demoRules);
		setOfGroups = ruimte.createCorrelationGroups();
		group = setOfGroups.getGroup(0);
		aConstr.clear();
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		aConstr.add(true);
		oplossingConflict = ruimte.tryConflictSolution(group, aConstr, false);
		assertEquals(ConflictSolution.Status.ERROR_IN_INPUT, oplossingConflict.getStatus());
		oplossingConflict.commitSolution(demoRules);
		testRuleNumbers(oplossingConflict, demoRules);

	}

	/**
	 * Test methode showScope van klasse Space.
	 * 
	 * @throws CustomException
	 *             Wordt opgegooid als er een fout gebeurd bij deze testen.
	 */
	@Test
	public void testShowScope() throws CustomException {
		DemoRulesGroup demoRulesObject = new DemoRulesGroup();
		DemoRulesGroup demoRulesObject2 = new DemoRulesGroup();
		ArrayList<Rule> demoRules = demoRulesObject.getDemoRules(); // ruleset
		// van 36
		// regels
		printRuleSet(demoRules);
		ArrayList<Rule> nieuweRules = demoRulesObject2.getDemoRules(); // kies
		// hieruit
		// een
		// rule
		// als
		// nieuwe
		// rule
		// om te
		// testen
		Space ruimte = new Space(demoRules);
		Rule nieuweRule;

		// regel 5 geeft conflict met regel 2
		nieuweRule = nieuweRules.get(4);
		assertEquals(1, ruimte.showScope(nieuweRule));
		// regel 6 geeft conflict met regel 36
		nieuweRule = nieuweRules.get(5);
		assertEquals(35, ruimte.showScope(nieuweRule));

		nieuweRule = nieuweRules.get(3);
		assertEquals(2, ruimte.showScope(nieuweRule));
		/*
     
		 */
		// alleen action anders rule_29 was normaal gesproken overal zichtbaar

		IPPattern bronIP = new IPPattern(40, 1, 1, 0, 24);
		PortPattern bronPort = new PortPattern(-1, -1);
		IPPattern doelIP = new IPPattern(220, 210, 10, 8, 32);
		PortPattern doelPort = new PortPattern(5, 8);

		nieuweRule = new Rule(29, 6, bronIP, bronPort, doelIP, doelPort, false);
		// nieuweRule = new Rule(29, 6, 40, 1, 1, 0, 24, -1, -1, 220,
		// 210, 10, 8, 32, 5, 8, false);
		assertEquals(19, ruimte.showScope(nieuweRule));

		// regel die nergens conflict geeft
		nieuweRule = nieuweRules.get(29);
		assertEquals(36, ruimte.showScope(nieuweRule));

		nieuweRule = nieuweRules.get(26);
		assertEquals(36, ruimte.showScope(nieuweRule));
		// regelset is leeg
		demoRules.clear();
		nieuweRule = nieuweRules.get(4);
		assertEquals(0, ruimte.showScope(nieuweRule));

	}

	@Test
	public void testConflictStatus() throws CustomException {
		// maak config en test of alles unknown is
		ConflictStatus conflictStatus = new ConflictStatus();

		DemoRulesGroup demoRulesGroup = new DemoRulesGroup();
		ArrayList<Rule> mijnRules_even = demoRulesGroup.getDemoRules();
		ArrayList<Rule> mijnRules = new ArrayList<Rule>();

		for (int i = 0; i < 25; i++) {
			mijnRules.add(mijnRules_even.get(i));
		}

		Space ruimte = new Space(mijnRules);
		ArrayList<Segment> segmenten = ruimte.getSegments();
		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(0, conflictStatus.getSizeAllowed());
		assertEquals(0, conflictStatus.getSizeDenied());

		// maak nieuwe ruimte en alles moet UNKNOWN blijven bahalve 2 en 3 en 9,
		// 10 en 16,17,30
		ruimte = new Space(mijnRules);
		segmenten = ruimte.getSegments();
		conflictStatus.setStatus(segmenten.get(1), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(2), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(8), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(9), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(15), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(16), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(29), ConflictStatus.Status.APPROVED_YES);

		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(2, conflictStatus.getSizeAllowed());
		assertEquals(5, conflictStatus.getSizeDenied());

		// maak nieuwe ruimte en alles moet UNKNOWN blijven bahalve 2 en 7 en 11
		// en 12
		ruimte = new Space(mijnRules);
		segmenten = ruimte.getSegments();
		conflictStatus.setStatus(segmenten.get(1), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(2), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(8), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(9), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(15), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(16), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(29), ConflictStatus.Status.APPROVED_NO);

		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(2, conflictStatus.getSizeAllowed());
		assertEquals(3, conflictStatus.getSizeDenied());


		ruimte = new Space(mijnRules);
		segmenten = ruimte.getSegments();
		conflictStatus.setStatus(segmenten.get(1), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(2), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(8), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(9), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(15), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(16), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(29), ConflictStatus.Status.APPROVED_NO);

		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(2, conflictStatus.getSizeAllowed());
		assertEquals(3, conflictStatus.getSizeDenied());


		ruimte = new Space(mijnRules);
		segmenten = ruimte.getSegments();
		conflictStatus.setStatus(segmenten.get(1), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(2), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(8), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(9), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(15), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(16), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(22), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(23), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(29), ConflictStatus.Status.APPROVED_YES);
		conflictStatus.setStatus(segmenten.get(30), ConflictStatus.Status.APPROVED_YES);

		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(5, conflictStatus.getSizeAllowed());
		assertEquals(5, conflictStatus.getSizeDenied());


		ruimte = new Space(mijnRules);
		segmenten = ruimte.getSegments();
		conflictStatus.setStatus(segmenten.get(1), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(2), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(8), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(9), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(15), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(16), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(22), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(23), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(29), ConflictStatus.Status.APPROVED_NO);
		conflictStatus.setStatus(segmenten.get(30), ConflictStatus.Status.APPROVED_NO);

		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(5, conflictStatus.getSizeAllowed());
		assertEquals(5, conflictStatus.getSizeDenied());


		ruimte = new Space(mijnRules);
		segmenten = ruimte.getSegments();
		conflictStatus.setStatus(segmenten.get(1), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(2), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(8), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(9), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(15), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(16), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(22), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(23), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(29), ConflictStatus.Status.APPROVED_UNKNOWN);
		conflictStatus.setStatus(segmenten.get(30), ConflictStatus.Status.APPROVED_UNKNOWN);

		System.out.println();
		for (Segment segment : segmenten) {
			if (segment.isOverlapping() && segment.getConflicting()) {
				System.out.println("Segment : " + segment.getSegmentNumber() + " " + conflictStatus.getStatus(segment) + " " + segment.getRule(0).getAction());
			}
		}
		// nu de ArrayLists bekijken
		conflictStatus.printArrayLists();
		
		assertEquals(0, conflictStatus.getSizeAllowed());
		assertEquals(0, conflictStatus.getSizeDenied());

	}

	/**
	 * Test de regel nummering voor en na het oplossen van opgegeven
	 * configuratie conflict.
	 * 
	 * @param oplossingConflict
	 *            Beschouwd configuratie conflict.
	 * @param demoRules
	 *            Te testen regels.
	 */
	private static void testRuleNumbers(ConflictSolution oplossingConflict, ArrayList<Rule> demoRules) {
		int aantal;
		/*
		 * // testen als de nummers nog NIET zijn aangepast if
		 * (oplossingConflict.getStatus() ==
		 * OplossingConflict.OPLOSSING_GEVONDEN) {
		 * oplossingConflict.oplossingDoorvoeren(demoRules);
		 * 
		 * ArrayList<Integer> voor = oplossingConflict.getBetrokkenRegelNrs();
		 * ArrayList<Integer> na = oplossingConflict.getOplossingRegelNrs();
		 * aantal = voor.size(); int positie; for (int i = 0; i < aantal; i++ )
		 * { positie = voor.get(i)-1;
		 * assertEquals(demoRules.get(positie).getIndex(),(int) na.get(i) ); } }
		 */

		// testen als de nummers WEL zijn aangepast
		aantal = demoRules.size();
		for (int i = 0; i < aantal; i++) {

			assertEquals(demoRules.get(i).getRuleNr(), i + 1);
		}

	}

	/**
	 * Controleer oplossing van opgegeven correlatie groep.
	 * 
	 * @param group
	 *            Beschouwde correlatie groep.
	 * @param ruimte
	 *            Beschouwde ruimte.
	 * @param aConstr
	 *            Verwachte waarden van acties binnen deze test.
	 * @throws CustomException
	 *             Wordt opgegooid indien er een Exception optreedt bij het
	 *             uitvoeren van deze test.
	 */
	private static void controleerOplossing(CorrelationGroup group, Space ruimte, ArrayList<Boolean> aConstr) throws CustomException {
		int ruleIndex;
		int indexActionConstraint = 0;
		int aantal = group.getSize();
		for (int i = 0; i < aantal; i++) {
			Segment segment = group.getSegment(i);
			if (segment.isOverlapping() && segment.getConflicting()) {
				ruleIndex = ruimte.matchRuleNr(segment);
				assertEquals(aConstr.get(indexActionConstraint), ruimte.getRules().get(ruleIndex).getAction());
				indexActionConstraint++;
			}
		}
	}

	/**
	 * Print de waarden van opgegeven regels als tekst naar standaard uitvoer.
	 * 
	 * @param rules
	 *            Af te printen regels.
	 */
	private static void printRuleSet(ArrayList<Rule> rules) {
		for (Rule rule : rules) {
			System.out.println(rule.getRuleNr() + "\t" + rule.getProtocol() + "\t" + rule.getSourceIP().getNumber1() + "." + rule.getSourceIP().getNumber2() + "." + rule.getSourceIP().getNumber3()
					+ "." + rule.getSourceIP().getNumber4() + "\t" + rule.getSourceIP().getMask() + "\t" + rule.getSourcePort().getLowerBound() + "\t" + rule.getSourcePort().getUpperBound() + "\t"
					+ rule.getDestinationIP().getNumber1() + "." + rule.getDestinationIP().getNumber2() + "." + rule.getDestinationIP().getNumber3() + "." + rule.getDestinationIP().getNumber4()
					+ "\t" + rule.getDestinationIP().getMask() + "\t" + rule.getDestinationPort().getLowerBound() + "\t" + rule.getDestinationPort().getUpperBound() + "\t" + rule.getAction());

		}
	}

}

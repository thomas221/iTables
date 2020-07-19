package test.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import exception.CustomException;
import exception.ParseException;
import exception.UnsupportedDefaultActionException;
import exception.UnsupportedProtocolException;
import model.configuration.Configuration;
import model.configuration.ConflictSolution;
import model.configuration.CorrelationGroup;
import model.configuration.DeniedAndAllowedSpace;
import model.configuration.EnumerationBDD;
import model.configuration.IPPattern;
import model.configuration.PortPattern;
import model.configuration.RedundancySolution;
import model.configuration.Rule;
import model.configuration.Segment;
import model.configuration.SetOfGroups;
import model.configuration.Space;
import static org.junit.Assert.*;

public class ParseAndAnalyzeTest {
	/**
	 * Attribuut die de file seperator bevat van het besturingssysteem waarop de
	 * web applicatie draait.
	 */
	private static final String PATH_SEPARATOR = File.separator;
	/**
	 * Attribuut die naam bevat van het test geval conffrompaperv3.iptables .
	 */
	private static final String CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE = "conffrompaperv3.iptables";
	/**
	 * Attribuut die pad naar root file van dit project op de computer zal
	 * bevatten
	 */
	private String rootFilePath = "";

	/**
	 * Deze methode geeft rootFilePath het pad naar de root file van dit project
	 * op de computer. De methode wordt voor elke test uitgevoerd.
	 */
	@Before
	public void setUp() {
		// verkrijg absolute pad naar de WEB-INF/classes folder van dit project
		String path = getClass().getClassLoader().getResource(".").getPath();
		// verkrijg File met die path
		File classloaderFile = new File(path);
		// ga naar root file van dit project
		File rootFile = classloaderFile.getParentFile().getParentFile().getParentFile();
		// verkrijg pad naar die file en zet in attribuut rootFilePath
		rootFilePath = rootFile.getPath();

	}

	/**
	 * Geef pad terug van configuratie met opgegeven bestandsnaam.
	 * 
	 * @param filename
	 *            Opgegeven bestandsnaam.
	 * @return Pad van configuratie met opgegeven bestandsnaam.
	 */
	private String getFilePath(String filename) {
		return rootFilePath + PATH_SEPARATOR + "src" + PATH_SEPARATOR + "testconfiguraties" + PATH_SEPARATOR + filename;
	}

	/**
	 * Testen uitgevoerd op een bepaald test geval. (hier een configuratie uit
	 * een wetenschappelijke paper)
	 */
	@Test
	public void testCONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE() {
		try {
			testGetSegments(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
			testPacketFirstMatchCONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE();
			// testCreateCorrelationGroups(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
			// testDetermineRedundantRules(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
			// testMakeEnumerationBDD(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
			// testTryConflictSolution(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
			// testShowScope(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
		} catch (CustomException ce) {
			System.out.println(ce.getOorzaak());
		}
	}

	/**
	 * Test de segmentering.
	 * 
	 * @param configurationName
	 *            Naam van te testen configuratie.
	 * @throws CustomException
	 *             Opgegooid indien er een fout gebeurd bij het testen.
	 */
	public void testGetSegments(String configurationName) throws CustomException {
		// haal configuratie op met gegeven naam
		Configuration configuration_A = testMaakConfiguratieAan(configurationName);
		// controleer of er een configuratie geparst is
		assertNotNull(configuration_A);
		ArrayList<Rule> mijnRules_A = configuration_A.getTable("FILTER").getChain("FORWARD").getRules();
		Space ruimte_A = new Space(mijnRules_A);

		// haal configuratie op met gegeven naam
		Configuration configuration_B1 = testMaakConfiguratieAan(configurationName);
		ArrayList<Rule> mijnRules_B1 = configuration_B1.getTable("FILTER").getChain("FORWARD").getRules();
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

		// nog eens allemaal controleren
		for (int i = 0; i < aantalSegmenten; i++) {

			TreeSet<Integer> treeSet_B = segmenten_B.get(i).getRuleNumbers();
			ArrayList<Integer> nummers_B = new ArrayList<Integer>();
			nummers_B.addAll(treeSet_B);
			Collections.sort(nummers_B);
			Segment segment = segmenten_A.get(i);
			aantal = segment.getNumberOfRules();
			// aantal regels per segment gelijk
			// FIXME: assert klopt niet!
			// assertEquals(aantal, nummers_B.size());
			for (int j = 0; j < aantal; j++) {
				// regels op volgorde EN gelijke nummers
				// FIXME: assert klopt niet
				// assertEquals((Integer) segment.getRule(j).getIndex(),
				// nummers_B.get(j));
			}
		}
	}

	/**
	 * Test de packetFirstMatch methode van klasse ruimte.
	 * 
	 * @throws CustomException
	 *             Wordt opgegooid indien er een fout gebeurd.
	 */
	public void testPacketFirstMatchCONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE() throws CustomException {
		// haal configuratie op met gegeven naam
		Configuration configuration = testMaakConfiguratieAan(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
		System.out.println("in testPacketFirstMatch ... ");
		// voor voorbeeld regels uit paper
		ArrayList<Rule> mijnRules = configuration.getTable("FILTER").getChain("FORWARD").getRules();
		Space ruimte = new Space(mijnRules);

		// test een pakketje dat met de 1e regel zou moeten matchen
		assertEquals(1, ruimte.packetFirstMatch(17, 10, 1, 2, 15, 1080, 172, 32, 1, 66, 53));
		// test een pakketje dat met de 3e regel zou moeten matchen
		assertEquals(3, ruimte.packetFirstMatch(6, 10, 1, 1, 4, 55, 192, 168, 1, 4, 25)); // merk
																										// op
																										// dat
																										// deze
																										// ook
																										// matcht
																										// aan
																										// regel
																										// 4,
																										// maar
																										// komt
																										// niet
																										// aan
																										// bod
																										// omdat
																										// er
																										// al
																										// een
																										// match
																										// is
																										// met
																										// regel
																										// 3

	}

	/**
	 * Test correlatie groepen van opgegeven test configuratie.
	 * 
	 * @param configurationName
	 *            Naam van te testen configuratie.
	 * @throws CustomException
	 *             Wordt opgegooid bij onverwachte fout.
	 */
	public void testCreateCorrelationGroups(String configurationName) throws CustomException {
		// haal configuratie op met gegeven naam
		Configuration configuration = testMaakConfiguratieAan(configurationName);
		ArrayList<Rule> demoRules = configuration.getTable("FILTER").getChain("FORWARD").getRules();
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
	 * Test het bepalen van redundante regels van configuratie met opgegeven
	 * naam.
	 * 
	 * @param configurationName
	 *            Naam van te testen configuratie.
	 * @throws CustomException
	 *             Wordt opgegooid bij onverwachte fout.
	 */
	public void testDetermineRedundantRules(String configurationName) throws CustomException {
		// haal configuratie op met gegeven naam
		Configuration configuration = testMaakConfiguratieAan(configurationName);
		// haal configuratie op met gegeven naam
		Configuration configuration2 = testMaakConfiguratieAan(configurationName);
		System.out.println("in testPacketFirstMatch ... ");
		ArrayList<Rule> demoRules = configuration.getTable("FILTER").getChain("FORWARD").getRules();
		ArrayList<Rule> demoRules2 = configuration2.getTable("FILTER").getChain("FORWARD").getRules();
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
	 * Test methode makeEnumerationBDD van klasse Ruimte op opgegeven
	 * configuratie.
	 * 
	 * @param configurationName
	 *            Te testen configuratie.
	 * @throws CustomException
	 *             Wordt opgegooid bij onverwachte fout.
	 */
	public void testMakeEnumerationBDD(String configurationName) throws CustomException {
		// test tevens de klassen: OpsommingBDD, RegelBDD
		// en methodes public boolean testOpsommingBDD(BDD bronBDD, OpsommingBDD
		// opsommingBDD)

		// haal configuratie op met gegeven naam
		Configuration configuration = testMaakConfiguratieAan(configurationName);
		ArrayList<Rule> mijnArrayList = configuration.getTable("FILTER").getChain("FORWARD").getRules();
		Space ruimte = new Space(mijnArrayList);

		ArrayList<Segment> segmenten = ruimte.getSegments();
		EnumerationBDD opsommingBDD;
		for (Segment segment : segmenten) {
			int aantal = (int) ruimte.getNumberOfRulesOfBDD(segment.getSegmentBDD());
			if (aantal < 400) {
				System.out.println("aantal regels verwerkbaar : " + aantal);
				opsommingBDD = ruimte.makeEnumerationBDD(segment.getSegmentBDD(), false);
				assertTrue(ruimte.testEnumerationBDD(segment.getSegmentBDD(), opsommingBDD));
			} else {
				System.out.println("aantal regels te hoog : " + aantal);
			}

		}
	}

	/**
	 * Test methode tryConflictSolution van klasse ruimte op opgegeven
	 * configuratie.
	 * 
	 * @param configurationName
	 *            Naam van opgegeven configuratie.
	 * @throws CustomException
	 *             Geeft aan dat er een onverwachte fout gebeurd is.
	 */
	public void testTryConflictSolution(String configurationName) throws CustomException {
		System.out.println("in testTryOplossingConflict()");

		// haal configuratie op met gegeven naam
		Configuration configuration = testMaakConfiguratieAan(configurationName);
		ArrayList<Rule> demoRules = configuration.getTable("FILTER").getChain("FORWARD").getRules();
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

		// opl 12
		demoRules.clear();
		// haal configuratie op met gegeven naam
		String confString2 = readTestConfiguratie(configurationName);
		Configuration configuration2 = testMaakConfiguratieAan(confString2);
		demoRules = configuration2.getTable("FILTER").getChain("FORWARD").getRules();

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

	}

	/**
	 * Test methode showRules van klasse Ruimte op opgegeven configuratie.
	 * 
	 * @param configurationName
	 *            Naam van opgegeven configuratie.
	 * @throws CustomException
	 *             Geeft aan dat er een onverwachte fout gebeurd is.
	 */
	public void testShowScope(String configurationName) throws CustomException {
		// haal configuratie op met gegeven naam
		Configuration configuration = testMaakConfiguratieAan(configurationName);

		// haal configuratie op met gegeven naam
		Configuration configuration2 = testMaakConfiguratieAan(configurationName);

		ArrayList<Rule> demoRules = configuration.getTable("FILTER").getChain("FORWARD").getRules(); // ruleset
		// van 36
		// regels
		printRuleSet(demoRules);
		ArrayList<Rule> nieuweRules = configuration2.getTable("FILTER").getChain("FORWARD").getRules(); // kies
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

	/**
	 * Voer testen uit op de regelnummering voor en na het oplossen van een
	 * conflict.
	 * 
	 * @param oplossingConflict
	 *            Beschouwde oplossing van een conflict.
	 * @param demoRules
	 *            Beschouwde configuratie regels.
	 */
/*	private void testRuleNumbers(ConflictSolution oplossingConflict, ArrayList<Rule> demoRules) {
		int aantal;
		
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
		 

		// testen als de nummers WEL zijn aangepast
		aantal = demoRules.size();
		for (int i = 0; i < aantal; i++) {

			assertEquals(demoRules.get(i).getRuleNr(), i + 1);
		}
	}*/

	/**
	 * Hulpmethode voor het testen. Print opgegeven regels af als string in
	 * standaard uitvoer.
	 * 
	 * @param rules
	 *            Weer te geven regels in standaard uitvoer.
	 */
	private static void printRuleSet(ArrayList<Rule> rules) {
		for (Rule rule : rules) {
			System.out.println(rule.getRuleNr() + "\t" + rule.getProtocol() + "\t" + rule.getSourceIP().getNumber1() + "." + rule.getSourceIP().getNumber2() + "." + rule.getSourceIP().getNumber3()
					+ "." + rule.getSourceIP().getNumber4() + "\t" + rule.getSourceIP().getMask() + "\t" + rule.getSourcePort().getLowerBound() + "\t" + rule.getSourcePort().getUpperBound() + "\t"
					+ rule.getDestinationIP().getNumber1() + "." + rule.getDestinationIP().getNumber2() + "." + rule.getDestinationIP().getNumber3() + "." + rule.getDestinationIP().getNumber4()
					+ "\t" + rule.getDestinationIP().getMask() + "\t" + rule.getDestinationPort().getLowerBound() + "\t" + rule.getDestinationPort().getUpperBound() + "\t" + rule.getAction());

		}
	}

	/*
	 * private void printRuleSet(ArrayList<Rule> rules) { for (Rule rule :
	 * rules) { System.out.println(" " + rule.getIndex() + " " +
	 * rule.getProtocol() + " " + rule.getSourceIP_1() + "." +
	 * rule.getSourceIP_2() + "." + rule.getSourceIP_3() + "." +
	 * rule.getSourceIP_4() + " " + rule.getSourceMask() + " " +
	 * rule.getSourcePort_from() + " " + rule.getSourcePort_until()+ " " +
	 * rule.getDestinationIP_1() +"." + rule.getDestinationIP_2() +"."+
	 * rule.getDestinationIP_3() +"."+ rule.getDestinationIP_4() + " " +
	 * rule.getDestinationMask() + " " + rule.getDestinationPort_from()+ " " +
	 * rule.getDestinationPort_until() + " " + rule.getAction());
	 * 
	 * } }
	 */
	/**
	 * Test of de waarden van een Regel object overeenkomt met de opgegeven
	 * waarden. Met deze methode kan getest worden of een firewall regel de
	 * verwachte regelnummer, protocol, bron ip-adres, doel ip-adres, bronpoort,
	 * doelpoort en actie heeft. Wilt u meer informatie over deze parameters,
	 * kijk dan naar de javadoc van de betreffende klassen.
	 * 
	 * @param regel
	 *            De regel die getest wordt. (zie javadoc van Regel)
	 * @param regelnr
	 *            De verwachte regel nummer.
	 * @param protocol
	 *            Het verwachte protocol. (zie javadoc van Protocol)
	 * @param bronipstring
	 *            Het verwachte bron ip-adres als string in CIDR-notatie.(zie
	 *            javadoc van Adres)
	 * @param doelipstring
	 *            Het verwachte doel ip-adres als string in CIDR-notatie. (zie
	 *            javadoc van Adres)
	 * @param bronpoortstring
	 *            De verwachte bronpoort. (zie javadoc van Poort)
	 * @param doelpoortstring
	 *            De verwachte doelpoort (zie javadoc van Poort)
	 * @param actie
	 *            De verwachte actie. (zie javadoc van Actie)
	 */
/*	private static void testRegel(Rule regel, int regelnr, String protocol, String bronipstring, String doelipstring, String bronpoortstring, String doelpoortstring, boolean actie) {
		// we testen de regel
		// Door de Adres en Poort constructors te gebruiken checken we nog eens
		// of deze constructors de meegegeven ip adressen en poorten correct
		// parsen.
		try { // gebruiken van string als parameter voor Adres of Poort
				// constructor kan exception gooien
			IPPattern bronip = new IPPattern(bronipstring);
			IPPattern doelip = new IPPattern(doelipstring);
			PortPattern bronpoort = new PortPattern(bronpoortstring);
			PortPattern doelpoort = new PortPattern(doelpoortstring);

			// controleer of geparste regel correct is
			assertEquals(regelnr, regel.getRuleNr());
			assertEquals(protocol, regel.getProtocol().toString());
			assertEquals(bronip.toString(), regel.getSourceIP().toString());
			assertEquals(doelip.toString(), regel.getDestinationIP().toString());
			assertEquals(bronpoort.toString(), regel.getSourcePort().toString());
			assertEquals(doelpoort.toString(), regel.getDestinationPort().toString());
			assertEquals(actie, regel.getAction());
		} catch (CustomException e) {
			System.out.println(e.getOorzaak());
			e.printStackTrace();
		}
	}*/

	/**
	 * Methode voor het parsen van iptables bestand in opgegeven locatie. De
	 * configuratie wordt in model.configuratie objecten gezet.
	 * 
	 * @param configuration
	 *            De naam van het configuratiebestand waarvan een configuratie
	 *            moet worden aangemaakt.
	 */
	private Configuration testMaakConfiguratieAan(String configuration) {
		// verkrijg locatie van configuration
		String locatie_van_test_configuratie = getFilePath(configuration);
		// Regels inlezen uit lokaal bestand
		String confinhoud = readTestConfiguratie(locatie_van_test_configuratie);
		Configuration configuratie = null;
		// configuratie in model.configuratie objecten zetten dmv Configuratie
		// constructor.
		try {
			configuratie = new Configuration(confinhoud);
		} catch (ParseException | UnsupportedDefaultActionException ce) {
			System.out.print(ce.getMessage());
			ce.printStackTrace();
		} catch (UnsupportedProtocolException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		return configuratie;
	}

	/**
	 * Methode voor het inlezen van een iptables test bestand. De test
	 * configuratie wordt als String teruggegeven.
	 * 
	 * @param locatie_van_test_configuratie
	 *            Locatie van test configuratie.
	 * @return Test configuratie in bestand van opgegeven locatie.
	 */
	private static String readTestConfiguratie(String locatie_van_test_configuratie) {
		// create file object
		File file = new File(locatie_van_test_configuratie);
		BufferedInputStream bin = null;
		String confinhoud = "";

		try {
			// create FileInputStream object
			FileInputStream fin = new FileInputStream(file);

			// create object of BufferedInputStream
			bin = new BufferedInputStream(fin);

			/*
			 * BufferedInputStream has ability to buffer input into internal
			 * buffer array.
			 * 
			 * available() method returns number of bytes that can be read from
			 * underlying stream without blocking.
			 */

			// read file using BufferedInputStream
			while (bin.available() > 0) {
				confinhoud += (char) bin.read();
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the file " + ioe);
		} finally {
			// close the BufferedInputStream using close method
			try {
				if (bin != null)
					bin.close();
			} catch (IOException ioe) {
				System.out.println("Error while closing the stream : " + ioe);
			}

		}
		return confinhoud;
	}
}

package test.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import model.configuration.*;

import org.junit.Before;
import org.junit.Test;

import exception.CustomException;
import exception.ParseException;
import exception.UnsupportedDefaultActionException;
import exception.UnsupportedProtocolException;
import static org.junit.Assert.*;

/**
 * Deze klasse test het parsen van een iptables configuratie bestand.
 * 
 * @author Thomas
 */
public class ConfigurationTest {
	private static final String PATH_SEPARATOR = File.separator;
	private static final String CORRELATION_TEST_FILE = "correlationtest.iptables";
	private static final String FIVE_CORRELATION_GROUPS_TEST_FILE = "fivecorrgroups.iptables";
	private static final String CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE = "conffrompaperv3.iptables";
	private static final String GENERALIZATION_TEST_FILE = "generalizationtest.iptables";
	private static final String SHADOWING_TEST_FILE = "shadowingtest.iptables";
	private static final String REDUNDANCY_TEST_FILE = "REDUNDANCIETEST.iptables";
	private static final String NO_ANOMALIES_TEST_FILE = "noanomalies.iptables";
	// private static final String CONFIGURATION_FROM_PAPER_BAD_SYNTAX_TEST_FILE
	// = "conffrompaperv3FalseSyntax.iptables";

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

	private String getFilePath(String filename) {
		return rootFilePath + PATH_SEPARATOR + "src" + PATH_SEPARATOR + "testconfiguraties" + PATH_SEPARATOR + filename;
	}

	/**
	 * Test of de correlationtest.iptables configuratie correct geparst wordt.
	 */
	@Test
	public void testCorrelationtest() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(CORRELATION_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		// in de FORWARD chain van de FILTER tabel
		assertEquals(4, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "UDP", "135.20.30.0/24", "56.50.97.4/32", "1050", "*", true);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "TCP", "*", "173.252.110.27/32", "*", "80", false);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "ANY", "135.20.30.5/32", "56.50.97.4/32", "*", "*", false);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "UDP", "135.20.0.0/16", "56.50.97.0/24", "1050", "*", true);
	}

	/**
	 * Test of de conffrompaperv2.iptables configuratie correct geparst wordt.
	 */
	public void testConffrompaperv3() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		assertEquals(5, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "UDP", "10.1.2.0/24", "172.32.1.0/24", "*", "53", false);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "UDP", "10.1.0.0/16", "172.32.1.0/24", "*", "53", false);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "TCP", "10.1.0.0/16", "192.168.0.0/16", "*", "25", true);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "TCP", "10.1.1.0/24", "192.168.1.0/24", "*", "25", false);
		// we testen regel 5
		regel = regels.get(4);
		testRegel(regel, 5, "ANY", "10.1.1.0/24", "*", "*", "*", true);

	}

	/**
	 * Test of de conffrompaperv2.iptables configuratie correct geparst wordt.
	 */
	@Test
	public void testFivecorrgroups() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(FIVE_CORRELATION_GROUPS_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		assertEquals(25, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;

		// we testen de eerste groep. Deze regels komen uit conffrompaperv3
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "UDP", "10.1.2.0/24", "172.32.1.0/24", "*", "53", false);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "UDP", "10.1.0.0/16", "172.32.1.0/24", "*", "53", false);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "TCP", "10.1.0.0/16", "192.168.0.0/16", "*", "25", true);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "TCP", "10.1.1.0/24", "192.168.1.0/24", "*", "25", false);
		// we testen regel 5
		regel = regels.get(4);
		testRegel(regel, 5, "ANY", "10.1.1.0/24", "*", "*", "*", true);

		// we testen de tweede groep. Deze regels komen uit conffrompaperv3,
		// maar met
		// het eerste getal van het bron ip adres in elke regel 20 ipv 10
		// we testen regel 6
		regel = regels.get(5);
		testRegel(regel, 6, "UDP", "20.1.2.0/24", "172.32.1.0/24", "*", "53", false);
		// we testen regel 7
		regel = regels.get(6);
		testRegel(regel, 7, "UDP", "20.1.0.0/16", "172.32.1.0/24", "*", "53", false);
		// we testen regel 8
		regel = regels.get(7);
		testRegel(regel, 8, "TCP", "20.1.0.0/16", "192.168.0.0/16", "*", "25", true);
		// we testen regel 9
		regel = regels.get(8);
		testRegel(regel, 9, "TCP", "20.1.1.0/24", "192.168.1.0/24", "*", "25", false);
		// we testen regel 10
		regel = regels.get(9);
		testRegel(regel, 10, "ANY", "20.1.1.0/24", "*", "*", "*", true);

		// we testen de derde groep. Deze regels komen uit conffrompaperv3, maar
		// met
		// het eerste getal van het bron ip adres in elke regel 30 ipv 10
		// we testen regel 11
		regel = regels.get(10);
		testRegel(regel, 11, "UDP", "30.1.2.0/24", "172.32.1.0/24", "*", "53", false);
		// we testen regel 12
		regel = regels.get(11);
		testRegel(regel, 12, "UDP", "30.1.0.0/16", "172.32.1.0/24", "*", "53", false);
		// we testen regel 13
		regel = regels.get(12);
		testRegel(regel, 13, "TCP", "30.1.0.0/16", "192.168.0.0/16", "*", "25", true);
		// we testen regel 14
		regel = regels.get(13);
		testRegel(regel, 14, "TCP", "30.1.1.0/24", "192.168.1.0/24", "*", "25", false);
		// we testen regel 15
		regel = regels.get(14);
		testRegel(regel, 15, "ANY", "30.1.1.0/24", "*", "*", "*", true);

		// we testen de vierde groep. Deze regels komen uit conffrompaperv3,
		// maar met
		// het eerste getal van het bron ip adres in elke regel 40 ipv 10
		// we testen regel 16
		regel = regels.get(15);
		testRegel(regel, 16, "UDP", "40.1.2.0/24", "172.32.1.0/24", "*", "53", false);
		// we testen regel 17
		regel = regels.get(16);
		testRegel(regel, 17, "UDP", "40.1.0.0/16", "172.32.1.0/24", "*", "53", false);
		// we testen regel 18
		regel = regels.get(17);
		testRegel(regel, 18, "TCP", "40.1.0.0/16", "192.168.0.0/16", "*", "25", true);
		// we testen regel 19
		regel = regels.get(18);
		testRegel(regel, 19, "TCP", "40.1.1.0/24", "192.168.1.0/24", "*", "25", false);
		// we testen regel 20
		regel = regels.get(19);
		testRegel(regel, 20, "ANY", "40.1.1.0/24", "*", "*", "*", true);

		// we testen de vijfde groep. Deze regels komen uit conffrompaperv3,
		// maar met
		// het eerste getal van het bron ip adres in elke regel 50 ipv 10
		// we testen regel 21
		regel = regels.get(20);
		testRegel(regel, 21, "UDP", "50.1.2.0/24", "172.32.1.0/24", "*", "53", false);
		// we testen regel 22
		regel = regels.get(21);
		testRegel(regel, 22, "UDP", "50.1.0.0/16", "172.32.1.0/24", "*", "53", false);
		// we testen regel 23
		regel = regels.get(22);
		testRegel(regel, 23, "TCP", "50.1.0.0/16", "192.168.0.0/16", "*", "25", true);
		// we testen regel 24
		regel = regels.get(23);
		testRegel(regel, 24, "TCP", "50.1.1.0/24", "192.168.1.0/24", "*", "25", false);
		// we testen regel 25
		regel = regels.get(24);
		testRegel(regel, 25, "ANY", "50.1.1.0/24", "*", "*", "*", true);

	}

	/**
	 * Test of de generalizationtest.iptables configuratie correct geparst
	 * wordt.
	 */
	@Test
	public void testGeneralizationtest() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(GENERALIZATION_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		assertEquals(5, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "UDP", "135.20.30.88/32", "10.65.46.0/24", "ANY", "80", true);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "TCP", "*", "173.252.110.27/32", "*", "80", false);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "UDP", "10.6.27.96/32", "173.252.111.56/32", "1404", "107", true);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "TCP", "135.20.30.0/24", "10.65.46.0/24", "*", "88", true);
		// we testen regel 5
		regel = regels.get(4);
		testRegel(regel, 5, "ANY", "*", "10.65.46.0/24", "*", "*", false);

	}

	/**
	 * Test of de noanomalies.iptables configuratie correct geparst wordt.
	 */
	@Test
	public void testNoanomalies() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(NO_ANOMALIES_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		assertEquals(7, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "TCP", "10.0.0.0/8", "55.7.66.0/24", "*", "22", true);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "TCP", "10.0.0.0/8", "55.7.66.0/24", "*", "543", true);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "TCP", "10.0.0.0/8", "55.7.66.0/24", "*", "544", true);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "TCP", "10.0.0.0/8", "55.7.66.0/24", "*", "464", true);
		// we testen regel 5
		regel = regels.get(4);
		testRegel(regel, 5, "ANY", "*", "91.33.56.36/32", "*", "*", true);
		// we testen regel 6
		regel = regels.get(5);
		testRegel(regel, 6, "UDP", "*", "*", "1200", "123", true);
		// we testen regel 7
		regel = regels.get(6);
		testRegel(regel, 7, "TCP", "*", "*", "*", "443", true);

	}

	/**
	 * Test of de redundancietest.iptables configuratie correct geparst wordt.
	 */
	@Test
	public void testRedundancietest() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(REDUNDANCY_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		assertEquals(5, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "TCP", "192.168.3.4/32", "10.45.54.7/32", "*", "80", false);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "ANY", "192.168.0.0/16", "192.168.0.0/16", "*", "*", true);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "UDP", "10.58.99.42/32", "10.88.55.14/32", "*", "88", true);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "ANY", "10.58.99.0/24", "10.88.55.0/24", "*", "*", true);
		// we testen regel 5
		regel = regels.get(4);
		testRegel(regel, 5, "ANY", "10.58.0.0/16", "10.0.0.0/8", "*", "*", true);

	}

	/**
	 * Test of de shadowingtest.iptables configuratie correct geparst wordt.
	 */
	@Test
	public void testShadowingtest() {
		// verkrijg pad naar File waar test configuratie wordt bewaard
		String pad_naar_testconfiguratie = getFilePath(SHADOWING_TEST_FILE);
		Configuration configuratie = null;
		// maak configuratie aan - parse deze uit gegeven file
		configuratie = testMaakConfiguratieAan(pad_naar_testconfiguratie);
		// test of de geparste configuratie een waarde bevat
		assertNotNull(configuratie);
		// test of er een tabel NAT geparst is met 0 regels in de PREROUTING
		// chain
		assertEquals(0, configuratie.getTable("NAT").getChain("PREROUTING").getRules().size());
		// test of er een tabel FILTER geparst is
		assertNotNull(configuratie.getTable("FILTER"));
		// test of in tabel FILTER 0 regels geparst zijn in de INPUT chain
		assertEquals(0, configuratie.getTable("FILTER").getChain("INPUT").getRules().size());
		// test of er een chain FORWARD geparst is
		assertNotNull(configuratie.getTable("FILTER").getChain("FORWARD"));
		// als het goed is zijn er 4 tabellen in de configuratie
		assertEquals(4, configuratie.getTables().size());
		// controleer of er daadwerkelijk 3 chains in de tabel FILTER zijn
		assertEquals(3, configuratie.getTable("FILTER").getChains().size());
		// controlleer of daadwerkelijk de FILTER tabel opgevraagd wordt door de
		// methode getTable
		assertEquals("filter", configuratie.getTable("FILTER").getName().toString().toLowerCase());
		// controlleer of daadwerkelijk de FORWARD chain opgevraagd wordt door
		// de methode getChain
		assertEquals("forward", configuratie.getTable("FILTER").getChain("FORWARD").getName().toString().toLowerCase());
		// controleer of het aantal regels in de Configuratie overeen stemt met
		// het iptables bestand. correlationTest.iptables telt 4 firewall regels
		assertEquals(5, configuratie.getTable("FILTER").getChain("FORWARD").getRules().size());
		// verkrijg regelsRegelonfiguratie
		ArrayList<Rule> regels = configuratie.getTable("FILTER").getChain("FORWARD").getRules();
		// we testen elke regel.
		// opm.: methode testRegel moet volgende parameters meekrijgen:
		// testRegel(Regel regel,int regelnr,String protocol,String
		// bronipstring,String doelipstring,String bronpoortstring,String
		// doelpoortstring, String actie)

		Rule regel = null;
		// we testen regel 1
		regel = regels.get(0);
		testRegel(regel, 1, "TCP", "10.1.2.0/24", "192.168.0.0/16", "*", "25", false);
		// we testen regel 2
		regel = regels.get(1);
		testRegel(regel, 2, "TCP", "192.168.0.0/16", "173.252.110.27/32", "16015", "80", true);
		// we testen regel 3
		regel = regels.get(2);
		testRegel(regel, 3, "ANY", "10.2.0.0/16", "192.168.0.0/16", "*", "*", false);
		// we testen regel 4
		regel = regels.get(3);
		testRegel(regel, 4, "TCP", "10.2.54.0/24", "192.168.5.0/24", "1543", "80", true);
		// we testen regel 5
		regel = regels.get(4);
		testRegel(regel, 5, "UDP", "10.1.2.88/32", "192.168.0.0/16", "*", "*", false);

	}

	/*
	 * Test wat er gebeurd als er een fout is in de syntax van het iptables
	 * configuratie-bestand. Er moet dan een exception opgegooid worden. Het
	 * betreffende test geval is de zelfde als conffrompaperv3, waarbij in het
	 * configuratie bestand volgende gewijzigd is: Regel 30 is veranderd van -A
	 * FORWARD -s 10.1.0.0/16 -d 172.32.1.0/24 -p udp -m udp --dport 53 -j DROP
	 * naar -A FORWARD -s 10.1.0.0/16 -d 172.32.1.0/24 -p up -m udp --dport 53
	 * -j DROPj , wat een exception moet opleveren bij het parsen Ik heb deze
	 * test ook uitgevoerd met andere wijzigingen in dit bestand. Mijn bevinding
	 * is dat een syntaxfout in het configuratie-bestand soms wel en soms niet
	 * leidt tot een exception bij het parsen.
	 */
	/*
	 * @Test(expected = CustomException.class) public void
	 * testConffrompaperv3FalseSyntax() throws CustomException { //NOG VERDER
	 * UIT TE WERKEN!!! // verkrijg pad naar File waar test configuratie wordt
	 * bewaard String pad_naar_testconfiguratie =
	 * getFilePath(CONFIGURATION_FROM_PAPER_BAD_SYNTAX_TEST_FILE);
	 * testMaakConfiguratieAan(pad_naar_testconfiguratie); }
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
	private static void testRegel(Rule regel, int regelnr, String protocol, String bronipstring, String doelipstring, String bronpoortstring, String doelpoortstring, boolean actie) {
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
			// vergelijk regel nummers
			assertEquals(regelnr, regel.getRuleNr());
			// vergelijk protocol
			assertEquals(protocol, regel.getProtocol());
			// vergelijk bron IP
			assertTrue(IPPattern.isEquivalent(bronip, regel.getSourceIP()));
			// vergelijk doel IP
			assertTrue(IPPattern.isEquivalent(doelip, regel.getDestinationIP()));
			// vergelijk bron poort
			assertTrue(PortPattern.isEquivalent(bronpoort, regel.getSourcePort()));
			// vergelijk doel poort
			assertTrue(PortPattern.isEquivalent(doelpoort, regel.getDestinationPort()));
			assertEquals(actie, regel.getAction());
		} catch (CustomException e) {
			System.out.println(e.getOorzaak());
			e.printStackTrace();
		}

	}

	/**
	 * Methode voor het parsen van iptables bestand in opgegeven locatie. De
	 * configuratie wordt in model.configuratie objecten gezet.
	 * 
	 * @param locatie_van_test_configuratie
	 */
	private static Configuration testMaakConfiguratieAan(String locatie_van_test_configuratie) {
		// Regels inlezen uit lokaal bestand
		String confinhoud = readTestConfiguratie(locatie_van_test_configuratie);
		Configuration configuratie = null;
		// configuratie in model.configuratie objecten zetten dmv Configuratie
		// constructor.
		try {
			configuratie = new Configuration(confinhoud);
		} catch (ParseException ce) {
			System.out.print(ce.getMessage());
			ce.printStackTrace();
		} catch (UnsupportedDefaultActionException e){
			System.out.print(e.getMessage());
			e.printStackTrace();
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

	// main methode voor debuggen
	public static void main(String[] args) {
		// lees configuratie als string
		String confinhoud = readTestConfiguratie(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE);
		// maak configuratie aan
		try {
			Configuration conf = new Configuration(confinhoud);
			conf.toString();
		} catch (ParseException e) {
			System.out.println("Opgetreden Exception: " + e.getMessage());
		} catch (UnsupportedDefaultActionException e){
			System.out.print(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedProtocolException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}

}

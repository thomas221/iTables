package test.model;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.configuration.Configuration;

import org.junit.Before;
import org.junit.Test;

import exception.ParseException;
import exception.UnsupportedDefaultActionException;
import exception.UnsupportedProtocolException;

/**
 * Deze klasse test het exporteren van een configuratie naar iptables formaat.
 * @author Thomas
 *
 */
public class ExportFunctionTest {

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
	
	@Test
	public void test() {
		// lees configuratie als string
		String confinhoud = readTestConfiguratie(getFilePath(CONFIGURATION_FROM_PAPER_GOOD_SYNTAX_TEST_FILE));
		// maak configuratie aan
		try {
			Configuration conf = new Configuration(confinhoud);
			System.out.println(conf.getRuleSet().getExportRules());
			
		} catch (ParseException | UnsupportedDefaultActionException e) {
			System.out.println("Opgetreden Exception: " + e.getMessage());
		} catch (UnsupportedProtocolException e) {
			System.out.println("Opgetreden Exception: " + e.getMessage());
			e.printStackTrace();
		}
		assertTrue(true);
	}
	
	/**
	 * main methode voor debuggen
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}

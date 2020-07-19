package test.model;

import static org.junit.Assert.*;

import org.junit.Test;

import exception.CustomException;
import model.configuration.*;

public class PortPatternTest {

	/**
	 * Test het aanmaken van een PortPattern op basis van een String.
	 */
	@Test
	public void testPortPattern() {
		try {
			// test gevallen die goed moeten verlopen

			// test geval dat ondergrens en bovengrens opgegeven is
			PortPattern portPattern = new PortPattern("4:1000");
			testAttributes(4, 1000, portPattern);

			// test geval dat slecht 1 poort tot poortpatroon hoort. Dit kan op
			// 2 manieren.
			portPattern = new PortPattern("1055");
			testAttributes(1055, 1055, portPattern);
			portPattern = new PortPattern("1055:1055");
			testAttributes(1055, 1055, portPattern);

			// test geval dat "*" of "ANY" opgegeven wordt als poort patroon
			portPattern = new PortPattern("*");
			testAttributes(-1, -1, portPattern);
			portPattern = new PortPattern("ANY");
			testAttributes(-1, -1, portPattern);

		} catch (CustomException ce) {
			// de opgegeven test gevallen mogen geen Exception tot gevolg hebben
			assertTrue(false);
		}
		// test gevallen die fout moeten gaan

		// test geval dat poort niet tussen 0 en 65535
		testFalsePort("65536");
		// test geval eind poort niet tussen 0 en 65535
		testFalsePort("5:65536");
		// test geval dat niet begin poort <= eind poort
		testFalsePort("30099:1200");
		// test geval dat poort geen getal is
		testFalsePort("a");
		// test geval dat poort begint met cijfer maar geen getal is
		testFalsePort("32c");
		// test voor lege string
		testFalsePort("");
		// test geval dat ':' meer dan 1 keer voorkomt in string
		testFalsePort("100:230:550");
	}

	/**
	 * Test of attributen van portPattern de opgegeven waarde hebben.
	 * 
	 * @param lowerBound
	 *            Gewenste waarde van de ondergrens.
	 * @param upperBound
	 *            Gewenste waarde van de bovengrens.
	 * @param portPattern
	 *            PartPattern object waarvan men de attributen wil controleren.
	 */
	private static void testAttributes(int lowerBound, int upperBound, PortPattern portPattern) {
		assertEquals(lowerBound, portPattern.getLowerBound());
		assertEquals(upperBound, portPattern.getUpperBound());
	}

	/**
	 * Hulp methode die controleert of de verwachte exception opgegooid wordt
	 * bij het aanmaken van het PortPattern.
	 * 
	 * @param falsePort
	 *            String die fout moet opleveren bij het opgeven van deze als
	 *            parameter voor de constructor van PortPattern.
	 */
	private static void testFalsePort(String falsePort) {
		// test gevallen die een Exception moeten opleveren
		boolean thrown = false;
		try {
			new PortPattern(falsePort);
		} catch (CustomException ce) {
			thrown = true;

		}
		assertTrue(thrown);
	}

}

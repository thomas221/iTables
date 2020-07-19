/**
 * 
 */
package test.model;

import static org.junit.Assert.*;
import model.configuration.IPPattern;

import org.junit.Test;

import exception.CustomException;

/**
 * @author Thomas Van Poucke
 *
 */
public class IPPatternTest {

	/**
	 * Test het parsen van een string als een IPPattern object.
	 */
	@Test
	public void testIPPattern(){
		//test gevallen die correct moeten verlopen
		
		//test geval dat er geen / aanwezig is in adres
		try{
			IPPattern ipPattern = new IPPattern("20.30.40.50");
			testAttributes(20,30,40,50,32,ipPattern);
		
		//test geval dat adres * is of ANY
		
			IPPattern ipPattern2 = new IPPattern("*");
			testAttributes(0,0,0,0,0,ipPattern2);
			IPPattern ipPattern3 = new IPPattern("ANY");
			testAttributes(0,0,0,0,0,ipPattern3);
		//test geval dat er een masker is opgegeven
			IPPattern address = new IPPattern("20.10.2.3/8");
    		testAttributes(20,10,2,3,8, address);
		}catch(CustomException ce){
			assertTrue(false);
		}
		
		//test gevallen die een Exception moeten opleveren
		//geval masker geen getal tussen 0 en 32 (inc.)
		testFalseAddress("1.1.1.1/33");
		//geval masker geen getal
		testFalseAddress("2.2.2.2/aa");
		//geval dat er meer dan 4 getallen zijn voor IP adres
		testFalseAddress("1.2.3.1.3/0");
		//geval dat er minder dan 4 getallen zijn voor IP adres
		testFalseAddress("1.2.3/2");
		//geval dat 1 van de adres getallen geen getal is
		testFalseAddress("1.5.c.6/3");
		//geval dat 1 van de adres getallen begint met cijfer maar geen getal is
		testFalseAddress("1.3.3c.6/12");
		//geval dat er een masker is maar geen adres
		testFalseAddress("/22");
		//geval dat er meerdere slashen in adres zitten
		testFalseAddress("1.2.2.2/54/64");
		//geval dat 1 vd getallen van adres geen getal is tussen 0 en 255
		testFalseAddress("1.2.256.4/5");
		//geval dat lege string meegegeven wordt
		testFalseAddress("");
		
	}

	
	/**
	 * Hulpmethode die verwachte waarde van IPPattern object vergelijkt met de attribuutwaarden
	 */
	private static void testAttributes(int number1,int number2,int number3,int number4,int mask,IPPattern ipPattern){
		assertEquals(number1,ipPattern.getNumber1());
		assertEquals(number2,ipPattern.getNumber2());
		assertEquals(number3,ipPattern.getNumber3());
		assertEquals(number4,ipPattern.getNumber4());
		//masker moet 32 zijn
		assertEquals(mask,ipPattern.getMask());
	}
	
	/**
	 * Hulpmethode voor geval dat het parsen een Exception moet opleveren
	 */
	private static void testFalseAddress(String falseAddress){
		//test gevallen die een Exception moeten opleveren
		boolean thrown = false;
		try{
			new IPPattern(falseAddress);
		}catch(CustomException ce){
			thrown = true;
			
		}
		assertTrue(thrown);
	}
	
	
	/**
	 * Test method for {@link model.configuration.IPPattern#isEquivalent(model.configuration.IPPattern, model.configuration.IPPattern)}.
	 */
	@Test
	public void testIsEquivalent() {
		try{
    		new IPPattern("20.10.2.3/8");
    		
    		IPPattern address1 = new IPPattern("6.7.9.2/16");
    		IPPattern address2 = new IPPattern("6.7.10.10/16");
    		
    		//deze twee moeten equivalent zijn
    		assertTrue(IPPattern.isEquivalent(address1, address2));
    		
    		IPPattern addressA = new IPPattern("6.7.9.2/16");
    		
    		IPPattern addressB = new IPPattern("6.8.9.2/16");
    		
    		//deze twee mogen niet equivalent zijn
    		assertFalse(IPPattern.isEquivalent(addressA,addressB));
    	}catch(CustomException ce){
    		System.out.println(ce.getOorzaak());
    		ce.printStackTrace();
    	}
	}

	/**
	 * Test method for {@link model.configuration.IPPattern#toBoolean()}.
	 */
	@Test
	public void testToBoolean() {
		try{
			IPPattern address = new IPPattern("6.7.90.23/16");
			boolean[] bits = address.toBoolean();
			//verwacht resultaat
			boolean[] expectedBits = {false,false,false,false,  false,true,true,false,  false,false,false,false,  false,true,true,true,  false,true,false,true,  true,false,true,false,  false,false,false,true,  false,true,true,true};
			
			//vergelijk elke bit
			for(int i=0;i<32;i++){
				assertEquals(expectedBits[i],bits[i]);
			}
		}catch(CustomException ce){
			System.out.println(ce.getOorzaak());
		}
	}
	
	

}

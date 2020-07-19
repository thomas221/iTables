package model.configuration;

import java.util.ArrayList;

/**
 * De klasse<code> EnumerationBDD </code> bevat de regels van een opsomming van
 * de 5 dimensionale ruimte van een BDD. </br> Dit zijn niet de firewall regels.
 * 
 * @author Ron Melger
 */
public class EnumerationBDD {
	/**
	 * Bevat de opsommmings regels van een BDD.
	 */
	private ArrayList<RuleBDD> enumeration;

	/**
	 * Initialiseert een nieuw <code>EnumerationBDD</code> object dat geen
	 * elementen bevat.
	 */
	public EnumerationBDD() {
		enumeration = new ArrayList<RuleBDD>();
	}

	/**
	 * Voegt een opsommingsregel toe aan de lijst van opsommingsregels.
	 * 
	 * @param regelBDD
	 *            de opsommingsregel die wordt toegevoegd
	 */
	public void addRuleBDD(RuleBDD regelBDD) {
		enumeration.add(regelBDD);
	}

	/**
	 * Verwijdert het element van type RuleBDD op positie index.
	 * 
	 * @param index
	 *            de positie van het element dat moet worden verwijderd.
	 */
	public void removeRuleBDD(int index) {
		enumeration.remove(index);
	}

	/**
	 * Geeft het aantal elementen in de lijst van opsommingsregels.
	 * 
	 * @return het aantal opsommingsregels
	 */
	public int getSize() {
		return enumeration.size();
	}

	/**
	 * Geeft het <code>RuleBDD</code> object van de gespecificeerde index. Een
	 * index heeft een range van <code>0</code>tot en met
	 * <code>getSize() - 1</code>
	 * 
	 * @param index
	 *            de <code>index</code> van het <code>RuleBDD</code> object
	 * @return het <code>RuleBDD</code> object van de gespecificeerde index
	 */
	public RuleBDD getRuleBDD(int index) {
		return enumeration.get(index);
	}

	/**
	 * Geeft de opsommingsregels van de BDD terug.
	 * @return Opsommingsregels van de BDD.
	 */
	public ArrayList<RuleBDD> getRuleBDDs() {
		return enumeration;
	}

}

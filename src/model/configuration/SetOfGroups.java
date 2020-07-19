package model.configuration;

import java.util.ArrayList;

/**
 * De klasse <code> SetOfGroups </code> bevat de correlatie-groepen die zijn te
 * onderscheiden uit de segmenten.
 * 
 * @author Ron Melger
 * 
 */
public class SetOfGroups {
	/**
	 * Bevat de correlatie groepen die in dit <code>SetOfGroups</code> object
	 * zitten.
	 */
	ArrayList<CorrelationGroup> correlationGroups;

	/**
	 * Initialiseert een nieuw <code>SetOfGroups</code> object.
	 */
	public SetOfGroups() {
		this.correlationGroups = new ArrayList<CorrelationGroup>();
	}

	/**
	 * Geeft het aantal groepen dat deze klasse beheert.
	 * 
	 * @return het aantal groepen
	 */
	public int getNumberOfGroups() {
		return correlationGroups.size();
	}

	/**
	 * Geeft een arrayList met alle correlatie groepen terug.
	 * 
	 * @return de correlatie groepen
	 */
	public ArrayList<CorrelationGroup> getCorrelationGroups() {
		return correlationGroups;
	}

	/**
	 * Geeft de groep met de aangegeven index.
	 * 
	 * @param index
	 *            het rangnummer van de te retourneren groep
	 * @return de groep met de aangegeven index
	 */
	public CorrelationGroup getGroup(int index) {
		return correlationGroups.get(index);
	}

	/**
	 * Geeft het hoogste toegekende groepsnummer als een <code>int</code>.
	 * 
	 * @return ALS er groepen bestaan het hoogste toegekende groepsnummer ANDERS
	 *         0
	 */
	public int getMaxGroupNr() {
		int maxNumber = 0;
		int tempNumber;
		int aantal = getNumberOfGroups();
		for (int i = 0; i < aantal; i++) {
			tempNumber = correlationGroups.get(i).getGroupNumber();
			if (tempNumber > maxNumber) {
				maxNumber = tempNumber;
			}
		}
		return maxNumber;
	}

	/**
	 * Voegt een groep toe aan de lijst van groepen.
	 * 
	 * @param group
	 *            de toe te voegen groep
	 */
	public void addGroup(CorrelationGroup group) {
		correlationGroups.add(group);
	}

	/**
	 * Verwijdert de groep op positie index uit de lijst van groepen.
	 * 
	 * @param index
	 *            de positie van de te verwijderen groep
	 */
	public void removeGroup(int index) {
		correlationGroups.remove(index);
	}

	/*
	 * 
	 * public boolean removeGroup(int groupNumber) { boolean succesvolVerwijderd
	 * = false; Group group; int teVerwijderen = -1; int aantalGroups =
	 * getNumberOfGroups();
	 * 
	 * for (int i = 0; i < aantalGroups; i++ ) { group =
	 * correlationGroups.get(i); if (group.getGroupNumber() == groupNumber) { //
	 * neem dit nummer teVerwijderen = i; } } if (teVerwijderen != -1) {
	 * correlationGroups.remove(teVerwijderen); succesvolVerwijderd = true; }
	 * 
	 * return succesvolVerwijderd; }
	 */
	// voor debuggen
	/**
	 * Print informatie af over elke correlation group in de Set of Groups naar
	 * de standaard uitvoer.
	 */
	public void printGroups() {
		int aantalGroups = getNumberOfGroups();
		CorrelationGroup mijngroup;
		for (int k = 0; k < aantalGroups; k++) {
			mijngroup = getGroup(k);
			mijngroup.printGroup();
		}
	}
}

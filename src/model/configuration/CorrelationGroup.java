package model.configuration;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * De klasse<code> CorrelationGroup </code> bestaat uit 1 of meerdere segmenten. <br />
 * Deze correlatiegroep is als geisoleerd deel te analyseren op de aanwezigheid
 * van anomalieen. <br />
 * Regels kunnen herschikt of verwijderd worden en dat heeft op zich geen
 * invloed op andere groepen. Na een herschikking of verwijdering van regels
 * dient wel opnieuw de correlatiegroepen bepaalt te worden omdat regelnummers
 * veranderen, ook zullen de nummers en volgorde van segmenten meestal
 * veranderen. <br />
 * Tevens kan na verwijdering van redundante regels de groep gaan bestaan uit
 * meerdere groepen.<br />
 * Het testen op redundante firewall regels dient herhaalt te worden tot geen
 * redundante regels meer gevonden worden. Dit komt omdat gebruik wordt gemaakt
 * van een algoritme dat geen optimale oplossingen geeft, en na verwijdering de
 * toekenning van eigenschappen toepast op de dan ontstane situatie.
 * 
 * @author Ron Melger
 * 
 */
public class CorrelationGroup {
	/**
	 * Bevat de segmenten die tot de correlatie groep behoren.
	 */
	private ArrayList<Segment> segments;
	/**
	 * Bevat het nummer van de correlation groep. Nummering vanaf nummer 1.
	 */
	private int groupNumber;
	
	/**
	 * Houdt bij of deze groep reeds is opgelost door de gebruiker. Als de groep conflicterend is kan daardoor in tijdens de sessie
	 * bijgehouden worden of de gebruiker reeds deze groep opgelost heeft.
	 * @return true indien groep reeds is opgelost door de gebruiker.
	 */
	private boolean isSolved = false;

	/**
	 * Initialiseert een nieuw <code>CorrelationGroup</code> object.
	 * 
	 * @param groupNumber
	 *            het nummer van de nieuwe groep
	 */
	public CorrelationGroup(int groupNumber) {
		this.groupNumber = groupNumber;
		segments = new ArrayList<Segment>();
		segments.clear();
	}

	/**
	 * Geeft het segment met de aangegeven index.
	 * 
	 * @param index
	 *            het rangnummer van het te retourneren segment
	 * @return het segment met het aangegeven rangnummer, index in
	 *         [0...getSize() - 1]
	 */
	public Segment getSegment(int index) {
		return segments.get(index);
	}

	/**
	 * Geeft een ArrayList van de segmenten in deze groep terug.
	 * 
	 * @return ArrayList van segmenten in deze groep.
	 */
	public ArrayList<Segment> getSegments() {
		return segments;
	}

	/**
	 * Geeft het aantal segmenten in deze groep.
	 * 
	 * @return het aantal segmenten in deze groep
	 */
	public int getSize() {
		return segments.size();
	}

	/**
	 * Geeft de nummers van alle firewall regels binnen deze groep als een
	 * <code>TreeSet</code>.
	 * 
	 * @return de nummers van alle firewall regels binnen deze groep
	 */
	public TreeSet<Integer> getRuleNumbers() {
		TreeSet<Integer> tempSet = new TreeSet<Integer>();

		// haal ven de segmenten de RuleNummers op
		int groupSize = segments.size();
		Segment segment;

		for (int i = 0; i < groupSize; i++) {
			// haal een segment op
			segment = segments.get(i);
			int aantalRules = segment.getNumberOfRules();

			for (int j = 0; j < aantalRules; j++) {
				tempSet.add(segment.getRule(j).getRuleNr());
			}
		}
		return tempSet;
	}

	/**
	 * Geeft de Rule objecten terug die behoren tot deze correlatie groep.
	 * 
	 * @return Rule objecten die behoren tot deze correlatie groep.
	 */
	public TreeSet<Rule> getRules() {
		TreeSet<Rule> tempSet = new TreeSet<Rule>();

		// haal ven de segmenten de RuleNummers op
		int groupSize = segments.size();
		Segment segment;

		for (int i = 0; i < groupSize; i++) {
			// haal een segment op
			segment = segments.get(i);
			int aantalRules = segment.getNumberOfRules();

			for (int j = 0; j < aantalRules; j++) {
				tempSet.add(segment.getRule(j));
			}
		}
		return tempSet;
	}

	/**
	 * Geeft het nummer van deze groep als een <code>int</code>.
	 * 
	 * @return het nummer van deze groep
	 */
	public int getGroupNumber() {
		return groupNumber;
	}

	/**
	 * Vermindert het nummer van de groep met 1.
	 */
	public void decGroupNumber() {
		groupNumber--;
	}

	/**
	 * Voegt een segment aan de groep toe.
	 * 
	 * @param segment
	 *            het segment dat aan de groep wordt toegevoegd
	 */
	public void addSegment(Segment segment) {
		segments.add(segment);
	}

	/**
	 * Geeft aan dat er conflicterende segmenten in deze groep zitten.
	 * @return true indien er conflicterende segmenten in de groep zitten, anders false.
	 */
	public boolean isConflicting() {
		if (segments != null && segments.size() > 0) {
			for (Segment segment : segments) {
				if (segment.getConflicting()) {
					return true;
				}
			}

			return false;
		} else {
			return false;
		}
	}

	/**
	 * Print de inhoud van een group naar de standaard uitvoer. Voor debug en
	 * testdoeleinden.
	 */
	public void printGroup() {
		System.out.println("Group : " + getGroupNumber());
		int aantalSegmenten = segments.size();

		for (int j = 0; j < aantalSegmenten; j++) {
			// System.out.println("segment : " +
			// getSegment(j).getSegmentNumber());
			getSegment(j).printSegment();
		}
		System.out.println();
	}

	/**
	 * Print de toewijzing van properties aan segmenten in de group naar de
	 * standaard uitvoer. Voor debug en testdoeleinden.
	 */
	public void printPropertyAsssignment() {
		System.out.println("Property Assignment Group : " + (getGroupNumber()));
		int aantalSegmenten = segments.size();

		for (int j = 0; j < aantalSegmenten; j++) {
			getSegment(j).printPropertyAssignments();
		}
	}
	
	/**
	 * Geeft terug of gebruiker deze groep reeds opgelost heeft. Dit dient om bij conflicterende
	 * groepen te kunnen bijhouden of het conflicterende groep reeds opgelost is. 
	 * @return true indien men reeds de groep heeft opgelost.
	 */
	public boolean getIsSolved(){
		return isSolved;
	}
	
	/**
	 * Geeft nieuwe waarde aan isSolved. Dit dient om bij conflicterende
	 * groepen te kunnen bijhouden of het conflicterende groep reeds opgelost is.
	 * @param isSolved Nieuwe waarde voor attribuut isSolved.
	 */
	public void setIsSolved(boolean isSolved){
		this.isSolved = isSolved;
	}

} // end Group

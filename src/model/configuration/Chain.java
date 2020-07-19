package model.configuration;

import java.io.Serializable;
import java.util.ArrayList;

import exception.CustomException;

/**
 * Deze klasse stelt een chain voor van een iptables firewall configuratie.
 * 
 * @author Thomas Van Poucke
 */
public class Chain implements Serializable {
	private static final long serialVersionUID = 1L;

	

	/**
	 * Enumeratie klasse die de mogelijke standaard policy acties van een chain
	 * definieert.
	 * 
	 * @author Thomas Van Poucke
	 * 
	 */
	public enum PolicyAction {
		ACCEPT, DROP;
		
		public String getName(){
			return this.name();
		}
	}

	/**
	 * Geeft aan om wat voor chain het gaat. Alleen de standaard ingebouwde
	 * chains van iptables worden ondersteund.
	 */
	private String name;

	/**
	 * Geeft aan wat de standaard actie van de chain is, als geen enkele regel
	 * matcht met een pakketje.
	 */
	private PolicyAction policy;

	/**
	 * Dit attribuut bevat alle regels van een chain in volgorde.
	 */
	private ArrayList<Rule> rules = new ArrayList<Rule>();

	/**
	 * Maak Chain met opgegeven naam en policy. 
	 * 
	 * @param name
	 *            Naam van Chain. 
	 * @param policy
	 *            Standaard policy van de chain.
	 */
	public Chain(String name, PolicyAction policy) {
		this.name = name;
		this.policy = policy;		
	}

	/**
	 * Geeft een instantie van Space terug die wordt opgebouwd uit de regels van
	 * de chain.
	 * 
	 * @return instantie van Space
	 * @throws CustomException
	 *             Als er iets misloopt tijdens het opbouwen van de
	 *             space-instantie.
	 */
	public Space getSpace() throws CustomException {
		Space space = new Space(getRules());
		return space;
	}

	/**
	 * Deze methode geeft terug over wat voor chain het gaat. Dus om welke van
	 * de ingebouwde chains van iptables.
	 * 
	 * @return de naam van de chain
	 */
	public String getName() {
		return name;
	}

	/**
	 * Deze methode geeft terug wat de standaard actie van een chain is, als een
	 * pakket matcht met geen enkele van de regels.
	 * 
	 * @return de standaard-actie van de chain
	 */
	public PolicyAction getPolicy() {
		return policy;
	}

	/**
	 * Deze methode geeft de policy van de chain de opgegeven waarde.
	 * 
	 * @param policy
	 *            De nieuwe default policy van de chain.
	 */
	public void setPolicy(PolicyAction policy) {
		this.policy = policy;
	}

	/**
	 * Geeft de rules van een chain terug.
	 * 
	 * @return De rules van een chain.
	 */
	public ArrayList<Rule> getRules() {
		return rules;
	}

	/**
	 * Geeft terug hoeveel regels er in deze chain zitten.
	 * @return Aantal regels.
	 */
	public int getRuleCount() {
		if (getRules() != null) {
			return getRules().size();
		} else {
			return 0;
		}
	}

	/**
	 * Voegt de opgegeven regel toe aan de chain, als laatste regel. Geeft het
	 * regelnummer de correcte waarde.
	 * 
	 * @param rule
	 *            Achteraan toe te voegen regel.
	 */
	public void addRule(Rule rule) {
		int regelnummer = getRules().size();
		rule.setRuleNr(regelnummer + 1);
		rules.add(rule);
	}
	
	/**
	 * Voegt de opgegeven regel toe aan de chain op de opgegeven positie. De regel die op die positie
	 * stond wordt samen met alle volgende regels 1 positie achteraan de lijst opgeschoven.
	 * 
	 * @param rule Regel toevoegen op opgegeven positie
	 * @param index index positie van toegevoegde regel. Eerste positie heeft index 0, enzovoort.
	 */
	public void addRule(Rule rule, int index){
		rules.add(index, rule);
		updateRuleNumbers();
	}

	/**
	 * Pas regelnummers van elke regel in de chain aan naargelang positie in de chain. Eerste regel krijgt regelnummer 1 enzovoort.
	 */
	public void updateRuleNumbers() {
		for (int i = 0 ; i< rules.size();i++) {
			Rule rule = rules.get(i);
			rule.setRuleNr(i+1);
		}
	}

	/**
	 * Verwijdert regel met opgegeven regelnummer uit de chain.
	 * @param ruleNumber Regelnummer van te verwijderen regel. Begint te tellen vanaf 1.
	 * @return Regel die verwijdert is.
	 */
	public Rule deleteRule(int ruleNumber) {
		Rule deletedRule = rules.remove(ruleNumber - 1);

		// Regelnummering opnieuw instellen
		updateRuleNumbers();

		return deletedRule;
	}
	
	/**
	 * Verwijdert alle regels met opgegeven regelnummers. De regelnummers worden als Arraylist
	 * meegegeven in de parameter. De regelnummers beginnen te tellen vanaf 0.
	 * @param ruleNrs Bevat alle regelnummers van de regels die verwijderd moeten worden. De regelnummers beginnen te tellen vanaf 0.
	 */
	public void deleteRules(ArrayList<Integer> ruleNrs){
		//verkrijg te verwijderen regels
		ArrayList<Rule> toRemove = new ArrayList<Rule>();
		for(int i=0;i<ruleNrs.size();i++){
			toRemove.add(rules.get(ruleNrs.get(i)));
		}
		//verwijder al deze regels
		for(int i=0;i<toRemove.size();i++){
			rules.remove(toRemove.get(i));
		}
		// Regelnummering opnieuw instellen
		updateRuleNumbers();
	}

	/**
	 * Geeft regel terug uit deze chain met opgegeven regelnummer. Regelnummers beginnen te tellen vanaf 1.
	 * @param ruleNumber Regelnummer van gewenste regel.
	 * @return Regel met dat regelnummer.
	 */
	public Rule getRule(int ruleNumber) {
		if (rules != null && rules.size() > 0) {
			return rules.get(ruleNumber - 1);
		} else {
			return null;
		}
	}

	/**
	 * Verwisselt regel met positie 'positionFrom' met de regel met positie 'positionTo'.
	 * @param positionFrom Positie van regel die verwisselt wordt met andere regel.
	 * @param positionTo Positie van regel die verwisselt wordt met andere regel.
	 */
	public void repositionRule(int positionFrom, int positionTo) {
		ArrayList<Rule> rules = getRules();

		int indexFrom = positionFrom - 1;
		int indexTo = positionTo - 1;

		// Minstens 2 regels zijn nodig om een herpositionering te kunnen
		// uitvoeren
		if (rules != null && rules.size() >= 2) {
			Rule ruleFrom = rules.remove(indexFrom);
			rules.add(indexTo, ruleFrom);

			updateRuleNumbers();
		}
	}

	/**
	 * Geeft ArrayList terug met CorrelationGroup'en. De correlationgroepen worden door de methode gecreeerd.
	 * @return ArrayList met CorrelationGroup'en voor de regels van deze chain.
	 * @throws CustomException Fout bij opdelen in correlatie groepen.
	 */
	public ArrayList<CorrelationGroup> getCorrelationGroups() throws CustomException {
		if (getRules() != null && getRules().size() > 0) {
			Space space = new Space(getRules());
			SetOfGroups setOfGroups = space.createCorrelationGroups();
			return setOfGroups.getCorrelationGroups();
		} else {
			return null;
		}
	}
	
	/**
	 * Geeft ArrayList terug met CorrelationGroup'en. De correlationgroepen worden door de methode gecreeerd. De conflict status van de groepen worden bepaald met de parameter <code>conflictStatus</code>.
	 * De groep is 'opgelost' als al zijn segmenten de status 'opgelost' hebben in <code>conflictStatus</code>.
	 * @param conflictStatus De conflict status. De groep is 'opgelost' als al zijn segmenten de status 'opgelost' hebben. De status van elk segment staat in dit object.
	 * @return ArrayList met CorrelationGroup'en voor de regels van deze chain. Daarbij is de conflict status van elke groep ingesteld volgens de informatie in het <code>conflictStatus</code> object.
	 * @throws CustomException Fout bij opdelen in correlatie groepen.
	 */
	public ArrayList<CorrelationGroup> getCorrelationGroupsWithStatus(ConflictStatus conflictStatus) throws CustomException{
		if (getRules() != null && getRules().size() > 0) {
			Space space = new Space(getRules());
			SetOfGroups setOfGroups = space.createCorrelationGroups();
			ArrayList<CorrelationGroup> groups = setOfGroups.getCorrelationGroups();
			for(CorrelationGroup group : groups){
			
				//stel conflict status van elke groep in. Een groep is opgelost als al zijn segmenten opgelost zijn
				//een correlation group met conflicten is reeds opgelost als elk van zijn conflicterende segmenten reeds opgelost zijn.
				if(group.isConflicting()){
					boolean isSolved = true;
					for(Segment segment : group.getSegments()){
						if(segment.isOverlapping() && segment.getConflicting()){
							if (!(conflictStatus.getStatus(segment) == ConflictStatus.Status.APPROVED_YES)) {
								isSolved = false;
							}
						}
					}
					group.setIsSolved(isSolved);
				}
	
				else{
					//correlation groups zonder conflicten zijn uiteraard niet opgelost
					group.setIsSolved(false);
				}
			}
			
			return groups;
		} else {
			return null;
		}
	}
	
	/**
	 * Geeft true terug indien de chain een regel bevat met protocol ANY bevat en wel een poort. Dit is niet mogelijk in iptables.
	 * De export functie zal daardoor deze regel in 2 iptables regels omzetten: 1 voor tcp en 1 voor udp.
	 * @return true indien de chain een regel bevat die geen protocol bevat en wel een poort.
	 */
	public boolean containsRuleWithPortWithoutProtocol(){
		for(model.configuration.Rule rule : rules){
			if(rule.isPortWithoutProtocol()){
				return true;
			}
		}
		return false;
	}

}

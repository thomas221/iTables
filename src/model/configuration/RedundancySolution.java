package model.configuration; // laatste wijzigingen: 14 oktober 2013

import java.util.ArrayList;
import java.util.Collections;

/**
 * De klasse <code>RedundancySolution</code> bevat de gegevens die nodig zijn om
 * weer te geven welke firewall regels als redundant worden gekenmerkt. </br>
 * Tevens bevat de klasse een methode om de gevonden redundante regels
 * daadwerkelijk te verwijderen uit de regelset.</br>
 * 
 * @author Ron Melger
 * 
 */
public class RedundancySolution {
	/**
	 * Bevat de mogelijke statussen bij de analyse naar redundante firewall
	 * regels.
	 * 
	 * @author Thomas Van Poucke
	 * 
	 */
	public enum Status {
		/**
		 * Er is nog geen status. Hierop wordt geinitialiseerd.
		 */
		NO_STATUS_YET,
		/**
		 * Er zijn geen redundante regels gevonden.
		 */
		THERE_ARE_NO_REDUNDANT_RULES,
		/**
		 * Er zijn redundante regels gevonden.
		 */
		FOUND_REDUNDANT_RULES;
	}

	/**
	 * Zal de regel nummers van de redundante firewall regels bevatten.
	 */
	private ArrayList<Integer> redundantRuleNrs;
	/**
	 * Bevat de status van de analyse naar redundante firewall regels.
	 */
	private Status status;
	/**
	 * Houdt bij of de verwijdering van de redundante firewall regels reeds
	 * plaatsgevonden heeft.
	 */
	private boolean removalCommitted; // veiligheid, dat maar 1 keer kan worden
										// verwijderd

	/**
	 * Initialiseert een nieuw <code>RedundancySolution</code> object.
	 */
	public RedundancySolution() {
		redundantRuleNrs = new ArrayList<Integer>();
		status = Status.NO_STATUS_YET;
		removalCommitted = false;
	}

	/**
	 * Voert de verrwijdering van de gevonden redundante firewall regels door in
	 * de regelset.
	 * 
	 * @param ruleSet
	 *            de firewall regelset waarin gevonden redundante regels
	 *            verwijderd worden
	 */
	public void commitRemoval(ArrayList<Rule> ruleSet) {
		if (removalCommitted)
			return;

		int aantal = redundantRuleNrs.size();
		// bij de hoogste index beginnen
		for (int index = aantal - 1; index >= 0; index--) {
			ruleSet.remove(redundantRuleNrs.get(index) - 1);
		}
		if (aantal > 0) {
			removalCommitted = true;
			// regelnummers aanpassen
			int aantalRuleSet = ruleSet.size();
			int startpositie = redundantRuleNrs.get(0) - 1;
			for (int i = startpositie; i < aantalRuleSet; i++) {
				ruleSet.get(i).setRuleNr(i + 1);
			}
		}
	}

	/**
	 * Geeft de status van de analyse naar redundante firewall regels.
	 * 
	 * @return de status van de analyse
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Geeft aan of de verwijdering van gevonden redundante regels reeds in de
	 * regelset is doorgevoerd, geeft dit als een <code>boolean</code>.
	 * 
	 * @return <code>true</code> als de verwijdering heeft plaatsgevonden ANDERS
	 *         <code>false</code>
	 */
	public boolean isRemovalCommitted() {
		return removalCommitted;
	}

	/**
	 * Stelt de status in die het resultaat is van de analyse naar redundante
	 * firewall regels.
	 * 
	 * @param status
	 *            de status die het resultaat is van de analyse
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Voegt een regelnummer toe aan de set van regelnummers die als redundant
	 * worden gekenmerkt. Als het regelnummer al voorkomt wordt hij niet
	 * nogmaals toegevoegd. De firewall regelnummers blijven op oplopende
	 * volgorde.
	 * 
	 * @param ruleNr
	 *            het regelnummer dat toegevoegd wordt
	 */
	public void addRedundantRuleNrs(int ruleNr) {
		int index = redundantRuleNrs.size() - 1;
		if (index < 0) // eerste element
			redundantRuleNrs.add(ruleNr);
		else {
			if (redundantRuleNrs.contains(ruleNr)) {
				// doe niets, geen dubbele regelnummers
			} else // zorg dat de regelnummers in oplopende volgorde blijven
			{
				if (ruleNr > redundantRuleNrs.get(index)) {
					redundantRuleNrs.add(ruleNr);
				} else {
					redundantRuleNrs.add(ruleNr);
					Collections.sort(redundantRuleNrs);
				}
			}
		}
	}

	/**
	 * Geeft de regelnummers die als redundant zijn aangemerkt als een
	 * <code>ArrayList< Integer ></code>, die in oplopende volgorde is.
	 * De regel nummers beginnen te tellen vanaf 1.
	 * 
	 * @return de regelnummers die als redundant zijn aangemerkt
	 */
	public ArrayList<Integer> getRedundantRuleNrs() {
		return redundantRuleNrs;
	}

}

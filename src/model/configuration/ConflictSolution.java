package model.configuration;  // laatse wijzigingen: 14 oktober 2013

import java.util.ArrayList;
import java.util.Collections;

import model.importeer_exporteer.RuleSet;

/**
 * De klasse <code>ConflictSolution</code> bevat de gegevens die nodig zijn om weer te geven
 * welke firewall regels betrokken zijn bij een eventuele herschikking en, als een herschikking gevonden is, 
 * in welke volgorde deze na die herschikking komen te staan. </br>
 * Tevens bevat de klasse een methode om de gevonden herschikking (indien een oplossing was gevonden) 
 * van firewall regels door te voeren.</br>
 * De status van de analyse voor mogelijke herschikking kan worden opgevraagd. Hiervoor zijn constanten gedefinieerd.
 * @author Ron Melger
 *
 */
public class ConflictSolution
{ 
	/**
	 * Enumeratie klasse die de mogelijke statussen van de conflictanalyse bevat.
	 *
	 */
	public enum Status{
		/**
		 * Hierop initialiseren.
		 */
		NO_STATUS_YET,          // Hierop initialiseren	
		/**
		 * De regelvolgorde stemt al overeen met de voorkeur.
		 */
		NO_REORDERING_REQUIRED,// de regelvolgorde stemt al overeen met de voorkeur
		/**
		 * Er bestaat geen oplossing.
		 */
		NO_SOLUTION_EXISTS,     // er bestaat geen oplossing
		/**
		 * Er is een oplossing (herschikking) gevonden.
		 */
		SOLUTION_FOUND,       // er is een oplossing (herschikking) gevonden
		/**
		 * Er was een fout in de invoer.
		 */
		ERROR_IN_INPUT,
		/**
		 * Het is niet gelukt om binnen een tijdsduur een oplossing te vinden. 
		 * Er zijn te veel regels en/of segmenten waardoor het te complex is.
		 */
		TO_COMPLEX               // te veel regels en/of segmenten
	}
	
	/**
	 * Bevat de nummers van de firewall regels die betrokken zijn bij het conflict, in de volgorde waarin ze staan in de configuratie.  
	 */
	private ArrayList< Integer > involvedRuleNrs = new  ArrayList< Integer >();
	/**
	 * Zal de regelnummers van de betrokken firewall regels bevatten - in de volgorde zoals ze na de herschikking zullen staan.
	 */
	private ArrayList< Integer > solutionRuleNrs = new  ArrayList< Integer >();
	/**
	 * Bevat de status van de conflictanalyse.
	 */
	private Status status;
	/**
	 * Bevat een boolean waarde die <code>true</code> is als het herschikken gebeurd is. Zo houdt men bij of de herschikking al gebeurd is.
	 */
	private boolean reorderingCommitted; // veiligheid, dat maar 1 keer kan worden doorgevoerd
	
	/**
	 * Initialiseert een nieuw <code>ConflictSolution</code> object.
	 */
	public ConflictSolution()
	{
		involvedRuleNrs = new  ArrayList< Integer >();
		solutionRuleNrs = new  ArrayList< Integer >();
		status = ConflictSolution.Status.NO_STATUS_YET;
		reorderingCommitted = false;
	}
	
	/**
	 * Voert de gevonden herschikking door in de regelset, indien een herschikking was gevonden.
	 * Als geen herschikking was gevonden heeft een aanroep geen effect.
	 * Als een herschikking reeds was doorgevoerd heeft een aanroep geen effect.
	 * @param ruleSet de firewall regelset waarvan een aantal regels herschikt worden
	 */
	public void commitSolution(ArrayList< Rule > ruleSet)
	{
		int aantal = involvedRuleNrs.size();
		if (aantal != solutionRuleNrs.size()) 	return;
		if (reorderingCommitted == true)	return;
		int aanwijzer;
		int teTestenPositie;
		int teVerifierenRuleNr;
		if (status == ConflictSolution.Status.SOLUTION_FOUND) // voor de zekerheid
		{
			int huidigRuleNr;
			int teZoekenRuleNr;
			int huidigPositie;
			for (int i = 0; i < (aantal-1); i++ ) // doorloop betrokkenRules
			{
				huidigPositie = involvedRuleNrs.get(i)-1;
				huidigRuleNr = ruleSet.get(huidigPositie).getRuleNr();
				teZoekenRuleNr = solutionRuleNrs.get(i);
			  if (huidigRuleNr != teZoekenRuleNr)
			  {
			  	// zoek de juiste regel en verwissel ze
			  	aanwijzer = i+1;
			  	boolean gevonden = false;
			  	while (gevonden == false && aanwijzer < aantal)
			  	{
			  		teTestenPositie = involvedRuleNrs.get(aanwijzer)-1;
			  		teVerifierenRuleNr = ruleSet.get(teTestenPositie).getRuleNr();
			  		if (teVerifierenRuleNr == teZoekenRuleNr)
				  	{
				  		Collections.swap(ruleSet, huidigPositie, teTestenPositie);
				  		gevonden = true;
				  	}
			  		else
			  			aanwijzer++;
			  	}
			    if (gevonden == false)
			    {
			    	 System.out.println("Er is een fout ontstaan !");  
			    	 return;
			    }
			  } // end if
			  // regelnummer aanpassen
			  ruleSet.get(huidigPositie).setRuleNr(huidigPositie + 1);
			} // end for
			// laatste regel nog nummer aanpassen
			huidigPositie = involvedRuleNrs.get(aantal - 1) - 1;
			ruleSet.get(huidigPositie).setRuleNr(huidigPositie + 1);
			reorderingCommitted = true;
		}
		return;
	}
	
	/**
	 * Voert de gevonden herschikking door in de regelset, indien een herschikking was gevonden.
	 * De gevonden herschikking wordt ook doorgevoerd in de regelset die dient om achteraf de regel terug om te zettin in iptables formaat.
	 * Als geen herschikking was gevonden heeft een aanroep geen effect.
	 * Als een herschikking reeds was doorgevoerd heeft een aanroep geen effect.
	 * @param ruleSet de firewall regelset waarvan een aantal regels herschikt worden
	 * @param exportRuleSet De model.importeer_exporteer RuleSet die achteraf gebruikt kan worden om de configuratie terug te exporteren naar iptables formaat.
	 * @param tableName Naam van de table waar de regels in zitten.
	 * @param chainName Naam van de chain waar de regels in zitten.
	 */
	public void commitSolutionInRulesAndExportRules(ArrayList< model.configuration.Rule > ruleSet, model.importeer_exporteer.RuleSet exportRuleSet, String tableName, String chainName)
	{
		int aantal = involvedRuleNrs.size();
		if (aantal != solutionRuleNrs.size()) 	return;
		if (reorderingCommitted == true)	return;
		int aanwijzer;
		int teTestenPositie;
		int teVerifierenRuleNr;
		if (status == ConflictSolution.Status.SOLUTION_FOUND) // voor de zekerheid
		{
			int huidigRuleNr;
			int teZoekenRuleNr;
			int huidigPositie;
			for (int i = 0; i < (aantal-1); i++ ) // doorloop betrokkenRules
			{
				huidigPositie = involvedRuleNrs.get(i)-1;
				huidigRuleNr = ruleSet.get(huidigPositie).getRuleNr();
				teZoekenRuleNr = solutionRuleNrs.get(i);
			  if (huidigRuleNr != teZoekenRuleNr)
			  {
			  	// zoek de juiste regel en verwissel ze
			  	aanwijzer = i+1;
			  	boolean gevonden = false;
			  	while (gevonden == false && aanwijzer < aantal)
			  	{
			  		teTestenPositie = involvedRuleNrs.get(aanwijzer)-1;
			  		teVerifierenRuleNr = ruleSet.get(teTestenPositie).getRuleNr();
			  		if (teVerifierenRuleNr == teZoekenRuleNr)
				  	{
				  		Collections.swap(ruleSet, huidigPositie, teTestenPositie);
				  		gevonden = true;
				  		
				  		//doe dit ook in iptables regels voor exporteren van configuratie
				  		model.importeer_exporteer.Table exportTable = exportRuleSet.getTable(RuleSet.TableType.getType(tableName));
						model.importeer_exporteer.Chain exportChain = exportTable.getChain(chainName);
						//wijzig volgorde van regels
						exportChain.repositionRule(huidigPositie+1, teTestenPositie+1);
				  	}
			  		else
			  			aanwijzer++;
			  	}
			    if (gevonden == false)
			    {
			    	 System.out.println("Er is een fout ontstaan !");  
			    	 return;
			    }
			  } // end if
			  // regelnummer aanpassen
			  ruleSet.get(huidigPositie).setRuleNr(huidigPositie + 1);
			} // end for
			// laatste regel nog nummer aanpassen
			huidigPositie = involvedRuleNrs.get(aantal - 1) - 1;
			ruleSet.get(huidigPositie).setRuleNr(huidigPositie + 1);
			reorderingCommitted = true;
		}
		return;
	}
	
	/**
	 * Geeft de status van de analyse terug.
	 * @return de status van de analyse
	 */
	public Status getStatus()
	{
		return status;
	}
	
	/**
	 * Geeft aan of de gevonden herschikking reeds in de regelset is doorgevoerd, geeft dit als een <code>boolean</code>.
	 * @return <code>true</code> als de herschikking heeft plaatsgevonden
	 * ANDERS <code>false</code>
	 */
	public boolean isReorderingCommitted()
	{
		return reorderingCommitted;
	}
	
	/**
	 * Stelt de status in die het resultaat is van de analyse naar een geschikte herschikking van de betrokken firewall regels.
	 * @param status de status die het resultaat is van de analyse
	 */
	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	/**
	 * Voegt een regelnummer toe aan de array die de betrokken regelnummers bevat.
	 * @param ruleNr het regelnummer dat toegevoegd wordt
	 */
	public void addInvolvedRuleNr(int ruleNr)
	{
		this.involvedRuleNrs.add(ruleNr);
	}
	
	/**
	 * Voegt een regelnummer toe aan de array die de regelnummers bevat in de volgorde zoals ze na herschikking zullen zijn.
	 * @param ruleNr het regelnummer dat toegevoegd wordt
	 */
	public void addSolutionRuleNr(int ruleNr)
	{
		this.solutionRuleNrs.add(ruleNr);
	}
	
	/**
	 * Geeft de regelnummers die betrokken zijn bij de herschikking als een <code>ArrayList< Integer ></code>.
	 * @return de regelnummers die betrokken zijn bij de herschikking
	 */
	public ArrayList< Integer > getInvolvedRuleNrs()
	{
		return involvedRuleNrs;
	}
	
	/**
	 * Geeft de regelnummers die betrokken zijn bij de herschikking in de volgorde zoals ze na de herschikking zullen staan. 
	 * @return de betrokken regelnummers na herschikking
	 */
	public ArrayList< Integer > getSolutionRuleNrs()
	{
		return solutionRuleNrs;
	}
}

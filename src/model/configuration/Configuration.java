package model.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import exception.CustomException;
import exception.ParseException;
import exception.UnsupportedDefaultActionException;
import exception.UnsupportedProtocolException;
import model.importeer_exporteer.RuleSet;
import static util.Constants.*;

/**
 * Deze klasse stelt een firewall configuratie voor.
 * 
 * @author Thomas Van Poucke
 */
public class Configuration implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Attribuut met de tables van de iptables configuratie.
	 */
	private List<Table> tables = new ArrayList<Table>();

	/**
	 * Attribuut dat model.importeer_exporteer.RuleSet bijhoudt. Wordt gebruikt
	 * om achteraf configuratie terug te kunnen exporteren naar iptables.
	 */
	private model.importeer_exporteer.RuleSet ruleSet = null;

	/**
	 * Maakt een nieuwe Configuration aan op basis van een <code>String</code>
	 * die een native iptables configuratie is.
	 * 
	 * @param nativeIptablesConfiguration
	 *            Een string die een native iptables configuratie is. Op basis
	 *            hiervan wordt de Configuration opgebouwd.
	 * @throws ParseException
	 * @throws CustomException
	 */
	public Configuration(String nativeIptablesConfiguration) throws ParseException,UnsupportedDefaultActionException,UnsupportedProtocolException {
		try {

			// Inhoud parsen
			ruleSet = RuleSet.parse(nativeIptablesConfiguration);

			// parse elke table, en voeg de opgeleverde chains met rules ed aan
			// table toe
			parseTable(ruleSet);
		} catch (model.importeer_exporteer.ParsingException ex) {
			// geef gepast foutmelding indien parse fout opgegooid werd omdat er
			// geen chains in de configuratie zitten
			// indien het een ParsingException is kan de regel waarop de
			// Exception gebeurd meegegeven worden.
			ParseException parseException = new ParseException(ex.getParsingMessage(), Integer.toString(ex.getLine()));
			// De tekst is geen key in I18N bestand
			parseException.setErrorsAreI18Nkeys(false);
			throw parseException;
		} catch(exception.UnsupportedProtocolException e){
			throw e;
		} catch (exception.UnsupportedDefaultActionException e){
				throw new UnsupportedDefaultActionException(e.getMessage());	
		} catch (Exception e) {
			throw new ParseException(T_UPLOAD_UNEXPECTED_EXCEPTION, null);
		}
	}

	/**
	 * Geeft de tables terug van deze iptables configuratie.
	 * 
	 * @return De tables van deze iptables configuratie.
	 */
	public List<Table> getTables() {
		return tables;
	}

	/**
	 * Geeft de table terug met opgegeven naam.
	 * 
	 * @param naam
	 *            Naam van de gewenste table.
	 * @return De gevraagde table, of <code>null</code> als er geen table is met
	 *         opgegeven naam.
	 */
	public Table getTable(String naam) {
		for (Table table : tables) {
			if (naam != null && naam.toLowerCase().equals(table.getName().toString().toLowerCase())) {
				return table;
			}
		}
		return null;
	}

	/**
	 * Voegt opgegeven table toe aan de configuratie.
	 * 
	 * @param table
	 *            De toe te voegen table.
	 */
	public void addTable(Table table) {
		tables.add(table);
	}

	// rule nr begint bij 1, en heeft geen vastgelegde index
	// bedoeld wordt vermoedelijk fixRuleNrs()
	// alleen de geselecteerde regelset moet worden bigewerkt
	/**
	 * Past de regel nummers van elke chain in elke tabel aan. Op te roepen
	 * nadat er een regel verwijderd is, of als er regels van plaats verwisseld
	 * zijn. De methode zorgt er dan voor dat de regel nummers correct blijven.
	 */
	public void fixRuleNrs() {
		for (Table table : this.getTables()) {
			for (Chain chain : table.getChains()) {
				ArrayList<Rule> rules = chain.getRules();

				int regelNr = 1;
				for (Rule rule : rules) {
					rule.setRuleNr(regelNr);
					regelNr++;
				}
			}
		}
	}

	/**
	 * Print elke regel van elke chain in elke tabel af naar de standaard
	 * uitvoer.
	 */
	public void printToConsole() {
		for (Table table : tables) {
			System.out.println("Tabel: " + table.getName());

			for (Chain chain : table.getChains()) {
				System.out.println("   Chain: " + chain.getName());
				System.out.println("      Policy: " + chain.getPolicy());

				if (chain.getRules() != null) {
					for (Rule rule : chain.getRules()) {
						System.out.println("      Rule: ");
						System.out.println("         Protocol: " + rule.getProtocol());
						System.out.println("         Source IP: " + rule.getSourceIP());
						System.out.println("         Destination IP: " + rule.getDestinationIP());
						System.out.println("         Source Port: " + rule.getSourcePort());
						System.out.println("         Destination Port: " + rule.getDestinationPort());
						System.out.println("         Action: " + rule.getAction());
					}
				}
			}
		}
	}

	/**
	 * Verkrijg waarde van attribuut ruleSet.
	 * 
	 * @return Verwijzing naar attribuut ruleSet.
	 */
	public model.importeer_exporteer.RuleSet getRuleSet() {
		return ruleSet;
	}

	/**
	 * Methode die model.importeer_exporteer RuleSet omzet in
	 * model.Configuration objecten.
	 * 
	 * @param ruleset
	 *            De RuleSet die men wil parsen naar model.Configuration
	 *            objecten.
	 * @throws CustomException
	 *             Wordt opgegooid als er een fout gebeurd bij het parsen.
	 * @throws ParseException
	 */
	private void parseTable(RuleSet ruleset) throws exception.ParseException,exception.UnsupportedProtocolException {
		// doe voor elke tabel in de configuratie. Alleen de standaard tabellen
		// FILTER, MANGLE, NAT, en RAW worden ondersteund
		// hulpvariabele om totaal aantal chains in configuratie bij te houden.
		// Als er geen enkel chain in de configuratie is, dan is er iets mis.
		// Met een configuratie zonder chains kan men geen regels
		// hebben/toevoegen.
		int nrOfChains = 0;
		for (model.importeer_exporteer.Table table : ruleset.getTables()) {
			Collection<model.importeer_exporteer.Chain> chains = table.getChains();
			// verkrijg soort tabel
			try {
				model.configuration.Table.TableName tableName = model.configuration.Table.TableName.valueOf(table.getName().toUpperCase());

				// maak model.configuration.Table aan van deze soort
				model.configuration.Table newTable = new Table(tableName);
				// voeg elke chain van tabel FILTER een voor een toe
				for (model.importeer_exporteer.Chain chain : chains) {
					// totaal aantal chains in configuratie plus 1
					nrOfChains++;
					String naamChain = chain.getName();
					// verkrijg standaard actie als string
					String standaardActie = chain.getDefaultPolicy().toString();
					// zet dit in een model.configuration.Chain.Policy
					Chain.PolicyAction policy = Chain.PolicyAction.valueOf(standaardActie);
					// maak Chain aan
					model.configuration.Chain newChain = new Chain(naamChain, policy);

					// maak Rules aan
					int ruleNr = 1;
					for (model.importeer_exporteer.Rule rule : chain) {
						// lees regel alleen in als de actie ACCEPT of DROP is -
						// andere acties worden niet ondersteund
						if (rule.actionSupported()) {
							// verkrijg protocol van regel
							int protocol = rule.getProtocol();
							//indien protocol null is wil dit zeggen dat er een niet ondersteunde protocol in de configuratie zit
							if(protocol < -2 || protocol > 255){
								throw new UnsupportedProtocolException(T_UPLOAD_UNSUPPORTED_PROTOCOL);
							}
							// verkrijg bron IP van regel
							IPPattern sourceIP = rule.getBronIP();
							// verkrijg doel IP van regel
							IPPattern destinationIP = rule.getDoelIP();
							// verkrijg bron poort van regel
							PortPattern sourcePort = rule.getBronpoort();
							// verkrijg doel poort van regel
							PortPattern destinationPort = rule.getDoelpoort();
							// verkrijg actie van regel
							boolean action = rule.getActie();

							// maak regel aan
							model.configuration.Rule newRule = new model.configuration.Rule(ruleNr, protocol, sourceIP, sourcePort, destinationIP, destinationPort, action);
							// voeg regel toe aan chain
							newChain.addRule(newRule);
							ruleNr++;
						}
					}

					// voeg chain aan table toe
					newTable.addChain(newChain);
				}
				// voeg table aan configuration toe
				this.addTable(newTable);

				// indien er geen enkele chain in de configuratie is : er is
				// iets mis. Met een configuratie zonder chain kan men geen
				// regels hebben/toevoegen
				if (nrOfChains == 0) {
					throw new ParseException(T_UPLOAD_NO_CHAINS, null);
				}
			} catch (IllegalArgumentException iae) {
				throw new ParseException(T_UPLOAD_INVALID_TABLE, null);
			} catch (CustomException ce) {
				ParseException pe = new ParseException(ce.getMessage(), null);
				pe.setErrorsAreI18Nkeys(false);
				throw pe;
			}catch (UnsupportedProtocolException e){
				throw e;
				// er is een andere - onverwachte - fout gebeurd tijdens parsen
			} catch (Exception e) {
				ParseException pe = new ParseException(e.getMessage(), null);
				pe.setErrorsAreI18Nkeys(false);
				throw pe;
			}
		}

	}

	/**
	 * Geeft true terug indien de configuratie een regel bevat met protocol ANY
	 * bevat en wel een poort. Dit is niet mogelijk in iptables. De export
	 * functie zal daardoor deze regel in 2 iptables regels omzetten: 1 voor tcp
	 * en 1 voor udp.
	 * 
	 * @return true indien de configuratie een regel bevat die geen protocol
	 *         bevat en wel een poort.
	 */
	public boolean containsRuleWithPortWithoutProtocol() {
		for (model.configuration.Table table : tables) {
			if (table.containsRuleWithPortWithoutProtocol()) {
				return true;
			}
		}
		return false;
	}

}

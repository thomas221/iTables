package model.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stelt een tabel van iptables voor.
 * 
 * @author Thomas Van Poucke
 * 
 */
public class Table implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Geeft aan om welke tabel het gaat. iTables ondersteund enkel de 4
	 * standaard aanwezige tabellen.
	 * 
	 * @author Thomas Van Poucke
	 * 
	 */
	public enum TableName {
		/**
		 * Tabel filter.
		 */
		FILTER,
		/**
		 * Tabel mangle.
		 */
		MANGLE,
		/**
		 * Tabel RAW
		 */
		RAW;

		/**
		 * Geeft TableName terug als<codeString></code> . Dit is de naam van de
		 * tabel.
		 * 
		 * @return Naam van de tabel.
		 */
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	/**
	 * Bevat de naam van de tabel. Dit is FILTER, MANGLE of RAW.
	 */
	private TableName name;
	/**
	 * Bevat de chains die horen tot deze tabel.
	 */
	private List<Chain> chains = new ArrayList<Chain>();

	/**
	 * Constructor om Table aan te maken
	 * 
	 * @param name
	 *            De naam van de table. Dit is ofwel FILTER, MANGLE of RAW.
	 */
	public Table(TableName name) {
		this.name = name;
	}

	/**
	 * Geeft de naam terug van deze tabel.
	 * 
	 * @return Naam van deze tabel.
	 */
	public TableName getName() {
		return name;
	}
	
	public String getNameUppercase() {
		return name.toString().toUpperCase();
	}

	/**
	 * Geeft de chains terug die horen tot deze tabel.
	 * 
	 * @return Chains die horen tot deze tabel.
	 */
	public List<Chain> getChains() {
		return chains;
	}

	/**
	 * Geeft de chain terug van deze tabel met opgegeven naam. Als deze niet
	 * blijkt te bestaan wordt <code>null</code> teruggegeven.
	 * 
	 * @param naam
	 *            Naam van gewenste chain van deze tabel.
	 * @return Chain met opgegeven naam, of <code>null</code> indien deze niet
	 *         bestaat.
	 */
	public Chain getChain(String naam) {
		for (Chain chain : chains) {
			if (naam != null && chain.getName().toString().toLowerCase().equals(naam.toLowerCase())) {
				return chain;
			}
		}
		return null;
	}

	/**
	 * Voegt opgegeven chain toe aan de lijst van chains die horen tot deze
	 * tabel.
	 * 
	 * @param chain
	 *            Toe te voegen chain.
	 */
	public void addChain(Chain chain) {
		chains.add(chain);
	}

	/**
	 * Geeft het totale aantal regels in de configuratie terug in deze table.
	 * Dus van alle chains van deze table tezamen.
	 * 
	 * @return Totale aantal regels in de configuratie van deze table. Dus
	 *         aantal regels van alle chains behorende bij deze table.
	 */
	public int getRuleCount() {
		int count = 0;
		for (Chain chain : chains) {
			if (chain.getRules() != null) {
				count += chain.getRules().size();
			}
		}
		return count;
	}

	/**
	 * Geeft true terug indien de table een regel bevat met protocol ANY bevat
	 * en wel een poort. Dit is niet mogelijk in iptables. De export functie zal
	 * daardoor deze regel in 2 iptables regels omzetten: 1 voor tcp en 1 voor
	 * udp.
	 * 
	 * @return true indien de table een regel bevat die geen protocol bevat en
	 *         wel een poort.
	 */
	public boolean containsRuleWithPortWithoutProtocol() {
		for (model.configuration.Chain chain : chains) {
			if (chain.containsRuleWithPortWithoutProtocol()) {
				return true;
			}
		}
		return false;
	}

}

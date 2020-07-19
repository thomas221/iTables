package model.configuration;

/**
 * De klasse<code> RuleSubspace </code> wordt gebruikt voor de property-toekenning 
 * van het deel van de firewall regel dat bevat wordt door een segment. </br>
 * In een algoritme dat zoekt naar redundante regels wordt die property gebruikt. </br>
 * Een regel subruimte is dus waar een segment als kolom en de firewall regel als rij een cel vormt, als in een tabel.
 * @author Ron Melger
 *
 */
public class RuleSubspace
{
  /**
   * Enumeratie type voor properties die toegekend worden aan RuleSubspace objecten. Die properties worden gebruikt bij het algoritme voor het vinden van redundanties.
   *
   */
  public enum Property{
	  /**
	   * Nog geen property toegekend aan de subruimte. Hierop wordt geinitialiseerd.
	   */
	  NO_PROPERTY_YET,
	  /**
	   * Property Strong Irremovable. Een regel met deze property voor een subruimte is zeker niet redundant.
	   */
	  SI,
	  /**
	   * Property Weak Irremovable. Indien een regel binnen een segment de Strong Irremovable eigenschap heeft zal de Weak Irremovable property toegekend worden aan de overige subruimtes van die regel.
	   */
	  WI,    
	  /**
	   * Property Removable. Geeft aan dat de subruimte mogelijk (niet zeker!) verwijderd kan worden.
	   */
	  R,                  
	  /**
	   * Property Correlated. Wordt toegekend aan verschillende rule subspaces binnen een segment, als de actie van dit segment door elk van deze regels bepaald kan worden.
	   */
	  C;                  
  }
  /**
   * Bevat het nummer van de firewall regel waar de subruimte onderdeel van is.
   */
  private int number;
  /**
   * Bevat de toegekende property van deze regel subruimte.
   */
  private Property property;
	
  /**
   * Initialiseert een nieuw <code>RuleSubspace</code> object.
   * @param number het regelnummer van de betreffende firewall regel
   */
  public RuleSubspace(int number)
  {
  	this.number = number;
  	property = Property.NO_PROPERTY_YET;
  }
  
  /**
   * Geeft het nummer van de firewall regel waar de subruimte onderdeel van is.
   * @return het nummer van de betreffende firewall regel
   */
  public int getNumber()
  {
  	return this.number; 
  }
  
  /**
   * Geeft de property die aan de regel subruimte is toegekend terug.
   * @return de property van de firewall regel subruimte
   */
  public Property getProperty()
  {
  	return this.property; 
  }
  
  /**
   * Stelt de property in die aan de regel subruimte wordt toegekend.
   * @param property de property van de firewall regel subruimte
   */
  public void setProperty(Property property)
  {
  	this.property = property; 
  }
}

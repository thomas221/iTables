package model.configuration;

import net.sf.javabdd.BDD;

/**
 * De klasse <code>DeniedAndAllowedSpace</code> is te gebruiken voor de opslag van de toegestane en geweigerde ruimtes.</br>
 * In het algemeen kan ieder tweetal BDD's hierin worden opgeslagen.
 * @author Ron Melger
 *
 */
public class DeniedAndAllowedSpace
{
	/**
	 * BDD die gedeelte van de packet space voorstelt die pakketten weigert.
	 */
	private BDD deniedSpace;
	/**
	 * BDD die gedeelte van de packet space voorstelt die pakketten weigert.
	 */
	private BDD allowedSpace; 
	
	/**
	 * Initialiseert een nieuw <code>DeniedAndAllowedSpace</code> object.
	 */
	public DeniedAndAllowedSpace()
	{}
	
	/**
	 * Geeft de geweigerde ruimte als een BDD.
	 * @return de geweigerde ruimte
	 */
	public BDD getDeniedBDD()
	{
		return deniedSpace;
	}
	
	/**
	 * Geeft de toegestane ruimte als een BDD.
	 * @return de toegestane ruimte
	 */
	public BDD getAllowedBDD()
	{
		return allowedSpace;
	}
	
	/**
	 * Stelt de waarde van de geweigerde ruimte in.
	 * @param deniedSpace de waarde van de geweigerde ruimte
	 */
	public void setDeniedBDD(BDD deniedSpace)
	{
		this.deniedSpace = deniedSpace;
	}
	
	/**
	 * Stelt de waarde van de toegestane ruimte in.
	 * @param allowedSpace de waarde van de toegestane ruimte
	 */
	public void setAllowedBDD(BDD allowedSpace)
	{
		this.allowedSpace = allowedSpace;
	}
}

package exception;

import java.util.ArrayList;

/**
 * Deze klasse breidt Exception uit zodat deze ook een lijst strings van fouten kan bevatten, en een lijst strings van parameters.
 *
 */
public class ExtendableException extends Exception {
    private ArrayList<String> errors;
    private ArrayList<String> parameters;
    private boolean errorsAreI18Nkeys = true;

    /**
     * Maak een ExtendedException met opgegeven string als fout.
     * @param error Opgegeven fout.
     */
    public ExtendableException(String error) {
	this.errors = new ArrayList<String>();
	this.errors.add(error);
    }
    
    /**
     * Maak een ExtendedException met opgegeven string als fout en een lijst van strings met parameters.
     * @param error Opgegeven fout.
     * @param parameters Lijst van parameters.
     */
    public ExtendableException(String error, ArrayList<String> parameters) {
	this.errors = new ArrayList<String>();
	this.errors.add(error);
	this.parameters = parameters;
    }
    
    /**
     * Maak een ExtendedException met opgegeven string error als fout en opgegeven string parameter als parameter.
     * @param error Opgegeven fout als string.
     * @param parameter Opgegeven parameter als string.
     */
    public ExtendableException(String error, String parameter) {
	this.errors = new ArrayList<String>();
	this.errors.add(error);
	this.parameters = new ArrayList<String>();
	this.parameters.add(parameter);
    }

    /**
     * Maak een ExtendedException met opgegeven lijst van strings als de fouten.
     * @param errors Opgegeven fouten.
     */
    public ExtendableException(ArrayList<String> errors) {
	this.errors = errors;
    }

    /**
     * Geef alle fouten terug.
     * @return Lijst van alle fouten.
     */
    public ArrayList<String> getErrors() {
	return errors;
    }
    
    /**
     * Geef alle parameters terug.
     * @return Lijst van alle parameters.
     */
    public ArrayList<String> getParameters() {
	return parameters;
    }

    /**
     * Geef de fout terug. Als er meerdere fouten zijn wordt de eerste fout in de lijst teruggegeven. Als er geen fout is wordt null teruggegeven.
     * @return Fout behorende bij ExtendedException. Als er meerdere fouten zijn wordt de eerste fout in de lijst teruggegeven. Als er geen fout is wordt null teruggegeven.
     */
    public String getError() {
	if (errors != null && errors.size() > 0) {
	    return errors.get(0);
	} else {
	    return null;
	}
    }
    
    /**
     * Geef de parameter terug. Als er meerdere parameters zijn wordt de eerste parameter in de lijst teruggegeven. Als er geen parameter is wordt null teruggegeven.
     * @return Parameter behorende bij ExtendedException. Als er meerdere parameters zijn wordt de eerste parameter in de lijst teruggegeven. Als er geen parameter is wordt null teruggegeven.
     */
    public String getParameter() {
	if (parameters != null && parameters.size() > 0) {
	    return parameters.get(0);
	} else {
	    return null;
	}
    }

    /**
     * Geeft terug of de errors letterlijke teksten zijn (false) of een verwijzing zijn naar keys in de i18n text.properties bestand (true).
     * @return true indien errors keys zijn in i18n bestand. False indien errors letterlijke teksten zijn.
     */
	public boolean getErrorsAreI18Nkeys() {
		return errorsAreI18Nkeys;
	}

	/**
	 * Geef aan of de errors letterlijke teksten zijn (false) of verwijzingen zijn naar keys in de i18n text.properties bestand (true).
	 * Op false zetten is af te raden. In de model.importeer_exporteer package staan echter exceptions die geen gebruik maken van keys in het i18n bestand.
	 * Dit omdat deze package afkomstig is van derden. Voor het weergeven van deze Exception foutmeldingen kan attribuut errorsAreI18NKeys op false gezet worden.
	 * @param errorsAreI18Nkeys Zet op false indien errors letterlijke teksten zijn. Zet op true indien errors verwijzingen zijn naar keys in de i18n text.properties bestanden.
	 */
	public void setErrorsAreI18Nkeys(boolean errorsAreI18Nkeys) {
		this.errorsAreI18Nkeys = errorsAreI18Nkeys;
	}

}

package model.configuration;

import java.util.ArrayList;

/**
 * De klasse <code>ConflictStatus</code> houdt van conflicterende segmenten bij
 * welke de policy allowed respectievelijk denied als wenselijk hebben. Er zijn
 * twee lijsten, een voor segmenten waarvoor het wenselijk is dat de policy
 * 'Allowed' is en een voor segmenten waarvoor het wenselijk is dat de policy
 * 'Denied' is.<br />
 * De 4 mogelijkheden voor combinaties van policy van segment en status: <br />
 * De policy is Allow, status = APPROVED_YES dan komt segment in lijst
 * 'Allowed'.<br />
 * De policy is Allow, status = APPROVED_NO dan komt segment in lijst 'Denied'.<br />
 * De policy is Deny, status = APPROVED_YES dan komt segment in lijst' Denied'.<br />
 * De policy is Deny, status = APPROVED_NO dan komt segment in lijst 'Allowed'.<br />
 * Als de status = APPOVED_UNKNOWN dan komt het segment in geen van beide
 * lijsten voor.
 * 
 * @author Ron Melger
 * 
 */
public class ConflictStatus {
	/**
	 * Enumeratie klasse die de mogelijke statussen van de conflicterende
	 * segmenten bevat. Geeft aan of men akkoord is met de policy van een
	 * conflicterend segment.
	 * 
	 */
	public enum Status {
		/**
		 * Er is geen duidelijkheid over of de policy van het conflicterende
		 * segment, dat is de action van de regel in het segment met de laagste
		 * index, wel of niet akkoord is bevonden.<br />
		 * Hierop initialiseren.
		 */
		APPROVED_UNKNOWN,
		/**
		 * De policy van het conflicterende segment, dat is de action van de
		 * regel in het segment met de laagste index, is akkoord bevonden.
		 */
		APPROVED_YES,
		/**
		 * De policy van het conflicterende segment, dat is de action van de
		 * regel in het segment met de laagste index, is NIET akkoord bevonden.
		 */
		APPROVED_NO

	}

	/**
	 * Bevat de Strings van de BDD van een segment in disjuncte normaalvorm, 
	 * de Strings waarvan men wenst dat de policy doorlaten is.
	 */
	private ArrayList<String> segments_Allowed;
	
	/**
	 * Bevat de Strings van de BDD van een segment in disjuncte normaalvorm,
	 * de Strings waarvan men wenst dat de policy weigeren is.
	 */
	private ArrayList<String> segments_Denied;

	/**
	 * Initialiseert een nieuw <code>ConflictStatus</code> object.
	 */
	public ConflictStatus() {
		segments_Allowed = new ArrayList<String>();
		segments_Denied = new ArrayList<String>();
	}

	/**
	 * Geeft de status van het conflicterende segment terug.
	 * 
	 * @param segment
	 *            het conflicterende segment waarvan men de status wilt hebben.
	 * @return de status van het conflicterende segment
	 */
	public Status getStatus(Segment segment) {
		boolean actionSegment = segment.getRule(0).getAction();
		String stringBDD = segment.getSegmentBDD().toString();
		// String stringBDD = bdd.toString();
		// kijk of stringBDD in Allowed voorkomt
		boolean aanwezigInAllowed = false;

		for (String string_Allowed : segments_Allowed) {
			if (string_Allowed.equals(stringBDD)) {
				aanwezigInAllowed = true;
			}
		}

		boolean aanwezigInDenied = false;

		for (String string_Denied : segments_Denied) {
			if (string_Denied.equals(stringBDD)) {
				aanwezigInDenied = true;
			}
		}

		if (((aanwezigInAllowed == true) && (actionSegment == true)) || ((aanwezigInDenied == true) && (actionSegment == false)))
			return Status.APPROVED_YES;

		if (((aanwezigInAllowed == true) && (actionSegment == false)) || ((aanwezigInDenied == true) && (actionSegment == true)))
			return Status.APPROVED_NO;

		return Status.APPROVED_UNKNOWN;
	}

	/**
	 * Stelt de status van het conflicterende segment in.
	 * 
	 * @param segment
	 *            het conflicterende segment
	 * @param status
	 *            de status van het conflicterende segment
	 */
	public void setStatus(Segment segment, Status status) {
		String stringBDD = segment.getSegmentBDD().toString();
		boolean actionSegment = segment.getRule(0).getAction();

		if (status == Status.APPROVED_UNKNOWN) {
			// haal stringBDD uit segments_Allowed en uit segments_Denied
			boolean gevonden = false;
			int index = 0;
			while ((gevonden == false) && (index < segments_Allowed.size())) {
				if (segments_Allowed.get(index).equals(stringBDD)) {
					segments_Allowed.remove(index);
					gevonden = true;
				} else
					index++;
			}

			if (gevonden == false) {
				index = 0;
				while ((gevonden == false) && (index < segments_Denied.size())) {
					if (segments_Denied.get(index).equals(stringBDD)) {
						segments_Denied.remove(index);
						gevonden = true;
					} else
						index++;
				}
			}
		} else // status == Status.APPROVED_YES of status == Status.APPROVED_NO
		{
			ArrayList<String> toevoegen;
			ArrayList<String> verwijderen;

			if ((status == Status.APPROVED_YES && actionSegment == true) || (status == Status.APPROVED_NO && actionSegment == false)) {
				toevoegen = segments_Allowed;
				verwijderen = segments_Denied;
			} else {
				toevoegen = segments_Denied;
				verwijderen = segments_Allowed;
			}

			// verwijder uit verwijderen (als het daarin voorkomt)
			boolean gevondenVerwijderen = false;
			int index = 0;
			while ((gevondenVerwijderen == false) && (index < verwijderen.size())) {
				if (verwijderen.get(index).equals(stringBDD)) {
					verwijderen.remove(index);
					gevondenVerwijderen = true;
				} else
					index++;
			}

			boolean gevondenToevoegen = false;
			if (gevondenVerwijderen == false) {
				// kijk of het in toevoegen al voorkomt
				index = 0;
				while ((gevondenToevoegen == false) && (index < toevoegen.size())) {
					if (toevoegen.get(index).equals(stringBDD)) {
						gevondenToevoegen = true;
					} else
						index++;
				}
			}
			// voeg toe aan toevoegen
			if (gevondenToevoegen == false) {
				toevoegen.add(stringBDD);
			}
		}
	}
	
	// voor test en debug doeleinden
	public int getSizeAllowed()
	{
		return segments_Allowed.size();
	}
	
  //voor test en debug doeleinden
	public int getSizeDenied()
	{
		return segments_Denied.size();
	}
	
	// voor test en debug doeleinden
	public void printArrayLists() {
		System.out.println("segments_Allowed");
		for (String string : segments_Allowed) {
			System.out.println(string);
		}
		System.out.println("segments_Denied");
		for (String string : segments_Denied) {
			System.out.println(string);
		}
	}
}

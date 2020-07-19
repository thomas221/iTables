package model.logfile;

public enum LogMessageType {
	START_EMPTY_CONFIGURATION("logmessagetype.start_empty_configuration"), START_EXAMPLE_CONFIGURATION("logmessagetype.start_example_configuration"), START_UPLOADED_CONFIGURATION(
			"logmessagetype.start_uploaded_configuration"), ADD_FIREWALL_RULE("logmessagetype.add_firewall_rule"), EDIT_FIREWALL_RULE("logmessagetype.edit_firewall_rule"), DELETE_FIREWALL_RULE(
			"logmessagetype.delete_firewall_rule"), SEGMENT_VOORKEURSACTIES_WIJZIGEN("logmessagetype.segment_voorkeursacties_wijzigen"), REMOVE_REDUNDANT_RULES("logmessagetype.remove_redundant_rules"), SET_DEFAULT_POLICY("logmessagetype.set_default_policy");

	private String i18n;
	private String translation;

	private LogMessageType(String i18n) {
		this.i18n = i18n;
	}

	public String getI18n() {
		return i18n;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getTranslation() {
		return translation;
	}

}

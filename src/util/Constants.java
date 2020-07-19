package util;

import static util.Constants.V_ACTION_EDIT_PROTOCOL_WITH_MODAL;

import java.io.File;

/**
 * Deze klasse bevat constanten die in andere klassen gebruikt kunnen worden.
 */
public class Constants {
	// Versie van layoutbestanden
	public static final String VERSION_LAYOUT_FILES = "1.15";

	// Is de applicatie in beta-fase of debug-modus?
	public static final boolean BETA = true;
	public static final boolean DEBUG = false;

	// Constanten voor HTTP headers
	public static final int HEADER_EXPIRES_VALUE = 0;
	public static final String HEADER_PRAGMA_VALUE = "no-cache";
	public static final String HEADER_CACHE_CONTROL_VALUE = "private, no-store, no-cache, must-revalidate";
	public static final String HEADER_EXPIRES_NAME = "Expires";
	public static final String HEADER_PRAGMA_NAME = "Pragma";
	public static final String HEADER_CACHE_CONTROL_NAME = "Cache-Control";

	// Constanten voor parameters
	public static final String P_REDIRECT = "redirect";
	public static final String P_ALERT_MESSAGES = "alert_messages";
	public static final String P_ALERT_PARAMETERS = "alert_parameters";
	public static final String P_ALERT_MESSAGE_TYPE = "alert_message_type";
	public static final String P_FLASH_SCOPE_MAP = "flash_scope_map";
	public static final String P_UPLOAD_FILENAME = "upload_filename";
	public static final String P_CONFIGURATION = "configuration";
	public static final String P_INITIAL_CONFIGURATION = "initial_configuration";
	public static final String P_LOGFILE = "logfile";
	public static final String P_LIST_EXAMPLE_CONFIGURATIONS = "example_configurations";
	public static final String P_EXAMPLE_FILENAME = "example_filename";
	public static final String P_CHAIN = "chain";
	public static final String P_TABLE = "table";
	public static final String P_ACTION = "action";
	public static final String P_RULES = "rules";
	public static final String P_SPACE = "space";
	public static final String P_CORRELATION_GROUPS = "setOfGroups";
	public static final String P_SEGMENTS = "segments";
	public static final String P_MODAL_DELETE_RULE_INDEX = "modal-delete-rule-index";
	public static final String P_MODAL_EDIT_RULE_INDEX = "modal-edit-rule-index";
	public static final String P_EDITABLE_PK = "pk";
	public static final String P_EDITABLE_NAME = "name";
	public static final String P_EDITABLE_VALUE = "value";
	public static final String P_SEGMENT_NUMBER = "segment_number";
	public static final String P_COMPACT = "compact";
	public static final String P_FILE = "file";
	public static final String P_PORT_NO_PROTOCOL = "portnoprotocol";

	// Constanten voor mogelijke waarden van parameters
	public static final String V_ACTION_ADD_RULE = "add";
	public static final String V_ACTION_EDIT_RULE = "edit";
	public static final String V_ACTION_EDIT_RULE_WITH_MODAL = "edit_modal";
	public static final String V_ACTION_EDIT_RULE_ORDER = "edit_order";
	public static final String V_ACTION_EDIT_POLICY = "edit_policy";
	public static final String V_ACTION_DELETE_RULE = "delete";
	public static final String V_ACTION_EDIT_RULE_PROTOCOL = "protocol";
	public static final String V_ACTION_EDIT_RULE_SOURCE_IP = "source-ip";
	public static final String V_ACTION_EDIT_RULE_SOURCE_PORT = "source-port";
	public static final String V_ACTION_EDIT_RULE_DESTINATION_IP = "destination-ip";
	public static final String V_ACTION_EDIT_RULE_DESTINATION_PORT = "destination-port";
	public static final String V_ACTION_EDIT_RULE_ACTION = "action";
	public static final String V_ACTION_GET_ACTIONS = "get_actions";
	public static final String V_ACTION_GET_PROTOCOLS = "get_protocols";
	public static final String V_ACTION_EDIT_PROTOCOL_WITH_MODAL = "modal_choose_protocol";
	public static final String V_ACTION_DOWNLOAD_PDF_LOGFILE = "download_pdf";
	public static final String V_ACTION_DOWNLOAD_EXCEL_LOGFILE = "download_excel";
	public static final String V_IPTABLES = "iptables";
	public static final String V_ACTION_MATCH = "match";
	
	// Constanten voor letterlijke waarden
	public static final int RESPONSE_STATUS_CODE_GOOD_REQUEST = 200;
	public static final int RESPONSE_STATUS_CODE_BAD_REQUEST = 400;
	public static final String TRUE = "true";
	public static final String FLASH = "flash.";
	public static final String CONTENT_DISPOSITION = "content-disposition";
	public static final String FILENAME = "filename";
	public static final String EQUALS = "=";
	public static final String POINT_COMMA = ";";
	public static final String EMPTY_STRING = "";
	public static final String DOUBLE_QUOTE_WITH_BACKSLASH = "\"";
	public static final String UTF_8 = "UTF-8";
	public static final String CONTENT_PLAIN_TEXT = "text/plain";
	public static final String CONTENT_APPLICATION_ZIP = "application/zip";
	public static final String CONTENT_APPLICATION_JSON = "application/json";
	public static final String CONTENT_APPLICATION_EXCEL = "application/vnd.ms-excel";
	public static final String CONTENT_TEXT = "text/plain";

	public static final String CONTENT_APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String HEADER_CONTENT_DISPOSITION_KEY = "Content-Disposition";
	public static final String HEADER_CONTENT_DISPOSITION_VALUE = "attachment;filename=itables_export_";
	public static final String EXPORT_FILENAME_INITIAL_CONFIGURATION = "initial_configuration.iptables";
	public static final String EXPORT_FILENAME_MOST_RECENT_CONFIGURATION = "most_recent_configuration.iptables";
	public static final String EXPORT_FILENAME_ZIP_EXTENSION = ".zip";
	public static final String EXPORT_FILENAME_SUMMARY = "summary.pdf";
	public static final String LOGFILE_FILENAME = "iTables_export";
	public static final String LOGFILE_EXPORTFILE_FOOTER = "Geexporteerd op ";
	public static final String LOGFILE_VARIABLE_FOOTER = "footer";
	public static final String LOGILE_VARIABLE_LOGS = "logs";
	public static final String LOGFILE_VARIABLE_TABLES_ANOMALIES = "tablesAnomalies";
	public static final String LOGFILE_VARIABLE_TABLES_FINAAL = "tablesFinaal";
	public static final String LOGFILE_VARIABLE_TABLES_INITIEEL = "tablesStart";
	public static final String LOGFILE_EXPORT_FILE_TYPE_EXCEL = ".xls";
	public static final String CONFIGURATION_EXPORT_FILE_TYPE_IPTABLES = ".iptables";
	public static final String CONFIGURATION_ACTION_ACCEPT = "configuration.action.accept";
	public static final String CONFIGURATION_ACTION_REJECT = "configuration.action.reject";
	public static final String CONFIGURATION_ACTION_ACCEPT_CSS_CLASS_TR = "success";
	public static final String CONFIGURATION_ACTION_REJECT_CSS_CLASS_TR = "danger";
	public static final String CONFIGURATION_ACTION_ACCEPT_CSS_CLASS_LABEL = "label-success";
	public static final String CONFIGURATION_ACTION_REJECT_CSS_CLASS_LABEL = "label-danger";
	public static final String PROTOCOL_TCP = "configuration.protocol.tcp";
	public static final String PROTOCOL_UDP = "configuration.protocol.udp";
	public static final String PROTOCOL_ICMP = "configuration.protocol.icmp";
	public static final String PROTOCOL_ANY = "configuration.protocol.any";
	public static final String PROTOCOL_NOT_SPECIFIED = "configuration.protocol.not_specified";
	public static final String PATH_SEPARATOR = File.separator;
	public static final String FILENAME_EMPTY_CONFIGURATION = "testconfiguraties" + PATH_SEPARATOR + "empty_configuration.iptables";
	public static final String EXAMPLE_CONFIGURATION_PATH = "example_configurations" + PATH_SEPARATOR;
	public static final String EXAMPLE_CONFIGURATION_PATH_FROM_WEB_INF = "/WEB-INF" + PATH_SEPARATOR + "classes" + PATH_SEPARATOR + "example_configurations";
	public static final String EXAMPLE_CONFIGURATION_FILENAME_EXTENSION = ".iptables";
	public static final long FILE_SIZE_MAX = 262144000; //Dit is 250 MB, oftewel 250*1024*1024 bytes
	public static final String EXPORT_IPTABLES_FILE_PATH = "iptables_export"+PATH_SEPARATOR+"export.iptables";
	
	// Constanten voor exporteerbare configuratie
	public static final String EXPORT_RULESET = "export_ruleset";
	
	// Constanten voor parsen
	public static final String T_UPLOAD_NOT_MULTIPART = "upload.not_multipart";
	public static final String T_UPLOAD_NOT_A_FILE = "upload.not_a_file";
	public static final String T_UPLOAD_EMPTY_FILE = "upload.empty_file";
	public static final String T_UPLOAD_WRONG_TYPE = "upload.wrong_type";
	public static final String T_UPLOAD_NO_CHAINS = "upload.no_chains";
	public static final String T_UPLOAD_INVALID_TABLE = "upload.invalid_table";
	public static final String T_UPLOAD_UNEXPECTED_EXCEPTION = "upload.unexpected_exception";
	public static final String T_UPLOAD_UNSUPPORTED_PROTOCOL = "upload.unsupported_protocol";
	public static final String T_UNSUPPORTED_DEFAULT_ACTION = "upload.unsupported_default_action";
	
	// Constanten voor i18n-tekst
	public static final String T_CONFIGURATION_UPLOAD_SUCCESS = "configuration.upload.success";
	public static final String T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_OPTIONS = "configuration.upload.unsupported_iptables_options";
	public static final String T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_TABLES = "configuration.upload.unsupported_iptables_tables";
	public static final String T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_CHAINS = "configuration.upload.unsupported_iptables_chains";
	public static final String T_CONFIGURATION_UPLOAD_UNSUPPORTED_IPTABLES_UNSUPPORTED_ACTION = "configuration.upload.unsupported_iptables_action";
	public static final String T_CONFIGURATION_NEW_SUCCESS = "configuration.new.success";
	public static final String T_CONFIGURATION_EXAMPLE_SUCCESS = "configuration.example.success";
	public static final String T_CONFIGURATION_DELETE_SUCCESS = "configuration.delete.success";
	public static final String T_CONFIGURATION_SYNTAX_INVALID = "configuration.syntax.invalid";
	public static final String T_RULE_BAD_ACTION = "configuration.bad_action";
	public static final String T_RULE_NO_ACTION = "configuration.no_action";
	public static final String T_FILE_NOT_FOUND = "file.not_found";
	public static final String T_NO_EXAMPLE_CONFIGURATION_SELECTED = "configuration.no_example_configuration_selected";
	public static final String T_CHAIN_OR_TABLE_DOES_NOT_EXIST = "configuration.bad_chain_or_table";
	public static final String T_CONFIGURATION_STOP_SUCCESS = "configuration.stop.success";
	public static final String T_NO_VALID_CONFIGURATION_IN_SESSION = "no.valid.configuration.in.session";
	public static final String T_ADD_RULE_FAILED = "my_configuration.add_rule.failed";
	public static final String T_ADD_RULE_SUCCESS = "my_configuration.add_rule.success";
	public static final String T_EDIT_RULE_FAILED = "my_configuration.edit_rule.failed";
	public static final String T_EDIT_RULE_SUCCESS = "my_configuration.edit_rule.success";
	public static final String T_DELETE_RULE_FAILED = "my_configuration.delete_rule.failed";
	public static final String T_DELETE_RULE_SUCCESS = "my_configuration.delete_rule.success";
	public static final String T_INVALID_SITUATION = "invalid_situation";
	public static final String I_INVALID_IP_PATTERN = "my_configuration.rule.invalid_ip_pattern";
	public static final String T_INVALID_PORT_PATTERN = "my_configuration.rule.invalid_port_pattern";
	public static final String T_LOGFILE_EXPORT_FAILED = "my_configuration.logfile.export_failed";
	public static final String T_FILE_TO_LARGE = "upload.file_to_large";
	public static final String T_MATCH_RULE_FAILED = "match.failed";
	public static final String T_MATCH_RULE_SUCCESS = "match.success";
	public static final String T_SET_DEFAULT_POLICY_FAILED = "set_default_policy.failed";
	public static final String T_SET_DEFAULT_POLICY_SUCCESS_ACCEPT = "set_default_policy.success.accept";
	public static final String T_SET_DEFAULT_POLICY_SUCCESS_DROP = "set_default_policy.success.drop";
	public static final String T_SET_DEFAULT_POLICY_SUCCESS_REJECT = "set_default_policy.success.reject";
	public static final String T_SET_PROTOCOL_MODAL_FAILED = "set_protocol_modal.failed";
	public static final String T_SET_PROTOCOL_MODAL_SUCCESS = "set_protocol_modal.success";

	// Constanten voor letterlijke tekst
	public static final String TEXT_ONGELDIGE_SITUATIE_OPGETREDEN = "Ongeldige situatie opgetreden.";

	// Constanten voor logberichten
	public static final String L_IN_DO_GET = "in doGet";
	public static final String L_IN_DO_POST = "in doPost";
	public static final String L_IN_DO_FILTER = "in doFilter";
	
	// Constanten voor response headers
	public static final String H_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String H_PRAGMA = "Pragma";
	public static final String H_CACHE_CONTROL = "Cache-Control";
	public static final String H_EXPIRES = "Expires";
	
	public static final String HV_CONTENT_DISPOSITION = "attachment; filename=";
	public static final String HV_PRAGMA = "public";
	public static final String HV_CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String HV_EXPIRES = "0";

	// Constanten voor instellingen
	public static final boolean I_COOKIE_SECURITY = false;

	public static final int I_COOKIE_LIFETIME = 3600 * 24 * 7; // 1 week
	public static final int I_COOKIE_LIFETIME_INVALIDATE = 0;
	public static final int I_GRENSWAARDE_AANTAL_TOONBARE_REGELS_VAN_SEGMENT = 400;

	public static final String I_DATEFORMAT_EXPORT_FILENAME = "dd-MM-yyyy";
	public static final String I_DATEFORMAT_LOGMESSAGE = "dd/MM/yyyy '-' HH:mm:ss";
	public static final String I_DATEFORMAT_LOGFILE_EXPORT = "dd_MM_yyyy_HH_mm_ss";
	public static final String I_DATEFORMAT_LOGFILE_EXPORT_FOOTER = "dd/MM/yyyy 'om' HH:mm";
	public static final String I_NAME_ROOTLOGGER = "";
	public static final String I_LOGFILE_EXPORT_FILE_TEMPLATE_PATH = "/WEB-INF/excel_template/template.xls";

	// Constanten voor paden van servlets
	public static final String SERVLET_START = "/start";
	public static final String SERVLET_ERROR = "/error";
	public static final String SERVLET_UNDER_CONSTRUCTION = "/under_construction";
	public static final String SERVLET_MY_CONFIGURATION = "/my_configuration";
	public static final String SERVLET_CONFIGURATION_RULES = "/my_configuration/rules";
	public static final String SERVLET_CONFIGURATION_ANOMALIES = "/my_configuration/anomalies";
	public static final String SERVLET_CONFIGURATION_SEGMENTS = "/configuration_segments";
	public static final String SERVLET_CONFIGURATION_LOGFILE = "/my_configuration/anomalies";

	// Constanten voor paden van JSP-pagina's
	public static final String JSP_HOME = "/WEB-INF/views/home.jsp";
	public static final String JSP_START = "/WEB-INF/views/start_configuration_overview.jsp";
	public static final String JSP_UPLOAD = "/WEB-INF/views/start_configuration_upload.jsp";
	public static final String JSP_EXAMPLE = "/WEB-INF/views/start_configuration_example.jsp";
	public static final String JSP_DOCUMENTATION = "/WEB-INF/views/documentation.jsp";
	public static final String JSP_CONTACT = "/WEB-INF/views/contact.jsp";
	public static final String JSP_ERROR = "/WEB-INF/views/error.jsp";
	public static final String JSP_UNDER_CONSTRUCTION = "/WEB-INF/views/under_construction.jsp";
	public static final String JSP_MY_CONFIGURATION_DASHBOARD = "/WEB-INF/views/my_configuration_dashboard.jsp";
	public static final String JSP_MY_CONFIGURATION_RULES = "/WEB-INF/views/my_configuration_rules.jsp";
	public static final String JSP_MY_CONFIGURATION_ANOMALIES = "/WEB-INF/views/my_configuration_anomalies.jsp";
	public static final String JSP_MY_CONFIGURATION_LOGFILE = "/WEB-INF/views/my_configuration_logfile.jsp";
}

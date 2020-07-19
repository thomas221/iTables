package model.logfile;

import java.io.Serializable;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;

import static util.Constants.*;

public class LogMessage implements Serializable {
	private int id;
	private DateTime timestamp;
	private LogMessageType type;
	private String message;

	public LogMessage(int id, DateTime timestamp, LogMessageType type, String message, String typeTranslation) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.type = type;
		this.message = message;
		this.type.setTranslation(typeTranslation);
	}

	public int getId() {
		return id;
	}

	public int getIdStartOne() {
		return id + 1;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public String getTimestampFormatted() {
		return timestamp.toString(I_DATEFORMAT_LOGMESSAGE);
	}

	public LogMessageType getType() {
		return type;
	}

	public String getTypeTranslated() {
		return type.getTranslation();
	}

	/**
	 * Verkrijg logbericht. Dit bericht bevat speciale karakters in zijn html notatie. Bijvoorbeeld ge&amp;uuml;pload en niet ge&uuml;pload.
	 * @return Logbericht.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Verkrijg logbericht. Dit bericht bevat speciale karakters in Unicode. Dus bijvoorbeeld ge&uuml;pload en niet ge&amp;uuml;pload.
	 * @return Logbericht.
	 */
	public String getMessageWithUnescapeHTML(){
		return StringEscapeUtils.unescapeHtml(message);
		
	}

}

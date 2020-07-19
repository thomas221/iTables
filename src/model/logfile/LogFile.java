package model.logfile;

import java.io.Serializable;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class LogFile implements Serializable {
	private ArrayList<LogMessage> messages;

	public LogFile() {
		this.messages = new ArrayList<LogMessage>();
	}

	public ArrayList<LogMessage> getLogs() {
		return messages;
	}

	public void log(LogMessageType type, String message, String typeTranslation) {
		int id = messages.size();
		DateTime timestamp = new DateTime();

		LogMessage logMessage = new LogMessage(id, timestamp, type, message, typeTranslation);

		messages.add(logMessage);
	}
}

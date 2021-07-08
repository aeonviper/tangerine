package core.utility;

public class Logger {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("core");

	public static org.slf4j.Logger getLogger() {
		return logger;
	}

}

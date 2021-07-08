package tangerine.core;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import tangerine.bean.MailTemplate;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;

public class Constant extends orion.core.Constant {

	public static final Integer minimumPasswordLength = 8;
	public static final Integer itemPerPage = 50;
	public static final Integer timeThreshold = 60 * 60;
	
	public static final String cookieLanguage = "tanyatutor-language";

	public static final String assetQuestion = "resource/asset/question/attachment/";

	public static final String applicationPropertiesFilename = "application.properties";
	public static final String languagePropertiesFilename = "language.properties";

	public static final String databaseUrl;
	public static final String databaseUsername;
	public static final String databasePassword;

	public static final MailTemplate mailTemplate = new MailTemplate();
	public static final Map<String, String> languageKeyMap;

	public static final String fileSystemPathAsset;

	public enum DeploymentMode {
		PRODUCTION, DEVELOPMENT;
	}

	public static DeploymentMode deploymentMode = DeploymentMode.PRODUCTION;

	public static final String mailHost = "smtp-relay.gmail.com";	
	public static final int mailSmtpPort = 587;
	public static final String mailAuthenticationUsername = "tanyatutor@cerahceria.com";
	public static String mailAuthenticationPassword = "password";

	static {
		URL url = null;
		Properties properties = new Properties();

		String propertyDatabaseUrl = null;
		String propertyDatabaseUsername = null;
		String propertyDatabasePassword = null;
		String propertyFileSystemPathAsset = null;

		Map<String, String> languageMap = new HashMap<>();

		try {
			url = Thread.currentThread().getContextClassLoader().getResource(applicationPropertiesFilename);
			if (url != null) {
				properties.load(url.openStream());
				propertyDatabaseUrl = properties.getProperty("database.url");
				propertyDatabaseUsername = properties.getProperty("database.username");
				propertyDatabasePassword = properties.getProperty("database.password");
				propertyFileSystemPathAsset = properties.getProperty("filesystem.path.asset");
			}

			url = Thread.currentThread().getContextClassLoader().getResource(languagePropertiesFilename);
			if (url != null) {
				properties = new Properties();
				properties.load(url.openStream());
				for (Language language : Language.values()) {
					for (LanguageKey languageKey : LanguageKey.values()) {
						String key = "languages." + language.getCode() + "." + languageKey.name();
						String property = properties.getProperty(key);
						if (property != null) {
							languageMap.put(key, property);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		databaseUrl = propertyDatabaseUrl;
		databaseUsername = propertyDatabaseUsername;
		databasePassword = propertyDatabasePassword;
		fileSystemPathAsset = propertyFileSystemPathAsset;

		languageKeyMap = Collections.unmodifiableMap(languageMap);

	}

}

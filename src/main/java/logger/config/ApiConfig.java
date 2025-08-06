package logger.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {

    private static final String configFile = "api.properties";
    private static ApiConfig instance;
    private Properties properties;

    private ApiConfig() {
        loadProperties();
    }

    //This method is a Singleton pattern implementation. making sure only one instance of the ApiConfig class exists
    // in the entire application.
    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();

        // Try to load the configFile from the classpath resources.
        // getClass().getClassLoader().getResourceAsStream
        //getResourceAsStream() looks for the file inside the compiled project resources
        //If found, it returns an InputStream 'is' to read the file. If not found, is will be null.
        //We use a try-with-resources block to make sure the InputStream is closed automatically after use.
        //If the InputStream is not null (file found), we load all key-value pairs from the file into the properties object
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (is != null) {
                properties.load(is);
                return;
            }
        } catch (IOException e) {
            System.err.println("Could not load " + configFile + " from resources: " + e.getMessage());
        }

        // Fallback to system properties and environment variables
        loadFromSystemAndEnvironment();
    }

    /**
     * Loads the Gemini API key from system properties or environment variables:
     * java -Dgemini.api.key=YOUR_KEY -jar MyApp.jar - command to set your api key in system property
     * 1. checks if a system property gemini.api.key is set
     * 2. If not found, checks the OS env "GEMINI_API_KEY".
     * 3. If the key is found in either location, it sets or overrides the gemini.api.key property
     *    in our local properties
     */
    private void loadFromSystemAndEnvironment() {
        // Check system properties first
        String geminiKey = System.getProperty("gemini.api.key");
        if (geminiKey == null) {
            // Check environment variables
            geminiKey = System.getenv("GEMINI_API_KEY");
        }

        if (geminiKey != null) {
            properties.setProperty("gemini.api.key", geminiKey);
        }
    }

    //getting and checking the gemini.api.key property
    //If the key is not foun or if there is a space or typo, it throws a RuntimeException
    public String getGeminiApiKey() {
        String key = properties.getProperty("gemini.api.key");
        if (key == null || key.trim().isEmpty()) {
            throw new RuntimeException("Gemini API key not found. Please set it in api.properties, " +
                    "system property 'gemini.api.key', or environment variable 'GEMINI_API_KEY'");
        }
        return key;
    }

    public boolean hasGeminiApiKey() {
        String key = properties.getProperty("gemini.api.key");
        return key != null && !key.trim().isEmpty();
    }
}


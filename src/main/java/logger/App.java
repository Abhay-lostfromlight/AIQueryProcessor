package logger;

import logger.config.ApiConfig;
import logger.queryprocessor.QueryProcessor;
import logger.queryprocessor.QueryProcessorFactory;
import logger.utils.FilePathCheck;

import java.io.File;
import java.util.Scanner;

/**
 * Interactive QueryProcessor Application
 *
 * This application provides intelligent log processing capabilities:
 * - Natural Language Processing (via Gemini AI)
 * - Direct log file processing (SELECT and FIND operations)
 */
public class App {

    private static final String BANNER = "\u001B[35m" + // Magenta color start
            "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n" +
            "▓ ██╗      ██████╗  ██████╗  ██████╗ ███████╗██████╗    █████╗ ██╗ ▓\n" +
            "▓ ██║     ██╔═══██╗██╔════╝ ██╔════╝ ██╔════╝██╔══██╗  ██╔══██╗██║ ▓\n" +
            "▓ ██║     ██║   ██║██║  ███╗██║  ███╗█████╗  ██████╔╝  ███████║██║ ▓\n" +
            "▓ ██║     ██║   ██║██║   ██║██║   ██║██╔══╝  ██╔══██╗  ██╔══██║██║ ▓\n" +
            "▓ ███████╗╚██████╔╝╚██████╔╝╚██████╔╝███████╗██║  ██║  ██║  ██║██║ ▓\n" +
            "▓ ╚══════╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝  ╚═╝  ╚═╝╚═╝ ▓\n" +
            "▓ >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ▓\n" +
            "▓ ◆ INTELLIGENT LOG PROCESSING SYSTEM                              ▓\n" +
            "▓ ◆ AI-POWERED NATURAL LANGUAGE QUERIES                            ▓\n" +
            "▓ ◆ DIRECT LOG FILE SEARCH & ANALYSIS                              ▓\n" +
            "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n" +
            "\n" +
            "           >> INTELLIGENT LOG PROCESSING READY <<\n" +
            "\u001B[0m"; // Reset color

    private static final String helpList = "\u001B[36m" + // Cyan color
            "\n--> AVAILABLE COMMANDS:\n" +
            "  \u001B[32m1. nlp    \u001B[36m- Natural language query processing (requires API key)\n" +
            "  \u001B[32m2. find   \u001B[36m- Search for specific terms in log files\n" +
            "  \u001B[32m3. select \u001B[36m- Execute SELECT queries on log files\n" +
            "  \u001B[32m4. demo   \u001B[36m- Run demonstration examples\n" +
            "  \u001B[32m5. help   \u001B[36m- Show this help message\n" +
            "  \u001B[32m6. exit   \u001B[36m- Exit the application\n" +
            "\n--> EXAMPLES:\n" +
            "  \u001B[33m• nlp\u001B[36m  → \u001B[37m'Show me all error logs from today'\n" +
            "  \u001B[33m• find\u001B[36m → \u001B[37m'error' (searches for 'error' in log files)\n" +
            "  \u001B[33m• select\u001B[36m → \u001B[37m'complex SELECT operations'\n" +
            "\n--> DIRECT SQL:\n" +
            "  \u001B[33m• \u001B[36mYou can also type SQL commands directly:\n" +
            "  \u001B[33m• \u001B[37mSELECT * FROM logs WHERE severity = 'ERROR'\n" +
            "\u001B[0m"; // Reset color

    private static Scanner scanner = new Scanner(System.in);
    private static String currentLogFile = null;

    public static void main(String[] args) {
        displayBanner();    // this will is for all the banner and help part
        checkConfiguration();   // to check if the API configuration and setting  is correct beforehand
        runInteractiveMode();   // this method starts the main interactive loop for user input, queries and all

    }

    private static void displayBanner() {
        System.out.println(BANNER);
    }

    //    to check if the API configuration like api.proerties file is present and if the API key exists
//    to check if api.properties are loaded without errors and, are all api.key, api.endpoint inside it
//    get an instance of ApiConfig
//    then validate gemini.api.key
    private static void checkConfiguration() {

        boolean hasApiKey = ApiConfig.getInstance().hasGeminiApiKey();
        if (hasApiKey) {
            System.out.println("\u001B[32m✓ AI Natural Language Processing: ENABLED\u001B[0m");
        } else {
            System.out.println("\u001B[33m! AI Natural Language Processing: DISABLED (No API key found)\u001B[0m");
            System.out.println("\u001B[90m  → Set GEMINI_API_KEY environment variable to enable AI features\u001B[0m");
        }
        System.out.println("\u001B[32m✓ SQL Parser: READY\u001B[0m");
        System.out.println("\u001B[32m✓ Log File Processor: READY\u001B[0m");
        System.out.println();
    }

    private static void runInteractiveMode() {
        // Ask for log file path first
        System.out.println("\u001B[36m First, let's set up your log file for analysis\u001B[0m");
        currentLogFile = getLogFileFromUser();
        if (currentLogFile == null) {
            System.out.println("\u001B[31m--> X Cannot proceed without a valid log file. Exiting...\u001B[0m");
            return;
        }

        System.out.println("\u001B[36mGreat! Now you can run queries on your log file.\u001B[0m");
        System.out.println("\u001B[90mType 'help' for available commands or 'exit' to quit\u001B[0m");

        while (true) {
            System.out.print("\u001B[35m[LogQuery]\u001B[0m \u001B[32m>\u001B[0m ");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.isEmpty()) {
                continue;
            }

            switch (command) {
                case "exit":
                case "quit":
                case "q":
                    System.out.println("\u001B[36mThank you for using LogQuery AI! Goodbye! 👋\u001B[0m");
                    return;

                case "help":
                case "h":
                    System.out.println(helpList);
                    break;

//                case "demo":
//                    runDemoMode();
//                    break;

                case "nlp":
                    handleNaturalLanguageQuery();
                    break;

                case "find":
                    handleFindQuery();
                    break;

                case "select":
                    handleSelectQuery();
                    break;

                default:
                    // Check if it's a SQL command (starts with SELECT, INSERT, UPDATE, DELETE)
                    if (command.toLowerCase().startsWith("select") ||
                            command.toLowerCase().startsWith("insert") ||
                            command.toLowerCase().startsWith("update") ||
                            command.toLowerCase().startsWith("delete")) {
                        handleDirectSQLQuery(command);
                    } else {
                        System.out.println("\u001B[31mUnknown command: '" + command + "'. Type 'help' for available commands.\u001B[0m");
                    }
                    break;
            }

            System.out.println(); // Add spacing between commands
        }
    }

    private static void handleNaturalLanguageQuery() {
        if (!ApiConfig.getInstance().hasGeminiApiKey()) {
            System.out.println("\u001B[31m❌ Natural Language Processing is not available.\u001B[0m");
            System.out.println("\u001B[33m💡 Please set up your Gemini API key to use this feature.\u001B[0m");
            return;
        }

        System.out.println("\u001B[36m--> Natural Language Query Mode\u001B[0m");
        System.out.println("\u001B[90mDescribe what you want to find in your logs using natural language\u001B[0m");
        System.out.print("\u001B[33mYour query:\u001B[0m ");

        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("\u001B[31mNo query provided.\u001B[0m");
            return;
        }

        try {
            QueryProcessor processor = QueryProcessorFactory.getProcessor("nlp");
            processor.process(query, currentLogFile);
        } catch (Exception e) {
            System.out.println("\u001B[31m❌ Error processing natural language query: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void handleFindQuery() {
        System.out.println("\u001B[36m--> Find Mode\u001B[0m");
        System.out.println("\u001B[90mSearch for specific terms in your log files\u001B[0m");
        System.out.print("\u001B[33mSearch term:\u001B[0m ");

        String term = scanner.nextLine().trim();
        if (term.isEmpty()) {
            System.out.println("\u001B[31mNo search term provided.\u001B[0m");
            return;
        }

        try {
            QueryProcessor processor = QueryProcessorFactory.getProcessor("find");
            processor.process("FIND " + term, currentLogFile);
        } catch (Exception e) {
            System.out.println("\u001B[31m❌ Error processing find query: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void handleSelectQuery() {
        System.out.println("\u001B[36m--> SELECT Query Mode\u001B[0m");
        System.out.println("\u001B[90mExecute SELECT queries on your log files\u001B[0m");
        System.out.print("\u001B[33mSELECT Query:\u001B[0m ");

        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("\u001B[31mNo query provided.\u001B[0m");
            return;
        }

        //if query starts with select then process it
        try {
            QueryProcessor processor = QueryProcessorFactory.getProcessor("select");
            processor.process(query, currentLogFile);
        } catch (Exception e) {
            System.out.println("\u001B[31m❌ Error processing SELECT query: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void handleDirectSQLQuery(String command) {
        System.out.println("\u001B[36m--> Direct SQL Execution\u001B[0m");
        System.out.println("\u001B[90mExecuting SQL query: " + command + "\u001B[0m");

        try {
            QueryProcessor processor = QueryProcessorFactory.getProcessor("select");
            processor.process(command, currentLogFile);
        } catch (Exception e) {
            System.out.println("\u001B[31m❌ Error executing SQL query: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static String getLogFileFromUser() {
        System.out.print("\u001B[33mLog file path:\u001B[0m ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            System.out.println("\u001B[31mNo file path provided.\u001B[0m");
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("\u001B[31m--> X File not found: " + filePath + "\u001B[0m");
            return null;
        }

        if (!file.canRead()) {
            System.out.println("\u001B[31m--> X Cannot read file: " + filePath + "\u001B[0m");
            return null;
        }

        System.out.println("\u001B[32m✓ Using log file: " + filePath + "\u001B[0m");
        return filePath;
    }
}
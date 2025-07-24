package logger;

import logger.queryprocessor.QueryProcessor;
import logger.queryprocessor.QueryProcessorFactory;
import logger.utils.FilePathCheck;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        final String BANNER = "\u001B[35m" + // Cyan color start
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n" +
                "▓ ██╗      ██████╗  ██████╗  ██████╗ ███████╗██████╗    █████╗ ██╗ ▓\n" +
                "▓ ██║     ██╔═══██╗██╔════╝ ██╔════╝ ██╔════╝██╔══██╗  ██╔══██╗██║ ▓\n" +
                "▓ ██║     ██║   ██║██║  ███╗██║  ███╗█████╗  ██████╔╝  ███████║██║ ▓\n" +
                "▓ ██║     ██║   ██║██║   ██║██║   ██║██╔══╝  ██╔══██╗  ██╔══██║██║ ▓\n" +
                "▓ ███████╗╚██████╔╝╚██████╔╝╚██████╔╝███████╗██║  ██║  ██║  ██║██║ ▓\n" +
                "▓ ╚══════╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝  ╚═╝  ╚═╝╚═╝ ▓\n" +
                "▓ >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ▓\n" +
                "▓ ◆ LOGGING SYSTEM: ACTIVE                                         ▓\n" +
                "▓ ◆ AI ANALYTICS: ENABLED                                          ▓\n" +
                "▓ ◆ DATA PIPELINE: STREAMING                                       ▓\n" +
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n" +
                "\n" +
                "           >> INTELLIGENT LOG PROCESSING READY <<\n" +
                "\u001B[0m"; // Reset color

        System.out.println(BANNER);
        String filePath = "";


        Scanner scanner = new Scanner(System.in);
        boolean filePathValid = false;

        while (true) {
            System.out.println("press '?' for help or enter the absolute file path");
            String input = scanner.next();
                switch(input) {
                    case "?":
                        System.out.println("FIND and RANGE query is supported");
                        System.out.println("SELECT * from log.test where data LIKE 'db connected' and TIME between ('', '')");
                        System.out.println("FIND 'hello world' FROM test.log");
                        break;

                    case "":
                        System.out.println("press '?' for help or enter the absolute file path");
                        break;

                    default:
                        if(FilePathCheck.validFilePath(input)){
                            filePathValid = true;
                        }
                        break;
            }
            if (filePathValid) {
                scanner.nextLine();
                break;
            }
        }
        while (true) {
            System.out.println("query: ");
            String query = scanner.nextLine();

            try {
                QueryProcessorFactory queryProcessorFactory = new QueryProcessorFactory();
                QueryProcessor processorFactory = queryProcessorFactory.getQueryProcessor(query);
            } catch (Exception e) {
                System.out.println("Please use valid query- 'select' or 'find' ");
            }
        }
    }
}
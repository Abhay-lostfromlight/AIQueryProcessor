package logger;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String asciilogger = "\033[35m\n" +
                " _                               _____  _____ \n" +
                "| |                             |  _  ||_   _|\n" +
                "| | ___   __ _  __ _  ___ _ __  | |_| |  | |  \n" +
                "| |/ _ \\ / _` |/ _` |/ _ \\ '__| |  _  |  | |  \n" +
                "| | (_) | (_| | (_| |  __/ |    ||  | |  | |  \n" +
                "|_|\\___/ \\__, |\\__, |\\___|_|    |_| |_| |___|\n" +
                "          __/ | __/ |                         \n" +
                "         |___/ |___/                          \n" +
                "\033[0m";

        System.out.println(asciilogger);

        System.out.println("press '?' for help or enter the absolute file path");
        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        switch(input){
            case "?":
                System.out.println("FIND and RANGE query is supported");
                System.out.println("SELECT * from log.test where data LIKE 'db connected' and TIME between ('', '')");
                System.out.println("FIND 'hello world' FROM test.log");

        }
    }
}
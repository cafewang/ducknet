package org.example;

import org.apache.commons.cli.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        Option dumpTopology = new Option("d", "dump", false, "dump topology");
        options.addOption(dumpTopology);

        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        Scanner scanner = new Scanner(System.in);
        try {
            cmd = parser.parse(options, scanner.nextLine().split("\\s"));
            if (cmd.hasOption("d")) {
                System.out.println("ddddd");
            }
        } catch (ParseException e) {
            helper.printHelp("Usage:", options);
            throw new RuntimeException(e);
        }
    }
}

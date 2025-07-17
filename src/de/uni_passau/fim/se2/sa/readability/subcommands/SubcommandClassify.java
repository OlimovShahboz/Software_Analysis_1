package de.uni_passau.fim.se2.sa.readability.subcommands;

import picocli.CommandLine;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "classify", description = "Classifies Java code for readability")
public class SubcommandClassify implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "The code snippet to classify")
    private String code;

    @Override
    public Integer call() throws Exception {
        // Dummy classifier logic
        System.out.println("Classifying code snippet: " + code);
        // Normally, you'd call feature metrics here
        return 0;
    }
}

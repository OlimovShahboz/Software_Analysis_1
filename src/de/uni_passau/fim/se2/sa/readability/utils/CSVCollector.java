package de.uni_passau.fim.se2.sa.readability.utils;

import de.uni_passau.fim.se2.sa.readability.features.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CSVCollector {

    private static final double TRUTH_THRESHOLD = 3.6;

    public List<String> collectCSVBody(Path sourceDir, File truthFile, List<FeatureMetric> featureMetrics) throws IOException {
        List<String> csvLines = new ArrayList<>();

        Map<String, Double> truthMap = Files.lines(truthFile.toPath())
                .skip(1)
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        tokens -> tokens[0].trim(),
                        tokens -> Double.parseDouble(tokens[1].trim())
                ));

        List<File> files = Files.walk(sourceDir)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(f -> f.getName().endsWith(".jsnp"))
                .sorted(Comparator.comparingInt(f -> Integer.parseInt(f.getName().replace(".jsnp", ""))))
                .collect(Collectors.toList());

        for (File file : files) {
            String name = file.getName();
            String code = Files.readString(file.toPath());
            StringBuilder row = new StringBuilder(name);

            for (FeatureMetric feature : featureMetrics) {
                double value = feature.compute(code);
                row.append(",").append(String.format(Locale.US, "%.2f", value));
            }

            double avgScore = truthMap.getOrDefault(name, 0.0);
            String truth = avgScore >= 3.6 ? "Y" : "N";
            row.append(",").append(truth);

            csvLines.add(row.toString());
        }

        return csvLines;
    }
}

package de.uni_passau.fim.se2.sa.readability.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Locale;

import de.uni_passau.fim.se2.sa.readability.features.FeatureMetric;
import de.uni_passau.fim.se2.sa.readability.features.NumberLinesFeature;
import de.uni_passau.fim.se2.sa.readability.features.TokenEntropyFeature;
import de.uni_passau.fim.se2.sa.readability.features.HalsteadVolumeFeature;
import de.uni_passau.fim.se2.sa.readability.features.CyclomaticComplexityFeature;

public class Preprocess {

    private static final double TRUTH_THRESHOLD = 3.6;

    public static void collectCSVBody(Path sourceDir, File truth, StringBuilder csv, List<FeatureMetric> featureMetrics) throws IOException {
        Map<String, Double> truthMap = Files.lines(truth.toPath())
                .skip(1)
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        tokens -> tokens[0].trim(),
                        tokens -> Double.parseDouble(tokens[1].trim())
                ));

        List<File> snippetFiles = Files.walk(sourceDir)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(file -> file.getName().endsWith(".jsnp"))
                .sorted(Comparator.comparingInt(file -> Integer.parseInt(file.getName().replace(".jsnp", ""))))
                .collect(Collectors.toList());

        for (File snippetFile : snippetFiles) {
            String fileName = snippetFile.getName();
            String code = Files.readString(snippetFile.toPath());

            StringBuilder row = new StringBuilder();
            row.append(fileName);

            for (FeatureMetric metric : featureMetrics) {
                double value = metric.compute(code);
                row.append(",").append(String.format(Locale.US, "%.2f", value));
            }

            double avgScore = truthMap.getOrDefault(fileName, 0.0);
            row.append(",").append(avgScore >= TRUTH_THRESHOLD ? "Y" : "N");

            csv.append(row).append("\n");
        }
    }

}

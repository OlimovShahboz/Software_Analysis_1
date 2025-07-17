package de.uni_passau.fim.se2.sa.readability.features;

public class NumberLinesFeature extends FeatureMetric {

    @Override
    public double compute(String codeSnippet) {
        if (codeSnippet == null || codeSnippet.isBlank()) {
            return 1.0; // Even empty code counts as 1 line per test expectations
        }

        String[] lines = codeSnippet.split("\\r?\\n");
        int count = 0;

        for (String line : lines) {
            count++; // count all lines (comments, whitespace, etc.)
        }

        return (double) count;
    }

    @Override
    public String getIdentifier() {
        return "LINES";
    }

    public double computeMetric(String codeSnippet) {
        return compute(codeSnippet);
    }
}

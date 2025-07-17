package de.uni_passau.fim.se2.sa.readability.features;

public abstract class FeatureMetric {
    public abstract double compute(String codeSnippet);  // 👈 MUST be 'compute', not 'computeMetric'
    public abstract String getIdentifier();
}

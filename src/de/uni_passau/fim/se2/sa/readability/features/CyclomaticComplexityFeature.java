package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ast.Node;
import de.uni_passau.fim.se2.sa.readability.utils.CyclomaticComplexityVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.Parser;

public class CyclomaticComplexityFeature extends FeatureMetric {

    @Override
    public double compute(String codeSnippet) {
        if (codeSnippet == null || codeSnippet.isBlank()) {
            return 1.0;
        }

        Node node;
        try {
            node = Parser.parseJavaSnippet(codeSnippet);
        } catch (Exception e) {
            return 1.0; // fallback on parse failure
        }

        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        node.accept(visitor, null); // ✅ correct way to apply visitor
        return visitor.getComplexity();
    }

    @Override
    public String getIdentifier() {
        return "CYCLOMATIC_COMPLEXITY";
    }

    public double computeMetric(String codeSnippet) {
        return compute(codeSnippet);
    }
}

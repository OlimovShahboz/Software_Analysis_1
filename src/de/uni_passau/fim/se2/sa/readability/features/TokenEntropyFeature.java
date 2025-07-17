package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.JavaToken;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.body.BodyDeclaration;
import de.uni_passau.fim.se2.sa.readability.utils.Parser;

import java.util.HashMap;
import java.util.Map;

public class TokenEntropyFeature extends FeatureMetric {

    @Override
    public double compute(String codeSnippet) {
        TokenRange tokenRange = Parser.parseJavaSnippet(codeSnippet).getTokenRange().orElse(null);
        if (tokenRange == null) return 0.0;

        Map<String, Integer> tokenCounts = new HashMap<>();
        int totalTokens = 0;

        for (JavaToken token : tokenRange) {
            String text = token.getText();
            tokenCounts.put(text, tokenCounts.getOrDefault(text, 0) + 1);
            totalTokens++;
        }

        double entropy = 0.0;
        for (Map.Entry<String, Integer> entry : tokenCounts.entrySet()) {
            double p = (double) entry.getValue() / totalTokens;
            entropy -= p * (Math.log(p) / Math.log(2)); // log base 2
        }

        return entropy;
    }


    @Override
    public String getIdentifier() {
        return "TOKEN_ENTROPY";
    }

    public double computeMetric(String codeSnippet) {
        return compute(codeSnippet);
    }
}

package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import de.uni_passau.fim.se2.sa.readability.utils.OperandVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.OperatorVisitor;
import de.uni_passau.fim.se2.sa.readability.utils.OperatorVisitor.OperatorType;
import de.uni_passau.fim.se2.sa.readability.utils.Parser;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HalsteadVolumeFeature extends FeatureMetric {

    @Override
    public double compute(String codeSnippet) {
        try {

            BodyDeclaration<?> cu = Parser.parseJavaSnippet(codeSnippet);
            if (codeSnippet == null) return 0.0;

            int totalOperands = 0;
            int totalOperators = 0;
            Set<String> uniqueOperands = new HashSet<>();
            Set<OperatorType> uniqueOperatorTypes = new HashSet<>();

            for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
                OperandVisitor operandVisitor = new OperandVisitor();
                OperatorVisitor operatorVisitor = new OperatorVisitor();

                method.accept(operandVisitor, null);
                method.accept(operatorVisitor, null);

                // Count operands
                for (Map.Entry<String, Integer> entry : operandVisitor.getOperandsPerMethod().entrySet()) {
                    totalOperands += entry.getValue();
                    uniqueOperands.add(entry.getKey());
                }

                // Count operators by OperatorType (but only if they actually occurred)
                Map<OperatorType, Integer> typeMap = operatorVisitor.getOperatorsPerMethod();
                for (Map.Entry<OperatorType, Integer> entry : typeMap.entrySet()) {
                    int count = entry.getValue();
                    if (count > 0) {
                        totalOperators += count;
                        uniqueOperatorTypes.add(entry.getKey());
                    }
                }
            }

            int N = totalOperands + totalOperators;
            int n = uniqueOperands.size() + uniqueOperatorTypes.size();

            return (n > 0) ? N * (Math.log(n) / Math.log(2)) : 0.0;

        } catch (Exception e) {
            System.err.println("Error computing Halstead Volume: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public String getIdentifier() {
        return "H_VOLUME";
    }

    public double computeMetric(String codeSnippet) {
        return compute(codeSnippet);
    }
}
package de.uni_passau.fim.se2.sa.readability.features;

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
    public double computeMetric(String codeSnippet) {
        try {
            BodyDeclaration<?> root = Parser.parseJavaSnippet(codeSnippet);
            if (root == null) return 0.0;

            int operandCount = 0;
            int operatorCount = 0;
            Set<String> distinctOperands = new HashSet<>();
            Set<OperatorType> distinctOperatorTypes = new HashSet<>();

            for (MethodDeclaration method : root.findAll(MethodDeclaration.class)) {
                OperandVisitor operandVisitor = new OperandVisitor();
                OperatorVisitor operatorVisitor = new OperatorVisitor();

                method.accept(operandVisitor, null);
                method.accept(operatorVisitor, null);

                for (Map.Entry<String, Integer> operand : operandVisitor.getOperandsPerMethod().entrySet()) {
                    operandCount += operand.getValue();
                    distinctOperands.add(operand.getKey());
                }

                for (Map.Entry<String, Integer> opSymbol : operatorVisitor.getOperatorSymbols().entrySet()) {
                    operatorCount += opSymbol.getValue();
                }

                for (Map.Entry<OperatorType, Integer> opType : operatorVisitor.getOperatorsPerMethod().entrySet()) {
                    if (opType.getValue() > 0) {
                        distinctOperatorTypes.add(opType.getKey());
                    }
                }
            }

            int totalLength = operandCount + operatorCount;
            int totalVocabulary = distinctOperands.size() + distinctOperatorTypes.size();

            return totalVocabulary > 0
                    ? totalLength * (Math.log(totalVocabulary) / Math.log(2))
                    : 0.0;

        } catch (Exception e) {
            System.err.println("Halstead computation error: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public String getIdentifier() {
        return "H_VOLUME";
    }
}

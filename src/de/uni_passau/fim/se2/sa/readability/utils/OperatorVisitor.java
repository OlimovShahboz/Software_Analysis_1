package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class OperatorVisitor extends VoidVisitorAdapter<Void> {

    public enum OperatorType {
        ASSIGNMENT,
        BINARY,
        UNARY,
        CONDITIONAL,
        TYPE_COMPARISON
    }

    private final Map<OperatorType, Integer> operatorsPerMethod = new HashMap<>();
    private final Map<String, Integer> operatorSymbols = new HashMap<>();

    public Map<OperatorType, Integer> getOperatorsPerMethod() {
        return operatorsPerMethod;
    }

    public Map<String, Integer> getOperatorSymbols() {
        return operatorSymbols;
    }

    private void addOperator(OperatorType type, String symbol) {
        operatorsPerMethod.merge(type, 1, Integer::sum);
        operatorSymbols.merge(symbol, 1, Integer::sum);
    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        addOperator(OperatorType.ASSIGNMENT, n.getOperator().asString()); // =
        super.visit(n, arg); // make sure to visit children too
    }

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        // Always count as ASSIGNMENT (type declaration)
        addOperator(OperatorType.ASSIGNMENT, "=");
        super.visit(n, arg); // still visit initializer if present (e.g., x = y + z)
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        addOperator(OperatorType.BINARY, n.getOperator().asString()); // +, -, *, etc.
        super.visit(n, arg);
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        addOperator(OperatorType.UNARY, n.getOperator().asString()); // !, ++, --
        super.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        addOperator(OperatorType.CONDITIONAL, "?:"); // ternary operator
        super.visit(n, arg);
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        addOperator(OperatorType.TYPE_COMPARISON, "instanceof");
        super.visit(n, arg);
    }
}


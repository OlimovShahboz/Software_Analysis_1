package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {

    private int complexity = 1; // base complexity

    public int getComplexity() {
        return complexity;
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(SwitchEntry n, Void arg) {
        super.visit(n, arg);
        if (!n.getLabels().isEmpty()) {
            complexity++;
        }
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    // ✅ Add this to handle logical AND / OR
    @Override
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);
        BinaryExpr.Operator op = n.getOperator();
        if (op == BinaryExpr.Operator.AND || op == BinaryExpr.Operator.OR) {
            complexity++;
        }
    }
}
///
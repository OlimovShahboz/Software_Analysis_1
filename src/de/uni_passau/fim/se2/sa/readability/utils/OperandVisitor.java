package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class OperandVisitor extends VoidVisitorAdapter<Void> {

    private final Map<String, Integer> operands = new HashMap<>();

    public Map<String, Integer> getOperandsPerMethod() {
        return operands;
    }

    private void normalizeAndAdd(String raw) {
        if (raw == null || raw.isBlank()) return;

        String normalized = switch (raw.trim().toLowerCase()) {
            case "true", "\"true\"" -> "true";
            case "false", "\"false\"" -> "false";
            case "null", "\"null\"" -> "null";
            default -> raw;
        };

        operands.merge(normalized, 1, Integer::sum);
    }

    @Override
    public void visit(MethodDeclaration decl, Void arg) {
        normalizeAndAdd(decl.getNameAsString());
        decl.getParameters().forEach(p -> p.accept(this, arg));
        decl.getBody().ifPresent(b -> b.accept(this, arg));
    }

    @Override
    public void visit(Parameter param, Void arg) {
        normalizeAndAdd(param.getNameAsString());
    }

    @Override
    public void visit(VariableDeclarationExpr expr, Void arg) {
        if (expr.getElementType() instanceof ClassOrInterfaceType type) {
            normalizeAndAdd(type.getNameAsString());
            type.getTypeArguments().ifPresent(args -> args.forEach(argType -> {
                if (argType instanceof ClassOrInterfaceType generic) {
                    normalizeAndAdd(generic.getNameAsString());
                }
            }));
        }

        expr.getVariables().forEach(var -> {
            normalizeAndAdd(var.getNameAsString());
            var.getInitializer().ifPresent(init -> init.accept(this, arg));
        });
    }

    /*@Override
    public void visit(NameExpr expr, Void arg) {
        normalizeAndAdd(expr.getNameAsString());
    }*/
    @Override
    public void visit(SimpleName expr, Void arg) {
        normalizeAndAdd(expr.asString());
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        stmt.getInitialization().forEach(i -> i.accept(this, arg));
        stmt.getCompare().ifPresent(c -> c.accept(this, arg));
        stmt.getUpdate().forEach(u -> u.accept(this, arg));
        stmt.getBody().accept(this, arg);
    }

    @Override
    public void visit(StringLiteralExpr expr, Void arg) {
        normalizeAndAdd(expr.getValue());
    }

    @Override
    public void visit(IntegerLiteralExpr expr, Void arg) {
        normalizeAndAdd(expr.getValue());
    }

    @Override
    public void visit(LongLiteralExpr expr, Void arg) {
        normalizeAndAdd(expr.getValue());
    }

    @Override
    public void visit(BooleanLiteralExpr expr, Void arg) {
        normalizeAndAdd(String.valueOf(expr.getValue()));
    }

    @Override
    public void visit(CharLiteralExpr expr, Void arg) {
        normalizeAndAdd(expr.getValue());
    }

    @Override
    public void visit(DoubleLiteralExpr expr, Void arg) {
        normalizeAndAdd(expr.getValue());
    }

    @Override
    public void visit(NullLiteralExpr expr, Void arg) {
        normalizeAndAdd("null");
    }

    /*@Override
    public void visit(MethodCallExpr expr, Void arg) {
        normalizeAndAdd(expr.getNameAsString());
        expr.getScope().ifPresent(scope -> scope.accept(this, arg));
        expr.getArguments().forEach(a -> a.accept(this, arg));
    }

    @Override
    public void visit(FieldAccessExpr expr, Void arg) {
        normalizeAndAdd(expr.getNameAsString());
        expr.getScope().accept(this, arg);
    }

    @Override
    public void visit(ArrayAccessExpr expr, Void arg) {
        expr.getName().accept(this, arg);
        expr.getIndex().accept(this, arg);
    }

    @Override
    public void visit(ConditionalExpr expr, Void arg) {
        expr.getCondition().accept(this, arg);
        expr.getThenExpr().accept(this, arg);
        expr.getElseExpr().accept(this, arg);
    }

    @Override
    public void visit(InstanceOfExpr expr, Void arg) {
        expr.getExpression().accept(this, arg);
        normalizeAndAdd(expr.getType().asString());
    }

    @Override
    public void visit(ClassOrInterfaceType type, Void arg) {
        normalizeAndAdd(type.getNameAsString());
    }*/
}

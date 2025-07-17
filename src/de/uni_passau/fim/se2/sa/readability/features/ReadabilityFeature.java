package de.uni_passau.fim.se2.sa.readability.features;

import com.github.javaparser.ast.CompilationUnit;

public interface ReadabilityFeature {
    double compute(CompilationUnit cu);
}

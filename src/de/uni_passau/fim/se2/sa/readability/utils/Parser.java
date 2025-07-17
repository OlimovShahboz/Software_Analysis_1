package de.uni_passau.fim.se2.sa.readability.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.body.BodyDeclaration;

import java.io.StringReader;

public class Parser {

    /**
     * Parses the code of a Java snippet (.jsnp file) using the JavaParser library
     * so that it can be visited by JavaParser visitors.
     *
     * @param codeSnippet The code of the .jsnp file as a String
     * @return The parsed code snippet ready to accept visitors
     * @throws RuntimeException if parsing fails
     */
    public static BodyDeclaration<?> parseJavaSnippet(String codeSnippet) {
        JavaParser parser = new JavaParser();
        ParseResult<BodyDeclaration<?>> result = parser.parse(ParseStart.CLASS_BODY, Providers.provider(new StringReader(codeSnippet)));

        if (!result.isSuccessful() || result.getResult().isEmpty()) {
            throw new RuntimeException("Could not parse Java snippet:\n" + codeSnippet);
        }

        return result.getResult().get();
    }
}

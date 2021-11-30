package com.slinkydeveloper.assertjmigrator.migrations.junit;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.slinkydeveloper.assertjmigrator.nodes.AssertJBuilder;
import com.slinkydeveloper.assertjmigrator.nodes.Predicates;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.methodArgMatches;
import static com.slinkydeveloper.assertjmigrator.nodes.Predicates.or;

public class JUnitAssertNotEqualsWithDelta extends BaseJUnitAssertion {

    @Override
    protected String assertionName() {
        return "assertNotEquals";
    }

    @Override
    protected int assertionArity() {
        return 3;
    }

    @Override
    protected Predicate<Expression> additionalPredicate(int offset) {
        return methodArgMatches(offset + 2, or(Predicates::isFloat, Predicates::isDouble));
    }

    @Override
    protected void fillBuilder(AssertJBuilder builder, MethodCallExpr node) {
        builder.assertThat(node.getArgument(1));
        if (node.getArgument(2).isDoubleLiteralExpr() && node.getArgument(2).asDoubleLiteralExpr().asDouble() == 0) {
            // Assert equals is enough in this case
            builder.isNotEqualTo(node.getArgument(0));
            return;
        }
        builder.isNotCloseTo(node.getArgument(0), node.getArgument(2));
    }

    @Override
    public List<String> requiredImports() {
        return Arrays.asList("org.assertj.core.api.Assertions.assertThat", "org.assertj.core.api.Assertions.within");
    }

    @Override
    public String toString() {
        return "JUnit 4/5 assertEquals with delta";
    }
}

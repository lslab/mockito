package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.invocation.Invocation;

import java.util.*;

/**
 * Renders the stubbing hint on argument mismatches
 */
class StubbingArgMismatches {

    private final Map<Invocation, Set<Invocation>> mismatches = new LinkedHashMap<Invocation, Set<Invocation>>();
    private final String testName;

    StubbingArgMismatches(String testName) {
        this.testName = testName;
    }

    public void add(Invocation invocation, Invocation stubbing) {
        Set<Invocation> matchingInvocations = mismatches.get(stubbing);
        if (matchingInvocations == null) {
            matchingInvocations = new LinkedHashSet<Invocation>();
            mismatches.put(stubbing, matchingInvocations);
        }
        matchingInvocations.add(invocation);
    }

    public void log(MockitoLogger logger) {
        if (mismatches.isEmpty()) {
            return;
        }

        StubbingHint hint = new StubbingHint(testName);
        //TODO SF it would be nice to make the String look good if x goes multiple digits (padding)
        int x = 1;
        for (Map.Entry<Invocation, Set<Invocation>> m : mismatches.entrySet()) {
            hint.appendLine(x++, ". Unused... ", m.getKey().getLocation());
            for (Invocation invocation : m.getValue()) {
                hint.appendLine(" ...args ok? ", invocation.getLocation());
            }
        }

        logger.log(hint.toString());
    }
}

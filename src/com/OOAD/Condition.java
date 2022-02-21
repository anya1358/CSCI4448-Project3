package com.OOAD;

// I wanted these enums to have a value so I could decrement condition when I needed to...
// Good discussion of this at https://www.baeldung.com/java-enum-values

public enum Condition {
    POOR (1), FAIR (2), GOOD (3), VERYGOOD (4), EXCELLENT (5);
    public int level;
    Condition(int level) {
        this.level = level;
    }
}

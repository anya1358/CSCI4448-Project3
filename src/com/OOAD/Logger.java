package com.OOAD;

public interface Logger {
    default void out(String msg) {
        System.out.println(msg);
    }
}

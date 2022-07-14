package com.lxk.reggie.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static long get() {
        return threadLocal.get();
    }

    public static void set(long id) {
        threadLocal.set(id);
    }
}

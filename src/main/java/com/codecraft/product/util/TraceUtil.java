package com.codecraft.product.util;

public class TraceUtil {
    public static String generateTraceId() {
        return java.util.UUID.randomUUID().toString();
    }
}


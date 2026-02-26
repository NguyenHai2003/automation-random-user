package com.automation.globals;

public final class TokenContext {

    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();

    private TokenContext() {
    }

    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
    }

    public static String getToken() {
        return TOKEN_HOLDER.get();
    }

    public static boolean hasToken() {
        String token = TOKEN_HOLDER.get();
        return token != null && !token.isBlank();
    }

    public static void clear() {
        TOKEN_HOLDER.remove();
    }
}

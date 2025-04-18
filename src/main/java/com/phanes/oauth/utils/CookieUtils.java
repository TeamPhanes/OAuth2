package com.phanes.oauth.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Arrays;

public class CookieUtils {
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst()
                .orElseThrow()
                .getValue();
    }

    public static ResponseCookie createHttpOnlyCookie(String name, String value, Duration maxAge, String domain) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .domain(domain)
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    public static ResponseCookie deleteCookie(String name, String domain) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .domain(domain)
                .path("/")
                .maxAge(0)
                .build();
    }
}

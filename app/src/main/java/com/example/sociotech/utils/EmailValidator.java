package com.example.sociotech.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    // Regular expression for a simple email validation
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false; // Email is empty
        }

        Matcher matcher = pattern.matcher(email);
        return matcher.matches(); // Email matches the pattern
    }
}

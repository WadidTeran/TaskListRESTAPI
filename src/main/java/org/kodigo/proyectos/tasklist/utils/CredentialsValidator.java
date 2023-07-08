package org.kodigo.proyectos.tasklist.utils;

import java.util.regex.Pattern;

public class CredentialsValidator {
  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[a-z]+\\.(co|com)$";
  private static final int PASSWORD_MIN_LENGTH = 8;
  private static final String PASSWORD_REGEX =
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

  private CredentialsValidator() {}

  public static boolean isValidEmail(String email) {
    return email != null && Pattern.matches(EMAIL_REGEX, email);
  }

  public static boolean isValidPassword(String password) {
    return password != null
        && password.length() >= PASSWORD_MIN_LENGTH
        && Pattern.matches(PASSWORD_REGEX, password);
  }
}

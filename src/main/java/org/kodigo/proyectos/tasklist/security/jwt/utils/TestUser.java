package org.kodigo.proyectos.tasklist.security.jwt.utils;

public enum TestUser {
  EMAIL("test@gmail.com"),
  USERNAME("test"),
  PASSWORD("89wvn7YNFB0YX@");

  public final String value;

  TestUser(String value) {
    this.value = value;
  }
}

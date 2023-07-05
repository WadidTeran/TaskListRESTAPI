package org.kodigo.proyectos.tasklist.services;

public enum ParamStrings {
  PENDING("pending"),
  COMPLETED("completed"),
  TODAY("today"),
  FUTURE("future"),
  PREVIOUS("previous"),
  RELEVANCE("relevance"),
  CATEGORY("category");

  final String value;

  ParamStrings(String value) {
    this.value = value;
  }
}

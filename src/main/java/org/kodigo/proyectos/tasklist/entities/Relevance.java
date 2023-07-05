package org.kodigo.proyectos.tasklist.entities;

public enum Relevance {
  NONE("none"),
  LOW("low"),
  MEDIUM("medium"),
  HIGH("high");

  public final String value;

  Relevance(String value) {
    this.value = value;
  }

  public static Relevance fromValue(String value) {
    for (Relevance r : Relevance.values()) {
      if (value.equals(r.value)) {
        return r;
      }
    }
    return null;
  }
}

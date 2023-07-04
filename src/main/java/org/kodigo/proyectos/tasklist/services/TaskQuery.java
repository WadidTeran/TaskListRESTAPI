package org.kodigo.proyectos.tasklist.services;

public enum TaskQuery {
  ALL_COMPLETED(new String[] {ParamStrings.COMPLETED.value, null, null, null}),
  ALL_PENDING(new String[] {ParamStrings.PENDING.value, null, null, null}),
  FUTURE_PENDING(new String[] {ParamStrings.PENDING.value, ParamStrings.FUTURE.value, null, null}),
  PREVIOUS_PENDING(
      new String[] {ParamStrings.PENDING.value, ParamStrings.PREVIOUS.value, null, null}),
  PENDING_FOR_TODAY(
      new String[] {ParamStrings.PENDING.value, ParamStrings.TODAY.value, null, null}),
  PREVIOUS_COMPLETED(
      new String[] {ParamStrings.COMPLETED.value, ParamStrings.PREVIOUS.value, null, null}),
  COMPLETED_TODAY(
      new String[] {ParamStrings.COMPLETED.value, ParamStrings.TODAY.value, null, null}),
  PENDING_BY_RELEVANCE(
      new String[] {ParamStrings.PENDING.value, null, ParamStrings.RELEVANCE.value, null}),
  COMPLETED_BY_RELEVANCE(
      new String[] {ParamStrings.COMPLETED.value, null, ParamStrings.RELEVANCE.value, null}),
  FUTURE_PENDING_BY_RELEVANCE(
      new String[] {
        ParamStrings.PENDING.value, ParamStrings.FUTURE.value, ParamStrings.RELEVANCE.value, null
      }),
  PREVIOUS_PENDING_BY_RELEVANCE(
      new String[] {
        ParamStrings.PENDING.value, ParamStrings.PREVIOUS.value, ParamStrings.RELEVANCE.value, null
      }),
  PENDING_FOR_TODAY_BY_RELEVANCE(
      new String[] {
        ParamStrings.PENDING.value, ParamStrings.TODAY.value, ParamStrings.RELEVANCE.value, null
      }),
  PREVIOUS_COMPLETED_BY_RELEVANCE(
      new String[] {
        ParamStrings.COMPLETED.value,
        ParamStrings.PREVIOUS.value,
        ParamStrings.RELEVANCE.value,
        null
      }),
  COMPLETED_TODAY_BY_RELEVANCE(
      new String[] {
        ParamStrings.COMPLETED.value, ParamStrings.TODAY.value, ParamStrings.RELEVANCE.value, null
      }),
  PENDING_BY_CATEGORY(
      new String[] {ParamStrings.PENDING.value, null, null, ParamStrings.CATEGORY.value}),
  COMPLETED_BY_CATEGORY(
      new String[] {ParamStrings.COMPLETED.value, null, null, ParamStrings.CATEGORY.value}),
  FUTURE_PENDING_BY_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value, ParamStrings.FUTURE.value, null, ParamStrings.CATEGORY.value
      }),
  PREVIOUS_PENDING_BY_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value, ParamStrings.PREVIOUS.value, null, ParamStrings.CATEGORY.value
      }),
  PENDING_FOR_TODAY_BY_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value, ParamStrings.TODAY.value, null, ParamStrings.CATEGORY.value
      }),
  PREVIOUS_COMPLETED_BY_CATEGORY(
      new String[] {
        ParamStrings.COMPLETED.value, ParamStrings.PREVIOUS.value, null, ParamStrings.CATEGORY.value
      }),
  COMPLETED_TODAY_BY_CATEGORY(
      new String[] {
        ParamStrings.COMPLETED.value, ParamStrings.TODAY.value, null, ParamStrings.CATEGORY.value
      }),
  PENDING_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value, null, ParamStrings.RELEVANCE.value, ParamStrings.CATEGORY.value
      }),
  COMPLETED_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.COMPLETED.value,
        null,
        ParamStrings.RELEVANCE.value,
        ParamStrings.CATEGORY.value
      }),

  FUTURE_PENDING_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value,
        ParamStrings.FUTURE.value,
        ParamStrings.RELEVANCE.value,
        ParamStrings.CATEGORY.value
      }),
  PREVIOUS_PENDING_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value,
        ParamStrings.PREVIOUS.value,
        ParamStrings.RELEVANCE.value,
        ParamStrings.CATEGORY.value
      }),
  PENDING_FOR_TODAY_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.PENDING.value,
        ParamStrings.TODAY.value,
        ParamStrings.RELEVANCE.value,
        ParamStrings.CATEGORY.value
      }),
  PREVIOUS_COMPLETED_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.COMPLETED.value,
        ParamStrings.PREVIOUS.value,
        ParamStrings.RELEVANCE.value,
        ParamStrings.CATEGORY.value
      }),
  COMPLETED_TODAY_BY_RELEVANCE_AND_CATEGORY(
      new String[] {
        ParamStrings.COMPLETED.value,
        ParamStrings.TODAY.value,
        ParamStrings.RELEVANCE.value,
        ParamStrings.CATEGORY.value
      });
  final String[] params;

  TaskQuery(String[] params) {
    this.params = params;
  }
}

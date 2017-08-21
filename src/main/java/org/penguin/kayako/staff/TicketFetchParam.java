package org.penguin.kayako.staff;

public enum TicketFetchParam {
  DEPARTMENT_ID("departmentid"),
  STATUS_ID("statusid"),
  OWNER_ID("ownerid"),
  FILTER_ID("filterid"),
  TICKET_ID("ticketid"),
  WANT_TICKET_DATA("wantticketdata"),
  WANT_ATTACHMENT_DATA("wantattachmentdata"),
  SORT_BY("sortby"),
  SORT_ORDER("sortorder"),
  START("start"),
  LIMIT("limit");

  private final String value;

  TicketFetchParam(final String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}


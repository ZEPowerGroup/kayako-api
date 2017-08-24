package org.penguin.kayako.staff;

public enum TicketParam {
  TICKET_ID("ticketid"),
  SUBJECT("subject"),
  FULL_NAME("fullname"),
  EMAIL("email"),
  CREATOR("creator"),
  USERID("userid"),
  TYPE("type"),
  SEND_AUTO_RESPONDER("sendautoresponder"),
  DEPARTMENT_ID("departmentid"),
  STATUS_ID("statusid"),
  TICKET_STATUS_ID("ticketstatusid"),
  TICKET_PRIORITY_ID("ticketpriorityid"),
  TICKET_TYPE_ID("tickettypeid"),
  OWNER_STAFF_ID("ownerstaffid"),
  FLAG_TYPE("flagtype"),
  TAGS("tags"),
  CCTO("ccto"),
  BCCTO("bccto"),
  WATCH("watch"),
  REPLY("reply"),
  CONTENTS("contents"),
  ATTACHMENT("attachment");

  private final String value;

  TicketParam(final String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}

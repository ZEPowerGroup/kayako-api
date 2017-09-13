package org.penguin.kayako.staff;

/**
 * Contains the commands that are supported by the Staff API.
 */
public enum Command {
  /**
   * Create a ticket.
   */
  CREATE,
  /**
   * Update a ticket.
   */
  UPDATE,
  /**
   * Fetch a list of tickets.
   */
  RETRIEVE
}

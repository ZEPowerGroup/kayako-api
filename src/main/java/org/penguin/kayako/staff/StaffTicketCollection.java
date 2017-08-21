package org.penguin.kayako.staff;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "kayako_staffapi")
public class StaffTicketCollection {
  @XmlElementWrapper(name = "tickets")
  @XmlElement(name = "ticket")
  private List<StaffTicket> tickets;

  public List<StaffTicket> getTickets() {
    return tickets;
  }
}

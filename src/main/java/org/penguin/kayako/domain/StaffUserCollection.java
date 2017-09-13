package org.penguin.kayako.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ricky.sandhu on 9/12/2017.
 */
@XmlRootElement(name="staffusers")
public class StaffUserCollection {
  @XmlElement(name="staff")
  private final List<StaffUser> users = new ArrayList<>();

  public List<StaffUser> getUsers() {
    return users;
  }
}

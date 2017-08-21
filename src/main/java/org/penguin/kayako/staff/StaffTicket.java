package org.penguin.kayako.staff;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.penguin.kayako.domain.AbstractTicket;
import org.penguin.kayako.domain.Post;

@XmlRootElement(name = "ticket")
public class StaffTicket extends AbstractTicket{
  @XmlElement(name = "post")
  private List<Post> posts;

  public List<Post> getPosts() {
    return posts;
  }
}

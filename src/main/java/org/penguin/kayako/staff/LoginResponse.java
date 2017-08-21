package org.penguin.kayako.staff;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="kayako_staffapi")
public class LoginResponse {
  @XmlElement
  private String status;
  @XmlElement
  private String error;
  @XmlElement
  private String version;
  @XmlElement(name = "sessionid")
  private String sessionId;
  @XmlElement(name = "sessiontimeout")
  private String sessionTimeout;
  @XmlElement(name = "staffid")
  private String staffId;

  public String getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getVersion() {
    return version;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getSessionTimeout() {
    return sessionTimeout;
  }

  public String getStaffId() {
    return staffId;
  }
}

package org.penguin.kayako.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ricky.sandhu on 9/12/2017.
 */
@XmlRootElement(name="staff")
public class StaffUser {
  @XmlElement
  private int id;
  @XmlElement(name="staffgroupid")
  private int staffGroupId;
  @XmlElement(name="firstname")
  private String firstName;
  @XmlElement(name="lastname")
  private String lastName;
  @XmlElement(name="fullname")
  private String fullName;
  @XmlElement(name="username")
  private String userName;
  @XmlElement
  private String email;
  @XmlElement
  private String designation;
  @XmlElement
  private String greeting;
  @XmlElement(name="mobilenumber")
  private String mobileNumber;
  @XmlElement(name="isenabled")
  private boolean isEnabled;
  @XmlElement
  private String timezone;
  @XmlElement(name="enabledst")
  private boolean enableDst;
  @XmlElement
  private String signature;

  public int getId() {
    return id;
  }

  public int getStaffGroupId() {
    return staffGroupId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return fullName;
  }

  public String getUserName() {
    return userName;
  }

  public String getEmail() {
    return email;
  }

  public String getDesignation() {
    return designation;
  }

  public String getGreeting() {
    return greeting;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public boolean getIsEnabled() {
    return isEnabled;
  }

  public String getTimezone() {
    return timezone;
  }

  public boolean getEnableDst() {
    return enableDst;
  }

  public String getSignature() {
    return signature;
  }
}

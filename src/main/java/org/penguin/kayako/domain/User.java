package org.penguin.kayako.domain;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.penguin.kayako.adapters.NullableDateAdapter;
import org.penguin.kayako.adapters.UnixDateAdapter;

/**
 * A representation of a kayako User.
 *
 * @author eugene
 */
@XmlRootElement(name = "user")
public final class User {
  @XmlElement
  private int id;
  @XmlElement(name = "usergroupid")
  private int userGroupId;
  @XmlElement(name = "userrole")
  private String userRole;
  @XmlElement(name = "userorganizationid")
  private int userOrganizationId;
  @XmlElement(name = "salutation")
  private String salutation;
  @XmlElement(name = "userexpiry")
  @XmlJavaTypeAdapter(NullableDateAdapter.class)
  private Date userExpiry;
  @XmlElement(name = "fullname")
  private String fullName;
  @XmlElement(name = "email")
  private List<String> emails;
  @XmlElement(name = "designation")
  private String designation;
  @XmlElement(name = "phone")
  private String phone;
  @XmlElement(name = "dateline")
  @XmlJavaTypeAdapter(UnixDateAdapter.class)
  private Date dateline;
  @XmlElement(name = "lastvisit")
  @XmlJavaTypeAdapter(NullableDateAdapter.class)
  private Date lastVisit;
  @XmlElement(name = "isenabled")
  private boolean enabled;
  @XmlElement(name = "timezone")
  private String timeZone;
  @XmlElement(name = "enabledst")
  private boolean enableDst;
  @XmlElement(name = "slaplanid")
  private int slaplanId;
  @XmlElement(name = "slaplanexpiry")
  @XmlJavaTypeAdapter(NullableDateAdapter.class)
  private Date slaplanExpiry;

  public int getId() {
    return id;
  }

  public int getUserGroupId() {
    return userGroupId;
  }

  public String getUserRole() {
    return userRole;
  }

  public int getUserOrganizationId() {
    return userOrganizationId;
  }

  public String getSalutation() {
    return salutation;
  }

  public Date getUserExpiry() {
    return userExpiry;
  }

  public String getFullName() {
    return fullName;
  }

  public List<String> getEmails() {
    return emails;
  }

  public String getDesignation() {
    return designation;
  }

  public String getPhone() {
    return phone;
  }

  public Date getDateline() {
    return dateline;
  }

  public Date getLastVisit() {
    return lastVisit;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public boolean isEnableDst() {
    return enableDst;
  }

  public int getSlaplanId() {
    return slaplanId;
  }

  public Date getSlaplanExpiry() {
    return slaplanExpiry;
  }
}

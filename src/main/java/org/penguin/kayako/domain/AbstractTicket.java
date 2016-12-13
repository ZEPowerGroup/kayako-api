package org.penguin.kayako.domain;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.penguin.kayako.adapters.TagStringAdapter;
import org.penguin.kayako.adapters.UnixDateAdapter;

/**
 * @author Roman Romanchuk<br>
 *         Date: 4/26/13<br>
 *         Time: 11:34 AM<br>
 * @version $Revision$
 */
public abstract class AbstractTicket {
    @XmlAttribute
    private int id;
    @XmlElement(name = "displayid")
    private String displayId;
    @XmlElement(name = "departmentid")
    private int departmentId;
    @XmlElement(name = "statusid")
    private int statusId;
    @XmlElement(name = "priorityid")
    private int priorityId;
    @XmlElement(name = "typeid")
    private int typeId;
    @XmlElement(name = "userid")
    private int userId;
    @XmlElement(name = "userorganization")
    private String userOrganizationName;
    @XmlElement(name = "userorganizationid")
    private int userOrganizationId;
    // The user full name
    @XmlElement(name = "fullname")
    private String userFullName;
    @XmlElement(name = "email")
    private String userEmail;
    @XmlElement(name = "ownerstaffid")
    private int ownerStaffId;
    private String ownerStaffName;
    @XmlElement(name = "lastreplier")
    private String lastReplierFullName;
    @XmlElement(name = "subject")
    private String subject;
    @XmlElement(name = "creationtime")
    @XmlJavaTypeAdapter(UnixDateAdapter.class)
    private Date creationDate;
    @XmlElement(name = "lastactivity")
    @XmlJavaTypeAdapter(UnixDateAdapter.class)
    private Date lastActivityDate;
    @XmlElement(name = "laststaffreply")
    private int lastStaffReplyId;
    @XmlElement(name = "lastuserreply")
    @XmlJavaTypeAdapter(UnixDateAdapter.class)
    private Date lastUserReplyDate;
    @XmlElement(name = "slaplanid")
    private int slaPlanId;
    @XmlElement(name = "nextreplydue")
    @XmlJavaTypeAdapter(UnixDateAdapter.class)
    private Date nextReplyDueDate;
    @XmlElement(name = "resolutiondue")
    @XmlJavaTypeAdapter(UnixDateAdapter.class)
    private Date resolutionDueDate;
    @XmlElement(name = "replies")
    private int replies;
    @XmlElement(name = "ipaddress")
    private String ipAddress;
    @XmlElement(name = "creator")
    private int creatorId;
    @XmlElement(name = "creationmode")
    private int creationMode;
    @XmlElement(name = "creationtype")
    private int creationType;
    @XmlElement(name = "isescalated")
    private boolean escalated;
    @XmlElement(name = "escalationruleid")
    private int escalationRuleId;
    @XmlElement(name = "templategroupid")
    private int templateGroupId;
    @XmlElement(name = "templategroupname")
    private String templateGroupName;
    @XmlElement(name = "tags")
    @XmlJavaTypeAdapter(TagStringAdapter.class)
    private List<String> tags;

    @XmlElement(name = "note")
    private List<Note> notes;
    @XmlElement(name = "watcher")
    private List<TicketWatcher> watchers;
    @XmlElement(name = "workflow")
    private List<TicketWorkflow> workflows;

    public int getId() {
        return id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getStatusId() {
        return statusId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserOrganizationName() {
        return userOrganizationName;
    }

    public int getUserOrganizationId() {
        return userOrganizationId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getOwnerStaffId() {
        return ownerStaffId;
    }

    public String getOwnerStaffName() {
        return ownerStaffName;
    }

    public String getLastReplierFullName() {
        return lastReplierFullName;
    }

    public String getSubject() {
        return subject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public int getLastStaffReplyId() {
        return lastStaffReplyId;
    }

    public Date getLastUserReplyDate() {
        return lastUserReplyDate;
    }

    public int getSlaPlanId() {
        return slaPlanId;
    }

    public Date getNextReplyDueDate() {
        return nextReplyDueDate;
    }

    public Date getResolutionDueDate() {
        return resolutionDueDate;
    }

    public int getReplies() {
        return replies;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public int getCreationMode() {
        return creationMode;
    }

    public int getCreationType() {
        return creationType;
    }

    public boolean isEscalated() {
        return escalated;
    }

    public int getEscalationRuleId() {
        return escalationRuleId;
    }

    public int getTemplateGroupId() {
        return templateGroupId;
    }

    public String getTemplateGroupName() {
        return templateGroupName;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<TicketWatcher> getWatchers() {
        return watchers;
    }

    public List<TicketWorkflow> getWorkflows() {
        return workflows;
    }

    @XmlRootElement(name = "watcher")
    public static class TicketWatcher {
        @XmlAttribute(name = "staffid")
        private int staffId;
        @XmlAttribute(name = "name")
        private String staffName;

        public int getStaffId() {
            return staffId;
        }

        public String getStaffName() {
            return staffName;
        }
    }

    @XmlRootElement(name = "workflow")
    public static class TicketWorkflow {
        @XmlAttribute
        private int id;
        @XmlAttribute
        private String title;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}

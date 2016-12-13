package org.penguin.kayako.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * An object used to unmarshall api responses from Kayako.
 *
 * @author eugene
 */
@XmlRootElement(name = "users")
public class UserCollection {
    @XmlElement(name = "user")
    private List<User> users = Lists.newArrayList();

    public List<User> getUsers() {
        return users;
    }
}

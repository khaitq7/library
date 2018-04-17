package funtap.com.demochat.model;

import java.util.List;

/**
 * Created by Vinh on 4/15/2018.
 */

public class Group {
    String groupName;
    String groupId;

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

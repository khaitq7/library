package funtap.com.demochat.model;

import java.util.List;

/**
 * Created by Vinh on 4/12/2018.
 */

public class User {
    private String displayName;
    private String image;
    private String roleId;
    private String areaId;
    private Group group;
    private List<Friends> friends;
    public User() {
    }

    public User(String displayName, String image, String roleId, String areaId, List<Friends> friends) {
        this.displayName = displayName;
        this.image = image;
        this.roleId = roleId;
        this.areaId = areaId;
        this.friends = friends;
    }

    public User(Group group) {
        this.group = group;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Friends> getFriends() {
        return friends;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }
}

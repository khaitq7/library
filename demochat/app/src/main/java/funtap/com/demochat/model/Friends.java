package funtap.com.demochat.model;

/**
 * Created by Vinh on 4/17/2018.
 */

public class Friends {
    private String displayName;
    private String image;
    private String roleId;
    private String areaId;

    public Friends() {
    }

    public Friends(String displayName, String image, String roleId, String areaId) {
        this.displayName = displayName;
        this.image = image;
        this.roleId = roleId;
        this.areaId = areaId;
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
}

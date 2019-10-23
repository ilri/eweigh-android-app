package org.ilri.eweigh.accounts.models;

public class User {

    /**
     *
     * Request/response vars
     *
     * */
    public static final String ID = "userid";
    public static final String FULL_NAME = "fullname";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String ID_NUMBER = "idnumber";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county";
    public static final String PASSWORD = "password";
    public static final String VERIFICATION_CODE = "verification_code";
    public static final String FCM_TOKEN = "fcm_token";

    /**
     *
     * Model vars
     *
     * */
    private int userId;
    private String fullName;
    private String email;
    private String mobile;
    private String photo;
    private int groupId;
    private String groupName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

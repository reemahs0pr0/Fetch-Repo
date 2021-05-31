package com.example.fetchrepo;

public class Repository {

    private String displayName;
    private String type;
    private String dateOfCreation;
    private String avatar;

    public Repository(String displayName, String type, String dateOfCreation, String avatar) {
        this.displayName = displayName;
        this.type = type;
        this.dateOfCreation = dateOfCreation;
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "displayName='" + displayName + '\'' +
                ", type='" + type + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

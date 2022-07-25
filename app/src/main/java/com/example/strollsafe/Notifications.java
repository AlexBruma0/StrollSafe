package com.example.strollsafe;

import io.realm.RealmObject;

public class Notifications extends RealmObject{
    private String pwdFirstName;
    private String pwdLastName;
    private String notificationTitle;
    private String NotificationContent;
    private int notificationPriority;
    private Long timestamp;
    public Notifications(){
    }

    public String getPwdFirstName() {
        return pwdFirstName;
    }

    public String getPwdLastName() {
        return pwdLastName;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationContent() {
        return NotificationContent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public int getNotificationPriority() {
        return notificationPriority;
    }

    public void setNotificationContent(String notificationContent) {
        NotificationContent = notificationContent;
    }

    public void setNotificationPriority(int notificationPriority) {
        this.notificationPriority = notificationPriority;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public void setPwdFirstName(String pwdFirstName) {
        this.pwdFirstName = pwdFirstName;
    }

    public void setPwdLastName(String pwdLastName) {
        this.pwdLastName = pwdLastName;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

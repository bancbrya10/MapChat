package edu.temple.chatapplication;

public class Message {
    private String userID;
    private long messageID;
    private String data;
    private boolean belongsToThisUser;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setBelongsToThisUser(boolean belongsToThisUser) {
        this.belongsToThisUser = belongsToThisUser;
    }

    public String getUserID() {

        return userID;
    }

    public long getMessageID() {
        return messageID;
    }

    public String getData() {
        return data;
    }

    public boolean belongsToThisUser() {
        return belongsToThisUser;
    }
}

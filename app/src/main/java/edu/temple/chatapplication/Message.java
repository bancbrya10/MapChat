package edu.temple.chatapplication;

public class Message {
    private String data;
    private boolean belongsToThisUser;

    public void setData(String data) {
        this.data = data;
    }

    public void setBelongsToThisUser(boolean belongsToThisUser) {
        this.belongsToThisUser = belongsToThisUser;
    }

    public String getData() {
        return data;
    }

    public boolean belongsToThisUser() {
        return belongsToThisUser;
    }
}

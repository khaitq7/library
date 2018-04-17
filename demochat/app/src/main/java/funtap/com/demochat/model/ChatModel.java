package funtap.com.demochat.model;


/**
 * Created by Vinh on 4/12/2018.
 */

public class ChatModel {

    private String sendId;
    private String receiverId;
    private UserModel user;
    private String message;
    private String timeStamp;
    private FileModel file;

    public ChatModel() {
    }

    public ChatModel(UserModel user, String message, String timeStamp, FileModel file) {
        this.user = user;
        this.message = message;
        this.timeStamp = timeStamp;
        this.file = file;
    }

    public ChatModel(String sendId, String receiverId, UserModel user, String message, String timeStamp, FileModel file) {
        this.sendId = sendId;
        this.receiverId = receiverId;
        this.user = user;
        this.message = message;
        this.timeStamp = timeStamp;
        this.file = file;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                ", file=" + file +
                ", timeStamp='" + timeStamp + '\'' +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}

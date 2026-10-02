public class Notification {
    private int notiId;
    private int userId;
    private int taskId;
    private String message;
    private boolean isRead;
    private String createdAt;

    public Notification(int notiId, int userId, int taskId, String message, boolean isRead, String createdAt) {
        this.notiId = notiId;
        this.userId = userId;
        this.taskId = taskId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getNotiId() { return notiId; }
    public int getUserId() { return userId; }
    public int getTaskId() { return taskId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }
}

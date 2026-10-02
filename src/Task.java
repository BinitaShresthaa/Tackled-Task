public class Task {
    private int taskId;
    private int userId;
    private String title;
    private String dueDate;   // store as String for easy display
    private String status;
    private String createdAt;

    // Constructor for new task
    public Task(int userId, String title, String dueDate, String status) {
        this.userId = userId;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Constructor for tasks from DB
    public Task(int taskId, int userId, String title, String dueDate, String status, String createdAt) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
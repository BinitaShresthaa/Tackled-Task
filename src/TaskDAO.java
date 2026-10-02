import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    // ===== Add Task =====
    public boolean addTask(Task task) {

        String sql = "INSERT INTO task (user_id, title, due_date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDueDate());
            stmt.setString(4, task.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== Get Tasks By User =====
    public List<Task> getTasksByUser(int userId) {

        List<Task> list = new ArrayList<>();

        String sql = "SELECT * FROM task WHERE user_id=? ORDER BY due_date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Task task = new Task(
                        rs.getInt("task_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("due_date"),
                        rs.getString("status"),
                        rs.getString("created_at")
                );

                list.add(task);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== Update Task =====
    public boolean updateTask(Task task) {

        String sql = "UPDATE task SET title=?, due_date=?, status=? WHERE task_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDueDate());
            stmt.setString(3, task.getStatus());
            stmt.setInt(4, task.getTaskId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ===== Delete Task =====
    public boolean deleteTask(int taskId) {

        String sql = "DELETE FROM task WHERE task_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
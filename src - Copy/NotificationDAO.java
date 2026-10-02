import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    // Notifications that should show now (created_at <= NOW and unread)
    public List<Notification> getDueNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id=? AND is_read=0 AND created_at <= NOW() ORDER BY created_at";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("noti_id"),
                        rs.getInt("user_id"),
                        rs.getInt("task_id"),
                        rs.getString("message"),
                        rs.getBoolean("is_read"),
                        rs.getString("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // All notifications (for viewing)
    public List<Notification> getAllNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id=? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("noti_id"),
                        rs.getInt("user_id"),
                        rs.getInt("task_id"),
                        rs.getString("message"),
                        rs.getBoolean("is_read"),
                        rs.getString("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mark single notification as read
    public void markAsRead(int notiId) {
        String sql = "UPDATE notifications SET is_read=1 WHERE noti_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notiId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
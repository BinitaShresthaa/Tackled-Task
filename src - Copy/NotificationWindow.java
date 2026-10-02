import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NotificationWindow extends JFrame {

    private int userId;
    private JTable table;
    private DefaultTableModel model;

    public NotificationWindow(int userId) {
        this.userId = userId;

        setTitle("Notifications");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Message", "Time", "Read"}, 0);
        table = new JTable(model);

        JButton markReadBtn = new JButton("Mark as Read");
        JButton refreshBtn = new JButton("Refresh");

        JPanel btnPanel = new JPanel();
        btnPanel.add(markReadBtn);
        btnPanel.add(refreshBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        markReadBtn.addActionListener(e -> markSelectedRead());
        refreshBtn.addActionListener(e -> loadNotifications());

        loadNotifications();
        setVisible(true);
    }

    private void loadNotifications() {
        model.setRowCount(0);
        NotificationDAO dao = new NotificationDAO();

        for (Notification n : dao.getAllNotifications(userId)) {
            model.addRow(new Object[]{
                    n.getNotiId(),
                    n.getMessage(),
                    n.getCreatedAt(),
                    n.isRead() ? "Yes" : "No"
            });
        }
    }

    private void markSelectedRead() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a notification first!");
            return;
        }

        int notiId = (int) model.getValueAt(row, 0);
        NotificationDAO dao = new NotificationDAO();
        dao.markAsRead(notiId);

        loadNotifications();
    }
}
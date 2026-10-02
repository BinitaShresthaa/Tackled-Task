import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Dashboard extends JFrame {

    private int userId;
    private String username;

    private JTable taskTable;
    private DefaultTableModel tableModel;

    public Dashboard(int userId, String username) {
        this.userId = userId;
        this.username = username;

        setTitle("Tackled Task - Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        // ===== Search & Filter Panel (UI only) =====
        JPanel searchPanel = new JPanel();

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        JComboBox<String> filterCombo =
                new JComboBox<>(new String[]{"All", "Completed", "Pending"});

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterCombo);
        searchPanel.add(backButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        // ===== Table =====
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Due Date", "Status"}, 0) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton notificationButton = new JButton("Notifications");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(notificationButton);

        // ===== Layout =====
        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Logout =====
        logoutButton.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        // ===== Add Task =====
        addButton.addActionListener(e ->
                new AddTaskForm(userId, this));

        // ===== Edit Task =====
        editButton.addActionListener(e -> {

            int row = taskTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a task first!");
                return;
            }

            int taskId = (int) tableModel.getValueAt(row, 0);
            String title = (String) tableModel.getValueAt(row, 1);
            String dueDate = (String) tableModel.getValueAt(row, 2);
            String status = (String) tableModel.getValueAt(row, 3);

            new EditTaskForm(userId, this, taskId, title, dueDate, status);
        });

        // ===== Delete Task =====
        deleteButton.addActionListener(e -> {

            int row = taskTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a task first!");
                return;
            }

            int taskId = (int) tableModel.getValueAt(row, 0);

            TaskDAO dao = new TaskDAO();

            if (dao.deleteTask(taskId)) {
                JOptionPane.showMessageDialog(this, "Task Deleted!");
                loadTasks();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed!");
            }
        });

        // ===== Notification (Disabled) =====
        notificationButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Notifications disabled."));

        loadTasks();

        setVisible(true);
    }

    // ===== Load Tasks =====
    public void loadTasks() {

        TaskDAO dao = new TaskDAO();

        tableModel.setRowCount(0);

        for (Task task : dao.getTasksByUser(userId)) {

            tableModel.addRow(new Object[]{
                    task.getTaskId(),
                    task.getTitle(),
                    task.getDueDate(),
                    task.getStatus()
            });
        }
    }
}

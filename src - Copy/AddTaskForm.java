import javax.swing.*;
import java.awt.*;
import java.util.Date;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;

public class AddTaskForm extends JFrame {

    private int userId;
    private Dashboard dashboard;
    private JTextField titleField;
    private JSpinner dateSpinner;

    public AddTaskForm(int userId, Dashboard dashboard) {
        this.userId = userId;
        this.dashboard = dashboard;

        setTitle("Add Task");
        setSize(420, 220);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Title:");
        JLabel dueLabel = new JLabel("Due Date & Time:");

        titleField = new JTextField();

        // ✅ Date + Time Picker
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd hh:mm a");        dateSpinner.setEditor(editor);

        JButton saveButton = new JButton("Save Task");

        gbc.gridx = 0; gbc.gridy = 0;
        add(titleLabel, gbc);
        gbc.gridx = 1;
        add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(dueLabel, gbc);
        gbc.gridx = 1;
        add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(saveButton, gbc);

        saveButton.addActionListener(e -> saveTask());

        setVisible(true);
    }

    private void saveTask() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title required!");
            return;
        }

        Date selectedDate = (Date) dateSpinner.getValue();

        // ✅ validation: must be future
        if (selectedDate.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Date must be future!");
            return;
        }

        // ✅ store in 24‑hour format for DB
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dueDateTime = sdf.format(selectedDate);

        Task task = new Task(userId, title, dueDateTime, "pending");
        TaskDAO dao = new TaskDAO();

        if (dao.addTask(task)) {
            JOptionPane.showMessageDialog(this, "Task Added!");
            dashboard.loadTasks();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed!");
        }
    }
}
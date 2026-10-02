import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTaskForm extends JFrame {

    private int userId;
    private int taskId;
    private Dashboard dashboard;

    private JTextField titleField;
    private JComboBox<String> statusBox;
    private JSpinner dateSpinner;

    public EditTaskForm(int userId, Dashboard dashboard, int taskId,
                        String title, String dueDate, String status) {

        this.userId = userId;
        this.dashboard = dashboard;
        this.taskId = taskId;

        setTitle("Edit Task");
        setSize(420,250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Title:");
        JLabel dueLabel = new JLabel("Due Date & Time:");
        JLabel statusLabel = new JLabel("Status:");

        titleField = new JTextField(title);

        // Date spinner
        SpinnerDateModel model =
                new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);

        dateSpinner = new JSpinner(model);

        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(dateSpinner,"yyyy-MM-dd hh:mm a");

        dateSpinner.setEditor(editor);

        // Load existing date
        try {
            SimpleDateFormat inFmt =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d = inFmt.parse(dueDate);

            dateSpinner.setValue(d);

        } catch (Exception e) {
            e.printStackTrace();
        }

        statusBox = new JComboBox<>(new String[]{"pending","completed"});
        statusBox.setSelectedItem(status);

        JButton updateButton = new JButton("Update Task");

        gbc.gridx=0; gbc.gridy=0;
        add(titleLabel,gbc);
        gbc.gridx=1;
        add(titleField,gbc);

        gbc.gridx=0; gbc.gridy=1;
        add(dueLabel,gbc);
        gbc.gridx=1;
        add(dateSpinner,gbc);

        gbc.gridx=0; gbc.gridy=2;
        add(statusLabel,gbc);
        gbc.gridx=1;
        add(statusBox,gbc);

        gbc.gridx=0; gbc.gridy=3;
        gbc.gridwidth=2;
        add(updateButton,gbc);

        updateButton.addActionListener(e -> updateTask());

        setVisible(true);
    }

    private void updateTask(){

        String title = titleField.getText().trim();
        String status = (String) statusBox.getSelectedItem();

        if(title.isEmpty()){
            JOptionPane.showMessageDialog(this,"Title required!");
            return;
        }

        Date date = (Date) dateSpinner.getValue();

        // Optional validation (same as AddTask)
        if(date.before(new Date())){
            JOptionPane.showMessageDialog(this,"Date must be in the future!");
            return;
        }

        SimpleDateFormat outFmt =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dueDateTime = outFmt.format(date);

        Task task = new Task(taskId,userId,title,dueDateTime,status,null);

        TaskDAO dao = new TaskDAO();

        if(dao.updateTask(task)){

            JOptionPane.showMessageDialog(this,"Task Updated!");

            dashboard.loadTasks();

            dispose();

        }else{

            JOptionPane.showMessageDialog(this,"Update failed!");

        }
    }
}
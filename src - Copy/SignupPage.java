import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.sql.*;

public class SignupPage extends JFrame {

    private JTextField emailField, usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signupButton, backButton;

    public SignupPage() {
        setTitle("Tackled Task - Sign Up");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel emailLabel = new JLabel("Email:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmLabel = new JLabel("Confirm Password:");

        emailField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        signupButton = new JButton("Sign Up");
        backButton = new JButton("Back to Login");

        gbc.gridx = 0; gbc.gridy = 0;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(confirmLabel, gbc);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(signupButton, gbc);

        gbc.gridy = 5;
        add(backButton, gbc);

        signupButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        setVisible(true);
    }

    private void registerUser() {
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        // ✅ username only letters
        if (!username.matches("^[A-Za-z]+$")) {
            JOptionPane.showMessageDialog(this, "Username must contain only letters!");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters!");
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String checkSql = "SELECT * FROM users WHERE email=? OR username=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            checkStmt.setString(2, username);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Email or Username already exists!");
                return;
            }

            String insertSql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, hashPassword(password));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration Successful!");

            emailField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");

            new LoginPage();
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
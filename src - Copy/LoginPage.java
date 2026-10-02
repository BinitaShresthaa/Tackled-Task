import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.sql.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;

    public LoginPage() {
        setTitle("Tackled Task - Login");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        signupButton = new JButton("Don't have account? Sign Up");

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridy = 3;
        add(signupButton, gbc);

        // Actions
        loginButton.addActionListener(e -> loginUser());
        signupButton.addActionListener(e -> {
            new SignupPage(); // open signup page
            dispose();        // close login window
        });

        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());


        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new Dashboard(rs.getInt("user_id"), rs.getString("username"));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Password hashing (same as signup)
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
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginPage() {
        super("Login Page");
        setSize(300, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel());
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        add(statusLabel);
        add(new JLabel());

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (validateLogin(username, password)) {
            statusLabel.setText("Login successful!");
            statusLabel.setForeground(new Color(0, 128, 0));
            SwingUtilities.invokeLater(() -> {
                dispose();
                new FinalProject().setVisible(true);
            });
        } else {
            statusLabel.setText("Invalid credentials.");
            statusLabel.setForeground(Color.RED);
        }
    }

    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String fileUsername = parts[0].trim();
                    String filePassword = parts[1].trim();
                    if (username.equals(fileUsername) && password.equals(filePassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {}
        return false;
    }
}

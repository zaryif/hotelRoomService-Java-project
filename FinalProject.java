import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FinalProject extends JFrame implements ActionListener, MouseListener {
    private JTextField nameField, dateField, dayField;
    private JButton confirmButton;
    private JButton[] roomButtons = new JButton[10];
    private JTextArea bookingInfoArea;
    private boolean[] roomOccupied = new boolean[10];
    private int currentRoom = 0;
    private final int ROOM_PRICE = 100;

    public FinalProject() {
        super("Hotel Room Service");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        ImageIcon logoIcon = new ImageIcon("rsz_logo.png");
        JLabel logoLable = new JLabel(logoIcon);
        logoLable.setBounds((800-logoIcon.getIconWidth())/2, 10, logoIcon.getIconWidth(), logoIcon.getIconHeight());
        add(logoLable);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 170, 50, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(65, 170, 50, 30);
        nameField.addMouseListener(this);
        add(nameField);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(130, 170, 75, 30);
        add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(165, 170, 50, 30);
        dateField.addMouseListener(this);
        add(dateField);

        JLabel dayLabel = new JLabel("Days:");
        dayLabel.setBounds(230, 170, 75, 30);
        add(dayLabel);

        dayField = new JTextField();
        dayField.setBounds(270, 170, 50, 30);
        dayField.addMouseListener(this);
        add(dayField);

        confirmButton = new JButton("Book");
        confirmButton.setBounds(345, 170, 100, 30);
        confirmButton.addActionListener(this);
        confirmButton.addMouseListener(this);
        add(confirmButton);

        bookingInfoArea = new JTextArea();
        bookingInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingInfoArea);
        scrollPane.setBounds(20, 500, 740, 220);
        add(scrollPane);

        for (int i = 0; i < 10; i++) {
            roomButtons[i] = new JButton("Room " + (i + 1));
            roomButtons[i].setBounds(20 + (i % 5) * 150, 220 + (i / 5) * 150, 140, 100);
            roomButtons[i].setBackground(Color.GREEN);
            roomButtons[i].setEnabled(false);
            roomButtons[i].addMouseListener(this);
            add(roomButtons[i]);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String date = dateField.getText();
        String day = dayField.getText();

        if (!name.isEmpty() && !date.isEmpty() && currentRoom < 10) {
            roomButtons[currentRoom].setBackground(Color.RED);
            roomOccupied[currentRoom] = true;
            int totalPrice = ROOM_PRICE * Integer.parseInt(day);
            bookingInfoArea.append("Room: " + (currentRoom + 1) + " " + "Name: " + name + ", Date: " + date + ", Days: " + day + ", Price: $" + totalPrice + "\n");
            currentRoom++;
        }
    }

    public void mouseClicked(MouseEvent me) {}

    public void mousePressed(MouseEvent me) {}

    public void mouseReleased(MouseEvent me) {}

    public void mouseEntered(MouseEvent me) {
        if (me.getSource() == confirmButton) {
            confirmButton.setBackground(Color.YELLOW);
        }
    }

    public void mouseExited(MouseEvent me) {
        if (me.getSource() == confirmButton) {
            confirmButton.setBackground(null);
        }
    }
}

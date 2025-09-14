import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class FinalProject extends JFrame implements ActionListener, MouseListener {
    private JTextField nameField,dateField,dayField;
    private JButton confirmButton;
    private JButton[] roomButtons=new JButton[10];
    private JTextArea bookingInfoArea;
    private boolean[] roomOccupied=new boolean[10];
    private int currentRoom=0;
    private final int ROOM_PRICE=100;
    private JComboBox<String> packageCombo;
    private JLabel totalLabel;

    public FinalProject() {
        super("Hotel Room Service");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        ImageIcon logoIcon=new ImageIcon("rsz_logo.png");
        JLabel logoLable=new JLabel(logoIcon);
        logoLable.setLocation(350,10);
        logoLable.setSize(logoIcon.getIconWidth(),logoIcon.getIconHeight());
        add(logoLable);

        JLabel nameLabel=new JLabel("Name:");
        nameLabel.setLocation(20,170);
        nameLabel.setSize(50,30);
        add(nameLabel);

        nameField=new JTextField();
        nameField.setLocation(65,170);
        nameField.setSize(50,30);
        nameField.addMouseListener(this);
        add(nameField);

        JLabel dateLabel=new JLabel("Date:");
        dateLabel.setLocation(130,170);
        dateLabel.setSize(75,30);
        add(dateLabel);

        dateField=new JTextField();
        dateField.setLocation(165,170);
        dateField.setSize(50,30);
        dateField.addMouseListener(this);
        add(dateField);

        JLabel dayLabel=new JLabel("Days:");
        dayLabel.setLocation(230,170);
        dayLabel.setSize(75,30);
        add(dayLabel);

        dayField=new JTextField();
        dayField.setLocation(270,170);
        dayField.setSize(50,30);
        dayField.addMouseListener(this);
        add(dayField);

        JLabel packageLabel=new JLabel("Package:");
        packageLabel.setLocation(20,210);
        packageLabel.setSize(80,30);
        add(packageLabel);

        String[] packages={"1 Room","2 Rooms","3 Rooms"};
        packageCombo=new JComboBox<>(packages);
        packageCombo.setLocation(100,210);
        packageCombo.setSize(100,30);
        add(packageCombo);

        confirmButton=new JButton("Book");
        confirmButton.setLocation(345,170);
        confirmButton.setSize(100,30);
        confirmButton.addActionListener(this);
        confirmButton.addMouseListener(this);
        add(confirmButton);

        totalLabel=new JLabel("Total: $0");
        totalLabel.setLocation(220,210);
        totalLabel.setSize(150,30);
        add(totalLabel);

        bookingInfoArea=new JTextArea();
        bookingInfoArea.setEditable(false);
        JScrollPane scrollPane=new JScrollPane(bookingInfoArea);
        scrollPane.setLocation(20,500);
        scrollPane.setSize(740,220);
        add(scrollPane);

        for(int i=0;i<10;i++){
            roomButtons[i]=new JButton("Room "+(i+1));
            roomButtons[i].setLocation(20+(i%5)*150,250+(i/5)*150);
            roomButtons[i].setSize(140,100);
            roomButtons[i].setBackground(new Color(0,255,0));
            roomButtons[i].setOpaque(true);
            roomButtons[i].setBorderPainted(false);
            roomButtons[i].setEnabled(true);
            roomButtons[i].addActionListener(this);
            roomButtons[i].addMouseListener(this);
            add(roomButtons[i]);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==confirmButton){
            String name=nameField.getText();
            String date=dateField.getText();
            String day=dayField.getText();
            
            if(!name.isEmpty() && !date.isEmpty() && !day.isEmpty()){
                String selectedPackage=(String)packageCombo.getSelectedItem();
                int numRooms=1;
                if(selectedPackage.equals("2 Rooms")) numRooms=2;
                else if(selectedPackage.equals("3 Rooms")) numRooms=3;
                
                if(currentRoom+numRooms<=10){
                    int totalPrice=ROOM_PRICE*Integer.parseInt(day)*numRooms;
                    
                    bookingInfoArea.append("Hotel Booking\n");
                    bookingInfoArea.append("Customer Name: "+name+"\n");
                    bookingInfoArea.append("Check-in Date: "+date+"\n");
                    bookingInfoArea.append("Number of Days: "+day+"\n");
                    bookingInfoArea.append("Package: "+selectedPackage+"\n");
                    
                    for(int i=0;i<numRooms;i++){
                        roomButtons[currentRoom].setBackground(new Color(255,0,0));
                        roomOccupied[currentRoom]=true;
                        bookingInfoArea.append("Room "+(currentRoom+1)+": $"+ROOM_PRICE+" x "+day+" days = $"+ROOM_PRICE*Integer.parseInt(day)+"\n");
                        currentRoom++;
                    }
                    
                    bookingInfoArea.append("Total Amount: $"+totalPrice+"\n");
                    bookingInfoArea.append("\n");
                    
                    totalLabel.setText("Total: $"+totalPrice);
                    
                    saveBookingToFile(name,date,day,selectedPackage,numRooms,totalPrice);
                    
                    nameField.setText("");
                    dateField.setText("");
                    dayField.setText("");
                }else{
                    JOptionPane.showMessageDialog(this,"Not enough rooms available!");
                }
            }else{
                JOptionPane.showMessageDialog(this,"Please fill all fields!");
            }
        }else{
            for(int i=0;i<10;i++){
                if(e.getSource()==roomButtons[i]){
                    if(!roomOccupied[i]){
                        roomButtons[i].setBackground(new Color(255,0,0));
                        roomOccupied[i]=true;
                        bookingInfoArea.append("Room "+(i+1)+" is now booked\n");
                    }else{
                        roomButtons[i].setBackground(new Color(0,255,0));
                        roomOccupied[i]=false;
                        bookingInfoArea.append("Room "+(i+1)+" is now available\n");
                    }
                    break;
                }
            }
        }
    }

    private void saveBookingToFile(String name,String date,String day,String packageType,int numRooms,int totalPrice){
        try{
            FileWriter writer=new FileWriter("bookings.txt",true);
            writer.write("Booking Date: "+new Date()+"\n");
            writer.write("Customer: "+name+"\n");
            writer.write("Check-in: "+date+"\n");
            writer.write("Days: "+day+"\n");
            writer.write("Package: "+packageType+"\n");
            writer.write("Rooms: "+numRooms+"\n");
            writer.write("Total: $"+totalPrice+"\n");
            writer.write("----------------------------------------\n");
            writer.close();
        }catch(IOException e){
            System.out.println("Error saving file: "+e.getMessage());
        }
    }

    public void mouseClicked(MouseEvent me) {}

    public void mousePressed(MouseEvent me) {}

    public void mouseReleased(MouseEvent me) {}

    public void mouseEntered(MouseEvent me) {
        if(me.getSource()==confirmButton){
            confirmButton.setBackground(Color.YELLOW);
        }
    }

    public void mouseExited(MouseEvent me) {
        if(me.getSource()==confirmButton){
            confirmButton.setBackground(null);
        }
    }
}

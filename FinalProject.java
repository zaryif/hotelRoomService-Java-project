import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class FinalProject extends JFrame implements ActionListener, MouseListener {
    private JTextField nameField,dateField,dayField;
    private JButton confirmButton;
    private JButton clearButton;
    private JButton[] roomButtons=new JButton[10];
    private JTextArea bookingInfoArea;
    private boolean[] roomOccupied=new boolean[10];
    private boolean[] roomSelected=new boolean[10];
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

        String[] packages={"1 Room","2 Rooms","3 Rooms","4 Rooms","5 Rooms","6 Rooms","7 Rooms","8 Rooms","9 Rooms","10 Rooms"};
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

        clearButton=new JButton("Clear All");
        clearButton.setLocation(455,170);
        clearButton.setSize(100,30);
        clearButton.addActionListener(this);
        add(clearButton);

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
                int days;
                try{ days=Integer.parseInt(day); }
                catch(NumberFormatException nfe){
                    JOptionPane.showMessageDialog(this,"Days must be a number");
                    return;
                }

                int requiredCount=getRequiredRoomCount();
                java.util.List<Integer> roomsToBook=new java.util.ArrayList<>();
                for(int i=0;i<10;i++){
                    if(roomSelected[i] && !roomOccupied[i]) roomsToBook.add(i);
                }

                if(roomsToBook.size()==0){
                    JOptionPane.showMessageDialog(this,"Please select "+requiredCount+" room(s) by clicking the green buttons (they turn yellow)." );
                    return;
                }

                if(roomsToBook.size()!=requiredCount){
                    JOptionPane.showMessageDialog(this,"Selected "+roomsToBook.size()+" room(s). You must select exactly "+requiredCount+".");
                    return;
                }

                int totalPrice=ROOM_PRICE*days*roomsToBook.size();

                bookingInfoArea.append("Hotel Booking\n");
                bookingInfoArea.append("Customer Name: "+name+"\n");
                bookingInfoArea.append("Check-in Date: "+date+"\n");
                bookingInfoArea.append("Number of Days: "+days+"\n");
                bookingInfoArea.append("Rooms: ");

                for(int idx=0; idx<roomsToBook.size(); idx++){
                    int roomIndex=roomsToBook.get(idx);
                    // Confirm booking
                    roomButtons[roomIndex].setBackground(new Color(255,0,0));
                    roomOccupied[roomIndex]=true;
                    roomSelected[roomIndex]=false;
                    bookingInfoArea.append(""+(roomIndex+1)+(idx<roomsToBook.size()-1?", ":"\n"));
                }

                for(int roomIndex: roomsToBook){
                    bookingInfoArea.append("Room "+(roomIndex+1)+": $"+ROOM_PRICE+" x "+days+" days = $"+(ROOM_PRICE*days)+"\n");
                }

                bookingInfoArea.append("Total Amount: $"+totalPrice+"\n\n");

                totalLabel.setText("Total: $"+totalPrice);

                saveBookingToFile(name,date,String.valueOf(days),"Package-"+requiredCount,roomsToBook.size(),totalPrice,roomsToBook);

                nameField.setText("");
                dateField.setText("");
                dayField.setText("");
            }else{
                JOptionPane.showMessageDialog(this,"Please fill all fields!");
            }
        }else if(e.getSource()==clearButton){
            // Reset input fields
            nameField.setText("");
            dateField.setText("");
            dayField.setText("");
            if(packageCombo.getItemCount()>0){
                packageCombo.setSelectedIndex(0);
            }
            // Deselect any selected rooms; keep booked rooms red, available to green
            for(int i=0;i<10;i++){
                if(!roomOccupied[i]){
                    roomSelected[i]=false;
                    roomButtons[i].setBackground(new Color(0,255,0));
                }
            }
            totalLabel.setText("Total: $0");
            bookingInfoArea.setText("");
        }else{
            for(int i=0;i<10;i++){
                if(e.getSource()==roomButtons[i]){
                    if(roomOccupied[i]){
                        JOptionPane.showMessageDialog(this,"Room "+(i+1)+" is already booked.");
                    }else{
                        int requiredCount=getRequiredRoomCount();
                        int selectedCount=0;
                        for(int j=0;j<10;j++) if(roomSelected[j]) selectedCount++;
                        if(roomSelected[i]){
                            roomSelected[i]=false;
                            roomButtons[i].setBackground(new Color(0,255,0));
                        }else{
                            if(selectedCount>=requiredCount){
                                JOptionPane.showMessageDialog(this,"You can select at most "+requiredCount+" room(s) for this package.");
                            }else{
                                roomSelected[i]=true;
                                roomButtons[i].setBackground(Color.YELLOW);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private int getRequiredRoomCount(){
        String selectedPackage=(String)packageCombo.getSelectedItem();
        if(selectedPackage==null||selectedPackage.isEmpty()) return 1;
        if(selectedPackage.startsWith("10")) return 10;
        try{
            return Integer.parseInt(selectedPackage.substring(0,1));
        }catch(Exception ex){
            return 1;
        }
    }

    private void saveBookingToFile(String name,String date,String day,String packageType,int numRooms,int totalPrice, java.util.List<Integer> rooms){
        try{
            FileWriter writer=new FileWriter("bookings.txt",true);
            writer.write("Booking Date: "+new Date()+"\n");
            writer.write("Customer: "+name+"\n");
            writer.write("Check-in: "+date+"\n");
            writer.write("Days: "+day+"\n");
            writer.write("Package: "+packageType+"\n");
            writer.write("Rooms: "+numRooms+"\n");
            if(rooms!=null && !rooms.isEmpty()){
                writer.write("Room Numbers: ");
                for(int i=0;i<rooms.size();i++){
                    writer.write((rooms.get(i)+1)+ (i<rooms.size()-1?", ":"\n"));
                }
            }
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

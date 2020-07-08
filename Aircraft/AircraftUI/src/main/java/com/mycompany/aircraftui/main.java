/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aircraftui;
/**
 *
 * @author Prathamesh Kulkarni
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

class MyException extends Exception{
    String msg;
    
    MyException(String s){
        this.msg=s;
    }
    
    void print(){
        System.out.println(msg);
    }
}

class PassengerDetails{
    int flyPoints;
    int flightnum;
    String passName;

    PassengerDetails(int fnum,String pn,int fp){
        passName=pn;
        flyPoints=fp;
        flightnum=fnum;
    }

    void display(){
        System.out.println("Name: "+passName+"\n"+"Flight No: "+flightnum);
    }
}

class Ticket{
    PassengerDetails p;
    String seatNum;
    int tid;
    static int temp;
    int fnum;

    static{
        temp=1;
    }
    
    Ticket(String ss,int ff,String pn,int fp){
        seatNum=ss;
        tid=temp++;
        System.out.println("Seat initialized");
        p=new PassengerDetails(ff,pn,fp);

    }

    void display(){
        System.out.println("Id: "+tid+"\n"+"Seat no: "+seatNum);
        p.display();
        System.out.println("---------------------------");
    }
}

class Seat{
    String seatnum;
    boolean avail;

    Seat(String sn){
        seatnum=sn;
        avail=true;
    }

    void setSeat(){
        avail=false;
    }
}

class Luggage{
    int kg;
    int lid;
    String owner;
    static int temp;

    static{
        temp=1;
    }

    Luggage(int kkg,String own){
        kg=kkg;
        lid=temp++;
        owner=own;
    }
}

interface IAssign{
    void add(String a,int b) throws MyException;
    void delete(int temp) throws MyException;
    void display();
   

}

abstract class Aircraft<T> implements IAssign{
    int flightType;
    String airlineName;
    int flightnum;
    int freqFlightPoints;
    String departure;
    String arrival;
    String depTime;
    String arrTime;
    int capacity;
    int price;

    public Aircraft(int ft,String an,int fn,int fp,String d,String a,String dt,String at,int p){
        flightType=ft;
        airlineName=an;
        flightnum=fn;
        freqFlightPoints=fp;
        departure=d;
        arrival=a;
        depTime=dt;
        arrTime=at;
        price=p;

    }
    public abstract ArrayList<T> returnfun();
    
}

class PassengerFlight extends Aircraft {
    int rows;
    int cols;
    int seatOcc;
    int it;
    ArrayList <Ticket> allTickets;
    ArrayList <Seat> allSeats;

    PassengerFlight(int rr,int cc,int ft,String an,int fn,int fp,String d,String a,String dt,String at,int p){
        super(ft,an, fn, fp, d, a, dt, at, p);
        seatOcc=0;
        rows=rr;
        cols=cc;

        allSeats = new ArrayList<Seat>(rows*cols);
        allTickets = new ArrayList<Ticket>(rows*cols);
        String col_names="ABCDEFGHIJKLMNOP";
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                allSeats.add(new Seat(Integer.toString(i+1)+col_names.charAt(j)));
            }
        }
        
    }

    public void add(String pn,int fp) throws MyException{
        for(Seat it: allSeats){
            if(it.avail){
                it.avail=false;
                allTickets.add(new Ticket(it.seatnum,flightnum, pn,fp));
                System.out.println("Seat No "+it.seatnum);
                return;
            }
        }
        
        throw new MyException("Sorry Seats Full");
    }

    public void delete(int temp) throws MyException{
        for(Ticket it: allTickets){
            if(it.tid==temp){
                for(Seat sit: allSeats){
                    if(sit.seatnum == it.seatNum){
                        sit.avail=true;
                        allTickets.remove(it);
                        System.out.println("Ticket deleted");
                        return;
                    }
                }
                
            }
        }
        throw new MyException("Ticket Id not found");

    }

    public void display(){
        System.out.println("Displaying passenger details");
        String format = "%d%20s%20s%30d%20d\n";
        String format2 = "%s%20s%20s%30s%20s\n";
        System.out.format(format2,"TicketId","Seat no","Passenger Name","Flight no","Fly Points");
        System.out.println("===================================================================================================");
        for(Ticket t: allTickets){
            System.out.format(format,t.tid,t.seatNum,t.p.passName,t.p.flightnum,t.p.flyPoints);
        }
        
    }
    
    public ArrayList<Ticket> returnfun(){
        return allTickets;
    }
}

class CargoFlight extends Aircraft{
    ArrayList <Luggage> allLuggages;
    int maxkg;
    int currkg;

    CargoFlight(int mkg,int ft,String an,int fn,int fp,String d,String a,String dt,String at,int p){
        super(ft,an, fn, fp, d, a, dt, at, p);
        allLuggages = new ArrayList <Luggage> (20);
        maxkg=mkg;
        currkg=0;
    }

    public void add(String own,int temp) throws MyException{
        if(currkg+temp<=maxkg){
            allLuggages.add(new Luggage(temp,own));
            currkg=currkg+temp;
        }
        else{
            System.out.println("Max capacity exceeded");
            throw new MyException("Luggage limit Exceeded");
        }
            
        
    }

    public void delete(int temp) throws MyException{
        for(Luggage l: allLuggages){
            if(l.lid == temp){
                allLuggages.remove(l);
                System.out.println("Luggage Removed");
                currkg-=l.kg;
                return;
            }
        }
        throw new MyException("No Luggage ID found");
    }

    public void display(){
        System.out.println("Displaying Luggage details");
        System.out.println("Id     Weight");
        for(Luggage l: allLuggages){
            System.out.println(l.lid+"   "+l.kg);
        }
        
    }
    
    public ArrayList<Luggage> returnfun(){
        System.out.println("Nothing to worry");
        return allLuggages;
    }



}

public class main extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    
    public Aircraft ac[];
    public Passenger ap[];
    public Cargo cp[];
    public int index,index1,aindex;
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        submitDialog = new javax.swing.JDialog();
        HeaderLabel = new javax.swing.JLabel();
        HeaderLabel1 = new javax.swing.JLabel();
        FieldL1 = new javax.swing.JLabel();
        FieldL2 = new javax.swing.JLabel();
        FieldL3 = new javax.swing.JLabel();
        FieldL4 = new javax.swing.JLabel();
        FieldL5 = new javax.swing.JLabel();
        FieldL6 = new javax.swing.JLabel();
        FieldL7 = new javax.swing.JLabel();
        FieldL8 = new javax.swing.JLabel();
        FieldL9 = new javax.swing.JLabel();
        airlineNumber = new javax.swing.JTextField();
        freq = new javax.swing.JTextField();
        price = new javax.swing.JTextField();
        FlightType = new javax.swing.JComboBox<>();
        depCity = new javax.swing.JComboBox<>();
        arrCity = new javax.swing.JComboBox<>();
        depHour = new javax.swing.JComboBox<>();
        depMin = new javax.swing.JComboBox<>();
        arrHour = new javax.swing.JComboBox<>();
        arrMin = new javax.swing.JComboBox<>();
        submit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        FieldL10 = new javax.swing.JLabel();
        rows = new javax.swing.JTextField();
        FieldL11 = new javax.swing.JLabel();
        cols = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        FieldL12 = new javax.swing.JLabel();
        weight = new javax.swing.JTextField();
        logoutButton = new javax.swing.JButton();
        airlineName = new javax.swing.JComboBox<>();
        anoerror = new javax.swing.JLabel();
        freqerror = new javax.swing.JLabel();
        prerror = new javax.swing.JLabel();
        arrLabel = new javax.swing.JLabel();
        depLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        all = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        image = new javax.swing.JLabel();

        submitDialog.setTitle("Flight Created Successfully");

        javax.swing.GroupLayout submitDialogLayout = new javax.swing.GroupLayout(submitDialog.getContentPane());
        submitDialog.getContentPane().setLayout(submitDialogLayout);
        submitDialogLayout.setHorizontalGroup(
            submitDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        submitDialogLayout.setVerticalGroup(
            submitDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().setLayout(null);

        HeaderLabel.setFont(new java.awt.Font("Candara", 1, 48)); // NOI18N
        HeaderLabel.setForeground(new java.awt.Color(255, 255, 255));
        HeaderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HeaderLabel.setText("Aircraft Management System");
        getContentPane().add(HeaderLabel);
        HeaderLabel.setBounds(387, 24, 782, 73);

        HeaderLabel1.setFont(new java.awt.Font("Candara", 1, 24)); // NOI18N
        HeaderLabel1.setForeground(new java.awt.Color(255, 255, 255));
        HeaderLabel1.setText("Enter flight details");
        HeaderLabel1.setToolTipText("");
        getContentPane().add(HeaderLabel1);
        HeaderLabel1.setBounds(387, 115, 782, 49);

        FieldL1.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL1.setForeground(new java.awt.Color(255, 255, 255));
        FieldL1.setText("Airline Name:");
        getContentPane().add(FieldL1);
        FieldL1.setBounds(387, 193, 120, 27);

        FieldL2.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL2.setForeground(new java.awt.Color(255, 255, 255));
        FieldL2.setText("Airline Number:");
        getContentPane().add(FieldL2);
        FieldL2.setBounds(387, 238, 130, 27);

        FieldL3.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL3.setForeground(new java.awt.Color(255, 255, 255));
        FieldL3.setText("Frequent Flyer Points:");
        getContentPane().add(FieldL3);
        FieldL3.setBounds(390, 310, 180, 27);

        FieldL4.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL4.setForeground(new java.awt.Color(255, 255, 255));
        FieldL4.setText("Departure City:");
        getContentPane().add(FieldL4);
        FieldL4.setBounds(390, 370, 130, 27);

        FieldL5.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL5.setForeground(new java.awt.Color(255, 255, 255));
        FieldL5.setText("Arrival City:");
        getContentPane().add(FieldL5);
        FieldL5.setBounds(390, 420, 110, 27);

        FieldL6.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL6.setForeground(new java.awt.Color(255, 255, 255));
        FieldL6.setText("Departure Time:");
        getContentPane().add(FieldL6);
        FieldL6.setBounds(856, 193, 140, 27);

        FieldL7.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL7.setForeground(new java.awt.Color(255, 255, 255));
        FieldL7.setText("Arrival Time:");
        getContentPane().add(FieldL7);
        FieldL7.setBounds(856, 238, 110, 27);

        FieldL8.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL8.setForeground(new java.awt.Color(255, 255, 255));
        FieldL8.setText("Price:");
        getContentPane().add(FieldL8);
        FieldL8.setBounds(860, 310, 60, 27);

        FieldL9.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        FieldL9.setForeground(new java.awt.Color(255, 255, 255));
        FieldL9.setText("Flight Type:");
        getContentPane().add(FieldL9);
        FieldL9.setBounds(860, 370, 100, 27);
        getContentPane().add(airlineNumber);
        airlineNumber.setBounds(530, 240, 103, 30);
        getContentPane().add(freq);
        freq.setBounds(580, 310, 60, 30);
        getContentPane().add(price);
        price.setBounds(910, 310, 54, 30);

        FlightType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        FlightType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Flight Type", "Passenger", "Cargo" }));
        FlightType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FlightTypeActionPerformed(evt);
            }
        });
        getContentPane().add(FlightType);
        FlightType.setBounds(960, 370, 130, 40);

        depCity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        depCity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Departure City", "PNQ-PUNE", "HBX-HUBBALLI", "IGI-DELHI", "GOI-GOA", "BLR-BANGALORE", "BOM-BOMBAY", "CCS-CALCUTTA" }));
        depCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depCityActionPerformed(evt);
            }
        });
        getContentPane().add(depCity);
        depCity.setBounds(520, 370, 140, 30);

        arrCity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        arrCity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Arrival City", "HBX-HUBBALLI", "PNQ-PUNE", "IGI-DELHI", "GOI-GOA", "BLR-BANGALORE", "BOM-BOMBAY", "CCS-CALCUTTA" }));
        arrCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrCityActionPerformed(evt);
            }
        });
        getContentPane().add(arrCity);
        arrCity.setBounds(490, 420, 120, 30);

        depHour.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        depHour.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        getContentPane().add(depHour);
        depHour.setBounds(1000, 190, 50, 30);

        depMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        depMin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        depMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depMinActionPerformed(evt);
            }
        });
        getContentPane().add(depMin);
        depMin.setBounds(1050, 190, 50, 30);

        arrHour.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        arrHour.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        getContentPane().add(arrHour);
        arrHour.setBounds(980, 240, 50, 30);

        arrMin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        arrMin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        arrMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrMinActionPerformed(evt);
            }
        });
        getContentPane().add(arrMin);
        arrMin.setBounds(1030, 240, 50, 30);

        submit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        submit.setText("Submit");
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submitMouseClicked(evt);
            }
        });
        getContentPane().add(submit);
        submit.setBounds(740, 670, 86, 25);

        jLabel3.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("For Cargo Flights");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(390, 590, 190, 32);

        FieldL10.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        FieldL10.setForeground(new java.awt.Color(255, 255, 255));
        FieldL10.setText("Rows: ");
        getContentPane().add(FieldL10);
        FieldL10.setBounds(390, 550, 46, 27);
        getContentPane().add(rows);
        rows.setBounds(450, 550, 107, 30);

        FieldL11.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        FieldL11.setForeground(new java.awt.Color(255, 255, 255));
        FieldL11.setText("Columns: ");
        getContentPane().add(FieldL11);
        FieldL11.setBounds(590, 550, 67, 27);
        getContentPane().add(cols);
        cols.setBounds(660, 550, 107, 30);

        jLabel4.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("For Passenger Flights");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(390, 510, 190, 32);

        FieldL12.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        FieldL12.setForeground(new java.awt.Color(255, 255, 255));
        FieldL12.setText("Max weight:");
        getContentPane().add(FieldL12);
        FieldL12.setBounds(390, 630, 86, 27);
        getContentPane().add(weight);
        weight.setBounds(480, 630, 107, 30);

        logoutButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        getContentPane().add(logoutButton);
        logoutButton.setBounds(880, 670, 77, 25);

        airlineName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        airlineName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Air India", "Indigo", "Spice Jet", "Star Air", "Air Asia", "Jet Airways", " " }));
        getContentPane().add(airlineName);
        airlineName.setBounds(510, 190, 100, 23);

        anoerror.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(anoerror);
        anoerror.setBounds(390, 280, 280, 30);

        freqerror.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(freqerror);
        freqerror.setBounds(390, 343, 250, 20);

        prerror.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(prerror);
        prerror.setBounds(860, 343, 240, 20);

        arrLabel.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(arrLabel);
        arrLabel.setBounds(630, 420, 150, 30);

        depLabel.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(depLabel);
        depLabel.setBounds(670, 370, 180, 30);

        typeLabel.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(typeLabel);
        typeLabel.setBounds(1110, 380, 180, 20);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(390, 460, 270, 30);

        all.setText("Display Flights");
        all.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allMouseClicked(evt);
            }
        });
        getContentPane().add(all);
        all.setBounds(1520, 70, 210, 25);

        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(1520, 130, 310, 120);

        image.setIcon(new javax.swing.ImageIcon("C:\\Users\\Usman Khan\\Desktop\\Aircraft\\AircraftUI\\src\\main\\java\\com\\mycompany\\aircraftui\\aeroplane-wallpaper.jpg")); // NOI18N
        getContentPane().add(image);
        image.setBounds(0, 0, 1920, 1080);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitMouseClicked
        // TODO add your handling code here:
        System.out.println("enteres");
        int airNumber=0,freqFlyer=0,airPrice=0;
        int f1=0,f2=0,f3=0,f4=0,f5=0,f6 = 0,f7 = 0;
        String airName=airlineName.getSelectedItem().toString();
        try{
            airNumber=Integer.parseInt(airlineNumber.getText());
            anoerror.setVisible(false);
            f1=0;
        }
        catch(NumberFormatException ne)
        {
            anoerror.setVisible(true);
            anoerror.setText("Please enter numbers only!");
            f1=1;
        }
        
        try{
            airPrice=Integer.parseInt(price.getText());
            prerror.setVisible(false);
            f2=0;
        }
        catch(NumberFormatException ne)
        {
            prerror.setVisible(true);
            prerror.setText("Please enter numbers only!");
            f2=1;
        }
        
        
        try{
            freqFlyer=Integer.parseInt(freq.getText());
            freqerror.setVisible(false);
            f3=0;
        }
        catch(NumberFormatException ne)
        {
            freqerror.setVisible(true);
            freqerror.setText("Please enter numbers only!");
            f3=1;
        }
        
        
        String airDepHour=depHour.getSelectedItem().toString();
        String airDepMin=depMin.getSelectedItem().toString();
        String airArrHour=arrHour.getSelectedItem().toString();
        String airArrMin=arrMin.getSelectedItem().toString();
        String arrivalCity = arrCity.getSelectedItem().toString();
        String departureCity = depCity.getSelectedItem().toString();
        String flightValue = FlightType.getSelectedItem().toString();
        
        if(arrivalCity.equals(departureCity)){
            jLabel1.setVisible(true);
            jLabel1.setText("Departure and Arrival cities should be different!");
            f7 = 1;
        }
        
        else{
            f7 = 0;
            jLabel1.setVisible(false);
        }
        
        if(flightValue.equals("Select Flight Type")){
            typeLabel.setVisible(true);
            typeLabel.setText("Please select flight type");
            f6 = 1;
        }
        
        else{
            f6 = 0;
            typeLabel.setVisible(false);
        }
        
        if(arrivalCity.equals("Select Arrival City")){
            arrLabel.setVisible(true);
            arrLabel.setText("Please select arrival city");
            f4=1;
        }
        else{
            f4=0;
            arrLabel.setVisible(false);
        }
        
        if(departureCity.equals("Select Departure City")){
            depLabel.setVisible(true);
            depLabel.setText("Please select departure city");
            f5=1;
        }
        else{
            f5=0;
            depLabel.setVisible(false);
        }
        
        if(f1==1||f2==1||f3==1||f4==1||f5==1||f6 == 1||f7 == 1)
            return;
        
        if(flightValue.equals("Passenger"))
        {
            int row=Integer.parseInt(rows.getText());
            int col=Integer.parseInt(cols.getText());
            int ft=1;
            ac[aindex]=new PassengerFlight(row,col,ft,airName,airNumber,freqFlyer,departureCity,arrivalCity,airDepHour+":"+airDepMin,airArrHour+":"+airArrMin,airPrice);
            JOptionPane.showMessageDialog(this, "Aircraft Created Successfully", "Success",JOptionPane.INFORMATION_MESSAGE);
            ap[index] = new Passenger(ac[aindex]);
            ap[index].setVisible(true);
            index++;
            aindex++;
        }
        else
        {
            int kg=Integer.parseInt(weight.getText());
            int ft=2;
            ac[aindex]=new CargoFlight(kg,ft,airName,airNumber,freqFlyer,departureCity,arrivalCity,airDepHour+":"+airDepMin,airArrHour+":"+airArrMin,airPrice);
            JOptionPane.showMessageDialog(this, "Aircraft Created Successfully", "Success",JOptionPane.INFORMATION_MESSAGE);
            cp[index1] = new Cargo(ac[aindex]);
            cp[index1].setVisible(true);
            index1++;
            aindex++;
        }
        System.out.println("object created");
        for(int i=0;i<index;i++)
            System.out.println(ap[i].ac.airlineName);
        for(int i=0;i<aindex;i++)
            System.out.println(ac[i].airlineName);
    }//GEN-LAST:event_submitMouseClicked

    private void FlightTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FlightTypeActionPerformed
        // TODO add your handling code here:
        if(FlightType.getSelectedItem().toString().equals("Passenger"))
        {
            weight.setEnabled(false);
            rows.setEnabled(true);
            cols.setEnabled(true);
        }
        else{
            rows.setEnabled(false);
            cols.setEnabled(false);
            weight.setEnabled(true);
        }
    
        
    }//GEN-LAST:event_FlightTypeActionPerformed

    private void arrMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrMinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrMinActionPerformed

    private void depMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depMinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_depMinActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        new Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void depCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_depCityActionPerformed

    private void arrCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrCityActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_arrCityActionPerformed

    private void allMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allMouseClicked
        // TODO add your handling code here:
        for(int i=0;i<index;i++)
        {
            JOptionPane.showMessageDialog(this,ap[i].ac.airlineName+"\t"+ap[i].ac.flightnum , "Success",JOptionPane.INFORMATION_MESSAGE);
        }
        for(int i=0;i<index1;i++)
        {
            JOptionPane.showMessageDialog(this,cp[i].ac.airlineName+"\t"+cp[i].ac.flightnum , "Success",JOptionPane.INFORMATION_MESSAGE);
        }
            
        
    }//GEN-LAST:event_allMouseClicked

    
                                 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
        
        
          
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FieldL1;
    public static javax.swing.JLabel FieldL10;
    public static javax.swing.JLabel FieldL11;
    public static javax.swing.JLabel FieldL12;
    private javax.swing.JLabel FieldL2;
    private javax.swing.JLabel FieldL3;
    private javax.swing.JLabel FieldL4;
    private javax.swing.JLabel FieldL5;
    private javax.swing.JLabel FieldL6;
    private javax.swing.JLabel FieldL7;
    private javax.swing.JLabel FieldL8;
    private javax.swing.JLabel FieldL9;
    public static javax.swing.JComboBox<String> FlightType;
    private javax.swing.JLabel HeaderLabel;
    private javax.swing.JLabel HeaderLabel1;
    private javax.swing.JComboBox<String> airlineName;
    public static javax.swing.JTextField airlineNumber;
    private javax.swing.JButton all;
    private javax.swing.JLabel anoerror;
    public static javax.swing.JComboBox<String> arrCity;
    public static javax.swing.JComboBox<String> arrHour;
    private javax.swing.JLabel arrLabel;
    public static javax.swing.JComboBox<String> arrMin;
    public static javax.swing.JTextField cols;
    public static javax.swing.JComboBox<String> depCity;
    public static javax.swing.JComboBox<String> depHour;
    private javax.swing.JLabel depLabel;
    public static javax.swing.JComboBox<String> depMin;
    public static javax.swing.JTextField freq;
    private javax.swing.JLabel freqerror;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel prerror;
    public static javax.swing.JTextField price;
    public static javax.swing.JTextField rows;
    public static javax.swing.JButton submit;
    private javax.swing.JDialog submitDialog;
    private javax.swing.JLabel typeLabel;
    public static javax.swing.JTextField weight;
    // End of variables declaration//GEN-END:variables
    
    public main() {
        initComponents();
        ap=new Passenger[5];
        ac=new Aircraft[5];
        cp=new Cargo[5];
        index=0;
        index1=0;
        aindex=0;
        
        this.setExtendedState(MAXIMIZED_BOTH); 
//        submit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e){
//                airName=airlineName.getText();
//                String airNumber=airlineNumber.getText();
//                String airPrice=price.getText();
//                String freqFlyer=freq.getText();
//                String airDepHour=depHour.getSelectedItem().toString();
//                String airDepMin=depMin.getSelectedItem().toString();
//                String arrivalCity = arrCity.getSelectedItem().toString();
//                String departureCity = depCity.getSelectedItem().toString();
//                String flightValue = FlightType.getSelectedItem().toString();
//                
//                
//            }
//        
//        });
       
    }


}

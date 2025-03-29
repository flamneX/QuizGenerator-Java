package Program;

import Data.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class Login {
    private JFrame frame;
    private Account userAcc;
    private ArrayList<Student> studentList = new ArrayList<Student>();
    private ArrayList<Staff> staffList = new ArrayList<Staff>();
    private ArrayList<Admin> adminList = new ArrayList<Admin>();

    //Constructor
    public Login(JFrame frame) {
        this.frame = frame;
        Student.accGet(studentList);
        Staff.accGet(staffList);
        Admin.accGet(adminList);
        
        frame.setJMenuBar(null);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new LoginMenu());
        ((CardLayout) frame.getContentPane().getLayout()).first(frame.getContentPane());
    }


    //Login Menu
    private class LoginMenu extends Menu {
        private LoginMenu() {
            if (adminList.size() == 0) {
                new Menu.PopUp(frame, " Login", "No Admin Account Detected, Please Create A New Account").errorPane();
                new AccountMenu(frame, userAcc, null, "Sign Up");
            }

            //Menu Title
            gbc.insets = new Insets(0, 0, 10, 0);
            JPanel titlePanel = new JPanel();
            contentPanel.add(titlePanel, gbc);
            JLabel title = new JLabel("Login");
            title.setFont(new Font(Font.DIALOG, Font.PLAIN, 50));
            titlePanel.add(title);
            

            //Text Fields
            JPanel IDPanel = new JPanel();
            HintTextField IDField = new HintTextField("User ID", 20);
            JLabel IDIcon = new JLabel(new ImageIcon(new ImageIcon("image/user.png").getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
            IDPanel.add(IDIcon);
            IDPanel.add(IDField);
            contentPanel.add(IDPanel, gbc);

            JPanel passwordPanel = new JPanel();
            HintPasswordField passwordField = new HintPasswordField("Password", 20);
            JLabel passwordIcon = new JLabel(new ImageIcon(new ImageIcon("image/password.png").getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
            passwordPanel.add(passwordIcon);
            passwordPanel.add(passwordField);
            contentPanel.add(passwordPanel, gbc);


            //Button
            JPanel loginPanel = new JPanel();
            contentPanel.add(loginPanel, gbc);
            JButton login = new JButton("Login");
            shortButton(login);
            frame.getRootPane().setDefaultButton(login);
            login.addActionListener(ae -> {
                String[] loginCredentials = {IDField.getText().toUpperCase(), String.valueOf(passwordField.getPassword())};

                //Check For Matching Accounts
                for(Student student : studentList) {
                    if(loginCredentials[0].equals(student.getID()) && loginCredentials[1].equals(student.getPassword())) {
                        userAcc = student;
                        break;
                    }
                }
                for(Staff staff : staffList) {
                    if(loginCredentials[0].equals(staff.getID()) && loginCredentials[1].equals(staff.getPassword())) {
                        userAcc = staff;
                        break;
                    }
                }
                for(Admin admin : adminList) {
                    if(loginCredentials[0].equals(admin.getID()) && loginCredentials[1].equals(admin.getPassword())) {
                        userAcc = admin;
                        break;
                    }
                }

                //Check If Account Is Found
                if(!(userAcc instanceof Account)) {                    
                    IDField.reset();
                    passwordField.reset();
                    new Menu.PopUp(frame, "Login", "Invalid Username/Password").errorPane();
                } else {
                    new CourseMenu(frame, userAcc);
                }
            });
            loginPanel.add(login);
        }
    }
}
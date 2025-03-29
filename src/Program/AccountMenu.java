package Program;

import Data.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class AccountMenu {
    private JFrame frame;
    private Account userAcc;
    private Course selCourse;
    private String type;
    private Account editAcc;
    private ArrayList<Student> studentList = new ArrayList<Student>();
    private ArrayList<Staff> staffList = new ArrayList<Staff>();
    private ArrayList<Admin> adminList = new ArrayList<Admin>();

    //Constructor
    public AccountMenu(JFrame frame, Account userAcc, Course selCourse, String menuName) {
        this.frame = frame;
        this.userAcc = userAcc;
        this.selCourse = selCourse;

        switchPage(menuName);
    }

    //Page Switcher
    private void switchPage(String name) {
        frame.setJMenuBar(Menu.menuBar(frame, userAcc, selCourse));
        Container switchPanel = frame.getContentPane();

        //Self Edit
        if (name.equals("Self")) {
            switchPanel.add(new Menus().signUpUpdate(false));
            ((CardLayout) switchPanel.getLayout()).next(switchPanel);
        }

        else {
            switchPanel.removeAll();

            //Main Menu
            if (name.equals("Menu")) {
                switchPanel.add(new Menus().mainMenu());
            }

            //Add Menu
            else if (name.equals("Add")) {
                switchPanel.add(new Menus().maintenanceMenu(false));
            }

            //Edit Menu
            else if (name.equals("Edit")) {
                switchPanel.add(new Menus().maintenanceMenu(true));
            }
            
            //Sign Up
            else if (name.equals("Sign Up")) {
                switchPanel.add(new Menus().signUpUpdate(true));
            }

            //Type Select Menu
            else {
                switchPanel.add(new Menus().typeSelect());
            }

            ((CardLayout) switchPanel.getLayout()).first(switchPanel);
        }
    }


    private class Menus extends Menu {

        //Test If User ID Is Unique
        private boolean testID(String input) {
            for (Account student : studentList) {
                if (input.equals(student.getID())) {
                    return true;
                }
            }
            for (Account staff : staffList) {
                if (input.equals(staff.getID())) {
                    return true;
                }
            }
            for (Account admin : adminList) {
                if (input.equals(admin.getID())) {
                    return true;
                }
            }
            return false;
        }

        //Test If Username Is Unique
        private boolean testName(String input) {
            for (Account student : studentList) {
                if (input.toUpperCase().equals(student.getName().toUpperCase())) {
                    return true;
                }
            }
            for (Account staff : staffList) {
                if (input.toUpperCase().equals(staff.getName().toUpperCase())) {
                    return true;
                }
            }
            for (Account admin : adminList) {
                if (input.toUpperCase().equals(admin.getName().toUpperCase())) {
                    return true;
                }
            }
            return false;
        }


        //Account Type Selection Menu
        private JPanel typeSelect() {
            gbc.insets = new Insets(0, 0, 10, 0);
            
            //Menu Title
            titleLabel.setText("Select Account Type");


            //Content
            String[] labels = {"Student", "Staff", "Admin"};
            for (int i = 0; i < 3; i++) {
                JButton button = new JButton(labels[i]);
                button.setFont(standardFont);
                button.setPreferredSize(new Dimension(200, 40));
                button.addActionListener(ae -> {
                    type = button.getText();
                    switchPage("Menu");
                });

                JPanel panel = new JPanel();
                panel.add(button);
                contentPanel.add(panel, gbc);
            }
            

            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {new UserMenu(frame, userAcc, selCourse);});
            bWestPanel.add(back);

            return this;
        }


        //Main Selection Menu
        private JPanel mainMenu() {
            ArrayList<Account> accountList = new ArrayList<>();
            Student.accGet(studentList);
            Staff.accGet(staffList);
            Admin.accGet(adminList);
            if (type.equals("Student")) {
                for (Account student : studentList) {
                    accountList.add(student);
                }
            }
            else if (type.equals("Staff")) {
                for (Account staff : staffList) {
                    accountList.add(staff);
                }
            }
            else if (type.equals("Admin")) {
                for (Account admin : adminList) {
                    accountList.add(admin);
                }
            }

            //Menu Title
            titleLabel.setText("Select " + type + " Account");


            //Content
            if (accountList.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                for (Account account : accountList) {
                    JButton button = new JButton(Integer.toString(accountList.indexOf(account)+1));
                    listButton(button);
                    button.addActionListener(ae -> {
                        editAcc = account;
                        switchPage("Edit");
                    });
                    JLabel label = new JLabel(account.getName());
                    label.setFont(standardFont);
                    
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.add(button);
                    panel.add(label);
                    contentPanel.add(panel, gbc);
                }
                gbc.weighty = 1;
                contentPanel.add(new JPanel(), gbc);
            }


            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {switchPage("Type");});
            bWestPanel.add(back);

            //Add Button
            JButton add = new JButton("Add");
            shortButton(add);
            add.addActionListener(ad -> {switchPage("Add");});
            bEastPanel.add(add);

            return this;
        }


        //Add/Edit/Delete Accounts
        private JPanel maintenanceMenu(boolean edit) {
            gbc.insets = new Insets(0, 0, 10, 0);
            
            //Menu Title
            String title, message;
            if (edit) {
                studentList.remove(editAcc);
                staffList.remove(editAcc);
                adminList.remove(editAcc);
                title = " Update/Delete " + type + " Account";
                message = "Confirm " + type + " Account Edit?";
            }
            else {
                editAcc = null;
                title = " Add/Create New " + type + " Account";
                message = "Confirm New " + type + " Account?";
            }
            titleLabel.setText(title);
    
    
            //Content
            String[] labels = {"User ID", "Password", "Name", "E-Mail"};
            HintTextField[] textFields = new HintTextField[4];
            for (int i = 0; i < 4; i++) {
                JButton button = new JButton(labels[i]);
                textFields[i] = new HintTextField("Must Not Exceed 50 Characters", 25);
                labelButton(button, textFields[i]);
                if (i == 0) {
                    textFields[i].setHint("Must Be 8 Chracters Long");
                }

                JPanel panel = new JPanel();
                panel.add(button);
                panel.add(textFields[i]);
                contentPanel.add(panel, gbc);
            }
            if (edit) {
                textFields[0].setUserText(editAcc.getID());
                textFields[1].setUserText(editAcc.getPassword());
                textFields[2].setUserText(editAcc.getName());
                textFields[3].setUserText(editAcc.getEmail());
            }
    
    
            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {switchPage("Menu");});
            bWestPanel.add(back);

            if (edit) {
                //Delete button
                JButton delete = new JButton("Delete");
                shortButton(delete);
                if (userAcc.equals(editAcc)) {
                    delete.setEnabled(false);
                }
                delete.addActionListener(ae -> {
                    if (new Menu.PopUp(frame, title, "Confirm Account Deletion?").confirmPane()) {
                        if (type.equals("Student")) {
                            Student.accWrite(studentList);
                            if (edit) {
                                ((Student) editAcc).QuizDelete();
                            }
                        }
                        else if (type.equals("Staff")) {
                            Staff.accWrite(staffList);
                        }
                        else if (type.equals("Admin")) {
                            Admin.accWrite(adminList);
                        }
                        switchPage("Menu");
                    }
                });
                bEastPanel.add(delete);
            }

            //Submit Button
            JButton submit = new JButton("Submit");
            shortButton(submit);
            submit.setEnabled(false);
            TextUpdate textUpdate = new TextUpdate(textFields, submit);
            for (HintTextField textField : textFields) {
                textField.getDocument().addDocumentListener(textUpdate);
            }
            submit.addActionListener(ae -> {
                String[] newInfo = textUpdate.inputGet();

                //Valid Inputs
                if(!testID(newInfo[0]) && !testName(newInfo[2])) {
                    if (new Menu.PopUp(frame, title, message).confirmPane()) {
                        if (type.equals("Student")) {
                            studentList.add(new Student(newInfo));
                            Collections.sort(studentList, Student.nameCompare);
                            Student.accWrite(studentList);
                            if (edit) {
                                ((Student) editAcc).QuizRename(new Student(newInfo));
                            }
                        }
                        else if (type.equals("Staff")) {
                            staffList.add(new Staff(newInfo));
                            Collections.sort(staffList, Staff.nameCompare);
                            Staff.accWrite(staffList);
                        }
                        else if (type.equals("Admin")) {
                            adminList.add(new Admin(newInfo));
                            Collections.sort(adminList, Admin.nameCompare);
                            Admin.accWrite(adminList);
                        }
                        if (userAcc.equals(editAcc)) {
                            userAcc.setID(newInfo[0]);
                            userAcc.setPassword(newInfo[1]);
                            userAcc.setName(newInfo[2]);
                            userAcc.setEmail(newInfo[3]);
                        }
                        switchPage("Menu");
                    }
                }

                //Invalid Inputs
                else {
                    String errorMsg;
                    if(testID(newInfo[0])) {
                        errorMsg = "This User ID Already Exists";
                    }
                    else {
                            errorMsg = "This User Name Already Exists";
                        }
                    new Menu.PopUp(frame, title, errorMsg).errorPane();
                }
            });
            bEastPanel.add(submit);

            return this;
        }


        //Edit Own Account/Sign Up
        private JPanel signUpUpdate(boolean signup) {
            gbc.insets = new Insets(0, 0, 10, 0);
            Student.accGet(studentList);
            Staff.accGet(staffList);
            Admin.accGet(adminList);
            studentList.remove(userAcc);
            staffList.remove(userAcc);
            adminList.remove(userAcc);

            //Menu Title
            String title, message;
            if (signup) {
                title = " Sign Up";
                message = "Confirm New Account?";
            }
            else {
                title = " Account Edit";
                message = "Confirm Account Edit?";
            }
            titleLabel.setText(title);


            //Content
            String[] labels = {"User ID", "Password", "Name", "E-Mail"};
            HintTextField[] textFields = new HintTextField[4];
            for (int i = 0; i < 4; i++) {
                JButton button = new JButton(labels[i]);
                textFields[i] = new HintTextField("Must Not Exceed 50 Characters", 25);
                labelButton(button, textFields[i]);
                if (i == 0) {
                    textFields[i].setHint("Must Be 8 Chatacters Long");
                }

                JPanel panel = new JPanel();
                panel.add(button);
                panel.add(textFields[i]);
                contentPanel.add(panel, gbc);
            }
            if (!signup) {
                textFields[0].setEditable(false);
                textFields[0].setFocusable(false);
                textFields[0].setUserText(userAcc.getID());
                textFields[1].setUserText(userAcc.getPassword());
                textFields[2].setUserText(userAcc.getName());
                textFields[3].setUserText(userAcc.getEmail());
            }
            


            //Bottom Buttons
            //Back Button
            if (!signup) {
                JButton back = new JButton("Back");
                shortButton(back);
                back.addActionListener(ae -> {
                    frame.getContentPane().remove(this);
                    ((CardLayout) frame.getContentPane().getLayout()).previous(frame.getContentPane());
                });
                bWestPanel.add(back);
            }
            

            //Submit Button - Edit
            JButton submit = new JButton("Submit");
            shortButton(submit);
            submit.setEnabled(false);
            TextUpdate textUpdate = new TextUpdate(textFields, submit);
            for (HintTextField textField : textFields) {
                textField.getDocument().addDocumentListener(textUpdate);
            }
            submit.addActionListener(ae -> {
                String[] newInfo = textUpdate.inputGet();

                //Valid Inputs
                if(!testID(newInfo[0]) && !testName(newInfo[2])) {
                    if (new Menu.PopUp(frame, title, message).confirmPane()) {
                        if (userAcc instanceof Student) {
                            studentList.add(new Student(newInfo));
                            Collections.sort(studentList, Student.nameCompare);
                            Student.accWrite(studentList);
                        }
                        else if (userAcc instanceof Staff) {
                            staffList.add(new Staff(newInfo));
                            Collections.sort(staffList, Staff.nameCompare);
                            Staff.accWrite(staffList);
                        }
                        else if (userAcc instanceof Admin || signup) {
                            adminList.add(new Admin(newInfo));
                            Collections.sort(adminList, Admin.nameCompare);
                            Admin.accWrite(adminList);
                        }

                        //Exit Action
                        if (!signup) {
                            userAcc.setID(newInfo[0]);
                            userAcc.setPassword(newInfo[1]);
                            userAcc.setName(newInfo[2]);
                            userAcc.setEmail(newInfo[3]);
                            frame.getContentPane().remove(this);
                            ((CardLayout) frame.getContentPane().getLayout()).previous(frame.getContentPane());
                        }
                        else {
                            new Login(frame);
                        }
                    }
                }

                //Invalid Input
                else {
                    String errorMsg;
                    if(testID(newInfo[0])) {
                        errorMsg = "This User ID Already Exists";
                    } else {
                        errorMsg = "This User Name Already Exists";
                    }
                    new Menu.PopUp(frame, title, errorMsg).errorPane();
                }
            });
            bEastPanel.add(submit);

            return this;
        }


        //Check Upon TextField Update
        private class TextUpdate implements DocumentListener {
            private String[] input = new String[4];
            private HintTextField[] textFields;
            private JButton button;

            //Constructor
            private TextUpdate(HintTextField[] textFields, JButton button) {
                this.textFields = textFields;
                this.button = button;
            }

            //Format Inputs Into Strings
            private String[] inputGet() {
                input[0] = textFields[0].getText().replaceAll(";", "").replaceAll("\\s+", "").toUpperCase();
                input[1] = textFields[1].getText().trim().replaceAll(";", "");
                input[2] = textFields[2].getText().trim().replaceAll(";", "");
                input[3] = textFields[3].getText().trim().replaceAll(";", "");

                return input;
            }

            //Test Input Strings For Validity
            private boolean inputTest() {
                inputGet();

                if(input[0].length() != 8) {
                    return false;
                }
                else if (input[1].isEmpty() || input[1].length() > 50 || textFields[1].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[2].isEmpty() || input[2].length() > 50 || textFields[2].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[3].isEmpty() || input[3].length() > 50 || textFields[3].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else {
                    return true;
                }
            }

            //Events
            @Override
            public void changedUpdate(DocumentEvent e) {
                if (inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }
        }
    }
}
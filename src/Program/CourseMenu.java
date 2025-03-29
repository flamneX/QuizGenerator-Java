package Program;

import Data.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class CourseMenu{
    private JFrame frame;
    private Account userAcc;
    private boolean editMode;
    private Course editCourse;
    private ArrayList<Course> courseList = new ArrayList<Course>();

    //Constructor
    public CourseMenu(JFrame frame, Account userAcc) {
        this.frame = frame;
        this.userAcc= userAcc;

        switchPage("Menu");
    }

    //Page Switcher
    private void switchPage(String name) {
        frame.setJMenuBar(Menu.menuBar(frame, userAcc, null));
        Container switchPanel = frame.getContentPane();
        switchPanel.removeAll();

        //Add Menu
        if (name.equals("Add")) {
            switchPanel.add(new Menus().maintenanceMenu(false));
        }

        //Update/Delete Menu
        else if (name.equals("Edit")) {
            switchPanel.add(new Menus().maintenanceMenu(true));
        }

        //Main Menu
        else {
            switchPanel.add(new Menus().mainMenu());
        }

        ((CardLayout) switchPanel.getLayout()).first(switchPanel);
    }


    //Menu Panels
    private class Menus extends Menu {

        //Test If Course ID Is Unique
        private boolean testID(String input) {
            for (Course courseInfo : courseList) {
                if (input.equals(courseInfo.getCourseID())) {
                    return true;
                }
            }
            return false;
        }

        //Test If Course Name Is Unique
        private boolean testName(String input) {
            for (Course courseInfo : courseList) {
                if (input.toUpperCase().equals(courseInfo.getCourseName().toUpperCase())) {
                    return true;
                }
            }
            return false;
        }


        //Main/Edit Selection Menu
        private JPanel mainMenu() {
            Course.courseGet(courseList);
    
            //Menu Title
            if (editMode) {
                titleLabel.setText(" Select Course To Edit");
            }
            else {
                titleLabel.setText(" Select Course");
            }


            //Content
            if (courseList.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                for (Course course : courseList) {
                    JButton button = new JButton(course.getCourseID());
                    longButton(button);
                    button.addActionListener(ae -> {
                        if (editMode) {
                            editCourse = course;
                            switchPage("Edit");
                        }
                        else {
                            new UserMenu(frame, userAcc, course);
                        }
                    });
                    JLabel label = new JLabel(course.getCourseName());
                    label.setFont(standardFont);

                    JPanel buttonList = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    buttonList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    buttonList.add(button);
                    buttonList.add(label);
                    contentPanel.add(buttonList, gbc);
                }
                gbc.weighty = 1;
                contentPanel.add(new JPanel(), gbc);
            }


            //Bottom Buttons
            if (!(userAcc instanceof Student)) {
                //Back Button
                if (editMode) {
                    JButton back = new JButton("Back");
                    shortButton(back);
                    back.addActionListener(b -> {
                        editMode = false;
                        switchPage("Menu");
                    });
                    bWestPanel.add(back);
                }
                
                //Edit Mode Button
                else {
                    JButton edit = new JButton("Edit");
                    shortButton(edit);
                    edit.addActionListener(e -> {
                        editMode = true;
                        switchPage("Menu");
                    });
                    bEastPanel.add(edit);
                }

                //Add Button
                JButton add = new JButton("Add");
                shortButton(add);
                add.addActionListener(a -> {switchPage("Add");});
                bEastPanel.add(add);
            }

            return this;
        }


        //Add/Edit/Delete Courses
        private JPanel maintenanceMenu(boolean edit) {
            gbc.insets = new Insets(0, 0, 10, 0);

            //Menu Title
            String title, message;
            if (edit) {
                courseList.remove(editCourse);
                title = " Update/Delete Course";
                message = "Confirm Course Edit?";
            }
            else {
                editCourse = null;
                title = " Add/Create New Course";
                message = " Confirm New Course?";
            }
            titleLabel.setText(title);


            //Content
            String[] labels = {"Course ID", "Course Name"}, hints = {"Must Be 8 Characters Long", "Must Not Exceed 50 Characters"};
            HintTextField[] textFields = new HintTextField[2];
            for (int i = 0; i < 2; i++) {
                JButton button = new JButton(labels[i]);
                textFields[i] = new HintTextField(hints[i], 25);
                labelButton(button, textFields[i]);

                JPanel panel = new JPanel();
                panel.add(button);
                panel.add(textFields[i]);
                contentPanel.add(panel, gbc);
            };
            if (edit) {
                textFields[0].setUserText(editCourse.getCourseID());
                textFields[1].setUserText(editCourse.getCourseName());
            }


            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {switchPage("Menu");});
            bWestPanel.add(back);

            //Delete Button
            if (edit) {
                JButton delete = new JButton("Delete");
                shortButton(delete);
                delete.addActionListener(dl -> {
                    if (new Menu.PopUp(frame, title, "Confirm Course Deletion?").confirmPane()) {
                        editCourse.dirDelete();
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
            textFields[0].getDocument().addDocumentListener(textUpdate);
            textFields[1].getDocument().addDocumentListener(textUpdate);
            submit.addActionListener(ae -> {
                String[] courseInfo = textUpdate.inputGet();

                //Valid Inputs
                if (!testID(courseInfo[0]) && !testName(courseInfo[1])) {
                    if (new Menu.PopUp(frame, title, message).confirmPane()) {
                        if (edit) {
                            editCourse.dirRename(new Course(courseInfo));
                        }
                        else {
                            new Course(courseInfo).dirCreate();
                        }
                        switchPage("Menu");
                    }
                }

                //Invalid Inputs
                else {
                    String errorMsg;
                    if (testID(courseInfo[0])) {
                        errorMsg = "This Course ID Already Exists";
                    }
                    else {
                        errorMsg = "This Course Name Already Exists";
                    }
                    new Menu.PopUp(frame, title, errorMsg).errorPane();
                }
            });
            bEastPanel.add(submit);
            
            return this;
        }


        //Update Upon Text Field is Edited
        private class TextUpdate implements DocumentListener {
            private String[] input = new String[2];
            private HintTextField[] textFields;
            private JButton button;

            //Constructor
            private TextUpdate(HintTextField[] textFields, JButton button) {
                this.textFields = textFields;
                this.button = button;
            }

            //Format Inputs To String
            private String[] inputGet() {
                input[0] = textFields[0].getText().replaceAll("\\s+", "").replaceAll(";", "").toUpperCase();
                input[1] = textFields[1].getText().trim().replaceAll(";", "");

                return input;
            }

            //Test Input String For Validity
            private boolean inputTest() {
                inputGet();

                if (input[0].length() != 8) {
                    return false;
                }
                else if (input[1].isEmpty() || input[1].length() > 50 || textFields[1].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else {
                    return true;
                }
            }

            //Events
            @Override
            public void changedUpdate(DocumentEvent e) {
                if(inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                if(inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }
        }
    }
}

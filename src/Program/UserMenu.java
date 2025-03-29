package Program;

import Data.*;
import java.awt.*;

import javax.swing.*;

public class UserMenu extends Menu {
    private JFrame frame;
    private Account userAcc;
    private Course selCourse;

    //Constructor
    public UserMenu(JFrame frame, Account userAcc, Course selCourse) {
        this.frame = frame;
        this.userAcc = userAcc;
        this.selCourse = selCourse;

        frame.setJMenuBar(menuBar(frame, userAcc, selCourse));
        switchPage("Menu");
    }

    //Switch Page
    private void switchPage(String name) {
        Container switchPanel = frame.getContentPane();
        switchPanel.removeAll();

        //Account Edit Menu
        if (name.equals("Edit Account")) {
            new AccountMenu(frame, userAcc, selCourse, "Type");
        }

        //Chapter Sel - Question Edit Menu
        else if (name.equals("Edit Questions")) {
            new ChapterMenu(frame, userAcc, selCourse);
        }

        //Quiz Start Menu
        else if (name.equals("Start Quiz")) {
            new QuizMenu(frame, userAcc, selCourse);
        }

        //Quiz History Menu
        else if (name.equals("Quiz History")) {
            new QuizReport(frame, userAcc, selCourse, "Quiz List");
        }

        //Quiz Report Menu
        else if (name.equals("Quiz Report")) {
            new QuizReport(frame, userAcc, selCourse, "Student List");
        }

        //Main Menu
        else {
            switchPanel.add(new MainMenu());
        }

        ((CardLayout) switchPanel.getLayout()).first(switchPanel);
    }


    //Main Menu
    private class MainMenu extends Menu {
        private MainMenu() {
            gbc.insets = new Insets(0, 0, 10, 0);

            //Menu Title
            titleLabel.setText(" Main Menu");


            //Content
            String[] labels = new String[0];
            if (userAcc instanceof Student) {
                labels = new String[]{"Start Quiz", "Quiz History"};
            }
            else if (userAcc instanceof Staff) {
                labels = new String[]{"Edit Questions", "Quiz Report"};
            }
            else if (userAcc instanceof Admin) {
                labels = new String[]{"Edit Account", "Edit Questions", "Quiz Report"};
            }
            for (String label : labels) {
                JButton button = new JButton(label);
                button.setPreferredSize(new Dimension(200, 40));
                button.setFocusable(false);
                button.setFont(standardFont);
                button.addActionListener(ae -> {switchPage(label);});

                JPanel panel = new JPanel();
                panel.add(button);
                contentPanel.add(panel, gbc);
            }


            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(b -> {new CourseMenu(frame, userAcc);});
            bWestPanel.add(back);
        }
    }
}
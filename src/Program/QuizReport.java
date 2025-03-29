package Program;

import Data.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class QuizReport {
    private JFrame frame;
    private Account userAcc;
    private Course selCourse;
    private String selStudent;
    private QuizHistory selHistory;
    private ArrayList<QuizHistory> quizHistory = new ArrayList<QuizHistory>();

    //Constructor
    public QuizReport(JFrame frame, Account userAcc, Course selCourse, String menuName) {
        this.frame = frame;
        this.userAcc = userAcc;
        this.selCourse = selCourse;

        switchPage(menuName);
    }

    //Page Switcher
    private void switchPage(String name) {
        frame.setJMenuBar(Menu.menuBar(frame, userAcc, selCourse));
        Container switchPanel = frame.getContentPane();
        switchPanel.removeAll();

        //List Of Quiz Attempts
        if (name.equals("Quiz List")) {
            switchPanel.add(new Menus().quizList());
        }

        //Specific Quiz Detail
        else if (name.equals("Quiz Detail")) {
            switchPanel.add(new Menus().quizDetail(false));
        }

        //Specific Quiz Detail - Answers
        else if (name.equals("Quiz Answer")) {
            for (int i = 0; i < selHistory.getQuestions().size(); i++) {
                switchPanel.add(new Menus().quizAnswer(i));
            }
        }

        //Summary Screen
        else if (name.equals("Summary")) {
            switchPanel.add(new Menus().quizDetail(true));
        }

        //List Of Student Attempts
        else {
            switchPanel.add(new Menus().studentList());
        }

        ((CardLayout) switchPanel.getLayout()).first(switchPanel);
    }


    private class Menus extends Menu {
        
        //List Of Student Attempts
        private JPanel studentList() {
            HashMap<String, Integer> studentAttempts = new HashMap<String, Integer>();
            QuizHistory.studentGet(selCourse, studentAttempts);

            //Menu Title
            titleLabel.setText(" Students Attempts");

            
            //Content
            if (studentAttempts.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                for (String student : studentAttempts.keySet()) {
                    JButton button = new JButton(student);
                    longButton(button);
                    button.addActionListener(ae -> {
                        selStudent = student;
                        switchPage("Quiz List");
                    });
                    JLabel label = new JLabel(String.format("%5d Attempts", studentAttempts.get(student)));
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
            back.addActionListener(ae -> {new UserMenu(frame, userAcc, selCourse);});
            bWestPanel.add(back);

            return this;
        }


        //List Of Quiz Attempts
        private JPanel quizList() {
            if (userAcc instanceof Student) {
                selStudent = userAcc.getID();
            }
            QuizHistory.quizGet(selStudent, selCourse, quizHistory);

            //Menu Title
            titleLabel.setText(" Quiz Attempts");

            
            //Content
            if (quizHistory.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                int counter = 1;
                for (QuizHistory history : quizHistory) {
                    JButton button = new JButton(Integer.toString(counter));
                    counter++;
                    listButton(button);
                    button.addActionListener(ae -> {
                        selHistory = history;
                        switchPage("Quiz Detail");
                    });
                    JLabel label = new JLabel(String.format(" %s - %s", history.getDate(), history.getTime()));
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
            back.addActionListener(ae -> {
                if (userAcc instanceof Student) {
                    new UserMenu(frame, userAcc, selCourse);
                } else {
                    switchPage("Student List");
                }
            });
            bWestPanel.add(back);

            //Summary Button
            JButton summary = new JButton("Summary");
            longButton(summary);
            summary.addActionListener(ae -> {switchPage("Summary");});
            bEastPanel.add(summary);

            return this;
        }


        //Specific Quiz Detail
        private JPanel quizDetail(boolean summary) {
            String[] labels, text;
            if (summary) {
                int totalCorrect = 0, totalQuestion = 0;
                for (QuizHistory quiz : quizHistory) {
                    totalCorrect += quiz.correctAns();
                    totalQuestion += quiz.getQuestions().size();
                }

                //Menu Title
                titleLabel.setText(" Summary");

                //Data
                labels = new String[]{"Course", "Question Amount", "Correct Answer", "Marks"};
                text = new String[]{selCourse.getCourseID(), Integer.toString(totalQuestion), Integer.toString(totalCorrect), String.format("%.2f%%", (float) totalCorrect/totalQuestion * 100)};
            }
            else {
                int correct = selHistory.correctAns(), questionAmt = selHistory.getQuestions().size();

                //Menu Title
                titleLabel.setText(" Quiz Results");

                //Data
                labels = new String[]{"Date of Completion", "Time of Completion", "Selected Course", "Chapter(s) Selected", "Question Amount", "Correct Answer", "Marks"};
                text = new String[]{selHistory.getDate(), selHistory.getTime(), selHistory.getCourse().getCourseID(), Integer.toString(selHistory.getChapterAmt()), Integer.toString(questionAmt), Integer.toString(correct), String.format("%.2f%%", (float) correct/questionAmt * 100)};
            }


            //Content
            for (int i = 0; i < labels.length; i++) {
                if (i == 1 || i == 4) {
                    gbc.insets = new Insets(0, 0, 20, 0);
                } else {
                    gbc.insets = new Insets(0, 0, -10, 0);
                }
                JButton button = new JButton(labels[i]);
                HintTextField textField = new HintTextField("", 20);
                labelButton(button, textField);
                button.setPreferredSize(new Dimension(220, 40));
                textField.setUserText(text[i]);
                textField.setEditable(false);
                textField.setFocusable(false);
                
                JPanel panel = new JPanel();
                panel.add(button);
                panel.add(textField);
                contentPanel.add(panel, gbc);
            }


            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {switchPage("Quiz List");});
            bWestPanel.add(back);

            //Check Answer Button
            if (!summary) {
                JButton check = new JButton("Check Ans.");
                longButton(check);
                check.addActionListener(ae -> {switchPage("Quiz Answer");});
                bEastPanel.add(check);
            }

            return this;
        }


        //Specific Quiz Details - Answers
        private JPanel quizAnswer(int counter) {
            Question currentQuestion = selHistory.getQuestions().get(counter);
            String answer = selHistory.getAnwers()[counter];
            ArrayList<String> optionList = new ArrayList<String>() {{
                add(currentQuestion.getOption1());
                add(currentQuestion.getOption2());
                add(currentQuestion.getOption3());
                add(currentQuestion.getOption4());
            }};

            //Menu Title
            titleLabel.setText(" Question: " + (counter+1) + "/" + selHistory.getQuestions().size());


            //Content
            gbc.insets = new Insets(0, 0, 5, 0);
            HintTextArea textArea = new HintTextArea("", 35, 6);
            textArea.setQuizText(currentQuestion.getQuestion());
            JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            contentPanel.add(questionPanel, gbc);
            questionPanel.add(textArea);

            ButtonGroup options = new ButtonGroup();
            String[] optionIndicator = {"A", "B", "C", "D"};
            for (String option : optionList) {
                JRadioButton button = new JRadioButton(String.format(" %s. %s", optionIndicator[optionList.indexOf(option)], option));
                button.setFont(standardFont);
                button.setEnabled(false);
                button.setForeground(Color.BLACK);
                options.add(button);
                if (answer.equals(option)) {
                    button.setSelected(true);
                }
                if (currentQuestion.getAnswer().equals(optionIndicator[optionList.indexOf(option)])) {
                    button.setBackground(Color.GREEN);
                }

                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                panel.add(button);
                contentPanel.add(panel, gbc);
            }
            gbc.weighty = 1;
            contentPanel.add(new JPanel(), gbc);


            //Bottom Buttons
            //Previous Button
            JButton previous = new JButton("Previous");
            longButton(previous);
            previous.addActionListener(ae -> {((CardLayout) frame.getContentPane().getLayout()).previous(frame.getContentPane());});
            bWestPanel.add(previous);
            if (counter == 0) {
                previous.setEnabled(false);
            }

            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {switchPage("Quiz Detail");});
            bCenterPanel.add(back);

            //Next Button
            JButton next = new JButton("Next");
            longButton(next);
            next.addActionListener(ae -> {((CardLayout) frame.getContentPane().getLayout()).next(frame.getContentPane());});
            bEastPanel.add(next);
            if (counter == selHistory.getQuestions().size()-1) {
                next.setEnabled(false);
            }

            return this;
        }
    }
}

package Program;

import Data.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class QuizMenu {
    private JFrame frame;
    private Account userAcc;
    private Course selCourse;
    private ArrayList<Chapter> selChapters = new ArrayList<Chapter>();
    private ArrayList<Question> quizList = new ArrayList<Question>();
    private String[] answerList;
    private String date, time;

    //Constructor
    public QuizMenu(JFrame frame, Account userAcc, Course selCourse) {
        this.frame = frame;
        this.userAcc = userAcc;
        this.selCourse = selCourse;

        switchPage("Menu");
    }

    //Page Switcher
    public void switchPage(String name) {
        frame.setJMenuBar(Menu.menuBar(frame, userAcc, selCourse));
        Container switchPanel = frame.getContentPane();
        switchPanel.removeAll();

        //Quiz Menus
        if (name.equals("Start")) {
            for (int i = 0; i < quizList.size(); i++) {
                switchPanel.add(new QuizStart(i));
            }
        }
        
        //Quiz End Menu
        else if (name.equals("End")) {
            switchPanel.add(new QuizEnd());
        }

        //Quiz Answer Menu
        else if (name.equals("Answer")) {
            for (int i = 0; i < quizList.size(); i++) {
                switchPanel.add(new QuizList(i));
            }
        }
        
        //Chapter Select Menu
        else {
            switchPanel.add(new ChapterSelect());
        }

        ((CardLayout) switchPanel.getLayout()).first(switchPanel);
    }


    //Quiz Chapter Select Menu
    private class ChapterSelect extends Menu {
        private ChapterSelect() {
            ArrayList<Chapter> chapterList = new ArrayList<Chapter>();
            Chapter.chapterGet(selCourse, chapterList);
            JCheckBox[] checkBoxes = new JCheckBox[chapterList.size()];
            JButton cont = new JButton("Continue");

            //Menu Title
            titleLabel.setText(" Select Chapter");


            //Content
            if (chapterList.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                //Select All Check Box
                JCheckBox selectAll = new JCheckBox(" Select All");
                selectAll.setFont(standardFont);
                selectAll.addActionListener(i -> {
                    if (selectAll.isSelected()) {
                        for (JCheckBox checkBox : checkBoxes) {
                            checkBox.setSelected(true);
                        }
                    }
                    else {
                        for (JCheckBox checkBox : checkBoxes) {
                            checkBox.setSelected(false);
                        }
                    }
                });
                titlePanel.add(selectAll, BorderLayout.SOUTH);

                for(Chapter chapter : chapterList) {
                    JCheckBox checkBox = new JCheckBox(String.format(" %-2s %s", chapter.getChapterNo(), chapter.getChapterName()));
                    checkBox.setFont(standardFont);
                    checkBox.addItemListener(i -> {
                        if (checkBox.isSelected()) {
                            selChapters.add(chapter);
                            selectAll.setSelected(true);
                            for (JCheckBox checkB : checkBoxes) {
                                if (!(checkB.isSelected())) {
                                    selectAll.setSelected(false);
                                }
                            }
                        }
                        else {
                            selChapters.remove(chapter);
                            selectAll.setSelected(false);
                        }
                    });
                    checkBox.addItemListener(new CheckBoxUpdate(checkBoxes, cont));
                    checkBoxes[chapterList.indexOf(chapter)] = checkBox;

                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.add(checkBox);
                    contentPanel.add(panel, gbc);
                }
            }
            gbc.weighty = 1;
            contentPanel.add(new JPanel(), gbc);


            //Bottom Button Panel
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {new UserMenu(frame, userAcc, selCourse);});
            bWestPanel.add(back);

            //Continue Button
            longButton(cont);
            cont.setEnabled(false);
            cont.addActionListener(ae -> {
                frame.getContentPane().add(new QuestionAmount());
                ((CardLayout) frame.getContentPane().getLayout()).next(frame.getContentPane());
            });
            bEastPanel.add(cont);
        }

        //Update Upon Check Box Is Selected
        private class CheckBoxUpdate implements ItemListener {
            private JCheckBox[] checkBoxes;
            private JButton button;

            //Constructor
            private CheckBoxUpdate(JCheckBox[] checkBoxses, JButton button) {
                this.checkBoxes = checkBoxses;
                this.button = button;
            }

            //Event
            @Override
            public void itemStateChanged(ItemEvent e) {
                button.setEnabled(false);
                for (JCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        frame.getRootPane().setDefaultButton(button);
                        button.setEnabled(true);
                        break;
                    }
                }
            }
        }
    }


    //Question Amount Menu
    private class QuestionAmount extends Menu {
        private QuestionAmount() {
            ArrayList<Question> questionList = new ArrayList<Question>();
            Question.questionGet(selChapters, questionList);
            
            //Menu Title
            titleLabel.setText(" Enter Question Amount");


            //Content
            String[] labels = {"Chapter(s) Selected", "Question(s) Available", "Enter Question Amount"}, text = {Integer.toString(selChapters.size()), Integer.toString(questionList.size())};
            HintTextField[] textFields = new HintTextField[3];
            for (int i = 0; i < 3; i++) {
                JButton button = new JButton(labels[i]);
                textFields[i] = new HintTextField("A Number Between 1 To " + questionList.size(), 20);
                labelButton(button, textFields[i]);
                button.setPreferredSize(new Dimension(220, 40));

                if (i != 2) {
                    textFields[i].setUserText(text[i]);
                    textFields[i].setEditable(false);
                    textFields[i].setFocusable(false);
                }

                JPanel panel = new JPanel();
                panel.add(button);
                panel.add(textFields[i]);
                contentPanel.add(panel, gbc);
            }


            //Bottom Buttons
            //Back Button
            JButton back = new JButton("Back");
            shortButton(back);
            back.addActionListener(ae -> {
                frame.getContentPane().remove(this);
                ((CardLayout) frame.getContentPane().getLayout()).previous(frame.getContentPane());
            });
            bWestPanel.add(back);

            //Continue Button
            JButton cont = new JButton("Continue");
            longButton(cont);
            cont.setEnabled(false);
            textFields[2].getDocument().addDocumentListener(new TextUpdate(textFields[2], questionList.size(), cont));

            cont.addActionListener(ae -> {
                if (new Menu.PopUp(frame, "Start Quiz", "Confirm To Start Quiz?").confirmPane()) {
                    int questionAmt = Integer.parseInt(textFields[2].getText().replaceAll(";", "").replaceAll("\\s+", ""));

                    Random random = new Random();
                    for(int i = 0; i < questionAmt; i++) {
                        int randomIndex = random.nextInt(questionList.size());
                        quizList.add(questionList.get(randomIndex));
                        questionList.remove(randomIndex);
                    }
                    answerList = new String[quizList.size()];
                    switchPage("Start");
                }
            });
            bEastPanel.add(cont);
        }

        //Update Upon Text Field Is Updated
        private class TextUpdate implements DocumentListener {
            private HintTextField noField;
            private int questionAmt;
            private JButton button;
            
            //Constructor
            private TextUpdate(HintTextField noField, int questionAmt, JButton button) {
                this.noField = noField;
                this.questionAmt = questionAmt;
                this.button = button;
            }
    
            //Test Inputs For Validity
            private boolean inputTest() {
                int no = 0;
                try {
                    no = Integer.parseInt(noField.getText().replaceAll(";", "").replaceAll("\\s+", ""));
                } catch (NumberFormatException e) {
                    return false;
                }
    
                if (no < 1 || no > questionAmt) {
                    return false;
                } else {
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


    //Quiz Menus
    private class QuizStart extends Menu {
        private QuizStart(int counter) {
            //Shuffle Question Options
            Question currentQuestion = quizList.get(counter);
            ArrayList<String> optionList = new ArrayList<String>() {{
                add(currentQuestion.getOption1());
                add(currentQuestion.getOption2());
                add(currentQuestion.getOption3());
                add(currentQuestion.getOption4());
            }};
            Collections.shuffle(optionList);

            //Menu Title
            titleLabel.setText(" Question: " + (counter+1) + "/" + quizList.size());


            //Content
            gbc.insets = new Insets(0, 0, 5, 0);
            HintTextArea textArea = new HintTextArea("", 35, 6);
            textArea.setQuizText(currentQuestion.getQuestion());
            JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            questionPanel.add(textArea);
            contentPanel.add(questionPanel, gbc);

            ButtonGroup options = new ButtonGroup();
            String[] optionIndicator = {"A", "B", "C", "D"};
            JRadioButton[] radioButtons = new JRadioButton[4];
            for(String option : optionList) {
                JRadioButton button = new JRadioButton(String.format(" %s. %s", optionIndicator[optionList.indexOf(option)], option));
                button.setFont(standardFont);
                button.addActionListener(ae -> {answerList[counter] = option;});
                radioButtons[optionList.indexOf(option)] = button;
                options.add(button);

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

            //Continue Button
            JButton cont = new JButton();
            longButton(cont);
            cont.setEnabled(false);
            ButtonUpdate buttonUpdate = new ButtonUpdate(radioButtons, cont);
            for (int i = 0; i < 4; i++) {
                radioButtons[i].addItemListener(buttonUpdate);
            }

            //Continue Button - Next
            if (counter != quizList.size()-1) {
                cont.setText("Next");
                cont.addActionListener(ae -> {((CardLayout) frame.getContentPane().getLayout()).next(frame.getContentPane());});
            }

            //Continue Button - End
            else {
                cont.setText("End Quiz");
                cont.addActionListener(ae -> {
                    if (new Menu.PopUp(frame, "Quiz", "End Current Quiz And See Results?").confirmPane()) {
                        //Record Time/Date and Quiz Data
                        LocalDateTime now = LocalDateTime.now();
                        date = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                        time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        new QuizHistory(userAcc.getID(), selCourse, selChapters.size(), quizList, answerList, date, time).recorder();

                        switchPage("End");
                    }
                });
            }
            bEastPanel.add(cont);
        }

        //Checks Upon Radio Button Update
        private class ButtonUpdate implements ItemListener {
            private JRadioButton[] radioButtons;
            private JButton button;

            //Constructor
            private ButtonUpdate(JRadioButton[] radioButtons, JButton button) {
                this.radioButtons = radioButtons;
                this.button = button;
            }

            //Test Inputs For Validity
            private boolean inputTest() {
                for (JRadioButton radioButton : radioButtons) {
                    if (radioButton.isSelected()) {
                        return true;
                    }
                }
                return false;
            }

            //Events
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (inputTest()) {
                    button.setEnabled(true);
                    frame.getRootPane().setDefaultButton(button);
                } else {
                    button.setEnabled(false);
                }
            }
        }
    }


    //End Menu
    private class QuizEnd extends Menu {
        private QuizEnd() {
            int correct = 0;
            for (int i = 0; i < quizList.size(); i++) {
                if (quizList.get(i).getAnswer().equals(quizList.get(i).getAnswer(answerList[i]))) {
                    correct++;
                }
            }
            
            //Menu Title
            titleLabel.setText(" Quiz Results");


            //Content
            String[] labels = {"Date of Completion", "Time of Completion", "Selected Course", "Chapter(s) Selected", "Question Amount", "Correct Answer", "Marks"};
            String[] text = {date, time, selCourse.getCourseID(), Integer.toString(selChapters.size()), Integer.toString(quizList.size()), Integer.toString(correct), String.format("%.2f%%", (float) correct/quizList.size() * 100)};
            
            for (int i = 0; i < 7; i++) {
                if (i == 2 || i == 4) {
                    gbc.insets = new Insets(0, 0, 20, 0);
                }
                else {
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
            JButton check = new JButton("Check Ans.");
            longButton(check);
            check.addActionListener(ae -> {switchPage("Answer");});
            bEastPanel.add(check);

            JButton confirm = new JButton("Confirm");
            longButton(confirm);
            frame.getRootPane().setDefaultButton(confirm);
            confirm.addActionListener(ae -> {new UserMenu(frame, userAcc, selCourse);});
            bEastPanel.add(confirm);
        }
    }


    //List All Questions Of Quiz
    private class QuizList extends Menu {
        private QuizList(int counter) {
            Question currentQuestion = quizList.get(counter);
            ArrayList<String> optionList = new ArrayList<String>() {{
                add(currentQuestion.getOption1());
                add(currentQuestion.getOption2());
                add(currentQuestion.getOption3());
                add(currentQuestion.getOption4());
            }};

            //Menu Title
            titleLabel.setText(" Question: " + (counter+1) + "/" + quizList.size());


            //Content
            gbc.insets = new Insets(0, 0, 5, 0);
            HintTextArea textArea = new HintTextArea("", 35, 6);
            textArea.setQuizText(currentQuestion.getQuestion());
            JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            contentPanel.add(questionPanel, gbc);
            questionPanel.add(textArea);

            ButtonGroup options = new ButtonGroup();
            String[] optionIndicator = {"A", "B", "C", "D"};
            for(String option : optionList) {
                JRadioButton button = new JRadioButton(String.format(" %s. %s", optionIndicator[optionList.indexOf(option)], option));
                button.setFont(standardFont);
                button.setEnabled(false);
                options.add(button);
                if (answerList[counter].equals(option)) {
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
            back.addActionListener(ae -> {switchPage("End");});
            bCenterPanel.add(back);

            //Next Button
            JButton next = new JButton("Next");
            longButton(next);
            next.addActionListener(ae -> {((CardLayout) frame.getContentPane().getLayout()).next(frame.getContentPane());});
            bEastPanel.add(next);
            if (counter == quizList.size()-1) {
                next.setEnabled(false);
            }
        }
    } 
}
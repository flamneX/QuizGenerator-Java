package Program;

import Data.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class QuestionMenu {
    private JFrame frame;
    private Account userAcc;
    private Chapter selChapter;
    private Question editQuestion;
    private ArrayList<Question> questionList = new ArrayList<Question>();

    //Constructor
    public QuestionMenu(JFrame frame, Account userAcc, Chapter selChapter) {
        this.frame = frame;
        this.userAcc = userAcc;
        this.selChapter = selChapter;

        switchPage("Menu");
    }

    //Page Switcher
    private void switchPage(String name) {
        frame.setJMenuBar(Menu.menuBar(frame, userAcc, selChapter));
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


    //Main Panels
    private class Menus extends Menu {

        //Test If Question Is Unique
        private boolean testQ(String input) {
            for(Question question : questionList) {
                if(input.toUpperCase().equals(question.getQuestion().toUpperCase())) {
                    return true;
                }
            }
            return false;
        }

        //Test If Question Elements Is Unique
        private boolean testEle(String[] questionInfo) {
            for(int j = 0; j < questionInfo.length-1; j++) {
                for(int i = 0; i < questionInfo.length-1; i++) {
                    if(j == i) {
                        continue;
                    } else if (questionInfo[j].toUpperCase().equals(questionInfo[i].toUpperCase())) {
                        return true;
                    }
                }
            }
            return false;
        }


        //Main/Edit Selection Menu
        private JPanel mainMenu() {
            Question.questionGet(selChapter, questionList);

            //Menu Title
            titleLabel.setText(" Select Question To Edit");


            //Content
            if (questionList.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                for(Question question : questionList) {
                    JButton button = new JButton(Integer.toString(questionList.indexOf(question)+1));
                    listButton(button);
                    button.addActionListener(ae -> {
                        editQuestion = question;
                        switchPage("Edit");
                    });
                    JLabel label = new JLabel(question.getQuestion());
                    if (question.getQuestion().indexOf("\n") > 0) {
                        label.setText(question.getQuestion().substring(0, question.getQuestion().indexOf("\n")));
                    }
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
            back.addActionListener(ae -> {new ChapterMenu(frame, userAcc, selChapter);});
            bWestPanel.add(back);

            //Add Button
            JButton add = new JButton("Add");
            shortButton(add);
            add.addActionListener(ad -> {switchPage("Add");});
            bEastPanel.add(add);

            return this;
        }
    

        //Add/Edit/Delete Course
        private JPanel maintenanceMenu(boolean edit) {
            gbc.insets = new Insets(0, 0, 5, 0);

            //Menu Title
            String title, message;
            if (edit) {
                questionList.remove(editQuestion);
                title = " Update/Delete Question";
                message = "Confirm Question Edit?";
            }
            else {
                editQuestion = null;
                title = " Add/Create New Question";
                message = "Confirm New Question?";
            }
            titleLabel.setText(title);


            //Content
            //Question Area
            JButton qButton = new JButton("Question");
            HintTextArea qTextArea = new HintTextArea("Must Not Exceed 200 Characters", 27, 6);
            labelButton(qButton, qTextArea);
            JPanel qPanel = new JPanel();
            qPanel.add(qButton);
            qPanel.add(qTextArea);
            contentPanel.add(qPanel, gbc);

            //Options
            String[] labels = {"Option A", "Option B", "Option C", "Option D"};
            HintTextField[] textFields = new HintTextField[4];
            JRadioButton[] radioButtons = new JRadioButton[4];
            ButtonGroup buttonGroup = new ButtonGroup();
            for (int i = 0; i < 4; i++) {
                radioButtons[i] = new JRadioButton(labels[i]);
                labelButton(radioButtons[i]);
                buttonGroup.add(radioButtons[i]);
                textFields[i] = new HintTextField("Must Not Exceed 50 Characters", 25);

                JPanel panel = new JPanel();
                panel.add(radioButtons[i]);
                panel.add(textFields[i]);
                contentPanel.add(panel, gbc);
            }
            if (edit) {
                qTextArea.setUserText(editQuestion.getQuestion());
                textFields[0].setUserText(editQuestion.getOption1());
                textFields[1].setUserText(editQuestion.getOption2());
                textFields[2].setUserText(editQuestion.getOption3());
                textFields[3].setUserText(editQuestion.getOption4());

                if (editQuestion.getAnswer().equals("A")) {
                    radioButtons[0].setSelected(true);
                }
                else if (editQuestion.getAnswer().equals("B")) {
                    radioButtons[1].setSelected(true);
                }
                else if (editQuestion.getAnswer().equals("C")) {
                    radioButtons[2].setSelected(true);
                }
                else if (editQuestion.getAnswer().equals("D")) {
                    radioButtons[3].setSelected(true);
                }
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
                delete.addActionListener(ae -> {
                    if (new Menu.PopUp(frame, title, "Confirm Question Deletion?").confirmPane()) {
                        Question.questionWrite(selChapter, questionList);
                        switchPage("Menu");
                    }
                });
                bEastPanel.add(delete);
            }

            //Submit Button
            JButton submit = new JButton("Submit");
            shortButton(submit);
            submit.setEnabled(false);
            Update update = new Update(qTextArea, textFields, radioButtons, submit);
            qTextArea.getDocument().addDocumentListener(update);
            for (int i = 0; i < 4; i++) {
                textFields[i].getDocument().addDocumentListener(update);
                radioButtons[i].addItemListener(update);
            }
            submit.addActionListener(ae -> {
                String[] questionInfo = update.inputGet();

                //Valid Inputs
                if (!testQ(questionInfo[0]) && !testEle(questionInfo)) {
                    if (new Menu.PopUp(frame, title, message).confirmPane()) {
                        questionList.add(new Question(selChapter, questionInfo));
                        Collections.sort(questionList, Question.questionCompare);
                        Question.questionWrite(selChapter, questionList);
                        switchPage("Menu");
                    }
                }

                //Invalid Inputs
                else {
                    String errorMsg;
                    if (testQ(questionInfo[0])) {
                        errorMsg = "This Question Already Exists";
                    }
                    else {
                        errorMsg = "Elements Of Questions Cannot Contain Duplicates";
                    }
                    new Menu.PopUp(frame, title, errorMsg).errorPane();
                }
            });
            bEastPanel.add(submit);

            return this;
        }

    
        //Update Upoin Text Field And Radio Butons Are Edited
        private class Update implements DocumentListener, ItemListener {
            private String[] input = new String[6];
            private HintTextArea textArea;
            private HintTextField[] textFields;
            private JRadioButton[] radioButtons;
            private JButton button;

            //Constructor
            private Update(HintTextArea textArea, HintTextField[] textFields, JRadioButton[] radioButtons, JButton button) {
                this.textArea = textArea;
                this.textFields = textFields;
                this.radioButtons = radioButtons;
                this.button = button;
            }

            //Format Inputs To String
            private String[] inputGet() {
                input[0] = textArea.getText().trim().replaceAll(";", "");

                for (int i = 0; i < 4; i++) {
                    input[i+1] = textFields[i].getText().trim().replaceAll(";", "");
                }

                for (JRadioButton radioButton : radioButtons) {
                    if (radioButton.isSelected()) {
                        input[5] = radioButton.getActionCommand();
                        break;
                    }
                }
                return input;
            }

            //Test Input Strings For Validity
            private boolean inputTest() {
                inputGet();

                if(input[0].isEmpty() || input[0].length() > 250 || textArea.getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[1].isEmpty() || input[1].length() > 50 || textFields[0].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[2].isEmpty() || input[2].length() > 50 || textFields[1].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[3].isEmpty() || input[3].length() > 50 || textFields[2].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[4].isEmpty() || input[4].length() > 50 || textFields[3].getForeground().equals(Color.GRAY)) {
                    return false;
                }
                else if (input[5] == null) {
                    return false;
                }
                else {
                    return true;
                }
            }

            //Text Field Events
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

            //Button Event
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
}
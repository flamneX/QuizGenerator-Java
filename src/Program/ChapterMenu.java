package Program;

import Data.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class ChapterMenu {
    private JFrame frame;
    private Account userAcc;
    private Course selCourse;
    private boolean editMode;
    private Chapter editChapter;
    private ArrayList<Chapter> chapterList = new ArrayList<Chapter>();

    //Constructor
    public ChapterMenu(JFrame frame, Account userAcc, Course selCourse) {
        this.frame = frame;
        this.userAcc = userAcc;
        this.selCourse = selCourse;

        switchPage("Menu");
    }

    //Page Switcher
    private void switchPage(String name) {
        frame.setJMenuBar(Menu.menuBar(frame, userAcc, selCourse));
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

        ((CardLayout) switchPanel.getLayout()).next(switchPanel);
    }


    //Menu Panels
    private class Menus extends Menu {

        //Test If Chapter No. Is Unique
        private boolean testNo(String input) {
            for (Chapter chapterInfo : chapterList) {
                if (input.equals(chapterInfo.getChapterNo())) {
                    return true;
                }
            }
            return false;
        }

        //Test If Chapter Name Is Unique
        private boolean testName(String input) {
            for (Chapter chapterInfo : chapterList) {
                if (input.toUpperCase().equals(chapterInfo.getChapterName().toUpperCase())) {
                    return true;
                }
            }
            return false;
        }


        //Main/Edit Selection Menu
        private JPanel mainMenu() {
            Chapter.chapterGet(selCourse, chapterList);

            //Menu Title
            if (editMode) {
                titleLabel.setText(" Select Chapter To Edit");
            }
            else {
                titleLabel.setText(" Select Chapter");
            }


            //Content
            if (chapterList.size() == 0) {
                JLabel label = new JLabel("No Records Found");
                label.setFont(standardFont);
                contentPanel.add(label);
            }
            else {
                for(Chapter chapter : chapterList) {
                    JButton button = new JButton(chapter.getChapterNo());
                    listButton(button);
                    button.addActionListener(ae -> {
                        if (editMode) {
                            editChapter = chapter;
                            switchPage("Edit");
                        }
                        else {
                            new QuestionMenu(frame, userAcc, chapter);
                        }
                    });
                    JLabel label = new JLabel(chapter.getChapterName());
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
            back.addActionListener(b -> {
                if (editMode) {
                    editMode = false;
                    switchPage("Menu");
                }
                else {
                    new UserMenu(frame, userAcc, selCourse);
                }
            });
            bWestPanel.add(back);

            //Edit Button
            if (!editMode) {
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

            return this;
        }


        //Add/Edit/Delete Chapters
        private JPanel maintenanceMenu(boolean edit) {
            gbc.insets = new Insets(0, 0, 10, 0);

            //Menu Title
            String title, message;
            if (edit) {
                chapterList.remove(editChapter);
                title = " Update/Delete Chapter";
                message = "Confirm Chapter Edit?";
            }
            else {
                editChapter = null;
                title = " Add/Create New Chapter";
                message = "Confirm New Chapter?";
            }
            titleLabel.setText(title);
        
        
            //Content
            String[] labels = {"Chapter No.", "Chapter Name"}, hints = {"Must Be A Number Between 1 To 99", "Must Not Exceed 50 Characters"};
            HintTextField[] textFields = new HintTextField[2];
            for (int i = 0; i < 2; i++) {
                JButton button = new JButton(labels[i]);
                textFields[i] = new HintTextField(hints[i], 25);
                labelButton(button, textFields[i]);
            
                JPanel panel = new JPanel();
                panel.add(button);
                panel.add(textFields[i]);
                contentPanel.add(panel, gbc);
            }
            if (edit) {
                textFields[0].setUserText(editChapter.getChapterNo());
                textFields[1].setUserText(editChapter.getChapterName());
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
                    if (new Menu.PopUp(frame, title, "Confirm Chapter Deletion?").confirmPane()) {
                        editChapter.fileDelete();
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
                String[] chapterInfo = textUpdate.inputGet();
            
                //Valid Inputs
                if (!testNo(chapterInfo[0]) && !testName(chapterInfo[1])) {
                    if (new Menu.PopUp(frame, title, message).confirmPane()) {
                        if (edit) {
                            editChapter.fileRename(new Chapter(selCourse, chapterInfo));
                        }
                        else {
                            new Chapter(selCourse, chapterInfo).fileCreate();
                        }
                        switchPage("Menu");
                    }
                }

                //Invalid Inputs
                else {
                    String errorMsg;
                    if(testNo(chapterInfo[0])) {
                        errorMsg = "This Chapter No. Already Exists";
                    } else {
                        errorMsg = "This Chapter Name Already Exists";
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
                input[0] = textFields[0].getText().replaceAll("\\s+", "").replaceAll(";", "");
                input[1] = textFields[1].getText().trim().replaceAll(";", "");

                return input;
            }

            //Test Input Strings For Validity
            private boolean inputTest() {
                inputGet();
                int no = 0;
                try {
                    no = Integer.parseInt(input[0]);
                } catch (NumberFormatException e) {
                }

                if (no < 1 || no > 99 || input[0] == null) {
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

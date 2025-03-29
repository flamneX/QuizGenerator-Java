package Program;

import Data.*;
import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    protected JPanel titlePanel = new JPanel(new BorderLayout());
    protected JLabel titleLabel = new JLabel();
    protected JPanel contentPanel = new JPanel(new GridBagLayout());
    protected JPanel bottomPanel = new JPanel(new BorderLayout());
    protected JPanel bCenterPanel = new JPanel(new FlowLayout());
    protected JPanel bEastPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    protected JPanel bWestPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    protected GridBagConstraints gbc = new GridBagConstraints();
    protected Font standardFont = new Font(Font.DIALOG, Font.PLAIN, 20);
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Quiz");
        frame.setSize(650, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout());
        frame.setVisible(true);
        new Login(frame);
    }

    public Menu() {
        this.setLayout(new BorderLayout());
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        contentPanel.setBorder(BorderFactory.createEtchedBorder());

        bottomPanel.add(bCenterPanel, BorderLayout.CENTER);
        bottomPanel.add(bWestPanel, BorderLayout.WEST);
        bottomPanel.add(bEastPanel, BorderLayout.EAST);

        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bCenterPanel.setBackground(Color.LIGHT_GRAY);
        bWestPanel.setBackground(Color.LIGHT_GRAY);
        bEastPanel.setBackground(Color.LIGHT_GRAY);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1;
    }

    public void shortButton(JButton button) {
        button.setFont(standardFont);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(100, 35));
    }

    public void longButton(JButton button) {
        button.setFont(standardFont);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(150, 35));
    }

    public void listButton(JButton button) {
        button.setFont(standardFont);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(70, 40));
    }

    public void labelButton(JButton button, HintTextField textField) {
        button.setFont(standardFont);
        button.setText(button.getText() + "  ");
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setHorizontalAlignment(SwingConstants.RIGHT);
        button.setPreferredSize(new Dimension(150, 40));
        button.addActionListener(ae -> {textField.requestFocusInWindow();});
    }

    public void labelButton(JButton button, JTextArea textArea) {
        button.setFont(standardFont);
        button.setText(button.getText() + "  ");
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setHorizontalAlignment(SwingConstants.RIGHT);
        button.setPreferredSize(new Dimension(100, 160));
        button.addActionListener(ae -> {textArea.requestFocusInWindow();});
    }

    public void labelButton(JRadioButton radioButton) {
        String text = radioButton.getText();
        radioButton.setActionCommand(text.substring(text.length()-1));
        radioButton.setText("  " + text + "  ");
        radioButton.setFont(standardFont);
        radioButton.setFocusable(false);
        radioButton.setHorizontalAlignment(SwingConstants.RIGHT);
        radioButton.setPreferredSize(new Dimension(130, 40));
    }

    public static JMenuBar menuBar(JFrame frame, Account userAcc, Course course) {
        //Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
        menuBar.setBackground(Color.LIGHT_GRAY);


        //Course Display
        if(course instanceof Course) {
            JLabel title = new JLabel(" Course: "+course.getCourseID());
            title.setFont(new Font("Aptos", Font.PLAIN, 20));
            menuBar.add(title);

            menuBar.add(Box.createHorizontalGlue());
            //Home Button
            JButton homeButton = new JButton(new ImageIcon(new ImageIcon("image/home.png").getImage().getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)));
            homeButton.setFocusable(false);
            homeButton.setContentAreaFilled(false);
            homeButton.addActionListener(ae -> new UserMenu(frame, userAcc, course));
            menuBar.add(homeButton);            
        }
        else {
            menuBar.add(Box.createHorizontalGlue());
        }


        //Account
        if (userAcc instanceof Account) {
            JMenu user = new JMenu();
            user.setPreferredSize(new Dimension(50, 30));
            user.setIcon(new ImageIcon(new ImageIcon("image/userIcon.png").getImage().getScaledInstance(28, 28,  java.awt.Image.SCALE_SMOOTH)));

            JMenuItem editAcc = new JMenuItem("Edit Account");
            editAcc.setIcon(new ImageIcon(new ImageIcon("image/editAcc.png").getImage().getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH)));
            editAcc.setFont(new Font("Aptos", Font.PLAIN, 15));
            editAcc.addActionListener(ae -> {new AccountMenu(frame, userAcc, course, "Self");});
            user.add(editAcc);

            JMenuItem logout = new JMenuItem("Log Out");
            logout.setIcon(new ImageIcon(new ImageIcon("image/logout.png").getImage().getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH)));
            logout.setFont(new Font("Aptos", Font.PLAIN, 15));
            logout.addActionListener(ae -> {
                if (new Menu.PopUp(frame, "Log Out", "Confirm Log Out?").confirmPane()) {
                    new Login(frame);
                }
            });
            user.add(logout);
            menuBar.add(user);
        }
        return menuBar;
    }

    public static class PopUp extends JOptionPane {
        private JFrame frame;
        private String title;
        private String message;
        private Font font = new Font(Font.DIALOG, Font.PLAIN, 20);

        public PopUp(JFrame frame, String title, String message) {
            this.frame = frame;
            this.title = title;
            this.message = message;
        }

        public void errorPane() {
            Object[] button = {"Confirm"};
            UIManager.put("OptionPane.buttonFont", font);
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(font);
            
            showOptionDialog(frame, messageLabel, title, JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE, null, button, button[0]);
        }

        public boolean confirmPane() {
            Object[] options = {"Cancel", "Confirm"};
            UIManager.put("OptionPane.buttonFont", font);
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(font);

            int selection = showOptionDialog(frame, messageLabel, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (selection == JOptionPane.NO_OPTION) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
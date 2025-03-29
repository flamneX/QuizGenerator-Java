package Program;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class HintTextArea extends JTextArea {

    public HintTextArea(String hint, int width, int height) {
        super(hint, height, width);
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        setForeground(Color.GRAY);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getForeground().equals(Color.GRAY)) {
                    setForeground(Color.BLACK);
                    setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(hint);
                }
            }
        });
    }

    //Set to Specific Value
    public void setUserText(String text) {
        setForeground(Color.BLACK);
        setText(text);
    }

    public void setQuizText(String text) {
        setEditable(false);
        setFocusable(false);
        setForeground(Color.BLACK);
        setText(text);
    }
}

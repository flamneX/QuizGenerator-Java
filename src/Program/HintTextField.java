package Program;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import javax.swing.JTextField;

public class HintTextField extends JTextField {
    private String hint;

    public HintTextField(String hint, int column) {
        super(hint, column);
        this.hint = hint;
        setFont(new Font("Aptos", Font.PLAIN, 20));
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

    //Set Hint of Text Field
    public void setHint(String hint) {
        this.hint = hint;
        setText(hint);
    }

    //Set to Specific Value
    public void setUserText(String text) {
        setForeground(Color.BLACK);
        setText(text);
    }

    //Reset State
    public void reset() {
        setForeground(Color.GRAY);
        setText(hint);
    }
}

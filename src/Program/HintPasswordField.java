package Program;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import javax.swing.JPasswordField;

public class HintPasswordField extends JPasswordField {
    private String hint;
    
    public HintPasswordField(String hint, int column) {
        super(hint, column);
        this.hint = hint;
        setEchoChar((char) 0);
        setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        setForeground(Color.GRAY);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getForeground().equals(Color.GRAY)) {
                    setEchoChar('*');
                    setForeground(Color.BLACK);
                    setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setEchoChar((char) 0);
                    setForeground(Color.GRAY);
                    setText(hint);
                }
            }
        });
    }

    //Reset the Password Field
    public void reset() {
        setEchoChar((char) 0);
        setForeground(Color.GRAY);
        setText(hint);
    }
}
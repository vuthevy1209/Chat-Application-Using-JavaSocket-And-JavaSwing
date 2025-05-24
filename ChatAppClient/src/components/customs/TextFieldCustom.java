package components.customs;

import javax.swing.*;
import java.awt.*;

public class TextFieldCustom {
    public static JTextField createTextFieldCustom(String title, int width, int height) {
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(width, height));
        jTextField.setBorder(BorderFactory.createTitledBorder(title));

        return jTextField;
    }
}

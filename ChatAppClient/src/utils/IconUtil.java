package utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class IconUtil {
    public static ImageIcon getImageIcon(String path) {
        return getImageIcon(path, 20, 20);
    }

    public static ImageIcon getImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(IconUtil.class.getResource(path)));
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
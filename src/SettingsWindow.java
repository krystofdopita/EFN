
import javax.swing.*;
import java.awt.*;

    public class SettingsWindow extends JFrame {

        public SettingsWindow() {
            setTitle("Nastavení");
            setSize(300, 200);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(3, 1, 10, 10));

            JCheckBox soundCheck = new JCheckBox("Zvuk zapnutý", true);

            JButton saveButton = new JButton("Uložit");
            JButton backButton = new JButton("Zpět");

            add(soundCheck);
            add(saveButton);
            add(backButton);

            saveButton.addActionListener(e -> {
                boolean soundOn = soundCheck.isSelected();

            });

            backButton.addActionListener(e -> {
                dispose();
            });
        }
    }


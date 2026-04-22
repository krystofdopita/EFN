import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class SettingsWindow extends JFrame {
    private final Preferences prefs = Preferences.userNodeForPackage(SettingsWindow.class);
    private static final String SOUND_ENABLED = "sound_enabled";

    public SettingsWindow() {
        setTitle("Nastavení");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        //nacitani zvuku
        boolean isSoundOn = prefs.getBoolean(SOUND_ENABLED, true);
        JCheckBox soundCheck = new JCheckBox("Zvuk zapnutý", isSoundOn);

        JButton saveButton = new JButton("Uložit");
        JButton backButton = new JButton("Zpět");

        add(soundCheck);
        add(saveButton);
        add(backButton);

        saveButton.addActionListener(e -> {
            // ulozeni volby zvuku
            prefs.putBoolean(SOUND_ENABLED, soundCheck.isSelected());
            JOptionPane.showMessageDialog(this, "Nastavení bylo uloženo.");
            dispose();
        });

        backButton.addActionListener(e -> {
            dispose();
        });
    }
}

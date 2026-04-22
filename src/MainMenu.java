import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Moje hra");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new BorderLayout());

        // nadpis
        JLabel title = new JLabel("MOJE HRA", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        // panel na tlacitka
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.setLayout(new GridBagLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(30, 30, 30));
        buttonsPanel.setLayout(new GridLayout(3, 1, 0, 20));

        JButton startButton = createStyledButton("Start", new Color(0, 128, 0));
        JButton optionsButton = createStyledButton("Nastavení", new Color(255, 165, 0));
        JButton exitButton = createStyledButton("Konec", new Color(255, 0, 0));


        buttonsPanel.add(startButton);
        buttonsPanel.add(optionsButton);
        buttonsPanel.add(exitButton);

        centerPanel.add(buttonsPanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        //tlacitka
        startButton.addActionListener(e -> {
            new GameWindow();
        });
        optionsButton.addActionListener(e -> {
            new SettingsWindow().setVisible(true);
        });
        exitButton.addActionListener(e -> System.exit(0));

        add(panel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createEmptyBorder());

        // hover efekt(zmena barvy po najeti s mysi)
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 160, 210));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);

        button.setBackground(color);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // zruseni hnusnyho defaultniho okraje
        button.setBorderPainted(false);

        // kurzor ruky po najeti na tlacitko
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover efekt
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }
}
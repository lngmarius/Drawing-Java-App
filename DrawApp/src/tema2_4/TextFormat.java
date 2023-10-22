package tema2_4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextFormat extends JFrame {

    private static final long serialVersionUID = 1L;
	private static JComboBox<String> fontComboBox;
    private static JComboBox<Integer> sizeComboBox;
    private static JComboBox<String> styleComboBox;
    public static JTextArea textArea;
    private JLabel displayLabel;
    
    public TextFormat() {
        initComponents();
    }
    
  
    public static String getTextToDraw() {
        return textArea.getText();
    }
    
    private void initComponents() {
        setTitle("Font Selector");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));
       
        fontComboBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        sizeComboBox = new JComboBox<>(new Integer[]{8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 78});
        styleComboBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic", "Bold + Italic", "Bold + Plain", "Italic + Plain"});
        textArea = new JTextArea(5, 20);
        displayLabel = new JLabel();
        JButton generateButton = new JButton("Generate");

        add(new JLabel("Selecteaza font:"));
        add(fontComboBox);
        add(new JLabel("Selecteaza marime:"));
        add(sizeComboBox);
        add(new JLabel("Selecteaza stil:"));
        add(styleComboBox);
        add(new JLabel("Introdu text:"));
        add(new JScrollPane(textArea));
        add(displayLabel);
        add(generateButton);

       
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFont();
                getTextToDraw();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public static Font updateFont() {
        String fontName = fontComboBox.getSelectedItem().toString();
        int fontSize = sizeComboBox.getItemAt(sizeComboBox.getSelectedIndex());
        int fontStyle = getSelectedFontStyle(styleComboBox.getSelectedItem().toString());

        return new Font(fontName, fontStyle, fontSize);
    
    }
    
    private static int getSelectedFontStyle(String style) {
        switch (style) {
            case "Bold":
                return Font.BOLD;
            case "Italic":
                return Font.ITALIC;
            case "Bold + Italic":
                return Font.BOLD + Font.ITALIC;
            case "Bold + Plain":
                return Font.BOLD + Font.PLAIN;
            case "Italic + Plain":
                return Font.ITALIC + Font.PLAIN;
            default:
                return Font.PLAIN;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	TextFormat frame = new TextFormat();
        	frame.setVisible(true);
        });
    }
}

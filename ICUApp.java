package opencvaa;

//ICUApp.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import org.opencv.core.Core;

//===================================
//MAIN APPLICATION: ICUApp
//===================================
public class ICUApp extends JFrame {

 private static final Color PRIMARY_COLOR = new Color(108, 99, 255);
 private static final Color PRIMARY_DARK = new Color(90, 82, 224);
 private static final Color BG_DARK = new Color(15, 15, 35);
 private static final Color BG_MID = new Color(26, 26, 46);
 private static final Color TEXT_LIGHT = Color.WHITE;
 private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 36);
 private static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
 private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

 public ICUApp() {
     initializeWindow();
     showLoginScreen();
 }

 private void initializeWindow() {
     setTitle("ICU");
     setSize(1200, 700);
     setLocationRelativeTo(null);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setResizable(false);
 }

 // ===================================
 // LOGIN SCREEN
 // ===================================
 private void showLoginScreen() {
     getContentPane().removeAll();

     JPanel background = new JPanel() {
         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
             GradientPaint gradient = new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(), BG_MID);
             g2.setPaint(gradient);
             g2.fillRect(0, 0, getWidth(), getHeight());

             Paint glow = new RadialGradientPaint(
                 new Point2D.Float(getWidth() / 2f, getHeight() / 2f),
                 300,
                 new float[]{0f, 1f},
                 new Color[]{new Color(100, 100, 255, 40), new Color(0, 0, 0, 0)}
             );
             g2.setPaint(glow);
             g2.fillOval(getWidth() / 2 - 300, getHeight() / 2 - 300, 600, 600);
             g2.dispose();
         }
     };
     background.setLayout(null);

     JPanel loginCard = new JPanel(null) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g2.setColor(new Color(20, 20, 35, 230));
             g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));
             g2.setColor(new Color(100, 100, 255, 30));
             g2.setStroke(new BasicStroke(1.2f));
             g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 30, 30));
             g2.dispose();
         }
     };
     loginCard.setOpaque(false);
     loginCard.setBounds(400, 180, 400, 440);

     JLabel logo = new JLabel("ICU", SwingConstants.CENTER);
     logo.setFont(FONT_TITLE);
     logo.setForeground(TEXT_LIGHT);
     logo.setBounds(50, 40, 300, 50);

     JTextField usernameField = createFloatingTextField("Username");
     usernameField.setBounds(60, 120, 280, 50);

     JPasswordField passwordField = createFloatingPasswordField("Password");
     passwordField.setBounds(60, 190, 280, 50);

     JButton loginButton = new JButton("Enter ICU") {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), getHeight(), PRIMARY_DARK);
             g2.setPaint(gradient);
             g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
             g2.setColor(TEXT_LIGHT);
             g2.setFont(FONT_BUTTON);
             FontMetrics fm = g2.getFontMetrics();
             int x = (getWidth() - fm.stringWidth(getText())) / 2;
             int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
             g2.drawString(getText(), x, y);
             g2.dispose();
         }
     };
     loginButton.setBorderPainted(false);
     loginButton.setContentAreaFilled(false);
     loginButton.setFocusPainted(false);
     loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
     loginButton.setBounds(60, 270, 280, 50);

     // ✅ SINGLE ACTION: GO DIRECTLY TO camzra
     loginButton.addActionListener(e -> {
         SwingUtilities.getWindowAncestor(loginButton).dispose(); // Close login
         SwingUtilities.invokeLater(() -> new opencvaa.camzra().setVisible(true));
     });

     loginCard.add(logo);
     loginCard.add(usernameField);
     loginCard.add(passwordField);
     loginCard.add(loginButton);

     // Add drop shadow
     addDropShadow(background, loginCard, new Color(0, 0, 0, 60));
     background.add(loginCard);
     add(background);
     revalidate();
     repaint();
 }

 private JTextField createFloatingTextField(String placeholder) {
     JTextField field = new JTextField() {
         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             if (getText().isEmpty()) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                 g2.setColor(new Color(180, 180, 255, 150));
                 g2.setFont(FONT_REGULAR.deriveFont(14f));
                 FontMetrics fm = g2.getFontMetrics();
                 g2.drawString(placeholder, getInsets().left + 10, getInsets().top + 20);
                 g2.dispose();
             }
         }
     };
     field.setOpaque(false);
     field.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100, 100, 255, 100)),
         BorderFactory.createEmptyBorder(10, 20, 10, 20)
     ));
     field.setForeground(TEXT_LIGHT);
     field.setCaretColor(TEXT_LIGHT);
     field.setMargin(new Insets(10, 20, 10, 20));
     return field;
 }

 private JPasswordField createFloatingPasswordField(String placeholder) {
     JPasswordField field = new JPasswordField() {
         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             if (getPassword().length == 0) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                 g2.setColor(new Color(180, 180, 255, 150));
                 g2.setFont(FONT_REGULAR.deriveFont(14f));
                 FontMetrics fm = g2.getFontMetrics();
                 g2.drawString(placeholder, getInsets().left + 10, getInsets().top + 20);
                 g2.dispose();
             }
         }
     };
     field.setOpaque(false);
     field.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100, 100, 255, 100)),
         BorderFactory.createEmptyBorder(10, 20, 10, 20)
     ));
     field.setForeground(TEXT_LIGHT);
     field.setCaretColor(TEXT_LIGHT);
     field.setEchoChar('●');
     field.setMargin(new Insets(10, 20, 10, 20));
     return field;
 }

 // ✅ FIXED: Proper shadow painting
 private void addDropShadow(JPanel parent, JComponent comp, Color color) {
     JLabel shadow = new JLabel() {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g2.setColor(color);
             g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 30, 30);
             g2.dispose();
         }
     };
     shadow.setOpaque(false);
     shadow.setBounds(comp.getX() + 4, comp.getY() + 4, comp.getWidth(), comp.getHeight());
     parent.add(shadow, 0);

     comp.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentMoved(ComponentEvent e) {
             shadow.setLocation(comp.getX() + 4, comp.getY() + 4);
         }

         @Override
         public void componentResized(ComponentEvent e) {
             shadow.setBounds(comp.getX() + 4, comp.getY() + 4, comp.getWidth(), comp.getHeight());
         }
     });
 }

 // ===================================
 // SPLASH SCREEN CLASS (Static nested class)
 // ===================================
 public static class SplashScreen extends JWindow {
     private float opacity = 0f;
     private boolean fadeIn = true;
     private boolean fadeOut = false;
     private Timer pulseTimer, fadeTimer;

     public SplashScreen() {
         setSize(1200, 700);
         setLocationRelativeTo(null);

         JPanel content = new JPanel() {
             @Override
             protected void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                 GradientPaint gradient = new GradientPaint(
                     0, 0, new Color(10, 10, 25),
                     0, getHeight(), new Color(20, 20, 40)
                 );
                 g2.setPaint(gradient);
                 g2.fillRect(0, 0, getWidth(), getHeight());

                 Paint glow = new RadialGradientPaint(
                     new Point2D.Float(getWidth() / 2f, getHeight() / 2f),
                     500,
                     new float[]{0f, 1f},
                     new Color[]{new Color(120, 90, 255, 80), new Color(0, 0, 0, 0)}
                 );
                 g2.setPaint(glow);
                 g2.fillOval(getWidth() / 2 - 500, getHeight() / 2 - 500, 1000, 1000);
                 g2.dispose();
             }
         };
         content.setLayout(null);

         JLabel logo = new JLabel("ICU", SwingConstants.CENTER);
         logo.setFont(new Font("Segoe UI", Font.BOLD, 90));
         logo.setForeground(new Color(160, 160, 255));
         logo.setBounds(0, getHeight() / 2 - 80, getWidth(), 100);

         JLabel tagline = new JLabel("Intelligent. Clear. Understanding.", SwingConstants.CENTER);
         tagline.setFont(new Font("Segoe UI", Font.ITALIC, 24));
         tagline.setForeground(new Color(130, 130, 200));
         tagline.setBounds(0, getHeight() / 2 + 60, getWidth(), 40);

         content.add(logo);
         content.add(tagline);
         setContentPane(content);

         pulseTimer = new Timer(1000, e -> {
             if (logo.getFont().getSize() == 90) {
                 logo.setFont(new Font("Segoe UI", Font.BOLD, 96));
             } else {
                 logo.setFont(new Font("Segoe UI", Font.BOLD, 90));
             }
         });
         pulseTimer.start();

         addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 finishSplash();
             }
         });

         fadeTimer = new Timer(60, e -> {
             if (fadeIn) {
                 opacity += 0.03f;
                 if (opacity >= 1.0f) {
                     opacity = 1.0f;
                     fadeIn = false;
                     Timer wait = new Timer(2500, ev -> fadeOut = true);
                     wait.setRepeats(false);
                     wait.start();
                 }
             } else if (fadeOut) {
                 opacity -= 0.03f;
                 if (opacity <= 0f) {
                     finishSplash();
                 }
             }
             setOpacity(opacity);
         });
         fadeTimer.start();

         setOpacity(0f);
         setVisible(true);
     }

     private void finishSplash() {
         fadeTimer.stop();
         pulseTimer.stop();
         dispose();
         SwingUtilities.invokeLater(() -> new ICUApp().setVisible(true));
     }
 }

 // ✅ MAIN: Load OpenCV FIRST
 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
         try {
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception e) {
             e.printStackTrace();
         }

         // ✅ Load OpenCV before ANYTHING
         try {
             System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
             System.out.println("✅ OpenCV loaded successfully.");
         } catch (UnsatisfiedLinkError e) {
             System.err.println("❌ OpenCV native library failed to load: " + e.getMessage());
             JOptionPane.showMessageDialog(null, "OpenCV failed to initialize.", "Critical Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         new SplashScreen(); // Start app flow
     });
 }
}
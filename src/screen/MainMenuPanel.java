package screen;

import main.GameMain;
import util.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

public class MainMenuPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    private static final String HIGH_SCORE_FILE = "highscore.txt";
    private static final String[] BUTTON_LABELS = {
            "1 START GAME",
            "2 HIGH SCORE",
            "3 EXIT"
    };
    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 56;
    private static final int BUTTON_GAP = 18;
    private static final DecimalFormat SCORE_FORMAT = new DecimalFormat("#,##0");

    private final GameMain gameMain;
    private final Rectangle[] buttonBounds;
    private int hoveredIndex = -1;
    private int selectedIndex = 0;
    private int bestScore;

    public MainMenuPanel(GameMain gameMain) {
        this.gameMain = gameMain;
        this.buttonBounds = new Rectangle[BUTTON_LABELS.length];
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        initButtons();
        bestScore = loadBestScore();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    private void initButtons() {
        int totalHeight = BUTTON_LABELS.length * BUTTON_HEIGHT + (BUTTON_LABELS.length - 1) * BUTTON_GAP;
        int startY = (GameConstants.SCREEN_HEIGHT - totalHeight) / 2;
        int startX = (GameConstants.SCREEN_WIDTH - BUTTON_WIDTH) / 2;
        for (int i = 0; i < BUTTON_LABELS.length; i++) {
            int y = startY + i * (BUTTON_HEIGHT + BUTTON_GAP);
            buttonBounds[i] = new Rectangle(startX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
    }

    private int loadBestScore() {
        Path path = Path.of(HIGH_SCORE_FILE);
        if (!Files.exists(path)) {
            return 0;
        }
        try {
            String content = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return 0;
            }
            String cleaned = content.replaceAll("[^0-9-]", "");
            if (cleaned.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(cleaned);
        } catch (IOException e) {
            System.err.println("Failed to read high score file: " + e.getMessage());
            return 0;
        } catch (NumberFormatException e) {
            System.err.println("Invalid high score value in file.");
            return 0;
        }
    }

    private String getBestScoreText() {
        return "BEST: " + SCORE_FORMAT.format(bestScore);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font buttonFont = new Font("Dialog", Font.BOLD, 20);
        Font bestFont = new Font("Dialog", Font.PLAIN, 18);

        int activeIndex = hoveredIndex >= 0 ? hoveredIndex : selectedIndex;

        for (int i = 0; i < BUTTON_LABELS.length; i++) {
            Rectangle rect = buttonBounds[i];
            Color color = (i == activeIndex) ? new Color(255, 200, 0) : Color.WHITE;
            g2.setColor(color);
            g2.setFont(buttonFont);
            g2.drawRect(rect.x, rect.y, rect.width, rect.height);

            String label = BUTTON_LABELS[i];
            FontMetrics fm = g2.getFontMetrics();
            int textX = rect.x + (rect.width - fm.stringWidth(label)) / 2;
            int textY = rect.y + (rect.height + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(label, textX, textY);
        }

        g2.setFont(bestFont);
        g2.setColor(Color.WHITE);
        String bestText = getBestScoreText();
        FontMetrics bestFm = g2.getFontMetrics();
        int bestX = (GameConstants.SCREEN_WIDTH - bestFm.stringWidth(bestText)) / 2;
        int bestY = buttonBounds[BUTTON_LABELS.length - 1].y + BUTTON_HEIGHT + 40;
        g2.drawString(bestText, bestX, bestY);
    }

    private int getButtonIndexAt(Point point) {
        for (int i = 0; i < buttonBounds.length; i++) {
            if (buttonBounds[i].contains(point)) {
                return i;
            }
        }
        return -1;
    }

    private void performAction(int index) {
        switch (index) {
            case 0:
                gameMain.startGame();
                break;
            case 1:
                JOptionPane.showMessageDialog(
                        this,
                        getBestScoreText(),
                        "HIGH SCORE",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
            case 2:
                gameMain.dispose();
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int index = getButtonIndexAt(e.getPoint());
            if (index >= 0) {
                performAction(index);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hoveredIndex = -1;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int index = getButtonIndexAt(e.getPoint());
        if (index != hoveredIndex) {
            hoveredIndex = index;
            if (hoveredIndex >= 0) {
                selectedIndex = hoveredIndex;
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            selectedIndex = Math.max(0, selectedIndex - 1);
            hoveredIndex = -1;
            repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedIndex = Math.min(BUTTON_LABELS.length - 1, selectedIndex + 1);
            hoveredIndex = -1;
            repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            performAction(selectedIndex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

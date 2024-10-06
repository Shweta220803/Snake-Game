import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    private final int TILE_SIZE = 25;
    private final int WIDTH = 20 * TILE_SIZE;
    private final int HEIGHT = 20 * TILE_SIZE;
    private final int ALL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    
    private final int[] x = new int[ALL_TILES];
    private final int[] y = new int[ALL_TILES];
    private int bodyParts = 3;
    private int foodX, foodY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private int score = 0;
    private ScoreManager scoreManager;
    private AudioManager audioManager;
    
    public GameBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        scoreManager = new ScoreManager();
        audioManager = new AudioManager();
    }

    public void startGame() {
        spawnFood();
        running = true;
        timer = new Timer(200, this);
        timer.start();
        audioManager.playBackgroundMusic();
    }

    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'L': x[0] -= TILE_SIZE; break;
            case 'R': x[0] += TILE_SIZE; break;
            case 'U': y[0] -= TILE_SIZE; break;
            case 'D': y[0] += TILE_SIZE; break;
        }
    }

    private void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            score += 10;
            spawnFood();
            audioManager.playEatSound();
        }
    }

    private void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
            scoreManager.updateHighScore(score);
            audioManager.playGameOverSound();
        }
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (running) {
            switch (key) {
                case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
                case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
                case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
                case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
                case KeyEvent.VK_P: togglePause(); break;
            }
        } else if (key == KeyEvent.VK_R) {
            restartGame();
        }
    }

    private void togglePause() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }

    private void restartGame() {
        bodyParts = 3;
        direction = 'R';
        score = 0;
        running = true;
        spawnFood();
        timer.start();
        audioManager.playBackgroundMusic();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(i == 0 ? Color.GREEN : Color.GRAY);
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 10);
            g.drawString("High Score: " + scoreManager.getHighScore(), WIDTH - 120, 10);
        } else {
            showGameOver(g);
        }
    }

    private void showGameOver(Graphics g) {
        String msg = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 36));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);
        g.drawString("Press R to Restart", (WIDTH - metrics.stringWidth("Press R to Restart")) / 2, HEIGHT / 2 + 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }
}

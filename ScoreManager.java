public class ScoreManager {
    private int highScore = 0;

    public int getHighScore() {
        return highScore;
    }

    public void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }
}

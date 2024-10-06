import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {

    private Clip backgroundClip;

    public void playBackgroundMusic() {
        try {
            // File soundFile = new File("background.wav");
            // AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            // backgroundClip = AudioSystem.getClip();
            // backgroundClip.open(audioStream);
            // backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playEatSound() {
        // playSound("eat.wav");
    }

    public void playGameOverSound() {
        // playSound("gameover.wav");
        stopBackgroundMusic();
    }

    private void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }
}

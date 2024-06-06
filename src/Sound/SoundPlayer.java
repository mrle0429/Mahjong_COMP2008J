package Sound;

/**
 * The SoundPlayer class is responsible for playing music and sound effects.
 * It provides two methods: playMusic and playSoundEffect.
 *
 * @version 2024-06
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundPlayer {

    public void playMusic(String musicLocation) {
        try {
            File musicPath = new File(musicLocation);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();

                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void playSoundEffect(String soundEffectLocation) {
        try {
            File soundEffectPath = new File(soundEffectLocation);

            if (soundEffectPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundEffectPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
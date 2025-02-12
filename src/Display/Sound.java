package Display;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];

    /**
     * This following function
     */
    public Sound() {
        soundURL[0] = getClass().getResource("/Backend/Music/sound1.wav");
        soundURL[1] = getClass().getResource("/Backend/Music/sound2.wav");
        soundURL[2] = getClass().getResource("/Backend/Music/battle.wav");
    }

    public void setFile(int i) {
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (Exception e){
        }
    }
    public void play() {
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

}

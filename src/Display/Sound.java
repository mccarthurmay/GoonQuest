package Display;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * --IMPORTANT--
 * Default java library can only read .Wav file sounds.
 */
public class Sound {

    /**
     * We use clip to open audio files and import
     * We use the URL to store file path of these sounds
     */
    Clip clip;
    URL soundURL[] = new URL[30]; //An array

    /**
     * This following function
     */
    public Sound() {
        soundURL[0] = getClass().getResource("/Backend/Music/sound1.wav");
        soundURL[1] = getClass().getResource("/Backend/Music/sound2.wav");
        soundURL[2] = getClass().getResource("/Backend/Music/battle.wav");
        soundURL[3] = getClass().getResource("/Backend/Music/getItemSoundEffect.wav");
    }

    /**
     * Sets a music file, gets the audio, and then opens it.
     * @param i: A number that signal to the index of a song stored in our URL array.
     */
    public void setFile(int i) {
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (Exception e){
        }
    }

    /**
     * Starts playing the music
     */
    public void play() {
        clip.start();
    }

    /**
     * Put the music inside a loop so
     * that it never stops
     */
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops the loop and ends playing the song
     */
    public void stop(){
        clip.stop();
    }



}

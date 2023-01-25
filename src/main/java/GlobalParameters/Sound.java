package GlobalParameters;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public enum Sound {
    BOMB("boom.wav");

    private Clip clip;

    public void play(){
        clip.setFramePosition(500);
        clip.start();
    }

    Sound(String path){
        InputStream is = Sound.class.getClassLoader().getResourceAsStream(path);
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            this.clip = AudioSystem.getClip();
            clip.open(ais);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

package com.polsl.player.client.example;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class AudioExample implements Serializable {
    private static final long serialVersionUID = 2L;

    private File soundFile = new File(getClass().getResource("/wav/file_example_WAV_10MG.wav").getPath());
    private AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
    private AudioFormat audioFormat = audioInputStream.getFormat();
    private DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

    public AudioExample() throws UnsupportedAudioFileException, IOException {
    }

    public File getSoundFile() {
        return soundFile;
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public DataLine.Info getInfo() {
        return info;
    }
}

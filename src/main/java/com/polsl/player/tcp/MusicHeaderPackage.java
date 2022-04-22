package com.polsl.player.tcp;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class MusicHeaderPackage implements Serializable {
    private static final long serialVersionUID = 1L;

    public File soundFile = new File("C:\\Users\\Macintosh\\IdeaProjects\\ServerSidePlayer\\assets\\wav\\file_example_WAV_10MG.wav");
    public AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
    public AudioFormat audioFormat = audioInputStream.getFormat();
    public DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

    public MusicHeaderPackage() throws UnsupportedAudioFileException, IOException {
    }
}

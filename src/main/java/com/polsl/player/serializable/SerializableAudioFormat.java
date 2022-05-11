package com.polsl.player.serializable;

import javax.sound.sampled.AudioFormat;

public class SerializableAudioFormat {

    private static final long serialVersionUID = 1L;

    transient AudioFormat format;
    private String encoding;
    private float sampleRate;
    private int sampleSizeInBits;
    private int channels;
    private int frameSize;
    private float frameRate;
    private boolean bigEndian;

    public SerializableAudioFormat() {
    }

    public SerializableAudioFormat(AudioFormat format) {
        this.format = format;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public AudioFormat getFormat() {
        return format;
    }

    public String getEncoding() {
        return encoding;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public int getChannels() {
        return channels;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

    public AudioFormat convertToAudioFormat() {
        return new AudioFormat(new AudioFormat.Encoding(encoding), sampleRate, sampleSizeInBits, channels, frameSize,
                frameRate, bigEndian);
    }
}
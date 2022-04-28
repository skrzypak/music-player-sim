package com.polsl.player.serializable;

import javax.sound.sampled.AudioFormat;
import java.io.*;

public class SerializableAudioFormat implements Serializable {

    private static final long serialVersionUID = 1L;

    transient AudioFormat format;

    public SerializableAudioFormat(AudioFormat format) {
        this.format = format;
    }

    public AudioFormat getAf() {
        return this.format;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new SerializableEncoding(format.getEncoding()));
        out.writeFloat(format.getSampleRate());
        out.writeInt(format.getSampleSizeInBits());
        out.writeInt(format.getChannels());
        out.writeInt(format.getFrameSize());
        out.writeFloat(format.getFrameRate());
        out.writeBoolean(format.isBigEndian());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        format = new AudioFormat(((SerializableEncoding) in.readObject()).getEncoding(), in.readFloat(), in.readInt(),
                in.readInt(), in.readInt(), in.readFloat(), in.readBoolean());
    }

}
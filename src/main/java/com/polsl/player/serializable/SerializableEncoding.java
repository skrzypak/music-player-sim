package com.polsl.player.serializable;

import javax.sound.sampled.AudioFormat.Encoding;
import java.io.*;

public class SerializableEncoding implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    transient Encoding encoding;

    public SerializableEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public Encoding getEncoding() {
        return this.encoding;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(getEncodingName());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        encoding = new Encoding((String) in.readObject());
    }

    private String getEncodingName() {
        if(this.encoding.equals(new Encoding("PCM_SIGNED"))) return "PCM_SIGNED";
        if(this.encoding.equals(new Encoding("PCM_UNSIGNED"))) return "PCM_UNSIGNED";
        if(this.encoding.equals(new Encoding("ULAW"))) return "ULAW";
        return "ALAW";
    }

}
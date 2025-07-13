package ru.itis.vhsroni.semestrovka.protocol;

public class Message {
    private final int type;

    private final byte[] data;

    public Message(int type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }


    public byte[] getData() {
        return data;
    }
}

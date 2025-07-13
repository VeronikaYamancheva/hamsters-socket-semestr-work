package ru.itis.vhsroni.semestrovka.protocol;

import ru.itis.vhsroni.semestrovka.exception.message.*;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MessageProtocol {

    public static final byte[] START_BYTES = {0x0, 0x1};
    public static final int MAX_MESSAGE_LENGTH = 100 * 1024;
    public static final int INT_BYTES = 4;

    public static Message readMessage(InputStream in) throws ReadMessageException {
        byte[] buffer = new byte[START_BYTES.length + INT_BYTES * 2];
        try {
            in.read(buffer, 0, START_BYTES.length);
            int counter = 0;
            for (int i = 0; i < START_BYTES.length; i++) {
                if (buffer[i] == 0) counter++;
            }
            if (counter == START_BYTES.length) return null;
            if (!Arrays.equals(Arrays.copyOfRange(buffer, 0, START_BYTES.length), START_BYTES)) {
                throw new InvalidProtocolVersionException();
            }

            in.read(buffer, 0, INT_BYTES);
            int messageType = ByteBuffer.wrap(buffer, 0, INT_BYTES).getInt();
            if (!MessageType.getAllTypes().contains(messageType)) {
                throw new InvalidMessageTypeException();
            }

            in.read(buffer, 0, INT_BYTES);
            int messageLength = ByteBuffer.wrap(buffer, 0, INT_BYTES).getInt();
            if (messageLength > MAX_MESSAGE_LENGTH) {
                throw new InvalidMessageLengthException();
            }
            buffer = new byte[messageLength];
            in.read(buffer, 0, messageLength);
            return MessageCreator.createMessage(messageType, buffer);
        } catch (IOException e) {
            try {
                in.close();
            } catch (IOException ex) {
                throw new ReadMessageException(e);
            }
        }
        return null;
    }

    public static void writeMessage(OutputStream out, Message message) throws WriteMessageException {
        try {
            out.write(MessageProtocol.getBytes(message));
            out.flush();
        } catch (IOException e) {
            throw new WriteMessageException(e);
        }
    }

    public static byte[] getBytes(Message message) {
        ByteBuffer buffer = ByteBuffer.allocate(START_BYTES.length + INT_BYTES * 2 + message.getData().length);
        buffer.put(START_BYTES);

        int type = message.getType();
        if (!MessageType.getAllTypes().contains(type)) {
            throw new InvalidMessageTypeException();
        }
        buffer.putInt(type);

        int length = message.getData().length;
        if (length > MAX_MESSAGE_LENGTH) {
            throw new InvalidMessageLengthException();
        }
        buffer.putInt(length);

        buffer.put(message.getData());
        return buffer.array();
    }
}

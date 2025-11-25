package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import static org.bambrikii.tiny.db.storage.types.IntIOUtils.capacity;

public class CharIOUtils {
    private CharIOUtils() {
    }

    @SneakyThrows
    public static char readChar(ByteChannel channel, ByteBuffer charBuff) {
        return capacity(channel, charBuff) >= 0 ? charBuff.getChar() : 0;
    }

    @SneakyThrows
    public static void writeChar(char ch, ByteChannel channel, ByteBuffer charBuff) {
        charBuff.clear();
        charBuff.putChar(ch);
        charBuff.flip();
        channel.write(charBuff);
    }
}

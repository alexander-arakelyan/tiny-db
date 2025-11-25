package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class IntIOUtils {
    private IntIOUtils() {
    }

    @SneakyThrows
    public static int readInt(ByteChannel channel, ByteBuffer intBuff) {
        return capacity(channel, intBuff) >= 0 ? intBuff.getInt() : Integer.MIN_VALUE;
    }

    @SneakyThrows
    public static void writeInt(int val, ByteChannel channel, ByteBuffer intBuff) {
        intBuff.clear();
        intBuff.putInt(val);
        intBuff.flip();
        channel.write(intBuff);
    }

    static long capacity(ByteChannel channel, ByteBuffer buff) throws IOException {
        buff.clear();
        channel.read(buff);
        var available = buff.remaining();
        buff.flip();
        return buff.capacity() - available - 1;
    }
}

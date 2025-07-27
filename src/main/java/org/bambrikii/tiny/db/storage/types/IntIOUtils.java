package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IntIOUtils {
    private IntIOUtils() {
    }

    @SneakyThrows
    public static int readInt(FileChannel channel, ByteBuffer intBuff) {
        return capacity(channel, intBuff) >= 0 ? intBuff.getInt() : Integer.MIN_VALUE;
    }

    @SneakyThrows
    public static void writeInt(int val, FileChannel channel, ByteBuffer intBuff) {
        intBuff.clear();
        intBuff.putInt(val);
        intBuff.flip();
        channel.write(intBuff);
    }

    static long capacity(FileChannel channel, ByteBuffer buff) throws IOException {
        buff.clear();
        var pos1 = channel.position();
        channel.read(buff);
        var pos2 = channel.position();
        var available = pos2 - pos1;
        var limit = buff.limit();
        buff.flip();
        return available < limit
                ? available - limit
                : available;
    }
}

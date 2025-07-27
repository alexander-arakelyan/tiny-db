package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.bambrikii.tiny.db.storage.types.IntIOUtils.capacity;
import static org.bambrikii.tiny.db.storage.types.IntIOUtils.readInt;
import static org.bambrikii.tiny.db.storage.types.IntIOUtils.writeInt;

public class ByteIOUtils {
    private ByteIOUtils() {
    }


    @SneakyThrows
    public static byte[] readBytes(FileChannel channel, ByteBuffer intBuff) {
        int len = readInt(channel, intBuff);
        if (len == Integer.MIN_VALUE) {
            return null;
        }
        var buff = ByteBuffer.allocate(len);
        if (capacity(channel, buff) < 0) {
            return null;
        }
        return buff.array();
    }

    @SneakyThrows
    public static void writeBytes(byte[] bytes, FileChannel channel, ByteBuffer intBuff) {
        writeInt(bytes.length, channel, intBuff);
        channel.write(ByteBuffer.wrap(bytes));
    }

}

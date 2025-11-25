package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import static org.bambrikii.tiny.db.storage.types.IntIOUtils.capacity;
import static org.bambrikii.tiny.db.storage.types.IntIOUtils.readInt;
import static org.bambrikii.tiny.db.storage.types.IntIOUtils.writeInt;

public class ByteIOUtils {
    public static final int NULL_STR_LEN = -1;

    private ByteIOUtils() {
    }


    @SneakyThrows
    public static byte[] readBytes(ByteChannel channel, ByteBuffer intBuff) {
        int len = readInt(channel, intBuff);
        if (len == Integer.MIN_VALUE) {
            return null;
        }
        if (len == NULL_STR_LEN) {
            return null;
        }
        var buff = ByteBuffer.allocate(len);
        if (capacity(channel, buff) < 0) {
            return null;
        }
        return buff.array();
    }

    @SneakyThrows
    public static void writeBytes(byte[] bytes, ByteChannel channel, ByteBuffer intBuff) {
        if (bytes == null) {
            writeInt(NULL_STR_LEN, channel, intBuff);
            return;
        }
        int len = bytes.length;
        writeInt(len, channel, intBuff);
        channel.write(ByteBuffer.wrap(bytes));
    }
}

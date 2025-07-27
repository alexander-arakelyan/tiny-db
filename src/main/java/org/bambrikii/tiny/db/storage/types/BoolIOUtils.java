package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.bambrikii.tiny.db.storage.types.IntIOUtils.capacity;

public class BoolIOUtils {
    private BoolIOUtils() {
    }

    @SneakyThrows
    public static boolean readBool(FileChannel channel, ByteBuffer boolBuff) {
        return capacity(channel, boolBuff) >= 0 && boolBuff.get() == 1;
    }

    @SneakyThrows
    public static void writeBool(boolean val, FileChannel channel, ByteBuffer boolBuff) {
        boolBuff.clear();
        boolBuff.put((byte) (val ? 1 : 0));
        boolBuff.flip();
        channel.write(boolBuff);
    }
}

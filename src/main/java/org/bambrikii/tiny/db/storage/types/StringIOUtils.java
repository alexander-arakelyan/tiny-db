package org.bambrikii.tiny.db.storage.types;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.bambrikii.tiny.db.storage.types.ByteIOUtils.readBytes;
import static org.bambrikii.tiny.db.storage.types.ByteIOUtils.writeBytes;

public class StringIOUtils {
    private StringIOUtils() {
    }

    public static String readStr(FileChannel channel, ByteBuffer intBuff) {
        byte[] bytes = readBytes(channel, intBuff);
        return bytes != null ? new String(bytes, UTF_8) : null;
    }

    public static void writeStr(String table, FileChannel channel, ByteBuffer intBuff) {
        byte[] bytes = table.getBytes();
        writeBytes(bytes, channel, intBuff);
    }
}

package org.bambrikii.tiny.db.storage.types;

import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.bambrikii.tiny.db.storage.types.ByteIOUtils.readBytes;
import static org.bambrikii.tiny.db.storage.types.ByteIOUtils.writeBytes;

public class StringIOUtils {
    private StringIOUtils() {
    }

    public static String readStr(ByteChannel channel, ByteBuffer intBuff) {
        byte[] bytes = readBytes(channel, intBuff);
        return bytes != null ? new String(bytes, UTF_8) : null;
    }

    public static void writeStr(String str, ByteChannel channel, ByteBuffer intBuff) {
        writeBytes(str == null ? null : str.getBytes(), channel, intBuff);
    }
}

package org.bambrikii.tiny.db.storage.types;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Base64;

import static org.bambrikii.tiny.db.storage.types.ByteIOUtils.readBytes;
import static org.bambrikii.tiny.db.storage.types.ByteIOUtils.writeBytes;

public class ObjectIOUtils {
    private ObjectIOUtils() {
    }


    public static <T> T readObj(FileChannel channel, ByteBuffer intBuff) {
        var base64 = readBytes(channel, intBuff);
        return base64 != null ? deserialize(Base64.getDecoder().decode(base64)) : null;
    }

    public static void writeObj(Object obj, FileChannel channel, ByteBuffer intBuff) {
        var bytes = serialize(obj);
        var base64 = Base64.getEncoder().encode(bytes);
        writeBytes(base64, channel, intBuff);
    }

    @SneakyThrows
    private static <T> T deserialize(byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        try (var bais = new ByteArrayInputStream(bytes);
             var ois = new ObjectInputStream(bais)
        ) {
            return (T) ois.readObject();
        }
    }

    @SneakyThrows
    private static byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        try (var baos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(baos)
        ) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }
}

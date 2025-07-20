package org.bambrikii.tiny.db.storage.disk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Base64;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public class FileOps {
    public static final String ROW_ID_COLUMN_NAME = "rowid";
    public static final char COL_SEPARATOR = ',';
    public static final char LINE_SEPARATOR = '\n';

    private final FileChannel channel;

    private final ByteBuffer intBuff = ByteBuffer.allocate(4);
    private final ByteBuffer boolBuff = ByteBuffer.allocate(1);
    private final ByteBuffer charBuff = ByteBuffer.allocate(1);

    @SneakyThrows
    private byte[] readBytes() {
        int len = readInt();

        var buff = ByteBuffer.allocate(len);
        channel.read(buff);
        byte[] bytes = buff.array();
        return bytes;
    }

    private void writeBytes(byte[] bytes) throws IOException {
        writeInt(bytes.length);
        channel.write(ByteBuffer.wrap(bytes));
    }

    @SneakyThrows
    public String readStr() {
        byte[] bytes = readBytes();
        return new String(bytes, UTF_8);
    }

    @SneakyThrows
    public void writeStr(String table) {
        byte[] bytes = table.getBytes();
        writeBytes(bytes);
    }

    @SneakyThrows
    public int readInt() {
        intBuff.reset();
        channel.read(intBuff);
        return intBuff.getInt();
    }

    @SneakyThrows
    public void writeInt(int size) {
        intBuff.reset();
        intBuff.putInt(size);
        channel.write(intBuff);
    }

    @SneakyThrows
    public boolean readBool() {
        boolBuff.reset();
        channel.read(boolBuff);
        return boolBuff.getInt() == 1;
    }

    @SneakyThrows
    public void writeBool(boolean unique) {
        boolBuff.reset();
        boolBuff.put((byte) (unique ? 1 : 0));
        channel.write(boolBuff);
    }

    @SneakyThrows
    public char readChar() {
        charBuff.reset();
        channel.read(charBuff);
        return charBuff.getChar();
    }

    @SneakyThrows
    public void writeChar(char ch) {
        charBuff.reset();
        charBuff.putChar(ch);
        channel.write(charBuff);
    }

    @SneakyThrows
    public <T> T readObj() {
        var base64 = readBytes();
        var bytes = Base64.getDecoder().decode(base64);
        return deserialize(bytes);
    }

    @SneakyThrows
    public void writeObj(Object obj) {
        var bytes = serialize(obj);
        var base64 = Base64.getEncoder().encode(bytes);
        writeBytes(base64);
    }

    /**
     * 36 chars
     *
     * @return
     */
    @SneakyThrows
    public String readRowId() {
        return readStr();
    }

    /**
     * 36 chars
     *
     * @return
     */
    @SneakyThrows
    public String writeRowId() {
        var str = UUID.randomUUID().toString();
        writeStr(str);
    }

    @SneakyThrows
    private <T> T deserialize(byte[] bytes) {
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
    private byte[] serialize(Object obj) {
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

    public boolean readLineSeparator() {
        return readChar() == LINE_SEPARATOR;
    }

    public void writeLineSeparator() {
        writeChar(LINE_SEPARATOR);
    }

    public boolean readColSeparator() {
        return readChar() == COL_SEPARATOR;
    }

    public void writeColSeparator() {
        writeChar(COL_SEPARATOR);
    }
}

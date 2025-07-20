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
        if (len == Integer.MIN_VALUE) {
            return null;
        }
        var buff = ByteBuffer.allocate(len);
        if (capacity(buff) < 0) {
            return null;
        }
        return buff.array();
    }

    @SneakyThrows
    private void writeBytes(byte[] bytes) {
        writeInt(bytes.length);
        channel.write(ByteBuffer.wrap(bytes));
    }

    @SneakyThrows
    public String readStr() {
        byte[] bytes = readBytes();
        return bytes != null ? new String(bytes, UTF_8) : null;
    }

    @SneakyThrows
    public void writeStr(String table) {
        byte[] bytes = table.getBytes();
        writeBytes(bytes);
    }

    @SneakyThrows
    public int readInt() {
        return capacity(intBuff) >= 0 ? intBuff.getInt() : Integer.MIN_VALUE;
    }

    private long capacity(ByteBuffer buff) throws IOException {
        var pos1 = channel.position();
        buff.reset();
        channel.read(buff);
        var pos2 = channel.position();
        var available = pos2 - pos1;
        var limit = buff.limit();
        return available < limit
                ? available - limit
                : available;
    }

    @SneakyThrows
    public void writeInt(int val) {
        intBuff.reset();
        intBuff.putInt(val);
        channel.write(intBuff);
    }

    @SneakyThrows
    public boolean readBool() {
        return capacity(boolBuff) >= 0 && boolBuff.getInt() == 1;
    }

    @SneakyThrows
    public void writeBool(boolean unique) {
        boolBuff.reset();
        boolBuff.put((byte) (unique ? 1 : 0));
        channel.write(boolBuff);
    }

    @SneakyThrows
    public char readChar() {
        return capacity(charBuff) >= 0 ? charBuff.getChar() : 0;
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
        return base64 != null ? deserialize(Base64.getDecoder().decode(base64)) : null;
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
    public String writeRowId(String rowId) {
        var rowId2 = rowId != null ? rowId : UUID.randomUUID().toString();
        writeStr(rowId2);
        return rowId2;
    }

    public boolean readDeleted() {
        return readBool();
    }

    public void writeDeleted(boolean deleted) {
        writeBool(deleted);
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

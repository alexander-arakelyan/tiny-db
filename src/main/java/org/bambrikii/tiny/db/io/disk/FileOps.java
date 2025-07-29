package org.bambrikii.tiny.db.io.disk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bambrikii.tiny.db.storage.types.BoolIOUtils;
import org.bambrikii.tiny.db.storage.types.ByteIOUtils;
import org.bambrikii.tiny.db.storage.types.CharIOUtils;
import org.bambrikii.tiny.db.storage.types.IntIOUtils;
import org.bambrikii.tiny.db.storage.types.ObjectIOUtils;
import org.bambrikii.tiny.db.storage.types.StringIOUtils;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

@RequiredArgsConstructor
public class FileOps {
    public static final String ROW_ID_COLUMN_NAME = "rowid";
    public static final char COL_SEPARATOR = ',';
    public static final char LINE_SEPARATOR = '\n';

    private final FileChannel channel;

    private final ByteBuffer intBuff = ByteBuffer.allocate(4);
    private final ByteBuffer boolBuff = ByteBuffer.allocate(1);
    private final ByteBuffer charBuff = ByteBuffer.allocate(2);

    @SneakyThrows
    private byte[] readBytes() {
        return ByteIOUtils.readBytes(channel, intBuff);
    }

    @SneakyThrows
    private void writeBytes(byte[] bytes) {
        ByteIOUtils.writeBytes(bytes, channel, intBuff);
    }

    @SneakyThrows
    public String readStr() {
        return StringIOUtils.readStr(channel, intBuff);
    }

    @SneakyThrows
    public void writeStr(String table) {
        StringIOUtils.writeStr(table, channel, intBuff);
    }

    @SneakyThrows
    public int readInt() {
        return IntIOUtils.readInt(channel, intBuff);
    }

    @SneakyThrows
    public void writeInt(int val) {
        IntIOUtils.writeInt(val, channel, intBuff);
    }

    @SneakyThrows
    public boolean readBool() {
        return BoolIOUtils.readBool(channel, boolBuff);
    }

    @SneakyThrows
    public void writeBool(boolean val) {
        BoolIOUtils.writeBool(val, channel, boolBuff);
    }

    @SneakyThrows
    public char readChar() {
        return CharIOUtils.readChar(channel, charBuff);
    }

    @SneakyThrows
    public void writeChar(char ch) {
        CharIOUtils.writeChar(ch, channel, charBuff);
    }

    @SneakyThrows
    public <T> T readObj() {
        return ObjectIOUtils.readObj(channel, intBuff);
    }

    @SneakyThrows
    public void writeObj(Object obj) {
        ObjectIOUtils.writeObj(obj, channel, intBuff);
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

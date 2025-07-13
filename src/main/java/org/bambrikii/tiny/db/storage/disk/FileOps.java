package org.bambrikii.tiny.db.storage.disk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class FileOps {
    private final FileChannel channel;

    private final ByteBuffer intBuff = ByteBuffer.allocate(4);
    private final ByteBuffer boolBuff = ByteBuffer.allocate(1);

    @SneakyThrows
    public String readStr(FileChannel channel) {
        intBuff.reset();
        channel.read(intBuff);
        int size = intBuff.getInt();

        var buff = ByteBuffer.allocate(size);
        channel.read(buff);
        return new String(buff.array(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public int readInt(FileChannel channel) {
        intBuff.reset();
        channel.read(intBuff);
        return intBuff.getInt();
    }

    @SneakyThrows
    public void readObj(String col) {
        var objBuf = ByteBuffer.allocate(1);
        channel.read(objBuf);
        objBuf
    }

    @SneakyThrows
    public void writeStr(String table) {
        channel.write(ByteBuffer.wrap(table.getBytes()));
    }

    @SneakyThrows
    public void writeInt(int size) {
        intBuff.reset();
        intBuff.putInt(size);
        channel.write(intBuff);
    }

    @SneakyThrows
    public void writeBool(boolean unique) {
        boolBuff.reset();
        boolBuff.put((byte) (unique ? 1 : 0));
        channel.write(boolBuff);
    }

    @SneakyThrows
    public boolean readBool(FileChannel channel) {
        boolBuff.reset();
        channel.read(boolBuff);
        return boolBuff.getInt() == 1;
    }
}

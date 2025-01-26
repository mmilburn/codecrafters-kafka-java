package log;

import shared.VarInt;
import util.HexDump;

import java.nio.ByteBuffer;

public class UnimplementedRecord extends ValueRecord {
    private byte[] buf;

    protected UnimplementedRecord() {
    }

    public byte[] getBuf() {
        return buf;
    }

    @Override
    protected void parse(ByteBuffer data) {
        this.length = VarInt.fromByteBuffer(data);
        byte[] buffer = new byte[this.length.getValue()];
        data.get(buffer);
        this.buf = buffer;
        HexDump.printHexDump(this.buf);
    }
}

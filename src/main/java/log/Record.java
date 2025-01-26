package log;

import shared.VarInt;

import java.nio.ByteBuffer;

public class Record {
    private final VarInt length;
    private final byte attributes;
    private final VarInt timestampDelta;
    private final VarInt offsetDelta;
    private final Key key;
    private final ValueRecord value;
    //unsigned
    private final VarInt headersArrayCount;

    private Record(VarInt length, byte attributes, VarInt timestampDelta, VarInt offsetDelta, Key key, ValueRecord value, VarInt headersArrayCount) {
        this.length  = length;
        this.attributes = attributes;
        this.timestampDelta = timestampDelta;
        this.offsetDelta = offsetDelta;
        this.key = key;
        this.value = value;
        this.headersArrayCount = headersArrayCount;
    }

    public VarInt getLength() {
        return length;
    }

    public byte getAttributes() {
        return attributes;
    }

    public VarInt getTimestampDelta() {
        return timestampDelta;
    }

    public VarInt getOffsetDelta() {
        return offsetDelta;
    }

    public Key getKey() {
        return key;
    }

    public ValueRecord getValue() {
        return value;
    }

    public VarInt getHeadersArrayCount() {
        return headersArrayCount;
    }

    public static Record fromByteBuffer(ByteBuffer data) {
        return new Record(VarInt.fromByteBuffer(data), data.get(), VarInt.fromByteBuffer(data),
                VarInt.fromByteBuffer(data), Key.fromByteBuffer(data), ValueRecordFactory.fromByteBuffer(data),
                VarInt.fromByteBuffer(data));
    }
}

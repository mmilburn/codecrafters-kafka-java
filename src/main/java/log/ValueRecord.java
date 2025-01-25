package log;

import shared.VarInt;

import java.nio.ByteBuffer;

public abstract class ValueRecord {
    protected byte frameVersion;
    protected byte type;
    protected byte version;
    protected VarInt taggedFieldsCount;

    public byte getFrameVersion() {
        return frameVersion;
    }

    public byte getType() {
        return type;
    }

    public byte getVersion() {
        return version;
    }

    public VarInt getTaggedFieldsCount() {
        return taggedFieldsCount;
    }

   protected abstract void parse(ByteBuffer data);
}

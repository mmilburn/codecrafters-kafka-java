package log;

import shared.VarInt;

import java.nio.ByteBuffer;

public class Key {
    private final byte[] key;

    private Key(byte[] key) {
        this.key = key;
    }

    protected Key() {
        this.key = null;
    }

    public static Key fromByteBuffer(ByteBuffer data) {
        int len = VarInt.fromByteBuffer(data).getValue();
        if (len == -1) {
            return new NullKey();
        }
        byte[] bytes = new byte[len];
        data.get(bytes);
        return new Key(bytes);
    }
}

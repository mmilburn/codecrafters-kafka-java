package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CompactString {
    private String str;

    public CompactString(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
            dos.write(new VarInt(utf8.length + 1).toBytes());
            dos.write(utf8);
        });
    }

    public static CompactString fromByteBuffer(ByteBuffer data) {
        int strLen = VarInt.fromByteBuffer(data).getUnsignedValue() - 1;
        byte[] temp = new byte[(int) strLen];
        data.get(temp);
        return new CompactString(new String(temp, StandardCharsets.UTF_8));
    }
}

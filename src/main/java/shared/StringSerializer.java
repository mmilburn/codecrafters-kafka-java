package shared;

import shared.serializer.ElementSerializer;
import util.StreamUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringSerializer implements ElementSerializer<String> {
    @Override
    public byte[] toBytes(String element) {
        return StreamUtils.toBytes(dos -> {
            byte[] bytes = element.getBytes(StandardCharsets.UTF_8);
            dos.writeInt(bytes.length);
            dos.write(bytes);
        });
    }

    @Override
    public String fromByteBuffer(ByteBuffer data) {
        int len = data.getInt();
        byte[] bytes = new byte[len];
        data.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

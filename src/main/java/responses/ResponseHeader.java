package responses;

import util.StreamUtils;

import java.nio.ByteBuffer;

public record ResponseHeader(int correlationId) {

    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            dos.writeInt(correlationId);
        });
    }

    public static ResponseHeader fromByteBuffer(ByteBuffer data) {
        return new ResponseHeader(data.getInt());
    }
}
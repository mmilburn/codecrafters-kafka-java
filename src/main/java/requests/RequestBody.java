package requests;

import java.nio.ByteBuffer;

public abstract class RequestBody {
    public abstract APIVersionsRequest fromByteBuffer(ByteBuffer data);
    public abstract byte[] toBytes();
}

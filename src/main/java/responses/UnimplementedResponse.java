package responses;

import requests.Request;

import java.nio.ByteBuffer;

public class UnimplementedResponse extends ResponseBody {

    private UnimplementedResponse() {
    }

    public static UnimplementedResponse fromRequest(Request<?> request) {
        return new UnimplementedResponse();
    }

    @Override
    public ResponseBody fromBytebuffer(ByteBuffer data) {
        return null;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}

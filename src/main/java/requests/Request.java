package requests;

import util.StreamUtils;

import java.nio.ByteBuffer;

public class Request {
    private final RequestHeader header;
    private final RequestBody body;

    public Request(RequestHeader header, RequestBody body) {
        this.header = header;
        this.body = body;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public RequestBody getBody() {
        return body;
    }

    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            byte[] headerBytes = header.toBytes();
            byte[] bodyBytes = body.toBytes();
            dos.writeInt(headerBytes.length + bodyBytes.length);
            dos.write(headerBytes);
            dos.write(bodyBytes);
        });
    }

    public static Request fromByteBuffer(ByteBuffer data) {
        RequestHeader requestHeader = RequestHeader.fromByteBuffer(data);
        RequestBody requestBody = null;
        if (requestHeader.getRequestAPIKey() == 18) {
            requestBody = new APIVersionsRequest();
        }
        return new Request(requestHeader, requestBody);
    }
}

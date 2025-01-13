package responses;

import requests.APIVersionsRequest;
import requests.Request;
import util.StreamUtils;

import java.nio.ByteBuffer;


public class Response {
    private ResponseHeader responseHeader;
    private ResponseBody body;

    public Response(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public Response(Request request) {
        this.responseHeader = new ResponseHeader(request.getHeader().getCorrelationId());
        if (request.getBody() instanceof APIVersionsRequest) {
            this.body = new APIVersionsResponse(request);
        }
    }

    public ResponseHeader getHeader() {
        return responseHeader;
    }

    public void setHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public ResponseBody getBody() {
        return body;
    }

    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            byte[] headerBytes = responseHeader.toBytes();
            byte[] bodyBytes = body.toBytes();
            dos.writeInt(headerBytes.length + bodyBytes.length);
            dos.write(headerBytes);
            dos.write(bodyBytes);
        });
    }

    public static Response fromByteBuffer(ByteBuffer data) {
        int messageSize = data.getInt();
        int start = data.position();
        ResponseHeader responseHeader = ResponseHeader.fromByteBuffer(data);
        int headerSize = data.position() - start;
        byte[] body = new byte[messageSize - headerSize];
        data.get(body);
        return new Response(responseHeader);
    }

}

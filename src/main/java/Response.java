import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Response {
    private ResponseHeader responseHeader;
    private ResponseBody body;

    public Response(ResponseHeader responseHeader, ResponseBody body) {
        this.responseHeader = responseHeader;
        this.body = body;
    }

    public Response(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
        this.body = new ResponseBody();
    }

    public Response(Request request) {
        this.responseHeader = new ResponseHeader(request.getHeader().getCorrelationId());
        this.body = new ResponseBody(request);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            byte[] headerBytes = responseHeader.toBytes();
            byte[] bodyBytes = body.toBytes();
            dos.writeInt(headerBytes.length + bodyBytes.length);
            dos.write(headerBytes);
            dos.write(bodyBytes);
            dos.flush();
        } catch (IOException ioNo) {
            System.err.println(Arrays.toString(ioNo.getStackTrace()));
        }
        return baos.toByteArray();
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

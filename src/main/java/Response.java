import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Response {
    private ResponseHeader responseHeader;
    private byte[] body;

    public Response(ResponseHeader responseHeader, byte[] body) {
        this.responseHeader = responseHeader;
        this.body = body;
    }

    public Response(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
        this.body = new byte[0];
    }

    public ResponseHeader getHeader() {
        return responseHeader;
    }

    public void setHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            byte[] headerBytes = responseHeader.toBytes();
            dos.writeInt(headerBytes.length + body.length);
            dos.write(responseHeader.toBytes());
            dos.write(body);
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
        return new Response(responseHeader, body);
    }

}

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Request {
    private final RequestHeader header;
    private final byte[] body;

    public Request(RequestHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            byte[] headerBytes = header.toBytes();
            dos.writeInt(headerBytes.length + body.length);
            dos.write(headerBytes);
            dos.write(body);
           dos.flush();
        } catch (IOException ioNo) {
            System.err.println(Arrays.toString(ioNo.getStackTrace()));
        }
        return baos.toByteArray();
    }

    public static Request fromByteBuffer(ByteBuffer data) {
        int size = data.getInt();
        int start = data.position();
        RequestHeader requestHeader = RequestHeader.fromByteBuffer(data);
        //int headerSize = data.position() - start;
        //byte[] body = new byte[size - headerSize];
        //data.get(body);
        return new Request(requestHeader, new byte[0]);
    }
}

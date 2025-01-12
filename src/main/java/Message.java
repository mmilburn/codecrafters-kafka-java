import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Message {
    private Header header;
    private byte[] body;

    public Message(Header header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public Message(Header header) {
        this.header = header;
        this.body = new byte[0];
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
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
            byte[] headerBytes = header.toBytes();
            dos.writeInt(headerBytes.length + body.length);
            dos.write(header.toBytes());
            dos.write(body);
            dos.flush();
        } catch (IOException ioNo) {
            System.err.println(Arrays.toString(ioNo.getStackTrace()));
        }
        return baos.toByteArray();
    }

    public static Message fromByteBuffer(ByteBuffer data) {
        int messageSize = data.getInt();
        int start = data.position();
        Header header = Header.fromByteBuffer(data);
        int headerSize = data.position() - start;
        byte[] body = new byte[messageSize - headerSize];
        data.get(body);
        return new Message(header, body);
    }

}

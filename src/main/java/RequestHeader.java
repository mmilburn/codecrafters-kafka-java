import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RequestHeader {
    private final short requestAPIKey;
    private final short requestAPIVersion;
    private final int correlationId;
    private final String clientId;
    private byte[] tagBuffer;

    public RequestHeader(short requestAPIKey, short requestAPIVersion, int correlationId, String clientId) {
        this.requestAPIKey = requestAPIKey;
        this.requestAPIVersion = requestAPIVersion;
        this.correlationId = correlationId;
        this.clientId = clientId;
    }

    public short getRequestAPIKey() {
        return requestAPIKey;
    }

    public short getRequestAPIVersion() {
        return requestAPIVersion;
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public String getClientId() {
        return clientId;
    }

    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeShort(requestAPIKey);
            dos.writeShort(requestAPIVersion);
            dos.writeInt(correlationId);
            if (!clientId.isEmpty()) {
                dos.writeShort(clientId.length());
                dos.write(clientId.getBytes(StandardCharsets.UTF_8));
            } else {
                dos.writeShort(-1);
            }
            //TODO: implement tagbuffer
            dos.writeInt(tagBuffer.length + 1);
            dos.write(tagBuffer);
            dos.flush();
        }
        catch (IOException ioNo) {
            System.err.println(Arrays.toString(ioNo.getStackTrace()));
        }
        return baos.toByteArray();
    }

    public static RequestHeader fromByteBuffer(ByteBuffer data) {
        short requesetAPIKey = data.getShort();
        short requestAPIVersion = data.getShort();
        int correlationId = data.getInt();
        short clientIdLen = data.getShort();
        String clientId = "";
        if (clientIdLen != -1) {
            byte[] buf = new byte[clientIdLen];
            data.get(clientIdLen);
            clientId = new String(buf, StandardCharsets.UTF_8);
        }
        //TODO tagbuffer
        long tagLen = Integer.toUnsignedLong(data.getInt());

        return new RequestHeader(requesetAPIKey, requestAPIVersion, correlationId, clientId);
    }
}

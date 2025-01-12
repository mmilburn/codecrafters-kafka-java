import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public record Header(int correlationId) {

    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeInt(this.correlationId);
            dos.flush();
        } catch (IOException ioNo) {
            System.err.println(Arrays.toString(ioNo.getStackTrace()));
        }
        return baos.toByteArray();
    }

    public static Header fromByteBuffer(ByteBuffer data) {
        return new Header(data.getInt());
    }
}
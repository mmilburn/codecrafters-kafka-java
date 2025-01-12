import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ResponseBody {
    private short errorCode = 0;

    public ResponseBody() {
    }

    public ResponseBody(Request request) {
        if (request.getHeader().getRequestAPIVersion() != 4) {
            this.errorCode = 35;
        }
    }

    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeShort(errorCode);
            dos.flush();
        } catch (IOException ioNo) {
            System.err.println(Arrays.toString(ioNo.getStackTrace()));
        }
        return baos.toByteArray();
    }
}

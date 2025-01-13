package responses;

import requests.Request;
import shared.APIVersions;
import shared.CompactArray;
import shared.TagBuffer;
import util.StreamUtils;

import java.nio.ByteBuffer;
import java.util.Collections;

public class APIVersionsResponse extends ResponseBody {
    private short errorCode = 0;
    private CompactArray<APIVersions> apiVersionsArray;
    private int throttleTime = 0;
    private TagBuffer tagBuffer = new TagBuffer();

    public APIVersionsResponse(Request request) {
        if (request.getHeader().getRequestAPIVersion() != 4) {
            this.errorCode = 35;
        }
        APIVersions apiVersions = new APIVersions((short) 18, (short) 0, (short) 4, new TagBuffer());
        this.apiVersionsArray = new CompactArray<>(Collections.singletonList(apiVersions), apiVersions);
    }

    @Override
    public APIVersionsResponse fromBytebuffer(ByteBuffer data) {
        return null;
    }

    @Override
    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            dos.writeShort(errorCode);
            dos.write(apiVersionsArray.toBytes());
            dos.writeInt(throttleTime);
            dos.write(tagBuffer.toBytes());
        });
    }
}

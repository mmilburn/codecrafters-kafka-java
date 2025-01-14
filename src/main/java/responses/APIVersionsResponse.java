package responses;

import requests.Request;
import shared.APIVersions;
import shared.CompactArray;
import shared.TagBuffer;
import util.StreamUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIVersionsResponse extends ResponseBody {
    private short errorCode = 0;
    private CompactArray<APIVersions> apiVersionsArray;
    private int throttleTime = 0;
    private TagBuffer tagBuffer = new TagBuffer();

    public APIVersionsResponse(Request<?> request) {
        if (request.header().getRequestAPIVersion() != 4) {
            this.errorCode = 35;
        }
        List<APIVersions> apiVersionsList = new ArrayList<>(Arrays.asList(
                new APIVersions((short) 18, (short) 0, (short) 4, new TagBuffer()),
                new APIVersions((short) 75, (short) 0, (short) 0, new TagBuffer()),
                new APIVersions((short) 1, (short) 0, (short) 16, new TagBuffer())

        ));
        this.apiVersionsArray = new CompactArray<>(apiVersionsList,
                //FIXME: this is hacky!
                new APIVersions());
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

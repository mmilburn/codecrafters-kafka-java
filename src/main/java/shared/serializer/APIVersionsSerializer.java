package shared.serializer;

import shared.APIVersions;
import shared.TagBuffer;
import util.StreamUtils;

import java.nio.ByteBuffer;

public class APIVersionsSerializer implements ElementSerializer<APIVersions> {
    @Override
    public byte[] toBytes(APIVersions apiVersions) {
        return StreamUtils.toBytes(dos -> {
            dos.writeShort(apiVersions.getApiKey());
            dos.writeShort(apiVersions.getMinSupportedVersion());
            dos.writeShort(apiVersions.getMaxSupportedVersion());
            dos.write(apiVersions.getTagBuffer().toBytes());
        });
    }

    @Override
    public APIVersions fromByteBuffer(ByteBuffer data) {
        return new APIVersions(data.getShort(), data.getShort(), data.getShort(), TagBuffer.fromByteBuffer(data));
    }
}

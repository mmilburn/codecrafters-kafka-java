package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;

public class APIVersions implements ElementSerializer<APIVersions>{
    private short apiKey;
    private short minSupportedVersion;
    private short maxSupportedVersion;
    private TagBuffer tagBuffer;

    public APIVersions() {
    }

    public APIVersions(short apiKey, short minSupportedVersion, short maxSupportedVersion, TagBuffer tagBuffer) {
        this.apiKey = apiKey;
        this.minSupportedVersion = minSupportedVersion;
        this.maxSupportedVersion = maxSupportedVersion;
        this.tagBuffer = tagBuffer;
    }

    @Override
    public byte[] toBytes(APIVersions element) {
        return StreamUtils.toBytes(dos -> {
            dos.writeShort(apiKey);
            dos.writeShort(minSupportedVersion);
            dos.writeShort(maxSupportedVersion);
            dos.write(tagBuffer.toBytes());
        });
    }

    @Override
    public APIVersions fromByteBuffer(ByteBuffer data) {
        return new APIVersions(data.getShort(), data.getShort(), data.getShort(), TagBuffer.fromByteBuffer(data));
    }
}

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

    public short getApiKey() {
        return apiKey;
    }

    public short getMinSupportedVersion() {
        return minSupportedVersion;
    }

    public short getMaxSupportedVersion() {
        return maxSupportedVersion;
    }

    public TagBuffer getTagBuffer() {
        return tagBuffer;
    }

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

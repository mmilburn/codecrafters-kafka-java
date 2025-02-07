package responses;

import requests.Request;
import shared.CompactArray;
import shared.TagBuffer;
import shared.TopicResponse;
import shared.serializer.TopicResponseSerializer;
import util.StreamUtils;

import java.nio.ByteBuffer;

public class FetchResponse extends ResponseBody {
    private final int throttleTimeMs;
    private final short errorCode;
    private final int sessionId;
    private final CompactArray<TopicResponse> responses;
    private final TagBuffer tg;

    private FetchResponse(int throttleTimeMs, short errorCode, int sessionId, CompactArray<TopicResponse> responses, TagBuffer tg) {
        this.throttleTimeMs = throttleTimeMs;
        this.errorCode = errorCode;
        this.sessionId = sessionId;
        this.responses = responses;
        this.tg = tg;
    }

    protected static FetchResponse fromRequest(Request<?> req) {
        return new FetchResponse(
                0,
                (short) 0,
                0,
                CompactArray.empty(new TopicResponseSerializer(), TopicResponse::new),
                new TagBuffer()
        );
    }

    @Override
    public ResponseBody fromBytebuffer(ByteBuffer data) {
        return new FetchResponse(
                data.getInt(),
                data.getShort(),
                data.getInt(),
                CompactArray.fromByteBuffer(data, new TopicResponseSerializer(), TopicResponse::new),
                TagBuffer.fromByteBuffer(data)
        );
    }

    @Override
    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            dos.writeInt(this.throttleTimeMs);
            dos.writeShort(this.errorCode);
            dos.writeInt(this.sessionId);
            dos.write(this.responses.toBytes());
            dos.write(this.tg.toBytes());
        });
    }
}

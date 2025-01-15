package responses;

import requests.DescribeTopicPartitionsRequest;
import shared.*;
import shared.serializer.CursorSerializer;
import shared.serializer.ResponseTopicSerializer;
import util.StreamUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DescribeTopicPartitionsResponse extends ResponseBody {
    private int throttleTime = 0;
    private CompactArray<ResponseTopic> topicsArray;
    private Cursor nextCursor;
    private TagBuffer tagBuffer = new TagBuffer();

    public DescribeTopicPartitionsResponse(DescribeTopicPartitionsRequest req) {
        List<ResponseTopic> respTopics = new ArrayList<>();
        for (RequestTopic reqTopic : req.getTopicsArray().getElements()) {
            ResponseTopic respTopic = new ResponseTopic();
            respTopic.setTopicName(reqTopic.getTopicName());
            respTopic.setErrorCode((short) 3);
            respTopics.add(respTopic);
        }
        this.topicsArray = CompactArray.withElements(respTopics, new ResponseTopicSerializer());
        this.nextCursor = Cursor.nullCursor();
    }

    @Override
    public DescribeTopicPartitionsResponse fromBytebuffer(ByteBuffer data) {
        this.throttleTime = data.getInt();
        this.topicsArray = CompactArray.fromByteBuffer(data, new ResponseTopicSerializer(), ResponseTopic::new);
        this.nextCursor = Cursor.from(data, new CursorSerializer());
        this.tagBuffer = TagBuffer.fromByteBuffer(data);
        return this;
    }

    @Override
    public byte[] toBytes() {
        CursorSerializer serializer = new CursorSerializer();
        return StreamUtils.toBytes(dos -> {
            dos.writeInt(throttleTime);
            dos.write(topicsArray.toBytes());
            dos.write(serializer.toBytes(nextCursor));
            dos.write(tagBuffer.toBytes());
        });
    }
}

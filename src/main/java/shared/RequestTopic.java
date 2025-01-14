package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;

public class RequestTopic implements ElementSerializer<RequestTopic> {
    private CompactString topicName;
    private TagBuffer topicTagBuffer;

    public RequestTopic() {
    }

    public RequestTopic(CompactString topicName, TagBuffer topicTagBuffer) {
        this.topicName = topicName;
        this.topicTagBuffer = topicTagBuffer;
    }

    public CompactString getTopicName() {
        return topicName;
    }

    public TagBuffer getTopicTagBuffer() {
        return topicTagBuffer;
    }

    @Override
    public byte[] toBytes(RequestTopic requestTopic) {
        return StreamUtils.toBytes(dos -> {
            dos.write(requestTopic.getTopicName().toBytes());
            dos.write(requestTopic.getTopicTagBuffer().toBytes());
        });
    }

    @Override
    public RequestTopic fromByteBuffer(ByteBuffer data) {
        return new RequestTopic(CompactString.fromByteBuffer(data), TagBuffer.fromByteBuffer(data));
    }
}

package requests;

import shared.CompactArray;
import shared.Cursor;
import shared.RequestTopic;
import shared.TagBuffer;
import util.StreamUtils;

import java.nio.ByteBuffer;

public class DescribeTopicPartitionsRequest extends RequestBody<DescribeTopicPartitionsRequest> {
    private CompactArray<RequestTopic> topicsArray;
    private int responsePartitionLimit;
    private Cursor cursor;
    private TagBuffer tagBuffer;

    public CompactArray<RequestTopic> getTopicsArray() {
        return topicsArray;
    }

    public int getResponsePartitionLimit() {
        return responsePartitionLimit;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public TagBuffer getTagBuffer() {
        return tagBuffer;
    }

    @Override
    public DescribeTopicPartitionsRequest fromByteBuffer(ByteBuffer data) {
        this.topicsArray = CompactArray.fromByteBuffer(data, new RequestTopic());
        this.responsePartitionLimit = data.getInt();
        //FIXME: Another hack!
        this.cursor = Cursor.nullCursor().fromByteBuffer(data);
        this.tagBuffer = TagBuffer.fromByteBuffer(data);
        return this;
    }

    @Override
    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            dos.write(this.topicsArray.toBytes());
            dos.writeInt(this.responsePartitionLimit);
            dos.write(this.cursor.toBytes(this.cursor));
            dos.write(this.tagBuffer.toBytes());
        });
    }
}

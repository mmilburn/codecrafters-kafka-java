package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ResponseTopic implements ElementSerializer<ResponseTopic> {
    private short errorCode;
    private CompactString topicName;
    private UUID topicID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private boolean isInternal;
    private CompactArray<Partition> partitionCompactArray = new CompactArray<>(null, new Partition());
    private int topicAuthorizedOperations;
    private TagBuffer tagBuffer = new TagBuffer();

    public ResponseTopic() {
    }

    public ResponseTopic(short errorCode, CompactString topicName, UUID topicID, boolean isInternal, CompactArray<Partition> partitionCompactArray, int topicAuthorizedOperations, TagBuffer tagBuffer) {
        this.errorCode = errorCode;
        this.topicName = topicName;
        this.topicID = topicID;
        this.isInternal = isInternal;
        this.partitionCompactArray = partitionCompactArray;
        this.topicAuthorizedOperations = topicAuthorizedOperations;
        this.tagBuffer = tagBuffer;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public CompactString getTopicName() {
        return topicName;
    }

    public UUID getTopicID() {
        return topicID;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public CompactArray<Partition> getPartitionCompactArray() {
        return partitionCompactArray;
    }

    public int getTopicAuthorizedOperations() {
        return topicAuthorizedOperations;
    }

    public TagBuffer getTagBuffer() {
        return tagBuffer;
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public void setTopicName(CompactString topicName) {
        this.topicName = topicName;
    }

    @Override
    public byte[] toBytes(ResponseTopic element) {
        return StreamUtils.toBytes(dos -> {
            UUID uuid = element.getTopicID();
            dos.writeShort(element.getErrorCode());
            dos.write(element.getTopicName().toBytes());
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
            dos.writeBoolean(element.isInternal());
            dos.write(element.getPartitionCompactArray().toBytes());
            dos.writeInt(element.getTopicAuthorizedOperations());
            dos.write(element.getTagBuffer().toBytes());
        });
    }

    @Override
    public ResponseTopic fromByteBuffer(ByteBuffer data) {
        this.errorCode = data.getShort();
        this.topicName = CompactString.fromByteBuffer(data);
        this.topicID = new UUID(data.getLong(), data.getLong());
        this.isInternal = data.get() != 0;
        this.partitionCompactArray = CompactArray.fromByteBuffer(data, new Partition());
        this.topicAuthorizedOperations = data.getInt();
        this.tagBuffer = TagBuffer.fromByteBuffer(data);
        return this;
    }
}

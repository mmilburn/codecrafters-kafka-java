package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;
import java.util.Optional;

public class Cursor implements ElementSerializer<Cursor> {
    private final CompactString topicName;
    private final Integer partitionIndex;

    private Cursor(CompactString topicName, Integer partitionIndex) {
        this.topicName = topicName;
        this.partitionIndex = partitionIndex;
    }

    public static Cursor nullCursor() {
        return new Cursor(null, null);
    }

    public static Cursor of(CompactString topicName, Integer partitionIndex) {
        return new Cursor(topicName, partitionIndex);
    }

    public boolean isNull() {
        return topicName == null && partitionIndex == null;
    }

    public Optional<CompactString> getTopicName() {
        return Optional.of(topicName);
    }

    public Optional<Integer> getPartitionIndex() {
        return Optional.of(partitionIndex);
    }

    @Override
    public byte[] toBytes(Cursor element) {
        return StreamUtils.toBytes(dos -> {
            if (element.isNull()) {
                dos.write(-1);
            } else {
                //dos.write(new VarInt(0).toBytes());
                if (element.getTopicName().isPresent() && element.getPartitionIndex().isPresent()) {
                    dos.write(element.getTopicName().get().toBytes());
                    dos.writeInt(element.getPartitionIndex().get());
                }
            }
        });
    }

    @Override
    public Cursor fromByteBuffer(ByteBuffer data) {
        int pos = data.position();
        if (data.get() == -1) {
            return Cursor.nullCursor();
        } else {
            data.position(pos);
            CompactString str = CompactString.fromByteBuffer(data);
            return Cursor.of(str, data.getInt());
        }
    }
}

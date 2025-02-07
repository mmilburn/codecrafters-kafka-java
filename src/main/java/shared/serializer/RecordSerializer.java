package shared.serializer;

import log.Record;
import util.StreamUtils;

import java.nio.ByteBuffer;

public class RecordSerializer implements ElementSerializer<Record> {
    @Override
    public byte[] toBytes(Record element) {
        return StreamUtils.toBytes(dos -> {
            dos.write(element.getLength().toBytes());
            dos.write(element.getAttributes());
            dos.write(element.getTimestampDelta().toBytes());
            dos.write(element.getOffsetDelta().toBytes());
            dos.write(element.getKey().toBytes());
            dos.write(element.getValue().toBytes());
            dos.write(element.getHeadersArrayCount().toBytes());
        });
    }

    @Override
    public Record fromByteBuffer(ByteBuffer data) {
        return Record.fromByteBuffer(data);
    }
}

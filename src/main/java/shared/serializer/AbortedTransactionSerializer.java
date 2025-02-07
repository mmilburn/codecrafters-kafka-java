package shared.serializer;

import shared.AbortedTransaction;
import shared.TagBuffer;
import util.StreamUtils;

import java.nio.ByteBuffer;

public class AbortedTransactionSerializer implements ElementSerializer<AbortedTransaction> {
    @Override
    public byte[] toBytes(AbortedTransaction element) {
        return StreamUtils.toBytes(dos -> {
            dos.writeLong(element.getProducerId());
            dos.writeLong(element.getFirstOffset());
            dos.write(element.getTg().toBytes());
        });
    }

    @Override
    public AbortedTransaction fromByteBuffer(ByteBuffer data) {
        return new AbortedTransaction(data.getLong(), data.getLong(), TagBuffer.fromByteBuffer(data));
    }
}

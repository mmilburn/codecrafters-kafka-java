package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;

public class ReplicaNode implements ElementSerializer<ReplicaNode> {
    private final int nodeId;

    public ReplicaNode(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }

    @Override
    public byte[] toBytes(ReplicaNode element) {
        return StreamUtils.toBytes(dos -> {
            dos.writeInt(element.getNodeId());
        });
    }

    @Override
    public ReplicaNode fromByteBuffer(ByteBuffer data) {
        return new ReplicaNode(data.getInt());
    }
}

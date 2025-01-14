package shared;

import util.StreamUtils;

import java.nio.ByteBuffer;

public class Partition implements ElementSerializer<Partition> {
    private short errorCode;
    private int partitionIndex;
    private int leaderID;
    private int leaderEpoch;
    private CompactArray<ReplicaNode> replicaNodeArray = new CompactArray<>(null, new ReplicaNode(-1));
    private CompactArray<ReplicaNode> isrNodeArray = new CompactArray<>(null, new ReplicaNode(-1));
    private CompactArray<ReplicaNode> eligibleLeaderReplicas = new CompactArray<>(null, new ReplicaNode(-1));
    private CompactArray<ReplicaNode> lastKnownELR = new CompactArray<>(null, new ReplicaNode(-1));
    private CompactArray<ReplicaNode> offlineReplicas = new CompactArray<>(null, new ReplicaNode(-1));
    private TagBuffer tagBuffer = new TagBuffer();

    public Partition() {
    }

    public Partition(short errorCode, int partitionIndex, int leaderID, int leaderEpoch, CompactArray<ReplicaNode> replicaNodeArray, CompactArray<ReplicaNode> isrNodeArray, CompactArray<ReplicaNode> eligibleLeaderReplicas, CompactArray<ReplicaNode> lastKnownELR, CompactArray<ReplicaNode> offlineReplicas, TagBuffer tagBuffer) {
        this.errorCode = errorCode;
        this.partitionIndex = partitionIndex;
        this.leaderID = leaderID;
        this.leaderEpoch = leaderEpoch;
        this.replicaNodeArray = replicaNodeArray;
        this.isrNodeArray = isrNodeArray;
        this.eligibleLeaderReplicas = eligibleLeaderReplicas;
        this.lastKnownELR = lastKnownELR;
        this.offlineReplicas = offlineReplicas;
        this.tagBuffer = tagBuffer;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public int getPartitionIndex() {
        return partitionIndex;
    }

    public int getLeaderID() {
        return leaderID;
    }

    public int getLeaderEpoch() {
        return leaderEpoch;
    }

    public CompactArray<ReplicaNode> getReplicaNodeArray() {
        return replicaNodeArray;
    }

    public CompactArray<ReplicaNode> getIsrNodeArray() {
        return isrNodeArray;
    }

    public CompactArray<ReplicaNode> getEligibleLeaderReplicas() {
        return eligibleLeaderReplicas;
    }

    public CompactArray<ReplicaNode> getLastKnownELR() {
        return lastKnownELR;
    }

    public CompactArray<ReplicaNode> getOfflineReplicas() {
        return offlineReplicas;
    }

    public TagBuffer getTagBuffer() {
        return tagBuffer;
    }

    @Override
    public byte[] toBytes(Partition part) {
        return StreamUtils.toBytes(dos -> {
            dos.writeShort(part.getErrorCode());
            dos.writeInt(part.getPartitionIndex());
            dos.writeInt(part.getLeaderID());
            dos.writeInt(part.getLeaderEpoch());
            dos.write(part.getReplicaNodeArray().toBytes());
            dos.write(part.getIsrNodeArray().toBytes());
            dos.write(part.getEligibleLeaderReplicas().toBytes());
            dos.write(part.getLastKnownELR().toBytes());
            dos.write(part.getOfflineReplicas().toBytes());
            dos.write(part.getTagBuffer().toBytes());
        });
    }

    @Override
    public Partition fromByteBuffer(ByteBuffer data) {
        ReplicaNode dummy = new ReplicaNode(-1);
        this.errorCode = data.getShort();
        this.partitionIndex = data.getInt();
        this.leaderID = data.getInt();
        this.leaderEpoch = data.getInt();
        //FIXME: terrible hacks!
        this.replicaNodeArray = CompactArray.fromByteBuffer(data, dummy);
        this.isrNodeArray = CompactArray.fromByteBuffer(data, dummy);
        this.eligibleLeaderReplicas = CompactArray.fromByteBuffer(data, dummy);
        this.lastKnownELR = CompactArray.fromByteBuffer(data, dummy);
        this.offlineReplicas = CompactArray.fromByteBuffer(data, dummy);
        this.tagBuffer = TagBuffer.fromByteBuffer(data);
        return this;
    }
}

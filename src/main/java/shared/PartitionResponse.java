package shared;

import log.Record;

public class PartitionResponse {
    private int partitionIndex;
    private short errorCode;
    private long highWatermark;
    private long lastStableOffset;
    private long logStartOffset;
    private CompactArray<AbortedTransaction> abortedTransactions;
    private int preferredReadReplica;
    private CompactArray<Record> records;
    private TagBuffer tg;

    public PartitionResponse() {
    }

    public PartitionResponse(int partitionIndex, short errorCode, long highWatermark, long lastStableOffset, long logStartOffset, CompactArray<AbortedTransaction> abortedTransactions, int preferredReadReplica, CompactArray<Record> records, TagBuffer tg) {
        this.partitionIndex = partitionIndex;
        this.errorCode = errorCode;
        this.highWatermark = highWatermark;
        this.lastStableOffset = lastStableOffset;
        this.logStartOffset = logStartOffset;
        this.abortedTransactions = abortedTransactions;
        this.preferredReadReplica = preferredReadReplica;
        this.records = records;
        this.tg = tg;
    }

    public int getPartitionIndex() {
        return partitionIndex;
    }

    public void setPartitionIndex(int partitionIndex) {
        this.partitionIndex = partitionIndex;
    }

    public short getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public long getHighWatermark() {
        return highWatermark;
    }

    public void setHighWatermark(long highWatermark) {
        this.highWatermark = highWatermark;
    }

    public long getLastStableOffset() {
        return lastStableOffset;
    }

    public void setLastStableOffset(long lastStableOffset) {
        this.lastStableOffset = lastStableOffset;
    }

    public long getLogStartOffset() {
        return logStartOffset;
    }

    public void setLogStartOffset(long logStartOffset) {
        this.logStartOffset = logStartOffset;
    }

    public CompactArray<AbortedTransaction> getAbortedTransactions() {
        return abortedTransactions;
    }

    public void setAbortedTransactions(CompactArray<AbortedTransaction> abortedTransactions) {
        this.abortedTransactions = abortedTransactions;
    }

    public int getPreferredReadReplica() {
        return preferredReadReplica;
    }

    public void setPreferredReadReplica(int preferredReadReplica) {
        this.preferredReadReplica = preferredReadReplica;
    }

    public CompactArray<Record> getRecords() {
        return records;
    }

    public void setRecords(CompactArray<Record> records) {
        this.records = records;
    }

    public TagBuffer getTg() {
        return tg;
    }

    public void setTg(TagBuffer tg) {
        this.tg = tg;
    }
}
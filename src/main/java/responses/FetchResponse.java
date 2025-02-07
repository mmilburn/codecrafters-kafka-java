package responses;

import log.Record;
import log.RecordBatch;
import log.TopicRecord;
import requests.FetchRequest;
import requests.Request;
import shared.*;
import shared.serializer.PartitionResponseSerializer;
import shared.serializer.TopicResponseSerializer;
import util.StreamUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FetchResponse extends ResponseBody {
    private final int throttleTimeMs;
    private final short errorCode;
    private final int sessionId;
    private final CompactArray<TopicResponse> responses;
    private final TagBuffer tg;

    private FetchResponse(int throttleTimeMs, short errorCode, int sessionId, CompactArray<TopicResponse> responses, TagBuffer tg) {
        this.throttleTimeMs = throttleTimeMs;
        this.errorCode = errorCode;
        this.sessionId = sessionId;
        this.responses = responses;
        this.tg = tg;
    }

    protected static FetchResponse fromRequest(Request<?> req, List<RecordBatch> batches) {
        FetchRequest fetchReq = (FetchRequest) req.body();
        List<UUID> requestedIds = fetchReq.getTopics().getElements().stream()
                .map(RequestTopicElement::getTopicID).toList();

        if (requestedIds.isEmpty()) {
            return new FetchResponse(
                    0,
                    (short) 0,
                    0,
                    CompactArray.empty(new TopicResponseSerializer()),
                    new TagBuffer()
            );
        }

        Map<UUID, CompactString> topicMap = batches.stream()
                .flatMap(batch -> batch.getRecords().stream())
                .map(Record::getValue)
                .filter(valueRecord -> valueRecord instanceof TopicRecord)
                .map(valueRecord -> (TopicRecord) valueRecord)
                .collect(Collectors.toMap(TopicRecord::getTopicUUID, TopicRecord::getName));

/*        Map<UUID, Integer> partitionMap = batches.stream()
                .flatMap(batch -> batch.getRecords().stream())
                .map(Record::getValue)
                .filter(valueRecord -> valueRecord instanceof PartitionRecord)
                .map(valueRecord -> (PartitionRecord) valueRecord)
                .collect(Collectors.toMap(PartitionRecord::getTopicUUID, PartitionRecord::getPartitionID));
        System.err.println(partitionMap);*/

        List<TopicResponse> topicResponses = new ArrayList<>();
        for (UUID uuid : requestedIds) {
            if (topicMap.containsKey(uuid)) {
/*                if (partitionMap.containsKey(uuid)) {
                    //TODO
                    topicResponses.add(new TopicResponse());
                } else {
                    System.err.println("Topic UUID: " + uuid + " has no partitions!");
                }*/
            } else {
                //Topic is unknown to us.
                System.err.println("Unknown topic id: " + uuid);
                topicResponses.add(
                        new TopicResponse(
                                uuid,
                                CompactArray.withElements(List.of(PartitionResponse.UnknownTopicPartitionResponse()),
                                        new PartitionResponseSerializer()
                                ),
                                new TagBuffer()
                        ));
            }
        }
        return new FetchResponse(
                0,
                (short) 0,
                0,
                CompactArray.withElements(topicResponses, new TopicResponseSerializer()),
                new TagBuffer()
        );
    }

    @Override
    public ResponseBody fromBytebuffer(ByteBuffer data) {
        return new FetchResponse(
                data.getInt(),
                data.getShort(),
                data.getInt(),
                CompactArray.fromByteBuffer(data, new TopicResponseSerializer()),
                TagBuffer.fromByteBuffer(data)
        );
    }

    @Override
    public byte[] toBytes() {
        return StreamUtils.toBytes(dos -> {
            dos.writeInt(this.throttleTimeMs);
            dos.writeShort(this.errorCode);
            dos.writeInt(this.sessionId);
            dos.write(this.responses.toBytes());
            dos.write(this.tg.toBytes());
        });
    }
}

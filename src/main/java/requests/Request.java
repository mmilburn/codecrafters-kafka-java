package requests;

import java.nio.ByteBuffer;

public record Request<T>(RequestHeader header, T body) {

    public static Request<?> fromByteBuffer(ByteBuffer data) {
        RequestHeader requestHeader = RequestHeader.fromByteBuffer(data);
        switch (requestHeader.getRequestAPIKey()) {
            case 18 -> {
                return new Request<>(requestHeader, new APIVersionsRequest().fromByteBuffer(data));
            }
            case 75 -> {
                return new Request<>(requestHeader, new DescribeTopicPartitionsRequest().fromByteBuffer(data));
            }
            default -> {
                System.err.println("Unexpected value: " + requestHeader.getRequestAPIKey());
                return new Request<>(requestHeader, null);
            }
        }
    }
}

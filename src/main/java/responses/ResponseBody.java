package responses;

import requests.Request;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class ResponseBody {
    public abstract ResponseBody fromBytebuffer(ByteBuffer data);
    public abstract byte[] toBytes();
}

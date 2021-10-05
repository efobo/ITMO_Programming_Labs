package serialize;

import Communication.Request;
import Communication.RequestFile;
import Communication.Response;
import Communication.ResponseFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class Serialize {

    public Serialize () {}

    public static byte [] serializeLong (Long length) {
        ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        buf.putLong(length);
        return buf.array();
    }

    public static byte [] serializeRequestFile (RequestFile requestFile) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(buf);
        objectOutputStream.writeObject(requestFile);
        objectOutputStream.flush();
        return buf.toByteArray();
    }

    public static byte[] serializeRequest (Request request) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(buf);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        return buf.toByteArray();
    }

    public static byte[] serializeResponseFile (ResponseFile responseFile) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(buf);
        objectOutputStream.writeObject(responseFile);
        return buf.toByteArray();
    }

    public static byte[] serializeResponse(Response response) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(buf);
        objectOutputStream.writeObject(response);
        return buf.toByteArray();
    }
}

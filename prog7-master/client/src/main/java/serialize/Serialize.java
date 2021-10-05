package serialize;

import Communication.Request;
import Communication.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class Serialize {

    public Serialize () {}

    public static byte [] serializeInteger (Integer length) {
        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);
        buf.putInt(length);
        return buf.array();
    }


    public static byte[] serializeRequest (Request request) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(buf);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        return buf.toByteArray();
    }


    public static byte[] serializeResponse(Response response) throws IOException {
        System.out.println(response.toString());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(buf);
        objectOutputStream.writeObject(response);
        return buf.toByteArray();
    }
}

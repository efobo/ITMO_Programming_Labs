package serialize;

import Communication.Request;
import Communication.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class Deserialize {

    public Deserialize () {}

    public static Integer deserializeInteger (byte[] buf) {
        ByteBuffer bb = ByteBuffer.allocate(buf.length);
        bb.put(buf);
        bb.flip();
        return bb.getInt();
    }


    public static Request deserializeRequest (byte[] buf) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        return (Request) ois.readObject();
    }


    public static Response deserializeResponse(byte[] buf) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        return (Response) ois.readObject();
    }

}

package serialize;

import Communication.Request;
import Communication.RequestFile;
import Communication.Response;
import Communication.ResponseFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class Deserialize {

    public Deserialize () {}

    public static Long deserializeLong (byte[] buf) {
        ByteBuffer bb = ByteBuffer.allocate(buf.length);
        bb.put(buf);
        bb.flip();
        return bb.getLong();
    }

    public static RequestFile deserializeRequestFile (byte[] buf) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        return (RequestFile) ois.readObject();
    }

    public static Request deserializeRequest (byte[] buf) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        return (Request) ois.readObject();
    }

    public static ResponseFile deserializeResponseFile (byte[] buf) throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        return (ResponseFile) ois.readObject();
    }

    public static Response deserializeResponse(byte[] buf) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf));
        return (Response) ois.readObject();
    }

}

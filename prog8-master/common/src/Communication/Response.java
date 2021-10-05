package Communication;

import data.Labwork;

import java.io.Serializable;
import java.util.TreeSet;

public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    private String[] responseBodyArgs;
    private TreeSet<Labwork> labworks;


    public Response(ResponseCode responseCode, String responseBody, String[] responseBodyArgs, TreeSet<Labwork> labworks) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseBodyArgs = responseBodyArgs;
        this.labworks = labworks;
    }

    /**
     * @return responseCode.
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @return responseBody
     */
    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }

    public String[] getResponseBodyArgs() {
        return responseBodyArgs;
    }

    public void setResponseBodyArgs(String[] responseBodyArgs) {
        this.responseBodyArgs = responseBodyArgs;
    }

    public TreeSet<Labwork> getLabworks() {
        return labworks;
    }

    public void setLabworks(TreeSet<Labwork> labworks) {
        this.labworks = labworks;
    }
}


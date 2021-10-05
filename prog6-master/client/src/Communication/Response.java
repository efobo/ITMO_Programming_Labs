package Communication;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
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
}


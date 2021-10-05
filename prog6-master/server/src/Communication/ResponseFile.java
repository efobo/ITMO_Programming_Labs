package Communication;

import java.io.Serializable;

public class ResponseFile implements Serializable {
    private boolean fileStatus;
    private String responseBody;

    public ResponseFile (boolean fileStatus, String responseBody) {
        this.fileStatus = fileStatus;
        this.responseBody = responseBody;
    }

    /**
     * @return fileStatus
     */
    public boolean getFileStatus () {
        return fileStatus;
    }

    /**
     * @return responseBody
     */
    public String getResponseBody () {
        return responseBody;
    }

    @Override
    public String toString () {return "ResponseFile[ fileStatus = " + fileStatus + ", " + responseBody + "]";}

}

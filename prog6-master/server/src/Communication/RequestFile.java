package Communication;

import java.io.File;
import java.io.Serializable;

public class RequestFile implements Serializable {
    private File file;
    private Long fileLength;

    public RequestFile (File file, Long fileLength) {

        this.file = file;
        this.fileLength = fileLength;
    }

    /**
     * @return file
     */
    public File getFile () {return file;}

    /**
     * @param file
     */
    public void setFile (File file) {this.file = file;}

    /**
     * @return fileLength
     */
    public Long getFileLength () {return fileLength;}

    /**
     * @param fileLength
     */
    public void setFileLength (long fileLength) {this.fileLength = fileLength;}
}

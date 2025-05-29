package knu.oceanbackend.external;

import org.springframework.core.io.ByteArrayResource;

public class MultipartInputStreamFileResource extends ByteArrayResource {

    private final String filename;

    public MultipartInputStreamFileResource(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return this.getByteArray().length;
    }
}

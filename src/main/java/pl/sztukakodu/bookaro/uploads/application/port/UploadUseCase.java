package pl.sztukakodu.bookaro.uploads.application.port;

import lombok.Value;
import pl.sztukakodu.bookaro.uploads.domain.Upload;

public interface UploadUseCase {
    Upload save(SaveUploadCommand command);

    @Value
    class SaveUploadCommand {
        String filename;
        byte[] file;
        String contentType;

    }
}

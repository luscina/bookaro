package pl.sztukakodu.bookaro.uploads.application;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.uploads.application.port.UploadUseCase;
import pl.sztukakodu.bookaro.uploads.domain.Upload;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UploadService implements UploadUseCase {
    private final Map<String, Upload> storage = new ConcurrentHashMap<>();
    @Override
    public Upload save(SaveUploadCommand command) {
        String newId = RandomStringUtils.randomAlphabetic(8);
        Upload upload = new Upload(
                newId,
                command.getFile(),
                command.getContentType(),
                command.getFilename(),
                LocalDateTime.now()
        );
        storage.put(upload.getId(), upload);
        System.out.println("Upload saved : " + upload.getFilename() + " with id: " + newId);
        return upload;
    }
}
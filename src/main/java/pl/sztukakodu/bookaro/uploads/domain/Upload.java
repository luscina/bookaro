package pl.sztukakodu.bookaro.uploads.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Upload {
    @Id
    @GeneratedValue
    Long id;
    byte[] file;
    String contentType;
    String filename;
    @CreatedDate
    LocalDateTime createdAt;

    public Upload(String filename, byte[] file, String contentType){
        this.filename = filename;
        this.file = file;
        this.contentType = contentType;
    }
}

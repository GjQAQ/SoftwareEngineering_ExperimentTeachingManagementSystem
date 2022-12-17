package team.segroup.etms.filesys.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MaterialMeta {
    @Id
    private String id;
    private String name;
    private String description;
    private String creationTime;
    private String lastModificationTime;
    private String creatorNid;
}

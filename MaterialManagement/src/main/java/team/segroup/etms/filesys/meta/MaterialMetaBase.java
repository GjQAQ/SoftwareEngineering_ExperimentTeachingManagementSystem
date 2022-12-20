package team.segroup.etms.filesys.meta;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import team.segroup.etms.utils.DateTimeStringSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MaterialMetaBase {
    @Id
    private String id;
    private String name;
    private String description;
    @JsonSerialize(using = DateTimeStringSerializer.class)
    private String creationTime;
    @JsonSerialize(using = DateTimeStringSerializer.class)
    private String lastModificationTime;
    private String creatorNid;
}

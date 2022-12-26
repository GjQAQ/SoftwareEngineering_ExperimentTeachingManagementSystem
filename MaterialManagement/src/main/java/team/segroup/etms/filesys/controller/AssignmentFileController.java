package team.segroup.etms.filesys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.segroup.etms.filesys.meta.AssignmentFileMeta;
import team.segroup.etms.filesys.service.AssignmentFileService;
import team.segroup.etms.filesys.service.impl.MaterialServiceBase;

import static team.segroup.etms.utils.ControllerUtils.*;

import java.util.List;

@RestController
@RequestMapping("/files/assignments")
@Slf4j
public class AssignmentFileController extends FileControllerBase<AssignmentFileMeta> {
    @PostMapping
    public ResponseEntity<AssignmentFileMeta> uploadMaterial(
        @RequestPart("file") MultipartFile file,
        @RequestParam("description") String description,
        @RequestParam("creatorNid") String creatorNid,
        @RequestParam("asid") int asid
    ) {
        AssignmentFileMeta meta = new AssignmentFileMeta();
        meta.setAsid(asid);
        return uploadMaterialBase(meta, file, description, creatorNid);
    }

    @GetMapping(params = "asid")
    public ResponseEntity<List<AssignmentFileMeta>> findByAsid(
        @RequestParam("asid") int asid
    ) {
        AssignmentFileService service = (AssignmentFileService) materialService;
        List<AssignmentFileMeta> fileMetas = service.findByAsid(asid);
        return defaultNotFound(fileMetas.size() > 0, fileMetas);
    }
}

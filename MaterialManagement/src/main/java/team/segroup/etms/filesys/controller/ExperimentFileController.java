package team.segroup.etms.filesys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.segroup.etms.filesys.meta.ExperimentFileMeta;
import team.segroup.etms.filesys.service.ExperimentFileService;

import java.util.List;

import static team.segroup.etms.utils.ControllerUtils.defaultNotFound;

@RestController
@RequestMapping("/files/exps")
@Slf4j
public class ExperimentFileController extends FileControllerBase<ExperimentFileMeta> {
    @PostMapping
    public ResponseEntity<ExperimentFileMeta> uploadMaterial(
        @RequestPart("file") MultipartFile file,
        @RequestParam("description") String description,
        @RequestParam("creatorNid") String creatorNid,
        @RequestParam("eid") int eid
    ) {
        ExperimentFileMeta meta = new ExperimentFileMeta();
        meta.setEid(eid);
        return uploadMaterialBase(meta, file, description, creatorNid);
    }

    @GetMapping(params = "eid")
    public ResponseEntity<List<ExperimentFileMeta>> findByEid(
        @RequestParam("eid") int eid
    ) {
        ExperimentFileService service = (ExperimentFileService) materialService;
        List<ExperimentFileMeta> fileMetas = service.findByEid(eid);
        return defaultNotFound(fileMetas.size() > 0, fileMetas);
    }
}

package team.segroup.etms.filesys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.segroup.etms.filesys.meta.SubmissionFileMeta;
import team.segroup.etms.filesys.service.SubmissionFileService;

import java.util.List;

import static team.segroup.etms.utils.ControllerUtils.defaultNotFound;

@RestController
@RequestMapping("/files/submissions")
@Slf4j
public class SubmissionFileController extends FileControllerBase<SubmissionFileMeta> {
    @PostMapping
    public ResponseEntity<SubmissionFileMeta> uploadMaterial(
        @RequestPart("file") MultipartFile file,
        @RequestParam("description") String description,
        @RequestParam("creatorNid") String creatorNid,
        @RequestParam("asid") int asid,
        @RequestParam("nid") String nid
    ) {
        SubmissionFileMeta meta = new SubmissionFileMeta();
        meta.setAsid(asid);
        meta.setNid(nid);
        return uploadMaterialBase(meta, file, description, creatorNid);
    }

    @GetMapping(params = {"asid", "nid"})
    public ResponseEntity<List<SubmissionFileMeta>> findByAsidAndNid(
        @RequestParam("asid") int asid,
        @RequestParam("nid") String nid
    ) {
        SubmissionFileService service = (SubmissionFileService) materialService;
        List<SubmissionFileMeta> fileMetas = service.findByAsidAndNid(asid, nid);
        return defaultNotFound(fileMetas.size() > 0, fileMetas);
    }
}

package team.segroup.etms.filesys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.segroup.etms.filesys.meta.AssignmentFileMeta;
import team.segroup.etms.filesys.meta.MaterialMetaBase;
import team.segroup.etms.filesys.service.MaterialService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static team.segroup.etms.utils.ControllerUtils.defaultNotFound;

@Slf4j
public abstract class FileControllerBase<M extends MaterialMetaBase> {
    protected MaterialService<M> materialService;

    public ResponseEntity<M> uploadMaterialBase(
        M materialMeta,
        MultipartFile file,
        String description,
        String creatorNid
    ) {
        materialMeta.setName(file.getOriginalFilename());
        materialMeta.setDescription(description);
        materialMeta.setCreatorNid(creatorNid);

        try {
            M meta = materialService.upload(materialMeta, file.getInputStream());
            return ResponseEntity.ok(meta);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/meta")
    public ResponseEntity<M> findMeta(
        @PathVariable("id") String id
    ) {
        M meta = materialService.retrieveMeta(id);
        return defaultNotFound(meta != null, meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> download(
        @PathVariable("id") String id
    ) {
        InputStream inputStream = materialService.download(id);
        if (inputStream == null) {
            return defaultNotFound(false, null);
        } else {
            M meta = materialService.retrieveMeta(id);
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition",
                    "attachment;filename=" + encode(meta.getName()))
                .body(new InputStreamResource(inputStream));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<M> modify(
        @PathVariable("id") String id,
        @RequestPart(value = "file", required = false) MultipartFile file,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "creatorNid", required = false) String creatorNid
    ) {
        M meta = materialService.retrieveMeta(id);
        if (meta == null) {
            return defaultNotFound(false, null);
        }
        InputStream inputStream = null;
        if (file != null) {
            try {
                inputStream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        if (name != null) {
            meta.setName(name);
        }
        if (description != null) {
            meta.setDescription(description);
        }
        if (creatorNid != null) {
            meta.setCreatorNid(creatorNid);
        }
        M newMeta = materialService.modify(meta, inputStream);
        return defaultNotFound(newMeta != null, newMeta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @PathVariable("id") String id
    ) {
        boolean result = materialService.delete(id);
        return defaultNotFound(result, "ok");
    }

    private String encode(String filename) {
        try {
            return "\"" + URLEncoder.encode(filename, "utf-8") + "\"";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return filename;
        }
    }

    @Autowired
    public void setMaterialService(MaterialService<M> materialService) {
        this.materialService = materialService;
    }
}

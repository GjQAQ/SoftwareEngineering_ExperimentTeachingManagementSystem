package team.segroup.etms.expsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.expsys.dto.ExperimentDto;
import team.segroup.etms.expsys.service.ExperimentService;

import java.util.List;

import static team.segroup.etms.utils.ControllerUtils.*;

@RestController
@RequestMapping("/experiment")
public class ExperimentController {
    private ExperimentService experimentService;

    @GetMapping("/{name}")
    public ResponseEntity<ExperimentDto> findOne(@PathVariable("name") String name) {
        ExperimentDto experimentDto = experimentService.findByName(name);
        return defaultResponse(
            experimentDto != null,
            experimentDto,
            ResponseEntity.status(HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping(params = "course")
    public ResponseEntity<List<ExperimentDto>> findMany(
        @RequestParam("course") String courseCode
    ) {
        List<ExperimentDto> exps = experimentService.findByCourseCode(courseCode);
        return defaultResponse(!exps.isEmpty(), exps, ResponseEntity.status(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ExperimentDto> create(@RequestBody ExperimentDto experimentDto) {
        ExperimentDto experiment = experimentService.create(experimentDto);
        return defaultBadRequest(experiment != null, experiment);
    }

    @PatchMapping
    public ResponseEntity<ExperimentDto> modify(@RequestBody ExperimentDto experimentDto) {
        ExperimentDto experiment = experimentService.modify(experimentDto);
        return defaultBadRequest(experiment != null, experiment);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> delete(@PathVariable("name") String name) {
        boolean result = experimentService.deleteByName(name);
        return defaultBadRequest(result, "ok");
    }

    @Autowired
    public void setExperimentService(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }
}

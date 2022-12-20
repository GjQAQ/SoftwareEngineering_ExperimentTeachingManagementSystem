package team.segroup.etms.expsys.service;

import team.segroup.etms.expsys.dto.ExperimentDto;

import java.util.List;

public interface ExperimentService {
    ExperimentDto create(ExperimentDto experimentDto);

    boolean deleteByName(String name);

    boolean deleteByEid(int eid);

    ExperimentDto modify(ExperimentDto experimentDto);

    ExperimentDto findByName(String name);

    ExperimentDto findByEid(int eid);

    List<ExperimentDto> findByCourseCode(String courseCode);
}

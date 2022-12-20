package team.segroup.etms.expsys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.segroup.etms.expsys.dto.ExperimentDto;
import team.segroup.etms.expsys.entity.Experiment;
import team.segroup.etms.expsys.repository.ExperimentRepository;
import team.segroup.etms.expsys.service.ExperimentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExperimentServiceImpl implements ExperimentService {
    private ExperimentRepository experimentRepository;

    @Transactional
    @Override
    public ExperimentDto create(ExperimentDto experimentDto) {
        if (experimentDto.getEid() != null &&
            experimentRepository.findByEid(experimentDto.getEid()).isPresent()
        ) {
            return null;
        }
        return new ExperimentDto(experimentRepository.save(experimentDto.toExperiment()));
    }

    @Transactional
    @Override
    public boolean deleteByName(String name) {
        Optional<Experiment> expOpt = experimentRepository.findByName(name);
        if (!expOpt.isPresent()) {
            return false;
        }

        experimentRepository.deleteById(expOpt.get().getEid());
        return true;
    }

    @Transactional
    @Override
    public boolean deleteByEid(int eid) {
        Optional<Experiment> expOpt = experimentRepository.findByEid(eid);
        if (!expOpt.isPresent()) {
            return false;
        }
        experimentRepository.deleteById(eid);
        return true;
    }

    @Transactional
    @Override
    public ExperimentDto modify(ExperimentDto experimentDto) {
        if (experimentDto == null || experimentDto.getEid() == null) {
            return null;
        }
        if (!experimentRepository.findByEid(experimentDto.getEid()).isPresent()) {
            return null;
        }
        return new ExperimentDto(experimentRepository.save(experimentDto.toExperiment()));
    }

    @Transactional
    @Override
    public ExperimentDto findByName(String name) {
        return experimentRepository.findByName(name).map(ExperimentDto::new).orElse(null);
    }

    @Transactional
    @Override
    public ExperimentDto findByEid(int eid) {
        return experimentRepository.findByEid(eid)
            .map(ExperimentDto::new)
            .orElse(null);
    }

    @Transactional
    @Override
    public List<ExperimentDto> findByCourseCode(String courseCode) {
        return experimentRepository.findByCode(courseCode)
            .map(ExperimentDto::new)
            .collect(Collectors.toList());
    }

    @Autowired
    public void setExperimentRepository(ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }
}

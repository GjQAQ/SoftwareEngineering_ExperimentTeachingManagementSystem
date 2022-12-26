package team.segroup.etms.filesys.service;

import team.segroup.etms.filesys.meta.ExperimentFileMeta;

import java.util.List;

public interface ExperimentFileService extends MaterialService<ExperimentFileMeta> {
    List<ExperimentFileMeta> findByEid(int eid);
}

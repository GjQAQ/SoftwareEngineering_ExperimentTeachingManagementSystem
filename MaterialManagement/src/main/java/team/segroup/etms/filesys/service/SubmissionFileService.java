package team.segroup.etms.filesys.service;

import team.segroup.etms.filesys.meta.SubmissionFileMeta;

import java.util.List;

public interface SubmissionFileService extends MaterialService<SubmissionFileMeta> {
    List<SubmissionFileMeta> findByAsidAndNid(int asid, String nid);
}

package team.segroup.etms.filesys.service;

import team.segroup.etms.filesys.meta.AssignmentFileMeta;

import java.util.List;

public interface AssignmentFileService extends MaterialService{
    List<AssignmentFileMeta> findByAsid(int asid);
}

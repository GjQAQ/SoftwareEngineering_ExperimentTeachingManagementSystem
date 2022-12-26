package team.segroup.etms.filesys.service.impl;

import com.mongodb.client.gridfs.GridFSFindIterable;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import team.segroup.etms.filesys.meta.AssignmentFileMeta;
import team.segroup.etms.filesys.service.AssignmentFileService;

import java.util.List;

@Service
@Slf4j
public class AssignmentFileServiceImpl
    extends MaterialServiceBase<AssignmentFileMeta>
    implements AssignmentFileService {

    @Override
    public List<AssignmentFileMeta> findByAsid(int asid) {
        GridFSFindIterable files = gridFsTemplate.find(Query.query(
            Criteria.where("metadata.asid").is(asid)
        ));
        return gridFsIterable2List(files);
    }

    @Override
    protected AssignmentFileMeta fromDocumentMeta(Document document) {
        AssignmentFileMeta meta = new AssignmentFileMeta();
        setBaseFields(meta, document);
        meta.setAsid(document.getInteger("asid"));
        return meta;
    }
}

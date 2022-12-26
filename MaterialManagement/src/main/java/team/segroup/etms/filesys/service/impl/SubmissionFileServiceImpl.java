package team.segroup.etms.filesys.service.impl;

import com.mongodb.client.gridfs.GridFSFindIterable;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import team.segroup.etms.filesys.meta.SubmissionFileMeta;
import team.segroup.etms.filesys.service.SubmissionFileService;

import java.util.List;

@Service
@Slf4j
public class SubmissionFileServiceImpl
    extends MaterialServiceBase<SubmissionFileMeta>
    implements SubmissionFileService {
    @Override
    public List<SubmissionFileMeta> findByAsidAndNid(int asid, String nid) {
        GridFSFindIterable files = gridFsTemplate.find(Query.query(
            Criteria.where("metadata.asid").is(asid)
                .and("metadata.nid").is(nid)
        ));
        return gridFsIterable2List(files);
    }

    @Override
    protected SubmissionFileMeta fromDocumentMeta(Document document) {
        SubmissionFileMeta meta = new SubmissionFileMeta();
        setBaseFields(meta, document);
        meta.setAsid(document.getInteger("asid"));
        meta.setNid(document.getString("nid"));
        return meta;
    }
}

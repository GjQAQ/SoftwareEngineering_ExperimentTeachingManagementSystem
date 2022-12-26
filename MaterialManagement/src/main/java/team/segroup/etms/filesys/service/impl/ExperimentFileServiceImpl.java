package team.segroup.etms.filesys.service.impl;

import com.mongodb.client.gridfs.GridFSFindIterable;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import team.segroup.etms.filesys.meta.ExperimentFileMeta;
import team.segroup.etms.filesys.service.ExperimentFileService;

import java.util.List;

@Service
@Slf4j
public class ExperimentFileServiceImpl
    extends MaterialServiceBase<ExperimentFileMeta>
    implements ExperimentFileService {
    @Override
    public List<ExperimentFileMeta> findByEid(int eid) {
        GridFSFindIterable files = gridFsTemplate.find(Query.query(
            Criteria.where("metadata.eid").is(eid)
        ));
        return gridFsIterable2List(files);
    }

    @Override
    protected ExperimentFileMeta fromDocumentMeta(Document document) {
        ExperimentFileMeta meta = new ExperimentFileMeta();
        setBaseFields(meta, document);
        meta.setEid(document.getInteger("eid"));
        return meta;
    }
}

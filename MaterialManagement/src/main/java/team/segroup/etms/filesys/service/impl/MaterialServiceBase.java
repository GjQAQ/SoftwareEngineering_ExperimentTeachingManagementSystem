package team.segroup.etms.filesys.service.impl;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import team.segroup.etms.filesys.meta.MaterialMetaBase;
import team.segroup.etms.filesys.service.MaterialService;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public abstract class MaterialServiceBase<M extends MaterialMetaBase> implements MaterialService<M> {
    protected GridFsTemplate gridFsTemplate;
    protected GridFSBucket gridFSBucket;

    @Override
    public M upload(M meta, InputStream inputStream) {
        meta.setCreationTime(LocalDateTime.now().toString());
        meta.setLastModificationTime(LocalDateTime.now().toString());

        ObjectId id = gridFsTemplate.store(
            inputStream, meta.getName(), meta
        );
        meta.setId(id.toString());
        return meta;
    }

    @Override
    public M retrieveMeta(String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(
            Criteria.where("_id").is(id)
        ));
        if (gridFSFile == null) {
            return null;
        }
        Document metadata = gridFSFile.getMetadata();
        M meta = fromDocumentMeta(metadata);
        meta.setId(id);
        return meta;
    }

    @Override
    public InputStream download(String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(
            Criteria.where("_id").is(id)
        ));
        if (gridFSFile == null) {
            return null;
        }

        return gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
    }

    @Override
    public M modify(M meta, InputStream inputStream) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(
            Criteria.where("_id").is(meta.getId())
        ));
        if (gridFSFile == null) {
            return null;
        }

        if (inputStream == null) {
            inputStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        }
        meta.setLastModificationTime(LocalDateTime.now().toString());
        gridFsTemplate.store(inputStream, meta);
        return meta;
    }

    @Override
    public boolean delete(String id) {
        Query query = Query.query(
            Criteria.where("_id").is(id)
        );
        if (gridFsTemplate.findOne(query) == null) {
            return false;
        } else {
            gridFsTemplate.delete(query);
            return true;
        }
    }

    protected abstract M fromDocumentMeta(Document document);

    protected void setBaseFields(MaterialMetaBase meta, Document data) {
        meta.setName(data.getString("name"));
        meta.setDescription(data.getString("description"));
        meta.setCreationTime(data.getString("creationTime"));
        meta.setLastModificationTime(data.getString("lastModificationTime"));
        meta.setCreatorNid(data.getString("creatorNid"));
    }

    protected List<M> gridFsIterable2List(GridFSFindIterable iterable) {
        List<M> list = new ArrayList<>();
        for (GridFSFile file : iterable) {
            M meta = fromDocumentMeta(file.getMetadata());
            meta.setId(file.getObjectId().toString());
            list.add(meta);
        }
        return list;
    }

    @Autowired
    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Autowired
    public void setGridFSBucket(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }
}

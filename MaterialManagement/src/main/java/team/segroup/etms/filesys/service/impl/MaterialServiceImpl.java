package team.segroup.etms.filesys.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
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
import java.util.List;

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {
    private GridFsTemplate gridFsTemplate;
    private GridFSBucket gridFSBucket;

    @Override
    public MaterialMetaBase upload(MaterialMetaBase meta, InputStream inputStream) {
        meta.setCreationTime(LocalDateTime.now().toString());
        meta.setLastModificationTime(LocalDateTime.now().toString());

        ObjectId id = gridFsTemplate.store(
            inputStream, meta.getName(), meta
        );
        meta.setId(id.toString());
        return meta;
    }

    @Override
    public MaterialMetaBase retrieveMeta(String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(
            Criteria.where("_id").is(id)
        ));
        if (gridFSFile == null) {
            return null;
        }
        Document metadata = gridFSFile.getMetadata();
        return new MaterialMetaBase(
            id,
            metadata.getString("name"),
            metadata.getString("description"),
            metadata.getString("creationTime"),
            metadata.getString("lastModificationTime"),
            metadata.getString("creatorNid")
        );
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
    public List<MaterialMetaBase> listAll() {
        //TODO:implement
        throw new NotImplementedException();
    }

    @Override
    public MaterialMetaBase modify(MaterialMetaBase meta, InputStream inputStream) {
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

    @Autowired
    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Autowired
    public void setGridFSBucket(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }
}

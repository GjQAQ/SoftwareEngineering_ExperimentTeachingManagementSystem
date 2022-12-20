package team.segroup.etms.filesys.service;


import team.segroup.etms.filesys.meta.MaterialMetaBase;

import java.io.InputStream;
import java.util.List;

public interface MaterialService {
    MaterialMetaBase upload(MaterialMetaBase meta, InputStream inputStream);

    MaterialMetaBase retrieveMeta(String id);

    InputStream download(String id);

    List<MaterialMetaBase> listAll();

    MaterialMetaBase modify(MaterialMetaBase meta, InputStream inputStream);

    boolean delete(String id);
}

package team.segroup.etms.filesys.service;


import team.segroup.etms.filesys.meta.MaterialMetaBase;

import java.io.InputStream;
import java.util.List;

public interface MaterialService<M extends MaterialMetaBase> {
    M upload(M meta, InputStream inputStream);

    M retrieveMeta(String id);

    InputStream download(String id);

    M modify(M meta, InputStream inputStream);

    boolean delete(String id);
}

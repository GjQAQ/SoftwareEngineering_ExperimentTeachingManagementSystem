package team.segroup.etms.filesys.service;


import java.io.InputStream;
import java.util.List;

public interface MaterialService {
    MaterialMeta upload(MaterialMeta meta, InputStream inputStream);

    MaterialMeta retrieveMeta(String id);

    InputStream download(String id);

    List<MaterialMeta> listAll();

    MaterialMeta modify(MaterialMeta meta, InputStream inputStream);

    boolean delete(String id);
}

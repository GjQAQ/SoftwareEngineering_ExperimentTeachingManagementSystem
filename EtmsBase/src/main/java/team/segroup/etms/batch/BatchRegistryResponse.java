package team.segroup.etms.batch;

import java.util.HashMap;
import java.util.List;

public class BatchRegistryResponse extends HashMap<String, List<String>> {
    public BatchRegistryResponse() {
        super();
    }

    public BatchRegistryResponse(List<String> success, List<String> fail){
        super();
        put("pass", success);
        put("fail", success);
    }
}

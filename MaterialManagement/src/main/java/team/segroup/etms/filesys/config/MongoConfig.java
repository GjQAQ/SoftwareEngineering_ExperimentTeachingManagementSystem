package team.segroup.etms.filesys.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public GridFSBucket getGridFSBucket(
        MongoClient mongoClient,
        @Value("${spring.data.mongodb.database}") String db
    ) {
        MongoDatabase database = mongoClient.getDatabase(db);
        return GridFSBuckets.create(database);
    }

    @Bean
    public GridFSUploadOptions getGridFSUploadOptions() {
        return new GridFSUploadOptions();
    }
}

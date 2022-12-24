package team.segroup.etms.batch;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CsvUtils {
    public static <T> Stream<T> csvSimpleParse(
        MultipartFile csv,
        Function<CSVRecord, T> mapper
    ) {
        Iterable<CSVRecord> records;
        try {
            InputStreamReader reader = new InputStreamReader(csv.getInputStream());
            records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return StreamSupport.stream(records.spliterator(), false)
            .map(mapper);
    }
}

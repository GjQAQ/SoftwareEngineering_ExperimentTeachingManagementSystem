package team.segroup.etms.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    private LocalDate2Timestamp localDate2Timestamp = new LocalDate2Timestamp();

    @Override
    public void serialize(
        LocalDate localDate,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeNumber(localDate2Timestamp.convert(localDate));
    }
}

package team.segroup.etms.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeStringSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(
        String s,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException {
        LocalDateTime dateTime = LocalDateTime.parse(s);
        jsonGenerator.writeNumber(
            dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()
        );
    }
}

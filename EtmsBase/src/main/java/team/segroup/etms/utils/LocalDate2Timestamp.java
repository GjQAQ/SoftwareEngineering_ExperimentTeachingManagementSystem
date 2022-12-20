package team.segroup.etms.utils;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class LocalDate2Timestamp implements Converter<LocalDate, Long> {
    @Override
    public Long convert(LocalDate localDate) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}

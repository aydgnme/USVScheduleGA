package com.dm.data.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Converter(autoApply = true)
public class RobustLocalTimeConverter implements AttributeConverter<LocalTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        // Formats as HH:mm or HH:mm:ss or HH:mm:ss.nano
        return attribute != null ? attribute.toString() : null;
    }

    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            // Try standard ISO time (HH:mm:ss or HH:mm)
            return LocalTime.parse(dbData);
        } catch (DateTimeParseException e) {
            // Handle timestamp format "yyyy-MM-dd HH:mm:ss" (legacy seed data)
            if (dbData.contains(" ")) {
                String[] parts = dbData.split(" ");
                if (parts.length > 1) {
                    try {
                        return LocalTime.parse(parts[1]);
                    } catch (DateTimeParseException ex) {
                        throw new IllegalArgumentException("Could not parse time from timestamp: " + dbData, ex);
                    }
                }
            }
            throw new IllegalArgumentException("Unknown time format: " + dbData, e);
        }
    }
}

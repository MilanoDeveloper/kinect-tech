package com.kinect.contracts.config;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class LocalDateJacksonConfiguration {
    private static final DateTimeFormatter DASHED_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private static final DateTimeFormatter COMPACT_DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMuuuu");

    @Bean
    public JacksonModule localDateModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new ValueSerializer<>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator generator, SerializationContext context)
                    throws JacksonException {
                generator.writeString(DASHED_DATE_FORMAT.format(value));
            }
        });
        module.addDeserializer(LocalDate.class, new ValueDeserializer<>() {
            @Override
            public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
                String value = parser.getString();
                if (value.length() == 10 && value.charAt(2) == '-') {
                    return LocalDate.parse(value, DASHED_DATE_FORMAT);
                }
                if (value.length() == 10 && value.charAt(4) == '-') {
                    return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
                }
                return LocalDate.parse(value, COMPACT_DATE_FORMAT);
            }
        });
        return module;
    }
}

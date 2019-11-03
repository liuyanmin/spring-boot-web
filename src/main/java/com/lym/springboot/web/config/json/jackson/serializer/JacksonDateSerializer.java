package com.lym.springboot.web.config.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lym.springboot.web.util.TimeUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Created by liuyanmin on 2019/10/9.
 */
public class JacksonDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (date != null) {
            String dateString = TimeUtil.formatDate(date, TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE);
            jsonGenerator.writeString(dateString);
        }
    }

}

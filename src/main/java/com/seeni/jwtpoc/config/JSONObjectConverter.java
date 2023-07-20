package com.seeni.jwtpoc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class JSONObjectConverter implements Converter<String, JSONObject> {

    private final ResourceLoader resourceLoader;

    @Override
    public JSONObject convert(String source) {
        try {
            if (ResourceUtils.isUrl(source)) {
                var resource = resourceLoader.getResource(source);
                return new JSONObject(asString(resource));
            } else {
                return new JSONObject(source);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

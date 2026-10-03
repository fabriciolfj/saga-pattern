package com.exemplo.saga.orquestrador.util;

import tools.jackson.databind.json.JsonMapper;

public class JsonMapperUtil {

    private JsonMapperUtil() { }

    public static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
}

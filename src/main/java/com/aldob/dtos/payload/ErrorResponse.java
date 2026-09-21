package com.aldob.dtos.payload;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        String message,
        String traceId,
        OffsetDateTime timestamp,
        String path,
        Map<String, String> errors
) {
}

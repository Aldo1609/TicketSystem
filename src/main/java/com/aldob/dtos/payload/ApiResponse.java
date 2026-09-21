package com.aldob.dtos.payload;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {}

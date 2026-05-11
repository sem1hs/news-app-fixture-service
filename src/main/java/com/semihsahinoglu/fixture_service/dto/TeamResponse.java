package com.semihsahinoglu.fixture_service.dto;

public record TeamResponse(
        Long id,
        Long externalId,
        String name,
        String logoUrl
) {
}

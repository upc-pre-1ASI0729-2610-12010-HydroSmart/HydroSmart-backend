package com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources;

public record LoginResponseResource(String token, UserResource user) {}

package com.hydrosmart.backend.iam.infrastructure.interfaces.rest;

import com.hydrosmart.backend.iam.application.internal.commandservices.UserCommandService;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources.*;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.transform.UserResourceAssembler;
import com.hydrosmart.backend.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final UserCommandService userCommandService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseResource>> login(@Valid @RequestBody LoginRequestResource request) {
        return ResponseEntity.ok(ApiResponse.ok(userCommandService.authenticate(request)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResource>> me(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userCommandService.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(UserResourceAssembler.toResource(user)));
    }
}

package com.smatech.smatrentalpro.backend.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.smatech.smatrentalpro.backend.security.models.ChangePasswordRequest;
import com.smatech.smatrentalpro.backend.user.dto.UserDto;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.smatech.smatrentalpro.backend.utils.Helpers.convertToJson;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@RequestParam("id")Integer id,  @RequestBody @Nullable UserDto userDto) throws JsonProcessingException {
        return ResponseEntity.ok().body(convertToJson(service.update(id,userDto)));
    }
}

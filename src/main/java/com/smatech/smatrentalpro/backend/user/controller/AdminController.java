package com.smatech.smatrentalpro.backend.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.smatech.smatrentalpro.backend.house.dto.response.HouseResponse;
import com.smatech.smatrentalpro.backend.house.model.House;
import com.smatech.smatrentalpro.backend.house.service.HostService;
import com.smatech.smatrentalpro.backend.user.dto.UserDto;
import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smatech.smatrentalpro.backend.utils.Helpers.convertToJson;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private UserService userService;


    private HostService hostService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> findUserById(@PathVariable("id") Integer id) throws JsonProcessingException {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(convertToJson(user));
    }

    @PutMapping("/users/update")
    public ResponseEntity<String> updateUser(@RequestBody @Nullable UserDto userDto) throws JsonProcessingException {
        return ResponseEntity.ok().body(convertToJson(userService.save(userDto)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
    }

    @PostMapping("/users/{id}/approve")
    public ResponseEntity<String> ApproveUser(@PathVariable("id") Integer id) {
        userService.approve(id);

        return ResponseEntity.ok().body("{\"Status\": \"User Approved\"}");
    }

    @GetMapping("/users/unapproved")
    public ResponseEntity<List<UserDto>> findUnapprovedUsers() {
        return ResponseEntity.ok().body(userService.findUnapprovedUsers());
    }

    @GetMapping("/export/home/{id}/details")
    public ResponseEntity<House> findHomeDetails(@PathVariable("id") String id) throws Exception {
        House myHomeDto = hostService.findHomeDtoById(id);
            return ResponseEntity.ok().body(myHomeDto);
    }

    @GetMapping("/export/homes/details")
    public ResponseEntity<List<HouseResponse>> findAllHomesDetails(@RequestParam String format) throws Exception {
        List <HouseResponse> usersHomeList = hostService.findAll();
            return ResponseEntity.ok().body(usersHomeList);
    }
}
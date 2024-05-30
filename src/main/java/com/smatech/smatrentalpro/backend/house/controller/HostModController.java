package com.smatech.smatrentalpro.backend.house.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.smatech.smatrentalpro.backend.house.dto.request.HouseRequest;
import com.smatech.smatrentalpro.backend.house.dto.response.ReservationRes;
import com.smatech.smatrentalpro.backend.house.service.HostService;
import com.smatech.smatrentalpro.backend.house.service.ReservationService;
import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.smatech.smatrentalpro.backend.utils.Helpers.convertToJson;


@RestController
@RequestMapping("/api/v1/host")
//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class HostModController {

    @Autowired
    private HostService hostService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{id}/homes")
    public ResponseEntity<String> getHostHomes(@PathVariable("id") int id)  throws JsonProcessingException {
        User user = userService.findById(id);

        if(user.getApproved()==0||user.getApproved()!=0)
            return ResponseEntity.ok().body("{\"message\": \"Host Isn't approved yet by administrator\"}");
        else
            return ResponseEntity.ok().body(convertToJson(hostService.findByUserId(id)));
    }

    @GetMapping("/bookedHomes/{id}")
    public ResponseEntity<List<ReservationRes>> getUserBookedHomes(@PathVariable("id") int id)  throws JsonProcessingException {

            return ResponseEntity.ok().body(reservationService.findReservationByUserId(id));
    }

    @GetMapping("/{username}/homes")
    public ResponseEntity<String> getHostHomesByUsername(@PathVariable("username") String username)  throws JsonProcessingException {
        User user = userService.findByUsername(username);

        if(user.getApproved()==0||user.getApproved()!=0)
            return ResponseEntity.ok().body("{\"message\": \"Host Isn't approved yet by administrator\"}");
        else
            return ResponseEntity.ok().body(convertToJson(hostService.findByUsername(username)));
    }

    @PostMapping("/home/new")
    public ResponseEntity<String> createHome(@RequestBody HouseRequest myHomePostDto, Principal principal) throws Exception {
        User user = userService.findByUsername(principal.getName());
        if(user.getRole() !=null)
            return ResponseEntity.ok().body(convertToJson(hostService.save(myHomePostDto)));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"Error\"}");
    }

    @PutMapping("/home/update")
    public ResponseEntity<String> updateHome(@RequestBody HouseRequest myHomePostDto, Principal principal) throws Exception {
        User user = userService.findByUsername(principal.getName());
        if(user.getRole() !=null)
            return ResponseEntity.ok().body(convertToJson(hostService.save(myHomePostDto)));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"Error\"}");
    }

    @DeleteMapping("/home/{id}/delete")
    public ResponseEntity<String> simpleUpdate(@PathVariable("id") String id, Principal principal) throws JsonProcessingException {
        User user = userService.findByUsername(principal.getName());
        if(user.getRole() !=null) {
            hostService.deleteById(id);
            return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"User not found\"}");
        }
    }
}
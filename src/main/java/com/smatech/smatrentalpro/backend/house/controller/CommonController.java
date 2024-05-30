package com.smatech.smatrentalpro.backend.house.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.smatech.smatrentalpro.backend.house.dto.request.ReservationReq;
import com.smatech.smatrentalpro.backend.house.dto.response.ReservationRes;
import com.smatech.smatrentalpro.backend.house.service.HostService;
import com.smatech.smatrentalpro.backend.house.service.ReservationService;
import com.smatech.smatrentalpro.backend.user.dto.UserDto;
import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.processor.UserProcessor;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import static com.smatech.smatrentalpro.backend.utils.Helpers.convertToJson;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
public class CommonController {

    private final UserService userService;

    private final HostService hostService;


    private final ReservationService reservationService;



    @GetMapping("/user")
    public ResponseEntity<String> getUser(Principal principal) throws JsonProcessingException {
        User user = userService.findByUsername(principal.getName());
        if(user.getRole() !=null)
            return ResponseEntity.ok().body(convertToJson(userService.findById(user.getId())));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"User not found\"}");
    }


    @GetMapping("/user/{id}/image")
    public void renderImageFromDB(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        User user = userService.findById(id);
        UserDto userDto = UserProcessor.convertToDto(user);
        if(userDto!=null) {
            if(userDto.image() != null) {
                byte[] byteArray = new byte[userDto.image().length];
                int i = 0;

                for (Byte wrappedByte : userDto.image()) {
                    byteArray[i++] = wrappedByte; //auto unboxing
                }
                response.setContentType("image/jpeg");
                InputStream is = new ByteArrayInputStream(byteArray);
                IOUtils.copy(is, response.getOutputStream());
            }
        }
    }

    @PostMapping("/home/book")
    public ResponseEntity<ReservationRes> booking(@RequestBody ReservationReq reservationDto) throws Exception {
        User user = userService.findByUsername(reservationDto.getUserNameBooked());
        if(user.getRole() !=null)
            return ResponseEntity.ok().body(reservationService.save(reservationDto));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/home/{id}")
    public ResponseEntity<String> getBookingById(@RequestParam("id") String id) throws Exception {

            return ResponseEntity.ok().body(convertToJson(reservationService.findReservationDtoById(id)));
    }

    @PostMapping("/home/getAllBookings")
    public ResponseEntity<List<ReservationRes>> getAllBookings() throws Exception {

        return ResponseEntity.ok().body(reservationService.findAll());
    }

    @PutMapping("/home/book")
    public ResponseEntity<String> updateBooking(@RequestBody ReservationReq reservationDto, Principal principal) throws Exception {
        User user = userService.findByUsername(principal.getName());
        if(user.getRole() !=null)
            return ResponseEntity.ok().body(convertToJson(reservationService.save(reservationDto)));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"Error updating booking\"}");
    }

}
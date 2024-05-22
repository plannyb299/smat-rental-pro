package com.smatech.smatrentalpro.backend.user.dto;

import lombok.Data;


public record UserDto (

     Integer id,
     String firstname,
     String lastname,
     String email,
     String password,
     String telephone,
     String username,
     int approved,
     Byte[] image)
{ }

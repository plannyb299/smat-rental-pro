package com.smatech.smatrentalpro.backend.user.processor;


import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.dto.UserDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserProcessor {

    public static UserDto convertToDto(User user){

        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getTelephone(),
                user.getUsername(),
                user.getApproved(),
                user.getImage()
        );
    }

    public static User convert(UserDto userDto){

        if (userDto == null) {
            return null;
        }

        return User.builder()
                .id(userDto.id())
                .firstname(userDto.firstname())
                .lastname(userDto.lastname())
                .email(userDto.email())
                .password(userDto.password())
                .telephone(userDto.telephone())
                .username(userDto.username())
                .approved(userDto.approved())
                .image(userDto.image())
                .build();
    }
}

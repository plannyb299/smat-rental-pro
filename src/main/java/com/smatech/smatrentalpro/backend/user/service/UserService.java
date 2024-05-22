package com.smatech.smatrentalpro.backend.user.service;

import com.smatech.smatrentalpro.backend.security.models.ChangePasswordRequest;
import com.smatech.smatrentalpro.backend.user.repository.UserRepository;
import com.smatech.smatrentalpro.backend.user.dto.UserDto;
import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.processor.UserProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public User findByUsername(String id) {

        Optional<User> user = repository.findByUsername(id);

        if(user.isPresent()){
            return user.get();
        }else {
            return null;
        }
    }

    public User findById(int id) {

        Optional<User> user = repository.findById(id);

        if(user.isPresent()){
            return user.get();
        }else {
            return null;
        }
    }

    public User approve(Integer id) {

        Optional<User> user = repository.findById(id);

        if(user.isPresent()){
           User currentUser = user.get();
           currentUser.setApproved(1);
           User updatedUser = repository.save(currentUser);

           return updatedUser;
        }else {
            return null;
        }
    }


    public User update(Integer id, UserDto userDto) {

        Optional<User> user = repository.findById(id);

        if(user.isPresent()){
            User currentUser = user.get();
            currentUser.setApproved(1);
            User updatedUser = repository.save(currentUser);

            return updatedUser;
        }else {
            return null;
        }
    }
    public void deleteById(Integer id) {
        Optional<User> user = repository.findById(id);

        if(user.isPresent()){
           repository.deleteById(id);
        }
    }

    public List<UserDto> findUnapprovedUsers() {

        List<User> users = repository.findByApproved(0);
        List<UserDto> response = new ArrayList<>();

        for(User user : users){

            UserDto userDto = UserProcessor.convertToDto(user);

            response.add(userDto);
        }
        return response;
    }

    public User save(UserDto userDto) {
        Optional<User> user = repository.findById(userDto.id());
        if(user.isPresent()) {
            User user1 = user.get();
            BeanUtils.copyProperties(userDto, user);

            return repository.save(user1);
        }else return null;
    }

    public List<UserDto> findAll() {

        List<User> userList = repository.findAll();
        List<UserDto> response = new ArrayList<>();
        for (User user: userList){
            UserDto userDto = UserProcessor.convertToDto(user);

            response.add(userDto);
        }

        return response;
    }
}

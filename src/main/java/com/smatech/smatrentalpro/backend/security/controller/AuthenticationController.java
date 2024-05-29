package com.smatech.smatrentalpro.backend.security.controller;

import com.smatech.smatrentalpro.backend.exception.ResourceNotFoundException;
import com.smatech.smatrentalpro.backend.security.models.*;
import com.smatech.smatrentalpro.backend.security.service.AuthenticationService;
import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import com.smatech.smatrentalpro.backend.utils.RandomString;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserService userService;

  @Value("${rental.reset-token.link}")
  private String resetPassUrl;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/confirm-account")
  public ResponseEntity<SmatRentalApiResp> confirm(@RequestBody AccountConfirmation request) {

    if (service.confirmUserAccount(request.getUsername(), request.getCode())) {
      return ResponseEntity.ok(new SmatRentalApiResp("User confirmed successfully"));
    } else
      return ResponseEntity.badRequest().body(new SmatRentalApiResp("Failed to confirm user account"));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  @PostMapping("/reset_password")
  public ResponseEntity<SmatRentalApiResp> processResetPassword(HttpServletRequest request, Model model) {
    String token = request.getParameter("token");
    String password = request.getParameter("password");

    User user = service.getByResetPasswordToken(token);
    model.addAttribute("title", "Reset your password");

    if (user == null) {

      return ResponseEntity.badRequest().body(new SmatRentalApiResp("Token used nolonger valid"));

    } else {
      service.updatePassword(user, password);

      return ResponseEntity.ok(new SmatRentalApiResp("You have successfully changed your password. You can now login"));

    }

  }

  @GetMapping("/reset_password")
  public Object showResetPasswordForm(@Param(value = "token") String token, Model model) {
    User user = service.getByResetPasswordToken(token);
    model.addAttribute("token", token);

    if (user == null) {

      return ResponseEntity.badRequest().body(new SmatRentalApiResp("Invalid Token.."));
    }
    return ResponseEntity.ok(new SmatRentalApiResp("Token Successful"));
  }


  @PostMapping("/forgot_password")
  public ResponseEntity<SmatRentalApiResp> processForgotPassword(HttpServletRequest request, Model model) {
    String email = request.getParameter("email");
    String token = RandomString.generateCode(30);

    try {
      service.updateResetPasswordToken(token, email);
      String resetPasswordLink = resetPassUrl + "?token=" + token;
      service.sendEmail(email, resetPasswordLink);
      return ResponseEntity.ok(new SmatRentalApiResp("We have sent a reset password link to your email. Please check."));

    } catch (ResourceNotFoundException ex) {
      model.addAttribute("error", ex.getMessage());
      return ResponseEntity.badRequest().body(new SmatRentalApiResp("Failed to sent a reset password link"));
    } catch (UnsupportedEncodingException | MessagingException e) {
      model.addAttribute("error", "Error while sending email");
      return ResponseEntity.badRequest().body(new SmatRentalApiResp("Error while sending email"));
    }


  }


}

package com.smatech.smatrentalpro.backend.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smatech.smatrentalpro.backend.exception.ResourceNotFoundException;
import com.smatech.smatrentalpro.backend.exception.ValidationException;
import com.smatech.smatrentalpro.backend.security.models.AuthenticationRequest;
import com.smatech.smatrentalpro.backend.security.models.AuthenticationResponse;
import com.smatech.smatrentalpro.backend.security.models.ConfirmationToken;
import com.smatech.smatrentalpro.backend.security.models.RegisterRequest;
import com.smatech.smatrentalpro.backend.security.repo.ConfirmationTokenRepository;
import com.smatech.smatrentalpro.backend.security.repo.TokenRepository;
import com.smatech.smatrentalpro.backend.security.token.Token;
import com.smatech.smatrentalpro.backend.security.token.TokenType;
import com.smatech.smatrentalpro.backend.user.model.User;
import com.smatech.smatrentalpro.backend.user.repository.UserRepository;
import com.smatech.smatrentalpro.backend.utils.EmailSender;
import com.smatech.smatrentalpro.backend.utils.RandomString;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailSender emailSender;
  private final JavaMailSender mailSender;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
            .telephone(request.getTelephone())
            .approved(0)
        .build();
    var savedUser = repository.save(user);

    // Generate and Save confirmation token
    String token = RandomString.generateCode(6);

    ConfirmationToken confirmToken = new ConfirmationToken();
    confirmToken.setToken(token);
    confirmToken.setExpirationDate(LocalDateTime.now().plusMinutes(30));
    confirmToken.setUser(savedUser);
    confirmationTokenRepository.save(confirmToken);

    String emailText = emailSender.buildConfirmationEmail(user.getFirstname(), user.getUsername(), token);
    emailSender.send(user.getEmail(), "SmatRentalPro Account Verification", emailText);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public boolean confirmUserAccount(String username, String verificationCode) {
    log.debug("User Confirmation Request username: {}, verificationCode: {}", username, verificationCode);

    User user =  repository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));

    ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(verificationCode)
            .orElseThrow(() -> new ResourceNotFoundException("ConfirmationToken", "verification code", verificationCode));

    if (!confirmationToken.getToken().equals(verificationCode)) {
      throw new ValidationException("Verification Failed. Invalid verification code");
    }

    if (user.isVerified() || confirmationToken.getDateConfirmed() != null) {
      throw new ValidationException("Verification code already confirmed.");
    }

    if (confirmationToken.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new ValidationException("Verification Code already expired.");
    }

    log.info("Confirming new User - {}", user);
    user.setVerified(true);
    user.setActive(true);
    repository.save(user);

    confirmationToken.setDateConfirmed(LocalDateTime.now());
    confirmationTokenRepository.save(confirmationToken);
    return true;
  }

  public void updateResetPasswordToken(String token, String email) throws ResourceNotFoundException {
    User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));;
    if (user != null) {
      user.setResetPasswordToken(token);
      repository.save(user);
    } else {
      throw new ResourceNotFoundException("Could not find any %s with the email: ", "user" ,email);
    }
  }

  public User getByResetPasswordToken(String token) {

    return repository.findByResetPasswordToken(token);
  }

  public void updatePassword(User user, String newPassword) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);

    user.setResetPasswordToken(null);
    repository.save(user);
  }

  public void sendEmail(String recipientEmail, String resetPasswordLink)
          throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("panasherutimhu0@gmail.com", "SmatRentalPro Application");
    helper.setTo(recipientEmail);

    String subject = "Here's the link to reset your password";

    String content = "<p>Hello,</p>"
            + "<p>You have requested to reset your password.</p>"
            + "<p>Click the link below to change your password:</p>"
            + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
            + "<br>"
            + "<p>Ignore this email if you do remember your password, "
            + "or you have not made the request.</p>";

    helper.setSubject(subject);

    helper.setText(content, true);

    mailSender.send(message);
  }
}

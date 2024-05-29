package com.smatech.smatrentalpro.backend.user.model;

import com.smatech.smatrentalpro.backend.house.model.House;
import com.smatech.smatrentalpro.backend.security.enums.Role;
import com.smatech.smatrentalpro.backend.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Integer id;
  private String firstname;
  private String lastname;
  @Column(unique = true)
  private String email;
  private String password;
  private String telephone;
  @Column(unique = true)
  private String username;
  private int approved;
  private boolean verified;
  private boolean active;
  @Lob
  private Byte[] image;

  @Column(name = "reset_token")
  private String resetPasswordToken;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<House> myHomeList;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", telephone='" + telephone + '\'' +
            ", username='" + username + '\'' +
            ", approved=" + approved +
            ", verified=" + verified +
            ", active=" + active +
            ", image=" + Arrays.toString(image) +
            ", resetPasswordToken='" + resetPasswordToken + '\'' +
            ", role=" + role +
            ", tokens=" + tokens +
            ", myHomeList=" + myHomeList +
            '}';
  }
}

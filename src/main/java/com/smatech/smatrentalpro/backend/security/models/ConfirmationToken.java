package com.smatech.smatrentalpro.backend.security.models;

import com.smatech.smatrentalpro.backend.commons.AbstractEntity;
import com.smatech.smatrentalpro.backend.user.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken extends AbstractEntity {

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "date_confirmed")
    private LocalDateTime dateConfirmed;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    @ToString.Exclude
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getDateConfirmed() {
        return dateConfirmed;
    }

    public void setDateConfirmed(LocalDateTime dateConfirmed) {
        this.dateConfirmed = dateConfirmed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

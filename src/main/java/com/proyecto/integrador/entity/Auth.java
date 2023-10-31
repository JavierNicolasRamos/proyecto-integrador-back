package com.proyecto.integrador.entity;

import com.proyecto.integrador.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Auth implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Min(value = 6, message = "Password should be at least 6 characters")
    @Max(value = 12, message = "Password should be less than 12 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Usuario user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
}

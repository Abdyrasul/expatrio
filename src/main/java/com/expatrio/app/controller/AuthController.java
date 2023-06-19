package com.expatrio.app.controller;


import com.expatrio.app.model.Department;
import com.expatrio.app.model.Role;
import com.expatrio.app.model.User;
import com.expatrio.app.payload.ApiResponse;
import com.expatrio.app.payload.JwtAuthenticationResponse;
import com.expatrio.app.payload.LoginRequest;
import com.expatrio.app.payload.SignUpRequest;
import com.expatrio.app.repository.DepartmentRepository;
import com.expatrio.app.repository.UserRepository;
import com.expatrio.app.security.JwtTokenProvider;
import com.expatrio.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signing")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (userPrincipal.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals("ADMIN"))) {
            // User is not an ADMIN, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
//            System.out.println("username: "+userPrincipal.getUsername()+ "authority size: "+userPrincipal.getAuthorities().size());
//            System.out.println("role: "+userPrincipal.getAuthorities().stream().filter(x-> Objects.equals(x.getAuthority(), "ADMIN")).findAny().orElse(null
//            ).getAuthority());


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
//        System.out.println("token: "+jwt);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}

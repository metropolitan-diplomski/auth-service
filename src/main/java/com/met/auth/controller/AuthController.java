package com.met.auth.controller;

import com.met.auth.security.JwtTokenUtil;
import com.met.auth.security.JwtUser;
import com.met.auth.security.dto.JwtAuthenticationDto;
import com.met.auth.security.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(
            @RequestBody JwtAuthenticationDto jwtAuthenticationDto)
            throws Exception {

        // Checking username|email and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtAuthenticationDto.getUsername(), jwtAuthenticationDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtUser user = (JwtUser) authentication.getPrincipal();

        JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(jwtAuthenticationDto.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails, user.getDbUser().getId().toString());

        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        return ResponseEntity.ok(new JwtResponse(token, dateFormat.format(expiration), userDetails.getUsername(), roles));

    }

}

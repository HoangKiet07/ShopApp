package com.example.shopapp.services.Impl;

import com.example.shopapp.comoinents.JwtTokenUtil;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Role;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService  implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw  new DataIntegrityViolationException("Phone number already exists");
        }
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .password(userDTO.getPassword())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        user.setRole(role);

        //kiem tra neu co accountId, khong yeu cau password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }
        return userRepository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (!user.isPresent()){
            throw new DataNotFoundException("Invalid phone number or password");
        }

        User existingUser = user.get();
        //check password
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
            if (!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        //authenticate
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));
        return jwtTokenUtil.generateToken(existingUser);
    }














}

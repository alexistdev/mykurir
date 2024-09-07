package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.UserRepo;
import com.alexistdev.mykurir.v1.request.LoginRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", email)));
    }

    public User registerUser(User user) {
        boolean userExist = userRepo.findByEmail(user.getEmail()).isPresent();
        if (userExist) {
            throw new RuntimeException(
                    String.format("User %s already exists", user.getEmail())
            );
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setRole(Role.USER);
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    public User authenticate(LoginRequest loginRequest) {
        Optional<User> userExist = userRepo.findByEmail(loginRequest.getEmail());
        if(userExist.isPresent()){
            boolean authCheck = bCryptPasswordEncoder.matches(loginRequest.getPassword(), userExist.get().getPassword());
            if (!authCheck) {
                return null;
            }
           return userExist.get();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepo.findAll()
                .stream()
                .filter(c -> c.getRole() != Role.ADMIN)
                .collect(Collectors.toList());
        return users;
    }
}

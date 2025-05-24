package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.UserRepo;
import com.alexistdev.mykurir.v1.request.LoginRequest;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;



@Slf4j
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
        Date now = new Date();
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setRole(Role.USER);
        user.setPassword(encodedPassword);
//        user.setCreatedBy("System");
//        user.setModifiedBy("System");
//        user.setCreatedDate(now);
//        user.setModifiedDate(now);
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

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepo.findByRoleNot(Role.ADMIN,pageable);
    }

    public Page<User> getUserByFilter(Pageable pageable, String keyword){
        return userRepo.findByFilter(Role.ADMIN,keyword,pageable);
    }
}

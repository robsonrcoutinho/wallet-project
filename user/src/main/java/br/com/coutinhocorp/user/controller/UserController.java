package br.com.coutinhocorp.user.controller;

import br.com.coutinhocorp.user.dto.UserRecordDto;
import br.com.coutinhocorp.user.model.User;
import br.com.coutinhocorp.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users" )
    public ResponseEntity<User> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        var user = new User();
        BeanUtils.copyProperties(userRecordDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

}
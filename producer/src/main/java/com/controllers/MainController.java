package com.controllers;

import com.dto.UserDto;
import com.services.MainService;
import com.services.impl.MainServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Oleksandr on 6/16/2017.
 */
@Slf4j
@RestController
public class MainController {

    @Autowired
    private MainService mainService;

    /**
     * @description Method to create new user
     * @httpMethod POST
     * @httpUrl http://{host}:{port}/client/user
     * @httpUrlExample http://localhost:8081/producer/user
     * @requestBodyExample {"id" : "4", "username" : "Maya007"}
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> putUser(@RequestBody @Valid final UserDto userDto) {
        log.debug("Put user with id {} and username {} in MainController", userDto.getId(), userDto.getUsername());
        mainService.putUser(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

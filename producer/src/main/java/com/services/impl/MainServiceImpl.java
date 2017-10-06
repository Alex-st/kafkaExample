package com.services.impl;

import com.dto.UserDto;
import com.services.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 6/16/2017.
 */
@Slf4j
@Service
public class MainServiceImpl implements MainService {

    @Override
    public UserDto getUserDataById(long id) {

        //todo implement
        UserDto result = new UserDto();

        log.info("User {} was successfully fetched", result.getUsername());

        return result;
    }

    @Override
    public List<UserDto> getAllUsers() {

        //todo implement
        List<UserDto> result = new ArrayList<>();

        log.info("{} were fetched", result.size());

        return result;
    }

    @Override
    public long saveUser(UserDto dto) {

        //todo implement
        UserDto result = new UserDto();

        log.info("User {} was successfully created", result.getUsername());

        return result.getId();
    }
}

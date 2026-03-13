package th.team.stock.api.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import th.team.stock.commons.ApiConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import th.team.stock.commons.ApiConstant.LogType;
import th.team.stock.commons.CommonUtils;
import th.team.stock.services.EntityMapperService;
import th.team.stock.dto.CategoryData;
import th.team.stock.dto.UsersData;
import th.team.stock.models.Category;
import th.team.stock.models.Users;
import th.team.stock.repositories.UsersRepo;
import th.team.stock.services.UsersService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/stock-api/users")

public class UsersController implements ApiConstant{
    
    private final UsersService usersService;
    private final EntityMapperService mapperService;
    private final UsersRepo usersRepo;
    
    @PostMapping("find")
    public ResponseEntity<Map<String, Object>> findusers(HttpServletRequest request, HttpServletResponse response,
            @RequestBody UsersData data) {
        try {

            Map<String, Object> result = usersService.findUsers(data);

            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), SUCCESS, addOn), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    @PostMapping("save-user")
    public ResponseEntity<Map<String, Object>> saveUser(HttpServletRequest request, HttpServletResponse response,
            @RequestBody UsersData data) {
        try {

            Users users = mapperService.convertToEntity(data, Users.class);
            users.setName(data.getName());
            users.setUsername(data.getUsername());
            users.setRole(data.getRole());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            users.setPassword(encoder.encode(data.getPassword())); 

            usersRepo.save(users);
            UsersData result = mapperService.convertToEntity(users, UsersData.class);

            return new ResponseEntity<>(CommonUtils.response(result, "SUCCESS", null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError("Invalid input data for creating category"), HttpStatus.OK);
        }

    }   
@PutMapping("toggle-status/{id}")
public ResponseEntity<Map<String, Object>> toggleStatus(
        HttpServletRequest request, HttpServletResponse response,
        @PathVariable Long id) {
    try {
        Users user = usersRepo.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(CommonUtils.responseError("User not found"), HttpStatus.NOT_FOUND);
        }
        user.setActive(!user.getActive());
        usersRepo.save(user);
        return new ResponseEntity<>(CommonUtils.response(null, SUCCESS, null), HttpStatus.OK);
    } catch (Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
    }
}

    
       @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Map<String, Object>> deleteUsers(HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable(name = "id", required = true) Long id) {
                
        try {

            usersRepo.deleteById(id);

            return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
    
}

package com.sadeqstore.demo.controller;

import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/")
@RestController
public class Controller {
    private UserService userService;
    @Autowired
    public Controller(UserService userService){
        this.userService=userService;
    }
    @GetMapping(value = "health")
    public String getSession(){
        return "nothing";
    }

@PostMapping(value = "get-jwt")
    public ResponseEntity<String> getJWToken(@RequestParam String username,
                                             @RequestParam String password){
        return ResponseEntity.ok(userService.get_jwt(username,password));
}

    @PostMapping("/signup")
   /* @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 422, message = "Username is already in use")})*/
    public String signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @DeleteMapping(value = "admin/delete-user/{username}")
    public String delUser(@PathVariable String username){
        return userService.delete(username).toString();
    }
}

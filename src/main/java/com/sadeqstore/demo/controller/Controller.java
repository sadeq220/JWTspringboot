package com.sadeqstore.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadeqstore.demo.model.Product;
import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.repository.MyRepository;
import com.sadeqstore.demo.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@RequestMapping(value = "/")
@RestController
public class Controller {
    private UserService userService;
    private MyRepository pRepository;
    private ObjectMapper objectMapper;
    @Autowired
    public Controller(UserService userService, MyRepository pRepository,ObjectMapper objectMapper){
        this.userService=userService;
        this.pRepository=pRepository;
        this.objectMapper=objectMapper;
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> getSure(ResponseStatusException ex) throws JsonProcessingException {
        Properties properties=new Properties(3);
        properties.put("message",ex.getReason());
        properties.put("time", LocalDateTime.now().toString());
        properties.put("error-code",ex.getStatus().value());
        return new ResponseEntity<>(objectMapper.writeValueAsString(properties),ex.getStatus());
        //throw new ResponseStatusException(HttpStatus.CONFLICT,"try sm else");
        //response.sendError(409,"try sm else");
        //return "nothing";
    }
    @GetMapping(value = "whoami")
    @ApiOperation(value = "see if you registered or not, if yes see your name and your role")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    public String getID(Authentication authentication){
    if(authentication==null)
        return "you are not logged in";
    return authentication.getName()+authentication.getAuthorities();
    }
@PostMapping(value = "get-jwt")
@ApiOperation(value = "get JWT token for registered users")
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<String> getJWToken(@RequestParam String username,
                                             @RequestParam String password){
        return ResponseEntity.ok(userService.get_jwt(username,password));
}

    @PostMapping("/signup")
   @ApiOperation(value = "enroll/register client")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public String signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @DeleteMapping(value = "admin/delete-user/{username}")
    @ApiOperation(value = "delete user by name", authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    public String delUser(@PathVariable String username){
        return userService.delete(username).toString();
    }

    @DeleteMapping(value = "admin/delete-p/{product}")
    @ApiOperation(value = "delete product by name")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    public String delPs(@PathVariable String product){
    return pRepository.deleteByName(product).toString();
    }
    @PutMapping(value="admin/update-p/{product}")
    @ApiOperation(value = "update product by name")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    public String updateP(@PathVariable(name = "product") String productName,@RequestBody Product product){
    return pRepository.updateP(productName,product.getName(),product.getCost()).toString();
    }
    @PostMapping(value = "admin/add-p")
    @ApiOperation(value = "add product to database")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    public String addP(@RequestBody Product product){
    return pRepository.save(product).getName();
    }

    @GetMapping(value = "client/all-p")
    @ApiOperation(value = "list all products by name,sql wildcards enabled")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    //use some thing like localhost:8080/client/all-p?like=%25sara%25
    public List<Product> allP(@RequestParam(name = "like") String regexName){
        return pRepository.findAllByNameLike(regexName);
    }
}

package com.sadeqstore.demo.controller;

import com.sadeqstore.demo.model.Product;
import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.repository.MyRepository;
import com.sadeqstore.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/")
@RestController
public class Controller {
    private UserService userService;
    private MyRepository pRepository;
    @Autowired
    public Controller(UserService userService, MyRepository pRepository){
        this.userService=userService;
        this.pRepository=pRepository;
    }
    @GetMapping(value = "health")
    public String getSure(){
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

    @DeleteMapping(value = "admin/delete-p/{product}")
    public String delPs(@PathVariable String product){
    return pRepository.deleteByName(product).toString();
    }
    @PutMapping(value="admin/update-p/{product}")
    public String updateP(@PathVariable(name = "product") String productName,@RequestBody Product product){
    return pRepository.updateP(productName,product.getName(),product.getCost()).toString();
    }
    @PostMapping(value = "admin/add-p")
    public String addP(@RequestBody Product product){
    return pRepository.save(product).getName();
    }

    @GetMapping(value = "client/all-p")
    //use some thing like localhost:8080/client/all-p?like=%25sara%25
    public List<Product> allP(@RequestParam(name = "like") String regexName){
        return pRepository.findAllByNameLike(regexName);
    }
}

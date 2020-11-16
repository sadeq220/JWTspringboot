package com.sadeqstore.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sadeqstore.demo.model.TokenBucket;
import com.sadeqstore.demo.model.Product;
import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.model.UserDTO;
import com.sadeqstore.demo.paypalConfig.PayPalServiceProxy;
import com.sadeqstore.demo.repository.PsRepository;
import com.sadeqstore.demo.repository.TokensRepository;
import com.sadeqstore.demo.repository.UsersRepository;
import com.sadeqstore.demo.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RequestMapping(value = "/")
@RestController
@Api(tags = "users")
public class Controller {
    private UserService userService;
    private PsRepository pRepository;
    private ObjectMapper objectMapper;
    private PayPalServiceProxy payPalServiceProxy;
    private TokensRepository tokensRepository;
    @Autowired
    public Controller(UserService userService, PsRepository pRepository
            , ObjectMapper objectMapper, PayPalServiceProxy proxy
                , TokensRepository tokensRepository){
        this.userService=userService;
        this.pRepository=pRepository;
        this.objectMapper=objectMapper;
        payPalServiceProxy=proxy;
        this.tokensRepository=tokensRepository;
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
    public String signUp(@RequestBody UserDTO userDTO) {
        User user=new User();
        user.setPassword(userDTO.password);
        user.setName(userDTO.username);
        user.setRole(userDTO.role);
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

    @GetMapping(value = "client/p-name")
    @ApiOperation(value = "list all products by name,sql wildcards enabled")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    //use some thing like localhost:8080/client/all-p?like=%25sara%25
    public List<Product> pName(@RequestParam(name = "like") String regexName){
        return pRepository.findAllByNameLike(regexName);
    }
    @ApiOperation(value = "list all products,you can pass page num")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    @GetMapping(value = "client/all-p")
    public ResponseEntity<String> allP(@RequestParam(name = "page",defaultValue = "0")Integer pageN) throws JsonProcessingException {
        Slice slice=pRepository.products(PageRequest.of(pageN,10));
        Boolean next=slice.hasNext();
        JsonNode arrayNode=objectMapper.readTree(objectMapper.writeValueAsString(slice.getContent()));
        ObjectNode rootNode=objectMapper.createObjectNode();
        rootNode.put("hasNext",next);
        rootNode.set("Ps",arrayNode);
       return ResponseEntity.ok(objectMapper.writeValueAsString(rootNode));
    }
    @ApiOperation(value = "client by product by name")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token"),
            @ApiResponse(code = 409, message = "PayPal payment went wrong"),
            @ApiResponse(code = 404, message = "Product not found")})
    @GetMapping(value = "/order/{product}")
    public String buyP(@PathVariable(name = "product") String productName
                        ,@RequestParam(name = "desc",defaultValue = "nothing") String description
                            ,Authentication authentication){
        Product product=pRepository.findByName(productName);
        if(product==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"product not found");
        String url=payPalServiceProxy.payment(product.getCost(),description);
        String token=payPalServiceProxy.getTokenFromUrl(url);
        tokensRepository.save(new TokenBucket(productName,authentication.getName(),token));
      return url;
    }

    @ApiOperation(value = "client redirect to this url if payment was successful")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            //@ApiResponse(code = 403, message = "Access denied"),
            //@ApiResponse(code = 406, message = "Expired or invalid JWT token"),
            @ApiResponse(code = 409, message = "PayPal payment went wrong")})
    @GetMapping(value = "order/pay/success")
    public String successPay(@RequestParam(name = "paymentId")String paymentID
                                ,@RequestParam(name = "PayerID")String payerID
                                    ,@RequestParam String token){
        String result=payPalServiceProxy.successPay(paymentID,payerID);
        if(result.equals("success")){
            TokenBucket tokenBucket=tokensRepository.findByToken(token);
           if(tokenBucket != null){
           userService.updateUserPs(tokenBucket.getUsername(),new Product(tokenBucket.getProductName()));
           tokensRepository.deleteByToken(token);
            return "your product successfully bought";}
           return "your token not valid anymore";
        }
        return "something went wrong , call help center for refund";
    }

    @ApiOperation(value = "client redirect to this url if payment was canceled")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            //@ApiResponse(code = 403, message = "Access denied"),
           // @ApiResponse(code = 406, message = "Expired or invalid JWT token"),
            @ApiResponse(code = 409, message = "PayPal payment went wrong")})
    @GetMapping(value="order/pay/cancel")
    public String FailurePay(@RequestParam String token){
        /**
         *  :) database.save(principal.getName()+'messedUP')
         */
        return token;
    }
    @ApiOperation(value = "each user can see his/her own profile only")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 406, message = "Expired or invalid JWT token")})
    @GetMapping(value = "client/profile/{client-name}")
    public User clientProfile(@PathVariable(name = "client-name")String username ){
        User user=userService.getUser(username);
        user.setPassword("");
        return user;
    }
}

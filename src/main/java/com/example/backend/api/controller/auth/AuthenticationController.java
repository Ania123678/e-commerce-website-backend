package com.example.backend.api.controller.auth;

import com.example.backend.api.model.LoginBody;
import com.example.backend.api.model.LoginResponse;
import com.example.backend.api.model.RegistrationBody;
import com.example.backend.exception.EmailFailedException;
import com.example.backend.exception.UserExistsException;
import com.example.backend.exception.UserNotVerifiedException;
import com.example.backend.model.LocalUser;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// The @RestController annotation provides hints for people reading the code and for Spring that the class plays a specific role.
// In this case, our class is a web @Controller, so Spring considers it when handling incoming web requests.
// -----------------------------------------------------------------------------------------------------------------
// The @RequestMapping annotation provides “routing” information. It tells Spring that any HTTP request with the / path should be mapped to
// the home method. The @RestController annotation tells Spring to render the resulting string directly back to the caller.
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    // this refers to the current object in a method or constructor. The most common use of this keyword is to
    // eliminate the confusion between class attributes and parameters with the same name
    // (because a class attribute is shadowed by a method or constructor parameter)
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    // RegistrationBody registrationBody declares a variable named registrationBody with the type RegistrationBody.
    // This variable declaration is used to create a new instance of the RegistrationBody class,
    // which can be used to store and manipulate registration details for a user.
    // -----------------------------------------------------------------------------------------------------------------
    // @RequestBody annotation maps the HttpRequest body to a transfer or domain object,
    // enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
    // -----------------------------------------------------------------------------------------------------------------
    // @Valid is a Spring Boot annotation used to validate input data in a REST API. It is often used in conjunction with @RequestBody,
    // which is used to map HTTP request data to a Java object.
    // the validation framework checks the annotated constraints on the fields of the input object,
    // and if any of the constraints are violated, it will throw a MethodArgumentNotValidException exception.
    // In this case, we check whether the user has provided a correct password, such as 'email@.com' rather than just 'email'

    @PostMapping("/register")
    public ResponseEntity registerUser (@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailedException e) {
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = null;
        try {
            jwt = userService.loginUser(loginBody);
        } catch (UserNotVerifiedException ex) {
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            String reason = "USER_NOT_VERIFIED";
            if(!ex.isNewEmailSent()){
                reason += "_EMAIL_RESENT";
            }
            response.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (EmailFailedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if(userService.verifyUser(token)){
            return ResponseEntity.ok().build();
        } else{
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){
        return user;
    }

}

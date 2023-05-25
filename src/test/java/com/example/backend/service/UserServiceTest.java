package com.example.backend.service;

import com.example.backend.api.model.LoginBody;
import com.example.backend.api.model.RegistrationBody;
import com.example.backend.exception.EmailFailedException;
import com.example.backend.exception.UserExistsException;
import com.example.backend.exception.UserNotVerifiedException;
import com.example.backend.model.VerificationToken;
import com.example.backend.model.dao.VerificationTokenDAO;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.mail.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        RegistrationBody body = new RegistrationBody();
        body.setUsername("UserA");
        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        body.setFirstName("FirstName");
        body.setLastName("LastName");
        body.setPassword("SecretPasswordA123");

        Assertions.assertThrows(UserExistsException.class,
                () -> userService.registerUser(body), "username should already be in use");

        body.setUsername("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");

        Assertions.assertThrows(UserExistsException.class,
                () -> userService.registerUser(body), "email should already be in use");

        body.setEmail("UserServiceTest$testRegisterUser@junit.com");

        Assertions.assertDoesNotThrow(() -> userService.registerUser(body), "user should register successfulyy");
        Assertions.assertEquals(body.getEmail(), greenMailExtension.getReceivedMessages()[0]
                .getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, EmailFailedException {
        LoginBody body = new LoginBody();
        body.setUsername("UserA-NE");
        body.setPassword("PasswordA123");
        Assertions.assertNull(userService.loginUser(body), "The user should not exists");

        body.setUsername("UserA");
        body.setPassword("NotPasswordA123");
        Assertions.assertNull(userService.loginUser(body), "The password should be incorrect");

        body.setPassword("PasswordA123");
        Assertions.assertNotNull(userService.loginUser(body), "User should login successfully");

        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try{
            userService.loginUser(body);
            Assertions.assertTrue(false, "user email is not verified");
        } catch (UserNotVerifiedException ex){
            Assertions.assertTrue(ex.isNewEmailSent(), "email verification should be sent");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try{
            userService.loginUser(body);
            Assertions.assertTrue(false, "user email is not verified");
        } catch (UserNotVerifiedException ex){
            Assertions.assertFalse(ex.isNewEmailSent(), "email verification should not be resent");
            Assertions.assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void testVerifyUser() throws EmailFailedException{
        Assertions.assertFalse(userService.verifyUser("Bad Token"), "Token that is bad or does not exists");
        LoginBody body = new LoginBody();
        body.setUsername("UserB");
        body.setPassword("PasswordB123");
        try{
            userService.loginUser(body);
            Assertions.assertTrue(false, "user email is not verified");
        } catch (UserNotVerifiedException ex){
            List<VerificationToken> tokens = verificationTokenDAO.findByUser_IdOrderByIdDesc(2L);
            String token = tokens.get(0).getToken();
            Assertions.assertTrue(userService.verifyUser(token));
            Assertions.assertNotNull(body, "User is now verified, token is valid");
        }

    }
}

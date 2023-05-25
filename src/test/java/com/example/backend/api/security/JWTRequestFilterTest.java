package com.example.backend.api.security;

import com.example.backend.model.LocalUser;
import com.example.backend.model.dao.LocalUserDAO;
import com.example.backend.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTRequestFilterTest {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private LocalUserDAO localUserDAO;
    @Autowired
    private MockMvc mockMvc;

    private static final String AUTH_PATH = "/auth/me";

    @Test
    public void testUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get(AUTH_PATH)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testBadToken() throws Exception {
        mockMvc.perform(get(AUTH_PATH).header("Authorization", "TokenThatIsNotValid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));

        mockMvc.perform(get(AUTH_PATH).header("Authorization", "Bearer TokenThatIsNotValid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testUnverifiedUser() throws Exception {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserB").get();
        String token = jwtService.generateJWT(user);
        mockMvc.perform(get(AUTH_PATH).header("Authorization", "Bearer " + token))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithUserDetails("UserA")
    public void testSuccessful() throws Exception{
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateJWT(user);
        mockMvc.perform(get(AUTH_PATH).header("Authorization", "Bearer " + token))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

}

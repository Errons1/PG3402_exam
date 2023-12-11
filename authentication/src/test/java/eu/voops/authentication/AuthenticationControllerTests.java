package eu.voops.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.voops.authentication.dto.DtoCreateAccount;
import eu.voops.authentication.exception.AccountExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuthenticationService service;
    
    private DtoCreateAccount dtoCreateAccount;

    @BeforeEach
    public void beforeEach() {
        String internalId = "internalId1";
        String personalId = "personalId1";
        String password = "password";
        dtoCreateAccount = new DtoCreateAccount(internalId, personalId, password);
    }

    @Test
    public void createAccount_validInput_status201() throws Exception {
        doNothing().when(service).createAccount(isA(Authentication.class));
        
        String json = new ObjectMapper().writeValueAsString(dtoCreateAccount);
        
        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));

        verify(service).createAccount(isA(Authentication.class));
    }
    
    @Test
    public void createAccount_accountAlreadyExist() throws Exception {
        doThrow(new AccountExistException()).when(service).createAccount(isA(Authentication.class));
        
        String json = new ObjectMapper().writeValueAsString(dtoCreateAccount);

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(service).createAccount(isA(Authentication.class));
    }
    
    @Test
    public void createAccount_invalidInput() throws Exception{
        String internalId = dtoCreateAccount.getInternalId();
        String personalId = dtoCreateAccount.getPersonalId();
        String password = dtoCreateAccount.getPassword();
        
        dtoCreateAccount.setInternalId("");
        String json1 = new ObjectMapper().writeValueAsString(dtoCreateAccount);
        
        dtoCreateAccount.setInternalId(internalId);
        dtoCreateAccount.setPersonalId("");
        String json2 = new ObjectMapper().writeValueAsString(dtoCreateAccount);
        
        dtoCreateAccount.setPersonalId(personalId);
        dtoCreateAccount.setPassword("");
        String json3 = new ObjectMapper().writeValueAsString(dtoCreateAccount);

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json3))
                .andExpect(status().isBadRequest());
    }
    
}

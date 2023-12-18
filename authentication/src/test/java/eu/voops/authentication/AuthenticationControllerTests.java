package eu.voops.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.voops.authentication.dto.DtoCreateAccount;
import eu.voops.authentication.entity.Authentication;
import eu.voops.authentication.exception.ProfileExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    private Authentication authentication;
    
    @BeforeEach
    public void beforeEach() {
        String internalId = "internalId1";
        String personalId = "personalId1";
        String password = "password";
        dtoCreateAccount = new DtoCreateAccount(internalId, personalId, password);
        authentication = new Authentication(internalId, personalId, Hash.sha256(password));
    }

    @Test
    public void createAccount_validInput_status201() throws Exception {
        doNothing().when(service).createAccount(isA(Authentication.class));
        
        String json = new ObjectMapper().writeValueAsString(dtoCreateAccount);
        
        mockMvc.perform(post("/api/v1/create-authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));

        verify(service).createAccount(isA(Authentication.class));
    }
    
    @Test
    public void createAccount_accountAlreadyExist() throws Exception {
        doThrow(new ProfileExistException()).when(service).createAccount(isA(Authentication.class));
        
        String json = new ObjectMapper().writeValueAsString(dtoCreateAccount);

        mockMvc.perform(post("/api/v1/create-authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(service).createAccount(isA(Authentication.class));
    }
    
    @Test
    public void createAccount_invalidInput() throws Exception{
        String internalId = dtoCreateAccount.getInternalId();
        String personalId = dtoCreateAccount.getPersonalId();
        
        dtoCreateAccount.setInternalId("");
        String json1 = new ObjectMapper().writeValueAsString(dtoCreateAccount);
        
        dtoCreateAccount.setInternalId(internalId);
        dtoCreateAccount.setPersonalId("");
        String json2 = new ObjectMapper().writeValueAsString(dtoCreateAccount);
        
        dtoCreateAccount.setPersonalId(personalId);
        dtoCreateAccount.setPassword("");
        String json3 = new ObjectMapper().writeValueAsString(dtoCreateAccount);

        mockMvc.perform(post("/api/v1/create-authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json3))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void emergencyDelete_successfully_status200() throws Exception {
        String internalId = authentication.getInternalId();

        doNothing().when(service).emergencyDelete(internalId);

        mockMvc.perform(delete("/api/v1/emergency-delete/{internalId}", internalId))
                .andExpect(status().isOk());

        verify(service).emergencyDelete(internalId);
    }

    @Test
    public void emergencyDelete_accountDoesNotExist_status404() throws Exception {
        String internalId = authentication.getInternalId();

        doThrow(NoSuchElementException.class).when(service).emergencyDelete(internalId);

        mockMvc.perform(delete("/api/v1/emergency-delete/{internalId}", internalId))
                .andExpect(status().isNotFound());

        verify(service).emergencyDelete(internalId);
    }
    
}

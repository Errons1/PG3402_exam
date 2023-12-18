package eu.voops.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.voops.account.dto.DtoCreateAccount;
import eu.voops.account.entity.Account;
import eu.voops.account.exception.ProfileExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    private Account account;

    @BeforeEach
    public void beforeEach() {
        account = new Account("internalId1", "account name", "123456789123", 0L);
    }

    @Test
    public void createAccount_successfully_status201() throws Exception {
        DtoCreateAccount dto = new DtoCreateAccount(account.getInternalId(), account.getAccountName());

        when(service.makeAccountNumber()).thenReturn(account.getAccountNumber());
        doNothing().when(service).createAccount(isA(Account.class));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).makeAccountNumber();
        verify(service).createAccount(isA(Account.class));
    }

    @Test
    public void createAccount_accountExist_status409() throws Exception {
        DtoCreateAccount dto = new DtoCreateAccount(account.getInternalId(), account.getAccountName());

        when(service.makeAccountNumber()).thenReturn(account.getAccountNumber());
        doThrow(ProfileExistException.class).when(service).createAccount(isA(Account.class));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(service).makeAccountNumber();
        verify(service).createAccount(isA(Account.class));
    }

    @Test
    public void createAccount_wrongInput_status400() throws Exception {
        DtoCreateAccount dto1 = new DtoCreateAccount(null, account.getAccountName());
        DtoCreateAccount dto2 = new DtoCreateAccount(account.getInternalId(), null);
        
        String json1 = new ObjectMapper().writeValueAsString(dto1);
        String json2 = new ObjectMapper().writeValueAsString(dto2);

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void emergencyDelete_successfully_status200() throws Exception {
        String internalId = account.getInternalId();

        doNothing().when(service).emergencyDelete(internalId);

        mockMvc.perform(delete("/api/v1/emergency-delete/{internalId}", internalId))
                .andExpect(status().isOk());

        verify(service).emergencyDelete(internalId);
    }

    @Test
    public void emergencyDelete_accountDoesNotExist_status404() throws Exception {
        String internalId = account.getInternalId();

        doThrow(NoSuchElementException.class).when(service).emergencyDelete(internalId);

        mockMvc.perform(delete("/api/v1/emergency-delete/{internalId}", internalId))
                .andExpect(status().isNotFound());

        verify(service).emergencyDelete(internalId);
    }
    
}

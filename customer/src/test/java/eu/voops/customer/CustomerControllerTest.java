package eu.voops.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Test
    void checkIfAccountExist_accountExist_status200() throws Exception {
        String personalId = "1234";
        when(service.checkIfAccountExistByPersonalId(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/v1/check-if-customer-exist/" + personalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).checkIfAccountExistByPersonalId(personalId);
    }

    @Test
    void checkIfAccountExist_accountDoesNotExist_status404() throws Exception {
        String personalId = "1234";
        when(service.checkIfAccountExistByPersonalId(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/v1/check-if-customer-exist/" + personalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(content().string("false"));

        verify(service).checkIfAccountExistByPersonalId(personalId);
    }

    @Test
    public void getInternalIdByPersonalId_idFound_status200() throws Exception {
        String personalId = "testPersonalId";
        String internalId = "testInternalId";

        when(service.getInternalIdByPersonalId(personalId)).thenReturn(internalId);

        mockMvc.perform(get("/api/v1/get-internal-id-by-personal-id/{personalId}", personalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(internalId));
    }

    @Test
    public void getInternalIdByPersonalId_idNotFound_status404() throws Exception {
        String personalId = "testPersonalId";

        when(service.getInternalIdByPersonalId(personalId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/get-internal-id-by-personal-id/{personalId}", personalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void createCustomer() {
    }

    @Test
    void checkIfAccountExistByPersonalId() {
    }

    @Test
    void getInternalIdByPersonalId() {
    }
}
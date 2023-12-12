package eu.voops.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.voops.customer.dto.DtoCreateCustomer;
import eu.voops.customer.exception.ProfileExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    private Customer customer;

    @BeforeEach
    public void beforeEach() {
        customer = new Customer(
                "internalId1", "personalId1", "Ola",
                "Nordmann", "address", "123456789",
                "email@example.no"
        );
    }

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
        
        when(service.checkIfAccountExistByPersonalId(personalId)).thenReturn(false);

        mockMvc.perform(get("/api/v1/check-if-customer-exist/" + personalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(service).checkIfAccountExistByPersonalId(anyString());
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

        verify(service).getInternalIdByPersonalId(personalId);
    }

    @Test
    public void getInternalIdByPersonalId_idNotFound_status404() throws Exception {
        String personalId = "testPersonalId";

        when(service.getInternalIdByPersonalId(personalId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/get-internal-id-by-personal-id/{personalId}", personalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

        verify(service).getInternalIdByPersonalId(personalId);
    }


    @Test
    public void createCustomer_successfully_status201() throws Exception {
        DtoCreateCustomer dto = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );

        when(service.createInternalId(dto)).thenReturn(customer.getInternalId());
        doNothing().when(service).createCustomer(isA(Customer.class));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).createInternalId(dto);
        verify(service).createCustomer(isA(Customer.class));
    }

    @Test
    public void createCustomer_accountExist_status409() throws Exception {
        DtoCreateCustomer dto = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );

        when(service.createInternalId(dto)).thenReturn(customer.getInternalId());
        doThrow(ProfileExistException.class).when(service).createCustomer(isA(Customer.class));

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(service).createInternalId(dto);
        verify(service).createCustomer(isA(Customer.class));
    }

    @Test
    public void createCustomer_wrongInput_status400() throws Exception {
        DtoCreateCustomer dto1 = new DtoCreateCustomer(
                null, customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto2 = new DtoCreateCustomer(
                customer.getPersonalId(), null, customer.getLastName(),
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto3 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), null,
                customer.getAddress(), customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto4 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                null, customer.getTlf(), customer.getEmail()
        );
        DtoCreateCustomer dto5 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), null, customer.getEmail()
        );
        DtoCreateCustomer dto6 = new DtoCreateCustomer(
                customer.getPersonalId(), customer.getFirstName(), customer.getLastName(),
                customer.getAddress(), customer.getTlf(), null
        );

        String json1 = new ObjectMapper().writeValueAsString(dto1);
        String json2 = new ObjectMapper().writeValueAsString(dto2);
        String json3 = new ObjectMapper().writeValueAsString(dto3);
        String json4 = new ObjectMapper().writeValueAsString(dto4);
        String json5 = new ObjectMapper().writeValueAsString(dto5);
        String json6 = new ObjectMapper().writeValueAsString(dto6);

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json3))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json4))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json5))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/create-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json6))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void emergencyDelete_successfully_status200() throws Exception {
        String internalId = customer.getInternalId();

        doNothing().when(service).emergencyDelete(internalId);

        mockMvc.perform(delete("/api/v1/emergency-delete/{internalId}", internalId))
                .andExpect(status().isOk());

        verify(service).emergencyDelete(internalId);
    }

    @Test
    public void emergencyDelete_accountDoesNotExist_status404() throws Exception {
        String internalId = customer.getInternalId();

        doThrow(NoSuchElementException.class).when(service).emergencyDelete(internalId);

        mockMvc.perform(delete("/api/v1/emergency-delete/{internalId}", internalId))
                .andExpect(status().isNotFound());

        verify(service).emergencyDelete(internalId);
    }
}
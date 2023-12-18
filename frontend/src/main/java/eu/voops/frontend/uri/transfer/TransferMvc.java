package eu.voops.frontend.uri.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.voops.frontend.dto.DtoAccount;
import eu.voops.frontend.dto.DtoTransfer;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/transfer")
public class TransferMvc {

    private final TransferService service;

    @GetMapping("")
    public String transfer() {
        return "transfer/transfer";
    }

    @ModelAttribute("accounts")
    public List<DtoAccount> getAccounts(
            @CookieValue(value = "internalId", required = false) String internalIdCookie
    ) {
        return service.getAllAccounts(internalIdCookie);
    }

    @PostMapping("")
    public String handleTransfer(@Valid @NonNull @ModelAttribute DtoTransfer dto, Model model,
                                 @CookieValue(value = "internalId", required = false) String internalIdCookie
    ) {

        try {
            service.transferMoney(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info(dto.toString());
        String user = service.getFullNameWithInternalId(internalIdCookie);
        model.addAttribute("user", user);
        return "accounts/accounts";
    }

}

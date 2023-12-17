package eu.voops.transfer;

import eu.voops.transfer.dto.DtoTransfer;
import eu.voops.transfer.dto.DtoTransferAccountBalance;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class TransferService {


    private final RestTemplate restTemplate;

    public void processTransfer(DtoTransfer dtoTransfer) throws InterruptedException {
        String urlGetTransfer = "http://account/api/v1/get-transfer-data";
        ResponseEntity<DtoTransferAccountBalance> responseDtoBalance = restTemplate.postForEntity(
                urlGetTransfer, dtoTransfer, DtoTransferAccountBalance.class
        );

        DtoTransferAccountBalance dtoBalance = responseDtoBalance.getBody();
//        More sleep for more delay
        Thread.sleep((long) (Math.random() * 5 * 1000) + 1);


        if (dtoBalance != null && dtoBalance.getTransferFromBalance() - dtoTransfer.getAmount() >= 0) {
            Long balanceFrom = dtoBalance.getTransferFromBalance() - dtoTransfer.getAmount();
            Long balanceTo = dtoBalance.getTransferToBalance() + dtoTransfer.getAmount();

            Transfer transfer = new Transfer(
                    dtoTransfer.getTransferFrom(), balanceFrom,
                    dtoTransfer.getTransferTo(), balanceTo
            );

            String urlUpdateAccounts = "http://account/api/v1/update-account-balance";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                    urlUpdateAccounts, transfer, Boolean.class
            );
        
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }
    }

}

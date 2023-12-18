package eu.voops.transfer;

import eu.voops.transfer.dto.DtoTransfer;
import eu.voops.transfer.dto.DtoTransferAccountBalance;
import eu.voops.transfer.dto.DtoTransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class TransferService {
    
    private final RestTemplate restTemplate;

    public void processTransfer(DtoTransferRequest dtoTransferRequest) throws InterruptedException {
        String urlGetTransfer = "http://account/api/v1/get-transfer-data";
        ResponseEntity<DtoTransferAccountBalance> responseDtoBalance = restTemplate.postForEntity(
                urlGetTransfer, dtoTransferRequest, DtoTransferAccountBalance.class
        );

        DtoTransferAccountBalance dtoBalance = responseDtoBalance.getBody();
//        More sleep for more delay
        Thread.sleep((long) (Math.random() * 5 * 1000) + 1);


        if (dtoBalance != null &&
            dtoTransferRequest.getAmount() > 1 &&
            dtoBalance.getTransferFromBalance() - dtoTransferRequest.getAmount() >= 0 &&
            !dtoTransferRequest.getTransferFrom().equals(dtoTransferRequest.getTransferTo())
        ) {
            Long balanceFrom = dtoBalance.getTransferFromBalance() - dtoTransferRequest.getAmount();
            Long balanceTo = dtoBalance.getTransferToBalance() + dtoTransferRequest.getAmount();

            DtoTransfer transfer = new DtoTransfer(
                    dtoTransferRequest.getTransferFrom(), balanceFrom,
                    dtoTransferRequest.getTransferTo(), balanceTo
            );

            String urlUpdateAccounts = "http://account/api/v1/update-account-balance";
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                    urlUpdateAccounts, transfer, Boolean.class
            );
        }
    }
    
}

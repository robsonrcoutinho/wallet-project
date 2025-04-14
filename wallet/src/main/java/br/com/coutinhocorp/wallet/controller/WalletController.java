package br.com.coutinhocorp.wallet.controller;

import br.com.coutinhocorp.wallet.dto.WalletRecordDto;
import br.com.coutinhocorp.wallet.exceptions.RecordNotFoundException;
import br.com.coutinhocorp.wallet.exceptions.UserNotFoundException;
import br.com.coutinhocorp.wallet.model.User;
import br.com.coutinhocorp.wallet.model.Wallet;
import br.com.coutinhocorp.wallet.service.UserService;
import br.com.coutinhocorp.wallet.service.WalletService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wallet/v1")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @PostMapping("/createWallet")
    public ResponseEntity<Wallet> saveWallet(@RequestBody WalletRecordDto walletRecordDto) {
        var wallet = new Wallet();
        BeanUtils.copyProperties(walletRecordDto, wallet);
        Optional<User> user = userService.findWalletByUserDocument(walletRecordDto.numberDocumentUser());

        try {
            if (user.isPresent()) {
                System.out.println("user" + user.get().getNumberDocumentUser());
                wallet.setWalletUser(user.get());
                return ResponseEntity.status(HttpStatus.CREATED).body(walletService.save(wallet));
            } else {
                throw new UserNotFoundException("Error in creating the User resource. Try Again.");
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/currentBalanceWallet")
    @ResponseBody
    public ResponseEntity<Wallet> getCurrentBalance(@RequestParam String id) {
        Optional<Wallet> wallet = walletService.findWalletbByIUserId(id);

        if (null == id) {
            return ResponseEntity.badRequest().body(null);
        }else{
        try {
            return wallet.map(ResponseEntity::ok)
                    .orElseThrow(() -> new RecordNotFoundException("wallet not found for document id " + id));
        } catch (RecordNotFoundException e) {
            throw new RuntimeException(e);
        }
        }
    }


}

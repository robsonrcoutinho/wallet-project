package br.com.coutinhocorp.wallet.controller;

import br.com.coutinhocorp.wallet.dto.TransactionRecordDto;
import br.com.coutinhocorp.wallet.dto.TransactionTransferBalanceRecordDto;
import br.com.coutinhocorp.wallet.exceptions.WalletNotFoundException;
import br.com.coutinhocorp.wallet.exceptions.WalletWithoutFundsException;
import br.com.coutinhocorp.wallet.model.Transaction;
import br.com.coutinhocorp.wallet.model.Wallet;
import br.com.coutinhocorp.wallet.components.KafkaProducerComponent;
import br.com.coutinhocorp.wallet.service.TransactionService;
import br.com.coutinhocorp.wallet.service.UserService;
import br.com.coutinhocorp.wallet.service.WalletService;
import br.com.coutinhocorp.wallet.utils.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/wallet/v1")
public class TransactionController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private TransactionService transactionService;


    private final KafkaProducerComponent transactionKafkaProducer;

    @Autowired
    private UserService userService;

    public TransactionController(KafkaProducerComponent transactionKafkaProducer) {
        this.transactionKafkaProducer = transactionKafkaProducer;
    }

    @PostMapping("/depositFunds")
    public ResponseEntity<Transaction> sendFunds(@RequestBody TransactionRecordDto transactionRecordDto) {
        var transaction = new Transaction();
        BeanUtils.copyProperties(transactionRecordDto, transaction);
        Optional<Wallet> wallet = walletService.findWalletbByIUserId(transactionRecordDto.numberDocumentUser());
        try {
            if (wallet.isPresent()) {
                transaction.setUserReceiver(wallet.get().getWalletUser());
                transaction.setTimestampTransaction(getTimestampExecution());
                transaction.setTransactionType(TransactionType.DEPOSIT);
                transaction = transactionService.save(transaction);
                BigDecimal result = wallet.get().getBalance().add(transaction.getAmount());
                wallet.get().setBalance(result);
                transactionService.updateFunds(wallet.get());
                transactionKafkaProducer.sendTransactionMessage(transaction);
                return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transaction));
            } else {
                throw new WalletNotFoundException("Error in creating the Wallet resource. Try Again.");
            }
        } catch (WalletNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/withDrawFunds")
    public ResponseEntity<Transaction> withDrawFunds(@RequestBody TransactionRecordDto transactionRecordDto) {
        var transaction = new Transaction();
        BeanUtils.copyProperties(transactionRecordDto, transaction);
        Optional<Wallet> wallet = walletService.findWalletbByIUserId(transactionRecordDto.numberDocumentUser());
        try {
            if (wallet.isPresent()) {
                transaction.setUserReceiver(wallet.get().getWalletUser());
                transaction.setTimestampTransaction(getTimestampExecution());
                transaction.setTransactionType(TransactionType.WITHDRAW);
                if((wallet.get().getBalance().subtract(transaction.getAmount())).compareTo(BigDecimal.ZERO) > 0){
                    transaction = transactionService.save(transaction);
                    BigDecimal result = wallet.get().getBalance().subtract(transaction.getAmount());
                    wallet.get().setBalance(result);
                    transactionService.updateFunds(wallet.get());
                    transactionKafkaProducer.sendTransactionMessage(transaction);
                    return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transaction));
                }else{
                    throw new WalletWithoutFundsException("Error wallet without enough funds. Try Again.");
                }
            } else {
                throw new WalletNotFoundException("Error in creating the User resource. Try Again.");
            }
        } catch (WalletNotFoundException | WalletWithoutFundsException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transferFunds")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransactionTransferBalanceRecordDto transactionTransferBalanceRecordDto) {
        var transaction = new Transaction();
        BeanUtils.copyProperties(transactionTransferBalanceRecordDto, transaction);
        Optional<Wallet> walletSender = walletService.findWalletbByIUserId(transactionTransferBalanceRecordDto.numberDocumentUserSender());
        Optional<Wallet> walletReceiver = walletService.findWalletbByIUserId(transactionTransferBalanceRecordDto.numberDocumentUserReceiver());
        System.out.println("2 wallets existem" + transactionTransferBalanceRecordDto.numberDocumentUserReceiver() + " AND " + transactionTransferBalanceRecordDto.numberDocumentUserSender());
        try {
            if (walletSender.isPresent() && walletReceiver.isPresent()) {
                //System.out.println("2 wallets existem" + transactionTransferBalanceRecordDto.numberDocumentUserReceiver() + " AND " + transactionTransferBalanceRecordDto.numberDocumentUserSender());
                transaction.setUserReceiver(walletReceiver.get().getWalletUser());
                transaction.setUserSender(walletSender.get().getWalletUser());
                transaction.setTimestampTransaction(getTimestampExecution());
                transaction.setTransactionType(TransactionType.TRANSFER);
                if((walletSender.get().getBalance().subtract(transaction.getAmount())).compareTo(BigDecimal.ZERO) > 0){
                    transaction = transactionService.save(transaction);
                    BigDecimal resultSender = walletSender.get().getBalance().subtract(transaction.getAmount());
                    walletSender.get().setBalance(resultSender);
                    transactionService.updateFunds(walletSender.get());
                    BigDecimal resultReceiver = walletReceiver.get().getBalance().add(transaction.getAmount());
                    walletReceiver.get().setBalance(resultReceiver);
                    transactionService.updateFunds(walletReceiver.get());
                    transactionKafkaProducer.sendTransactionMessage(transaction);
                    return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transaction));
                }else{
                    throw new WalletWithoutFundsException("Error wallet without enough funds. Try Again.");
                }
            } else {
                throw new WalletNotFoundException("Error in creating the User resource. Try Again.");
            }
        } catch (WalletNotFoundException | WalletWithoutFundsException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/historyBalanceWalletByUser")
    public ResponseEntity<Page<Transaction>> historyBalanceWallet(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String id) {
        Page<Transaction> transaction = transactionService.getTransactionsByUser(pageNo, pageSize);
        return ResponseEntity.ok(transaction);
    }

    private Timestamp getTimestampExecution(){
        LocalDateTime now = LocalDateTime.now();
       return Timestamp.valueOf(now);
    }

}

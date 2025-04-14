package br.com.coutinhocorp.wallet.service;

import br.com.coutinhocorp.wallet.model.Transaction;
import br.com.coutinhocorp.wallet.model.Wallet;
import br.com.coutinhocorp.wallet.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
       // transactionRepository.withdraw(fromAccountId, amount);
       // transactionRepository.deposit(toAccountId, amount);
    }

    public Transaction save(Transaction transaction) {
        transaction = transactionRepository.save(transaction);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void updateFunds(Wallet wallet){
        transactionRepository.deposit(wallet.getWalletUser().getNumberDocumentUser(), wallet.getBalance());
    }

    public Page<Transaction> getTransactionsByUser(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return transactionRepository.findAll(pageable);
    }



}

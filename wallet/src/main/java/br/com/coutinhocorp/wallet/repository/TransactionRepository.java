package br.com.coutinhocorp.wallet.repository;

import br.com.coutinhocorp.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;



public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Modifying
    @Query("update Wallet set balance = :amount where walletUser.numberDocumentUser = :id")
    void deposit(@Param("id") String nuDocumentClientReceiver, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("update Wallet set balance = balance - :amount where id = :id")
    void withdraw(@Param("id") String nuDocumentClientReceiver, @Param("amount") BigDecimal amount);
}

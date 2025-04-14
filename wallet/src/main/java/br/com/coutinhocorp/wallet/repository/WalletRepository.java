package br.com.coutinhocorp.wallet.repository;

import br.com.coutinhocorp.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Query("SELECT w FROM Wallet w WHERE w.walletUser.numberDocumentUser = ?1")
    Optional<Wallet> findWalletByUserDocumentId(String walletUser);

    }


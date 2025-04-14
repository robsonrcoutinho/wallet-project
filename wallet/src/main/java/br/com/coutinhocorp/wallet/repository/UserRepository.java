package br.com.coutinhocorp.wallet.repository;

import br.com.coutinhocorp.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT w FROM User w WHERE w.numberDocumentUser = ?1")
   User findWalletByUserDocumentId(String numberDocumentUser);
}

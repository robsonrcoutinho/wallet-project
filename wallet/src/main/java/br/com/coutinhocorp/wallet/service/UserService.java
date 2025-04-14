package br.com.coutinhocorp.wallet.service;

import br.com.coutinhocorp.wallet.model.User;
import br.com.coutinhocorp.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findWalletByUserDocument(String id) {
        return Optional.ofNullable(userRepository.findWalletByUserDocumentId(id));
    }
}

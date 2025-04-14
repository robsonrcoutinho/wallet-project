package br.com.coutinhocorp.wallet.service;

import br.com.coutinhocorp.wallet.model.Wallet;
import br.com.coutinhocorp.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    public Wallet save(Wallet wallet) {
        wallet = walletRepository.save(wallet);
        return wallet;
    }

    public Optional<Wallet> findWalletbById(UUID id) {
        return walletRepository.findById(id);
    }

    public Optional<Wallet> findWalletbByIUserId(@PathVariable String nuDocumentClient) {
        return walletRepository.findWalletByUserDocumentId(nuDocumentClient);
    }

}

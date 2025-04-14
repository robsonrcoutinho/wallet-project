package br.com.coutinhocorp.user.service;


import br.com.coutinhocorp.user.model.User;
import br.com.coutinhocorp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User save(User user) {
        user = userRepository.save(user);
        return user;
    }
}

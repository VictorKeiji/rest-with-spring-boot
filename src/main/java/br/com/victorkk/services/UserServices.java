package br.com.victorkk.services;

import br.com.victorkk.controllers.PersonController;
import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.exceptions.RequiredObjectIsNullException;
import br.com.victorkk.exceptions.ResourceNotFoundException;
import br.com.victorkk.mapper.MyMapper;
import br.com.victorkk.model.User;
import br.com.victorkk.repositories.PersonRepository;
import br.com.victorkk.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {

    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    UserRepository repository;

    /* É possível injetar dependências (@Autowired) pelo construtor também.
    * Porém, as injeções serão obrigatórias na inicialização da aplicação */
    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name " + username + "...");
        var user = repository.findByUsername(username);
        if (user != null) return user;
        else
            throw new UsernameNotFoundException("Username " + username + " not found!");
    }
}

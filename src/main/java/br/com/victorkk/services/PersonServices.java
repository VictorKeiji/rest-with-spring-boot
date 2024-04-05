package br.com.victorkk.services;

import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.exceptions.ResourceNotFoundException;
import br.com.victorkk.mapper.PersonMapper;
import br.com.victorkk.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonVO> findAll() {

        logger.info("Finding all people!");

        return PersonMapper.INSTANCE.parseListPersonVOs(repository.findAll());
    }

    public PersonVO findById(Long id) {

        logger.info("Finding one person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return PersonMapper.INSTANCE.personToPersonVO(entity);
    }

    public PersonVO create(PersonVO person) {

        logger.info("Creating one person!");

        var entity = PersonMapper.INSTANCE.personVOToPerson(person);
        return PersonMapper.INSTANCE.personToPersonVO(repository.save(entity));
    }

    public PersonVO update(PersonVO person) {

        logger.info("Updating one person!");

        var entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return PersonMapper.INSTANCE.personToPersonVO(repository.save(entity));
    }

    public void delete(Long id) {

        logger.info("Deleting one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }
}

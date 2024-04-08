package br.com.victorkk.services;

import br.com.victorkk.controllers.PersonController;
import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.exceptions.ResourceNotFoundException;
import br.com.victorkk.mapper.MyMapper;
import br.com.victorkk.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        var persons = MyMapper.INSTANCE.parseListPersonVOs(repository.findAll());

        persons.forEach(
                p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getPersonId())).withSelfRel()));
        return persons;
    }

    public PersonVO findById(Long id) {

        logger.info("Finding one person...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = MyMapper.INSTANCE.personToPersonVO(entity);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO personVo) {

        logger.info("Creating one person!");

        var entity = MyMapper.INSTANCE.personVOToPerson(personVo);
        var vo = MyMapper.INSTANCE.personToPersonVO(repository.save(entity));

        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getPersonId())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO personVo) {

        logger.info("Updating one person!");

        var entity = repository.findById(personVo.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(personVo.getFirstName());
        entity.setLastName(personVo.getLastName());
        entity.setAddress(personVo.getAddress());
        entity.setGender(personVo.getGender());

        var vo = MyMapper.INSTANCE.personToPersonVO(repository.save(entity));

        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getPersonId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {

        logger.info("Deleting one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }
}

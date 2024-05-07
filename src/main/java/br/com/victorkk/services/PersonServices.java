package br.com.victorkk.services;

import br.com.victorkk.controllers.PersonController;
import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.exceptions.RequiredObjectIsNullException;
import br.com.victorkk.exceptions.ResourceNotFoundException;
import br.com.victorkk.mapper.MyMapper;
import br.com.victorkk.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all people!");

        var personPage = repository.findAll(pageable);
        var personVOsPage = personPage.map(p -> MyMapper.INSTANCE.personToPersonVO(p));
        personVOsPage.map(
                p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getPersonId())).withSelfRel()));

        Link link = linkTo(
                methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVOsPage, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
        logger.info("Finding all people!");

        var personPage = repository.findPersonByName(firstName, pageable);
        var personVOsPage = personPage.map(p -> MyMapper.INSTANCE.personToPersonVO(p));
        personVOsPage.map(
                p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getPersonId())).withSelfRel()));

        Link link = linkTo(
                methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVOsPage, link);
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
        if (personVo == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one person!");

        var entity = MyMapper.INSTANCE.personVOToPerson(personVo);
        var vo = MyMapper.INSTANCE.personToPersonVO(repository.save(entity));

        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getPersonId())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO personVo) {
        if (personVo == null) throw new RequiredObjectIsNullException();
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

    @Transactional
    public PersonVO disablePerson(Long id) {
        logger.info("Disabling one person...");
        repository.disablePerson(id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = MyMapper.INSTANCE.personToPersonVO(entity);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }
}

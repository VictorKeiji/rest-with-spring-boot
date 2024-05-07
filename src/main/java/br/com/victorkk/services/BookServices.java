package br.com.victorkk.services;

import br.com.victorkk.controllers.BookController;
import br.com.victorkk.data.vo.v1.BookVO;
import br.com.victorkk.exceptions.RequiredObjectIsNullException;
import br.com.victorkk.exceptions.ResourceNotFoundException;
import br.com.victorkk.mapper.MyMapper;
import br.com.victorkk.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookVO> assembler;

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
        logger.info("Finding all books!");

        var bookPage = repository.findAll(pageable);
        var bookVOsPage = bookPage.map(b -> MyMapper.INSTANCE.bookToBookVO(b));
        bookVOsPage.map(
                b -> b.add(linkTo(methodOn(BookController.class).findById(b.getBookId())).withSelfRel()));

        Link link = linkTo(
                methodOn(BookController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(bookVOsPage, link);
    }

    public BookVO findById(Long id) {
        logger.info("Finding one book...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = MyMapper.INSTANCE.bookToBookVO(entity);

        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one book!");

        var entity = MyMapper.INSTANCE.bookVOToBook(bookVO);
        var vo = MyMapper.INSTANCE.bookToBookVO(repository.save(entity));

        vo.add(linkTo(methodOn(BookController.class).findById(vo.getBookId())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one book!");

        var entity = repository.findById(bookVO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(bookVO.getAuthor());
        entity.setLaunchDate(bookVO.getLaunchDate());
        entity.setPrice(bookVO.getPrice());
        entity.setTitle(bookVO.getTitle());
        var vo = MyMapper.INSTANCE.bookToBookVO(repository.save(entity));

        vo.add(linkTo(methodOn(BookController.class).findById(vo.getBookId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }
}

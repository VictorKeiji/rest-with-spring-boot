package br.com.victorkk.services;

import br.com.victorkk.controllers.BookController;
import br.com.victorkk.data.vo.v1.BookVO;
import br.com.victorkk.exceptions.RequiredObjectIsNullException;
import br.com.victorkk.exceptions.ResourceNotFoundException;
import br.com.victorkk.mapper.MyMapper;
import br.com.victorkk.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    public List<BookVO> findAll() {
        logger.info("Finding all books!");

        var books = MyMapper.INSTANCE.parseListBookVOs(repository.findAll());

        books.forEach(
                b -> b.add(linkTo(methodOn(BookController.class).findById(b.getBookId())).withSelfRel()));
        return books;
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

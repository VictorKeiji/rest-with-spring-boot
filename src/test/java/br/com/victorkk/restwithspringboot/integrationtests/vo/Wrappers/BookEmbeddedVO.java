package br.com.victorkk.restwithspringboot.integrationtests.vo.Wrappers;

import br.com.victorkk.restwithspringboot.integrationtests.vo.BookTestVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bookVOList")
    private List<BookTestVO> books;

    public BookEmbeddedVO() {}

    public List<BookTestVO> getBooks() {
        return books;
    }

    public void setBooks(List<BookTestVO> books) {
        this.books = books;
    }
}

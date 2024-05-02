package br.com.victorkk.restwithspringboot.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement
public class BookTestVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String author;
    private Date launchDate;
    private Double Price;
    private String title;

    public BookTestVO() {}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookTestVO book = (BookTestVO) o;
        return Objects.equals(bookId, book.bookId) && Objects.equals(author, book.author) && Objects.equals(launchDate, book.launchDate) && Objects.equals(Price, book.Price) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, author, launchDate, Price, title);
    }
}

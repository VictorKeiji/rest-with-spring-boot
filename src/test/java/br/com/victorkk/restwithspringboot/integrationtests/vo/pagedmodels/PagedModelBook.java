package br.com.victorkk.restwithspringboot.integrationtests.vo.pagedmodels;

import br.com.victorkk.restwithspringboot.integrationtests.vo.BookTestVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelBook {

    @XmlElement(name = "content")
    private List<BookTestVO> content;

    public PagedModelBook() {}

    public List<BookTestVO> getContent() {
        return content;
    }

    public void setContent(List<BookTestVO> content) {
        this.content = content;
    }
}
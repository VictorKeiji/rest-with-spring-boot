package br.com.victorkk.restwithspringboot.integrationtests.vo.pagedmodels;

import br.com.victorkk.restwithspringboot.integrationtests.vo.PersonTestVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelPerson {

    @XmlElement(name = "content")
    private List<PersonTestVO> content;

    public PagedModelPerson() {}

    public List<PersonTestVO> getContent() {
        return content;
    }

    public void setContent(List<PersonTestVO> content) {
        this.content = content;
    }
}

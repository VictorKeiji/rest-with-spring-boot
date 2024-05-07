package br.com.victorkk.restwithspringboot.integrationtests.vo.Wrappers;

import br.com.victorkk.restwithspringboot.integrationtests.vo.PersonTestVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmbeddedVO implements Serializable {

    private static final long serialVersionUID = 1l;

    @JsonProperty("personVOList")
    private List<PersonTestVO> persons;

    public PersonEmbeddedVO() {}

    public List<PersonTestVO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonTestVO> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEmbeddedVO that = (PersonEmbeddedVO) o;
        return Objects.equals(persons, that.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(persons);
    }
}

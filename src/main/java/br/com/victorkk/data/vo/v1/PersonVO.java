package br.com.victorkk.data.vo.v1;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long personId;

    private String firstName;

    private String lastName;

    private String address;

    private String gender;

    public PersonVO() {}

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonVO person = (PersonVO) o;
        return Objects.equals(personId, person.personId) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(address, person.address) && Objects.equals(gender, person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, firstName, lastName, address, gender);
    }
}

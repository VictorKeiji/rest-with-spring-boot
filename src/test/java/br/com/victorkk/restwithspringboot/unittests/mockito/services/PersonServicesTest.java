package br.com.victorkk.restwithspringboot.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.exceptions.RequiredObjectIsNullException;
import br.com.victorkk.model.Person;
import br.com.victorkk.repositories.PersonRepository;
import br.com.victorkk.restwithspringboot.unittests.mapper.mocks.MockPerson;
import br.com.victorkk.services.PersonServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // Existe uma instância por classe
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Person entity = input.mockEntity(1);
        //entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getPersonId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("F", result.getGender());
    }

    @Test
    void testFindAll() {
        List<Person> list = input.mockEntityList();

        when(repository.findAll()).thenReturn((list));

        var people = service.findAll();
        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);
        assertNotNull(personOne);
        assertNotNull(personOne.getPersonId());
        assertNotNull(personOne.getLinks());
        assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("F", personOne.getGender());

        var personFour = people.get(4);
        assertNotNull(personFour);
        assertNotNull(personFour.getPersonId());
        assertNotNull(personFour.getLinks());
        assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
        assertEquals("Addres Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Last Name Test4", personFour.getLastName());
        assertEquals("M", personFour.getGender());

        var personSeven = people.get(7);
        assertNotNull(personSeven);
        assertNotNull(personSeven.getPersonId());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("Addres Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("F", personSeven.getGender());
    }

    @Test
    void testCreate() {
        Person entity = input.mockEntity(1);

        Person persisted = entity;
        //persisted.setId(1L);        // id inicial

        PersonVO vo = input.mockVO(1);
        //vo.setPersonId(1L);

        when(repository.save(entity)).thenReturn(persisted); // o .save irá atribuir novo id

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getPersonId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("F", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        Person entity = input.mockEntity(1);
        //entity.setId(1L);

        Person persisted = entity;

        PersonVO vo = input.mockVO(1);
        //vo.setPersonId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted); // o .save irá atribuir novo id

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getPersonId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("F", result.getGender());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        Person entity = input.mockEntity(1);
        //entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}
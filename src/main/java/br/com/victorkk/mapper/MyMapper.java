package br.com.victorkk.mapper;

import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MyMapper {

    MyMapper INSTANCE = Mappers.getMapper(MyMapper.class);

    List<PersonVO> parseListPersonVOs(List<Person> persons);
    List<Person> parseListPersons(List<PersonVO> persons);
    PersonVO personToPersonVO(Person person);
    Person personVOToPerson(PersonVO personVO);
}

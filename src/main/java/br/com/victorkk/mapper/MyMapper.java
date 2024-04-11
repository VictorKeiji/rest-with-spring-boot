package br.com.victorkk.mapper;

import br.com.victorkk.data.vo.v1.PersonVO;
import br.com.victorkk.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MyMapper {

    MyMapper INSTANCE = Mappers.getMapper(MyMapper.class);

    @Mapping(target="personId", source="id")
    List<PersonVO> parseListPersonVOs(List<Person> persons);

    @Mapping(target="id", source="personId")
    List<Person> parseListPersons(List<PersonVO> persons);

    @Mapping(target="personId", source="id")
    PersonVO personToPersonVO(Person person);

    @Mapping(target="id", source="personId")
    Person personVOToPerson(PersonVO personVO);
}

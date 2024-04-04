package br.com.victorkk.repositories;

import br.com.victorkk.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
// JpaRepository implementa um CRUD básico <tipoObjeto, tipoIdObj>
}

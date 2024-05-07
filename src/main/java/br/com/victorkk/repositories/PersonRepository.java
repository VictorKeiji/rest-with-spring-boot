package br.com.victorkk.repositories;

import br.com.victorkk.model.Person;
import br.com.victorkk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
// JpaRepository implementa um CRUD básico <tipoObjeto, tipoIdObj>

    // Controle de Transação
    // ACID - Atomicidade, Consistência, Isolação e Durabilidade
    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disablePerson(@Param("id") Long id);

    // Queries personalizadas, é possível fazer as mesmas através do JPA
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%', :firstName, '%'))")
    Page<Person> findPersonByName(@Param("firstName") String firstName, Pageable pageable);
}

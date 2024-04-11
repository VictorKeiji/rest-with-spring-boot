package br.com.victorkk.repositories;

import br.com.victorkk.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
// JpaRepository implementa um CRUD básico <tipoObjeto, tipoIdObj>
}

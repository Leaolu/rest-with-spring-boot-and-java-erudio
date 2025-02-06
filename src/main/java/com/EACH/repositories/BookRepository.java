package com.EACH.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EACH.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{}

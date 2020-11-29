package com.wannistudio.autowire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BookService {
    @Autowired @Qualifier("wanniBookRepository")
    BookRepository bookRepository;
//    @Autowired
//    List<BookRepository> bookRepositories;

//    public void printBookRepository() {
////        System.out.println(bookRepository.getClass());
//        bookRepositories.forEach(bookRepository -> System.out.println(bookRepository.getClass()));
//    }

    @PostConstruct
    public void setup() {
        System.out.println("@postConstruct : " + bookRepository.getClass());
    }
//    public BookService(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//    @Autowired(required = false) // Autowired가 있기 때문에 의존성 주입을 시도하려고 한다.
//    public void setBookRepository(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
}

package com.wannistudio.springapplicationcontext;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Setter
public class BookService {
    @Autowired
    BookRepository bookRepository;
}

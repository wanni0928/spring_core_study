package com.wannistudio.autowire;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class WanniBookRepository implements BookRepository {
}

package com.security.wrr.repository;

import com.security.wrr.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColRepository extends MongoRepository<Student,String>{
   // Student findByName(String name);
    List<Student> findByNameLike(String name);
    Page<Student> findByAgeBetween(int from, int to, Pageable pageable);

}

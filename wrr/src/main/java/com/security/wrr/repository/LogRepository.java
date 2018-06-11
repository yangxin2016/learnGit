package com.security.wrr.repository;

import com.security.wrr.model.FileInfo;
import com.security.wrr.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LogRepository extends MongoRepository<Log,String> {

}

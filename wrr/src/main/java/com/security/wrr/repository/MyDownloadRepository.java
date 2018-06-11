package com.security.wrr.repository;

import com.security.wrr.model.FileInfo;
import com.security.wrr.model.MyDownload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MyDownloadRepository extends MongoRepository<MyDownload,String> {

    Page<MyDownload> findByUserName(String user, Pageable pageable);
}

package com.security.wrr.repository;

import com.security.wrr.model.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CpabeRepository extends MongoRepository<FileInfo,String> {
    Page<FileInfo> findByKeywordsIn(List<String> keywords, Pageable pageable);

    FileInfo findByMd5Code(String md5Code);

    Page<FileInfo> findByUser(String user,Pageable pageable);

    /**
     * 逻辑或查询
     * @param keywords
     * @param pageable
     * @return
     */
    @Query("{'keywords':{'$in':?0},'policy':{'$in':?1}}")
    Page<FileInfo> orSearch(List<String> keywords,String[] position,Pageable pageable);

    /**
     * 逻辑且查询
     * @param keywords
     * @param pageable
     * @return
     */
    @Query("{'keywords':{'$all':?0},'policy':{'$in':?1}}")
    Page<FileInfo> andSearch(List<String> keywords,String[] position,Pageable pageable);


}

package com.example.seriesService.repo;


import com.example.seriesService.entity.ContentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends MongoRepository<ContentEntity,String> {

}

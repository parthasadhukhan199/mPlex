package com.uploadService.uploadService.repo;

import com.uploadService.uploadService.entity.ContentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepository extends MongoRepository<ContentEntity,String> {

}

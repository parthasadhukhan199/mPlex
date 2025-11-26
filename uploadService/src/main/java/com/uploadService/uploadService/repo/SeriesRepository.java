package com.uploadService.uploadService.repo;

import com.uploadService.uploadService.entity.SeriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeriesRepository extends MongoRepository<SeriesEntity,String> {

}

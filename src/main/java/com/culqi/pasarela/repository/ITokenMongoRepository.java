package com.culqi.pasarela.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.culqi.pasarela.dao.Cvv;

@Repository
public interface ITokenMongoRepository extends MongoRepository<Cvv, String> {

}

package com.culqi.pasarela.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.culqi.pasarela.dao.Pasarela;

@Repository
public interface ITokenMysqlRepository extends CrudRepository<Pasarela, Long> {

	Pasarela findPasarelaByToken(String token);
}

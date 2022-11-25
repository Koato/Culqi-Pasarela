package com.culqi.pasarela.services;

import java.util.Optional;

import com.culqi.pasarela.dao.Pasarela;

public interface ITokenServices {

	Optional<Pasarela> getPasarela(String token, String pk);

	Optional<String> saveToken(Pasarela pasarela, String pk);

	String generateToken();
}

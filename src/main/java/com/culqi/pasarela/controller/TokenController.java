package com.culqi.pasarela.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culqi.pasarela.dao.Pasarela;
import com.culqi.pasarela.services.ITokenServices;

@RestController
@Validated
@RequestMapping(value = "/v2")
public class TokenController {

	@Autowired
	ITokenServices iTokenServices;

	@PostMapping(value = "/tokens")
	public ResponseEntity<String> generateToken(@Validated @RequestBody Pasarela request,
			@RequestHeader HttpHeaders headers) throws Exception {
		String autenticacion = headers.get(HttpHeaders.AUTHORIZATION).get(0).split("Bearer ")[1];
		Optional<String> token = iTokenServices.saveToken(request, autenticacion);
		if (token.isEmpty()) {
			throw new Exception("El llamado al servicio fue secuestrado");
		}
		return ResponseEntity.ok(token.get());
	}

	@GetMapping(value = "/token/{token}")
	public ResponseEntity<Pasarela> getPasarela(@PathVariable String token, @RequestHeader HttpHeaders headers)
			throws Exception {
		String autenticacion = headers.get(HttpHeaders.AUTHORIZATION).get(0).split("Bearer ")[1];
		Optional<Pasarela> op = iTokenServices.getPasarela(token, autenticacion);
		if (op.isEmpty()) {
			throw new Exception("El PK proporcionado no es válido");
		}
		return ResponseEntity.ok(op.get());
	}

}

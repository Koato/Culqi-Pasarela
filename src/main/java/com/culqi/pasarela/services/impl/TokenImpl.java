package com.culqi.pasarela.services.impl;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.culqi.pasarela.dao.Pasarela;
import com.culqi.pasarela.repository.ITokenRepository;
import com.culqi.pasarela.services.ITokenServices;

@Service("iTokenServices")
public class TokenImpl implements ITokenServices {

	@Autowired
	private ITokenRepository iTokenRepository;

	// Rango ASCII – alfanumérico (0-9, a-z, A-Z)
	final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	@Override
	public Optional<Pasarela> getPasarela(String token, String pk) {
		if (checkPk(pk)) {
			return Optional.empty();
		}
		Optional<Pasarela> op = Optional.of(iTokenRepository.findPasarelaByToken(token));
		if (op.isPresent()) {
			return op;
		}
		return Optional.empty();
	}

	@Override
	public Optional<String> saveToken(Pasarela pasarela, String pk) {
		if (validateLength(pasarela) || checkPk(pk)) {
			pasarela.setCvv(null);
			pasarela.setToken(generateToken());
			iTokenRepository.save(pasarela);
			return Optional.of(pasarela.getToken());
		} else {
			return Optional.empty();
		}
	}

	@Override
	public String generateToken() {
		SecureRandom random = new SecureRandom();
		return IntStream.range(0, 16).map(i -> random.nextInt(chars.length()))
				.mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex))).collect(Collectors.joining());

	}

	private boolean checkPk(String pk) {
		if (pk.length() != 16) {
			return false;
		}
		if (!pk.startsWith("pk_")) {
			return false;
		}
		return true;
	}

	private boolean checkLuhn(String cardNumber) {
		int nDigits = cardNumber.length();
		int nSum = 0;
		boolean isSecond = false;
		for (int i = nDigits - 1; i >= 0; i--) {
			int d = cardNumber.charAt(i) - '0';
			if (isSecond == true) {
				d = d * 2;
			}
			nSum += d / 10;
			nSum += d % 10;
			isSecond = !isSecond;
		}
		return (nSum % 10 == 0);
	}

	private boolean validateLength(Pasarela pasarela) {
		// Comparacion longitud numero de tarjeta
		String cardNumber = pasarela.getCard_number().toString();
		if (checkLuhn(cardNumber) && cardNumber.length() < 13 && 16 < cardNumber.length()) {
			return false;
		}
		// Comparacion mes de expiracion tarjeta
		int mesExpiracion = Integer.parseInt(pasarela.getExpiration_month());
		if (mesExpiracion < 1 && 12 < mesExpiracion) {
			return false;
		}
		// Comparacion año de expiracion tarjeta
		int year = Integer.parseInt(pasarela.getExpiration_year());
		if (year < LocalDate.now().getYear() || LocalDate.now().getYear() + 5 < year) {
			return false;
		}
		// Comparacion correo
		String email = pasarela.getEmail();
		if (!email.contains("gmail.com") && !email.contains("hotmail.com") && !email.contains("yahoo.es")) {
			return false;
		}
		// Comparacion CVV
		int cvv = pasarela.getCvv();
		boolean isAmex = Pattern.compile("^3[47][0-9]{13}$").matcher(cardNumber).matches();
		boolean isVisa = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$").matcher(cardNumber).matches();
		boolean isMastercard = Pattern.compile("5[1-5][0-9]{14}$").matcher(cardNumber).matches();
		if (isAmex) {
			return cvv == 4532;
		} else if (isVisa || isMastercard) {
			return cvv == 123;
		}
		return true;
	}

}

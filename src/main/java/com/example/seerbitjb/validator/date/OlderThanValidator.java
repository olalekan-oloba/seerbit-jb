package com.example.seerbitjb.validator.date;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static com.example.seerbitjb.util.CustomDateUtils.nowInstant;

public class OlderThanValidator implements
		ConstraintValidator<OlderThan, Instant> {

	private int age;
	@Override
	public void initialize(OlderThan olderThan) {
		this.age=olderThan.age();
	}

	@Override
	public boolean isValid(Instant requestDate, ConstraintValidatorContext context) {

		Instant currentTime = nowInstant();
		System.out.println(requestDate.until(currentTime, ChronoUnit.SECONDS));

		return requestDate.until(currentTime, ChronoUnit.SECONDS) <= age;

		/*System.out.println("cccc/"+jo);
		System.out.println("now/"+nowInstant());

		Instant instant1 = Instant.parse("2024-01-12T19:46:36.318544Z");
		Instant instant2 = Instant.parse("2024-01-12T19:47:08.327721Z");
		System.out.println(instant2.until(instant1, ChronoUnit.SECONDS));*/

	}


}

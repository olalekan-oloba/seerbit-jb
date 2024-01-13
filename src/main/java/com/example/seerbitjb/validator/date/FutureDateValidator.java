package com.example.seerbitjb.validator.date;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FutureDateValidator implements
		ConstraintValidator<FutureDate, Instant> {


	@Override
	public void initialize(FutureDate customDate) {
	}

	@Override
	public boolean isValid(Instant date, ConstraintValidatorContext context) {

		//convert instant to local date  //todo move to util class
		if(null==date){
			return true;
		}
		ZoneId zone = ZoneId.systemDefault();
		LocalDate requestDate = LocalDate.ofInstant(date, zone);

		return !requestDate.isAfter(LocalDate.now());
	}


}

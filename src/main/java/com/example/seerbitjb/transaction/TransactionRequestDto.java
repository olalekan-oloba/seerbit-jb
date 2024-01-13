package com.example.seerbitjb.transaction;

import com.example.seerbitjb.util.CustomDateUtils;
import com.example.seerbitjb.validator.date.FutureDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDto {
    @NotNull
    @Schema( example ="144.00")
    private BigDecimal amount;
    //custom validator to disallow future txn date
    @NotNull
    @FutureDate
    @Schema( example = CustomDateUtils.DATE_TIME_FORMAT)
    private Instant timeStamp;

    //inner class to overide setter in lombok builder
    public static class TransactionRequestDtoBuilder {
        public TransactionRequestDtoBuilder amount(BigDecimal amount) {
            this.amount=amount.setScale(2, RoundingMode.HALF_UP);
            return this;
        }
    }
}

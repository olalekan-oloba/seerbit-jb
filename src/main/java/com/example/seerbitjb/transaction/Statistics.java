package com.example.seerbitjb.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;

    public void calculateAvrg() {
        if(sum!=null){
           this.avg= sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP) ;
        }
    }
}

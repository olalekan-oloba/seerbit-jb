package com.example.seerbitjb.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private BigDecimal sum=BigDecimal.ZERO;
    private BigDecimal avg=BigDecimal.ZERO;
    private BigDecimal max=BigDecimal.ZERO;
    private BigDecimal min=BigDecimal.ZERO;
    private long count;

    public void calculateAvrg() {
        if(sum!=null){
           this.avg= sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP) ;
        }
    }
}

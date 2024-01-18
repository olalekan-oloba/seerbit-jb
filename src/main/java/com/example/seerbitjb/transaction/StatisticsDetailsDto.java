package com.example.seerbitjb.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("NullAway.Init")
public class StatisticsDetailsDto {
    private String sum;
    private String avg;
    private String max;
    private String min;
    private String count;
}

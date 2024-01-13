package com.example.seerbitjb.algorithmSolution;

import com.example.seerbitjb.AlgorithmSolutions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
public class AlgorithmSolutionsTest {

    @InjectMocks
    AlgorithmSolutions algorithmSolutions;

    @ParameterizedTest
    @MethodSource("intervalPairsProvider")
    void intervalPairsProvider(int[] intervalPairs, int[] maximumSubArray) throws Exception {
      //TODO
    }

    static Stream<Arguments> intervalPairsProvider() {
        return Stream.of(
                arguments(new int[]{1,2,3,4}, new int[]{3,4})
        );
    }
}
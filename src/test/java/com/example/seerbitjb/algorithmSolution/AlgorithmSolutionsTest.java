package com.example.seerbitjb.algorithmSolution;

import com.example.seerbitjb.AlgorithmSolutions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
public class AlgorithmSolutionsTest {

    @InjectMocks
    AlgorithmSolutions algorithmSolutions;

    @ParameterizedTest
    @MethodSource("mergeOverlappingIntervalsProvider")
    void intervalPairsProvider(int[][] intervalPairs, int[][] maximumSubArray) throws Exception {
        //assertEquals(maximumSubArray, algorithmSolutions.mergeOverlappingIntervals(intervalPairs));
    }

    static Stream<Arguments> mergeOverlappingIntervalsProvider() {
        return Stream.of(
                arguments(new int[][]{{ 1, 4 }, { 4, 5 }}, new int[][]{{ 1, 5 }}),
                arguments(new int[][]{{ 1, 3 }, { 4, 6 },{ 8, 10 }, { 15, 18 }}, new int[][]{{ 1, 5 }})

        );
    }
}

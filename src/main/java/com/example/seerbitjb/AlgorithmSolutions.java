package com.example.seerbitjb;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmSolutions {
    public List mergeOverlappingIntervals(int[][] intervalPairs) {

        List nonOverlappingArr = new ArrayList();
        for (int i = 0; i < intervalPairs.length; i++) {
            if (i+1 == intervalPairs.length) {
                break;
            }
            if (isOverlappingIntervalPair(intervalPairs[i], intervalPairs[i+1])) {
                int[] mergedInterval = mergeIntervalPair(intervalPairs[i], intervalPairs[i+1]);
                intervalPairs[i+1] = mergedInterval;
            } else {
                nonOverlappingArr.add(intervalPairs[i]);
            }

        }
        nonOverlappingArr.add(intervalPairs[intervalPairs.length-1]);

        return nonOverlappingArr;
    }

    private int[] mergeIntervalPair(int[] intervalPair1, int[] intervalPair2) {
        //to merge overlapping pair create an array that contains as ist item, the first item in pair1
        //and second item, the second item in pair2
        return new int[]{intervalPair1[0], intervalPair2[1]};
    }

    private boolean isOverlappingIntervalPair(int[] intervalPair1, int[] intervalPair2) {
        //array overlap if second item in pair 1 greater or equal to first item in pair two.
        return intervalPair1[1] >= intervalPair2[0];
    }
}

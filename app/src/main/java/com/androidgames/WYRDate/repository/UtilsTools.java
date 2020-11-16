package com.androidgames.WYRDate.repository;

import java.util.Random;

public class UtilsTools {
    /*
     *Generate a random number
     * @Params
     * range - It's the range that is available to generate the random number
     *
     * return a number between 0 to (range - 1)
    */
    public static int generateRandomNumber(int range) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return random.nextInt(range);
    }
}

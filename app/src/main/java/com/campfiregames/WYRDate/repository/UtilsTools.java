package com.campfiregames.WYRDate.repository;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class UtilsTools {

    /*Genera un numero pseudo-aleatorio (range es el rango del que dispone para elegir un numero)
    *Recuerda que generara un numero entre "0 y (RANGE - 1)"
    */
    public static int generateRamdomNumber(int range) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return random.nextInt(range);
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

}

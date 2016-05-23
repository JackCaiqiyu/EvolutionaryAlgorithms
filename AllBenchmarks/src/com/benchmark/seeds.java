package com.benchmark;

import java.util.Random;

public class seeds {
    public static long [] seeds = {1111, 2222, 3333, 4444, 5555, 6666, 7777, 8888, 9999};

    public static long getSeed(int f){
        if(f >= 1 && f <= 4){
            return seeds[0];
        }else if(f>=9 && f<=10){
            return seeds[1];
        }else if(f>=15 && f<=17){
            return seeds[2];
        }else if(f>=18 && f<=19){
            return seeds[3];
        }else if(f>=21 && f<=23){
            return seeds[4];
        }else if(f>= 24 && f<=25){
            return seeds[5];
        }else if(f>25 && f <=30) {
            return  seeds[6];
        }else{
            return getRandomSeed();
        }
    }


    private static long getRandomSeed(){
        Random rand = new Random();
        return rand.nextLong();
    }

}

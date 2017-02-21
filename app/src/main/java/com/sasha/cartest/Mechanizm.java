package com.sasha.cartest;

/**
 * Created by Sasha on 20.02.2017.
 */

public class Mechanizm {
    protected String mName;

    public Mechanizm (String name){
       mName=name;
   }
    protected String getName(){
        return mName;
    }
}

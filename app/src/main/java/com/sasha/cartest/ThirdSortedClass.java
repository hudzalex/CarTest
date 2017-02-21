package com.sasha.cartest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sasha on 20.02.2017.
 */

public class ThirdSortedClass extends  BaseSortedClass {
    List<Ship> lShip;
    ThirdSortedClass(List<Ship> plains){
        lShip=plains;
        lShip.add(new Ship("Mousenest"));
        lShip.add(new Ship("Catch me who can"));
        lShip.add(new Ship("Essence of Peppermint"));
        lShip.add(new Ship("Who’s Afraid"));
        lShip.add(new Ship("Bull, Bear, and Horse"));
    }
    static Comparator<Ship> sShipComparator = new Comparator<Ship>() {

        public int compare(Ship o1, Ship o2) {
            return o1.mName.compareTo(o2.mName);
        }
    };
    @Override
    List allSort() {
        Collections.sort(lShip,  sShipComparator);
        return lShip;
    }
}

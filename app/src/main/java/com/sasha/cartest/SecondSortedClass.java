package com.sasha.cartest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sasha on 20.02.2017.
 */

public class SecondSortedClass extends  BaseSortedClass {
    List<Plain> lPlain;
    SecondSortedClass(List<Plain> plains){
        lPlain=plains;
        lPlain.add(new Plain("Boeing"));
        lPlain.add(new Plain("Douglas"));
        lPlain.add(new Plain("CASA"));
        lPlain.add(new Plain("Airbus"));
    }
    static Comparator<Plain> sPlainComparator = new Comparator<Plain>() {

        public int compare(Plain o1, Plain o2) {
            return o1.mName.compareTo(o2.mName);
        }
    };
    @Override
    List allSort() {
        Collections.sort(lPlain,  sPlainComparator);
        return lPlain;
    }
}

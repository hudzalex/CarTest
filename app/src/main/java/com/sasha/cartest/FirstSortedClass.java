package com.sasha.cartest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sasha on 20.02.2017.
 */

public class FirstSortedClass extends BaseSortedClass {
    List<Car> lCar;
    FirstSortedClass(List<Car> cars){
        lCar=cars;
        lCar.add(new Car("Volvo"));
        lCar.add(new Car("Reno"));
        lCar.add(new Car("BMW"));
    }
    static Comparator<Car> sCarComparator = new Comparator<Car>() {

        public int compare(Car o1, Car o2) {
            return o1.mName.compareTo(o2.mName);
        }
    };
    @Override
    List allSort() {
        Collections.sort(lCar,  sCarComparator);
        return lCar;
    }
}

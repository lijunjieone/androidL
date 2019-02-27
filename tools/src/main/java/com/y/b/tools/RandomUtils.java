package com.y.b.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static <V> List<V> randomList(List<V> sourceList, int size){
        if (sourceList==null || sourceList.size() == 0) {
            return sourceList;
        }

        if(size>sourceList.size()) {
            return sourceList;
        }

        ArrayList<V> randomList = new ArrayList<V>( size );
        do{
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
            size--;
        }while(size>0);

        return randomList;
    }
}

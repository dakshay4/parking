package com.dakshay.data;

import com.dakshay.models.ParkingTransaction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Dao<T> {

    protected Dao() {}


    public abstract T get(String id);
    public abstract void save(T t);
    public abstract List<T> getActiveTransactions();

}

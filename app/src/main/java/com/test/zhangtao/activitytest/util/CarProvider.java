package com.test.zhangtao.activitytest.util;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.test.zhangtao.activitytest.bean.ShoppingCar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/10/2.
 */
public class CarProvider
{
    private SparseArray<ShoppingCar> data = null;
    private Context mContext;
    private static final String CAR_JSON = "car_json";
    private static CarProvider carProviderInstance;

    private CarProvider()
    {
        data = new SparseArray<>(10);
    }

    public static CarProvider getInstance()
    {
        if (carProviderInstance == null)
        {
            carProviderInstance = new CarProvider();
        }
        return carProviderInstance;
    }

    public void setMyConext(Context context)
    {
        mContext = context;
        listToSpare();
    }

    public void put(ShoppingCar car)
    {
        ShoppingCar temp = data.get(car.getId().intValue());
        if (temp != null)
        {
            temp.setCount(temp.getCount()+1);
        }
        else
        {
            temp = car;
            temp.setCount(1);
        }
        data.put(car.getId().intValue() , temp);
        commit();
    }

    public void update(ShoppingCar car)
    {
        data.put(car.getId().intValue() , car);
        commit();
    }

    public void delete(ShoppingCar car)
    {
        data.remove(car.getId().intValue());
        commit();
    }

    public List<ShoppingCar> getAll()
    {
        return getDataFromLocal();
    }

    public void clear()
    {
        data.clear();
        commit();
    }

    public void commit()
    {
        List<ShoppingCar> cars = spareToList();
        PreferencesUtils.putString(mContext , CAR_JSON , JSONUtil.toJSON(cars));
    }

    private List<ShoppingCar> spareToList()
    {
        int size = data.size();
        List<ShoppingCar> list = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++)
        {
            list.add(data.valueAt(i));
        }
        return list;
    }

    private void listToSpare()
    {
        List<ShoppingCar> cars = getDataFromLocal();

        if (cars != null && cars.size() > 0)
        {
            for (ShoppingCar car : cars)
            {
                data.put(car.getId().intValue() , car);
            }
        }
    }

    public List<ShoppingCar> getDataFromLocal()
    {
        String json = PreferencesUtils.getString(mContext , CAR_JSON);
        List<ShoppingCar> cars = null;
        if (json != null)
        {
            cars = JSONUtil.fromJson(json , new TypeToken<List<ShoppingCar>>(){}.getType());
        }
        return cars;
    }
}

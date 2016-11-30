package com.test.zhangtao.activitytest.city;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by zhangtao on 16/11/5.
 */
public class ProvinceModel implements IPickerViewData
{
    private String name;
    private List<CityModel> cityList;

    public ProvinceModel()
    {
        super();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<CityModel> getCityList()
    {
        return cityList;
    }

    public void setCityList(List<CityModel> cityList)
    {
        this.cityList = cityList;
    }

    @Override
    public String getPickerViewText()
    {
        return name;
    }
}

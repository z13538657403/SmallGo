package com.test.zhangtao.activitytest.city;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by zhangtao on 16/11/5.
 */
public class CityModel implements IPickerViewData
{
    private String name;
    private List<DistrictModel> districtList;

    public CityModel()
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

    public List<DistrictModel> getDistrictList()
    {
        return districtList;
    }

    public void setDistrictList(List<DistrictModel> districtList)
    {
        this.districtList = districtList;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public String getPickerViewText()
    {
        return name;
    }
}

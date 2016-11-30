package com.test.zhangtao.activitytest.city;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 16/11/5.
 */
public class XmlParserHandler extends DefaultHandler
{
    /**
     *   存储所有的解析对象
     */
    private List<ProvinceModel> provinceList = new ArrayList<>();
    private ProvinceModel provinceModel = new ProvinceModel();
    private CityModel cityModel = new CityModel();
    private DistrictModel districtModel = new DistrictModel();

    public XmlParserHandler(){}

    public List<ProvinceModel> getDataList()
    {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException
    {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException
    {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (qName.equals("province"))
        {
            provinceModel = new ProvinceModel();
            provinceModel.setName(attributes.getValue(0));
            provinceModel.setCityList(new ArrayList<CityModel>());
        }
        else if (qName.equals("city"))
        {
            cityModel = new CityModel();
            cityModel.setName(attributes.getValue(0));
            cityModel.setDistrictList(new ArrayList<DistrictModel>());
        }
        else if (qName.equals("district"))
        {
            districtModel = new DistrictModel();
            districtModel.setName(attributes.getValue(0));
            districtModel.setZipcode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if(qName.equals("district"))
        {
            cityModel.getDistrictList().add(districtModel);
        }
        else if(qName.equals("city"))
        {
            provinceModel.getCityList().add(cityModel);
        }
        else if(qName.equals("province"))
        {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        super.characters(ch, start, length);
    }
}

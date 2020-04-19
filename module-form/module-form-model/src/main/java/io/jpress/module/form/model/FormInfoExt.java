package io.jpress.module.form.model;

import io.jboot.db.annotation.Table;
import io.jpress.module.form.model.base.BaseFormInfo;

import java.util.HashMap;

/**
 * Generated by JPress.
 */
@Table(tableName = "form_info", primaryKey = "id")
public class FormInfoExt extends FormInfo {

    private String houseTypeDesc;
    public FormInfoExt(FormInfo info) {
        map.put(0,"选择房型");
        map.put(1,"一室一厅");
        map.put(2,"两室两厅");
        map.put(3,"三室一厅");
        map.put(4,"三室两厅");
        map.put(5,"四室两厅");
        map.put(6,"四室三厅");
        map.put(7,"五室两厅");
        map.put(8,"五室三厅");
        map.put(9,"别墅");
        map.put(10,"大平层");
        map.put(11,"大厦/小区/商超");
        map.put(12,"酒店/宾馆");
        map.put(13,"其他");

        setId(info.getId());
        setName(info.getName());
        setMobile(info.getMobile());
        setAddress(info.getAddress());
        setArea(info.getArea());
        setHousetype(info.getHousetype());
        setHouseTypeDesc(map.get(info.getHousetype()));
        setDesc(info.getDesc());
        setTime(info.getTime());
    }

    private static final long serialVersionUID = 1L;
    private static HashMap<Integer,String> map = new HashMap();

    public String getHouseTypeDesc() {
        return houseTypeDesc;
    }

    public void setHouseTypeDesc(String houseTypeDesc) {
        this.houseTypeDesc = houseTypeDesc;
    }
}

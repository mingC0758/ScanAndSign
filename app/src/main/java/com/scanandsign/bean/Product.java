package com.scanandsign.bean;

import org.litepal.crud.DataSupport;

/**
 * @author mingC
 * @date 2018/1/31
 */
public class Product extends DataSupport{
	private String name;
	private int num;
	private String unit;

	public Product(String name, int num, String unit) {
		this.name = name;
		this.num = num;
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Product{" + "name='" + name + '\'' + ", num=" + num + ", unit='" + unit + '\'' + '}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}

package com.BTClient1;

public class Dict {
	
	private int id;
	private String name;
	private int orders;
	private String desc;
	private int types;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getTypes() {
		return types;
	}
	public void setTypes(int types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "Dict [id=" + id + ", name=" + name + ", orders=" + orders
				+ ", types=" + types + "]";
	}

}

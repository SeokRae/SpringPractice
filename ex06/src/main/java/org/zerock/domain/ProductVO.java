package org.zerock.domain;

/**
 * 모델 객체 만들기
 * @author "SeokRae"
 *
 */
public class ProductVO {
	
	private String name;
	private double price;

	public ProductVO(String name, int price) {
		super();
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductVO [name=" + name + ", price=" + price + "]";
	}

}

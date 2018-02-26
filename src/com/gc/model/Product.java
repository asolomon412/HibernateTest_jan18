package com.gc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable {
	/**
	 * The serialization runtime associates with each serializable class a version
	 * number, called a serialVersionUID, which is used during deserialization to
	 * verify that the sender and receiver of a serialized object have loaded
	 * classes for that object that are compatible with respect to serialization.
	 */
	private static final long serialVersionUID = 1L;
	private int productID;
	private String code;
	private String description;
	private double listPrice;

	
	public Product() {

		// productID = 0;
		// code = "";
		// description = "";
		// listPrice = 0;
	}

	public Product(int productID, String code, String description, double listPrice) {
		super();
		this.productID = productID;
		this.code = code;
		this.description = description;
		this.listPrice = listPrice;
	}

    @Id
    @GeneratedValue
    @Column(name = "productID")
	public int getProductID() {
		return productID;
	}

	
	public void setProductID(int productID) {
		this.productID = productID;
	}
	@Column
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column
	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	@Override
	public String toString() {
		return "Primary key assigned as 0, represents us assigning null to the query: " + productID + ", code=" + code
				+ ", description=" + description + ", listPrice=" + listPrice;
	}

}
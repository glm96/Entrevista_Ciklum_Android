package com.gonzalo.testciklumentrevistaapp;

import android.util.Log;

/**
 * This class represents the main entity used in the database, it stores
 * both inbound and outbound flights, as well as the chosen hotel and the 
 * package price.
 * @author tyrio
 *
 */
public class HolidayPackage {
	private Long id;
	private Flight inbound;
	private Flight outbound;
	private Hotel lodging;
	private double price;
	
	/**
	 * Default constructor, used for Objectify and Jackson object management
	 */
	public HolidayPackage() {}
	
	/**
	 * Constructor used for generating random test data
	 * @param inbound Inbound flight of the package
	 * @param outbound Outbound flight of the package
	 * @param lodging Hotel included on the package
	 * @param price Final price of the package
	 */
	public HolidayPackage(Flight inbound, Flight outbound, Hotel lodging, double price) {
		super();
		this.inbound = inbound;
		this.outbound = outbound;
		this.lodging = lodging;
		this.price = price;
	}
	
	/**
	 * Checks if every parameter on the entity is according to expected format
	 * @return Returns true if it is a correct declaration
	 */
	public boolean checkCorrect() {
		String regex = "\\d+(\\.\\d{1,2})?";
		if (!inbound.checkCorrect()) {
			Log.d("checks", "1");
			return false;
		}
		if (!outbound.checkCorrect()) {
			Log.d("checks", "1");
			return false;
		}
		if(!lodging.checkCorrect()) {
			Log.d("checks", "1");
			return false;
		}
		if(!Double.toString(price).matches(regex)) {
			Log.d("checks", "1");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return id of the package
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id New value of id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 *
	 * @return Flight object representing the inbound flight of the package 
	 */
	public Flight getInbound() {
		return inbound;
	}

	/**
	 * 
	 * @param inbound Inbound flight to append to the package
	 */
	public void setInbound(Flight inbound) {
		this.inbound = inbound;
	}

	/**
	 * 
	 * @return Flight object representing the outbound flight of the package
	 */
	public Flight getOutbound() {
		return outbound;
	}

	/**
	 * 
	 * @param outbound Outbound flight to append to the package
	 */
	public void setOutbound(Flight outbound) {
		this.outbound = outbound;
	}

	/**
	 * 
	 * @return Hotel specified in the package
	 */
	public Hotel getLodging() {
		return lodging;
	}

	/**
	 * 
	 * @param lodging Hotel to include on the package 
	 */
	public void setLodging(Hotel lodging) {
		this.lodging = lodging;
	}

	/**
	 * 
	 * @return price of the package
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * 
	 * @param price Price to be set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inbound == null) ? 0 : inbound.hashCode());
		result = prime * result + ((lodging == null) ? 0 : lodging.hashCode());
		result = prime * result + ((outbound == null) ? 0 : outbound.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HolidayPackage other = (HolidayPackage) obj;
		if (inbound == null) {
			if (other.inbound != null)
				return false;
		} else if (!inbound.equals(other.inbound))
			return false;
		if (lodging == null) {
			if (other.lodging != null)
				return false;
		} else if (!lodging.equals(other.lodging))
			return false;
		if (outbound == null) {
			if (other.outbound != null)
				return false;
		} else if (!outbound.equals(other.outbound))
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		return true;
	}

	
	
}

package jp.co.sss.java_spring_test.form;

import jakarta.validation.constraints.NotBlank;

public class OrderForm {
	
	private Integer totalAmount;
	
	private String status;
	
	@NotBlank(message = "お名前を入力してください")
	private String deliveryName;
	
	@NotBlank(message = "住所を入力してください")
	private String deliveryAddress;
	
	private String deliveryApartment;
	
	private String paymentMethod;

	private String expireMonth;
	
	private String expireYear;
	
	private String cardNumber1;

	private String cardNumber2;
	
	private String cardNumber3;
	
	private String cardNumber4;
	
	private String securityCode;

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	public String getDeliveryApartment() {
		return deliveryApartment;
	}

	public void setDeliveryApartment(String deliveryApartment) {
		this.deliveryApartment = deliveryApartment;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(String expireMonth) {
		this.expireMonth = expireMonth;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(String expireYear) {
		this.expireYear = expireYear;
	}

	public String getCardNumber1() {
		return cardNumber1;
	}

	public void setCardNumber1(String cardNumber1) {
		this.cardNumber1 = cardNumber1;
	}

	public String getCardNumber2() {
		return cardNumber2;
	}

	public void setCardNumber2(String cardNumber2) {
		this.cardNumber2 = cardNumber2;
	}

	public String getCardNumber3() {
		return cardNumber3;
	}

	public void setCardNumber3(String cardNumber3) {
		this.cardNumber3 = cardNumber3;
	}

	public String getCardNumber4() {
		return cardNumber4;
	}

	public void setCardNumber4(String cardNumber4) {
		this.cardNumber4 = cardNumber4;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	
	

}

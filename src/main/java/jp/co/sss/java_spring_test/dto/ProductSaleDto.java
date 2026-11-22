package jp.co.sss.java_spring_test.dto;

import jp.co.sss.java_spring_test.entity.Products;
import jp.co.sss.java_spring_test.entity.SalesItems;

public class ProductSaleDto {

	// フィールド定義
	private Products product;
	private SalesItems saleItem;

	// コンストラクタ
	public ProductSaleDto(Products product, SalesItems saleItem) {
		this.product = product;
		this.saleItem = saleItem;
	}

	// セール中かどうかの判定
	public boolean isSale() {
		return saleItem != null;
	}

	// セール価格計算（税抜・既存互換用に残す）
	public Integer getSalePrice() {
		if (!isSale() || saleItem.getDiscountRate() == null || product.getPrice() == null) {
			return product.getPrice();
		}
		return product.getPrice() * (100 - saleItem.getDiscountRate()) / 100;
	}

	// 実際に適用する税抜単価（セール中なら割引後、通常なら定価）
	public Integer getEffectivePrice() {
		if (!isSale() || saleItem.getDiscountRate() == null || product.getPrice() == null) {
			return product.getPrice();
		}
		return product.getPrice() * (100 - saleItem.getDiscountRate()) / 100;
	}

	// 実際に適用する税込単価（セール中なら includeTax に割引率を適用、通常なら includeTax）
	public Integer getEffectiveIncludeTax() {
		if (product.getIncludeTax() == null) {
			return null;
		}
		if (!isSale() || saleItem.getDiscountRate() == null) {
			return product.getIncludeTax();
		}
		return product.getIncludeTax() * (100 - saleItem.getDiscountRate()) / 100;
	}

	// Getter / Setter
	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public SalesItems getSaleItem() {
		return saleItem;
	}

	public void setSaleItem(SalesItems saleItem) {
		this.saleItem = saleItem;
	}
}
package jp.co.sss.java_spring_test.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "sales_items")
@EntityListeners(AuditingEntityListener.class)
public class SalesItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer saleItemId;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	// referencedColumnName = "productId"は省略。結合カラムが主キーの場合省略可。
	// referencedColumnNameはEntityにあるフィールド名、@JoinCoulmnのnameは自テーブルの物理名を指定する。
	// 今回どちらのテーブルも「product_id」だが、
	// Entityはキャメルケース型で「productId」としているため、分かりづらい。

	private Products product;
	
	@Column
	private Integer companyId;
	
	@Column
	private String saleName;
	
	@Column
	private String description;
	
	@Column
	private Integer discountRate;
	
	@Column
	private String salesImgPath;
	
	@Column
	private Timestamp startMonth;
	
	@Column
	private Timestamp endMonth;
	
	@Column(name = "created_at", updatable = false)
	@CreatedDate
	private Timestamp createdAt;
	
	@Column(name = "updated_at")
	@LastModifiedDate
	private Timestamp updatedAt;

	public Integer getSaleItemId() {
		return saleItemId;
	}

	public void setSaleItemId(Integer saleItemId) {
		this.saleItemId = saleItemId;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}

	public String getSalesImgPath() {
		return salesImgPath;
	}

	public void setSalesImgPath(String salesImgPath) {
		this.salesImgPath = salesImgPath;
	}

	public Timestamp getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Timestamp startMonth) {
		this.startMonth = startMonth;
	}

	public Timestamp getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Timestamp endMonth) {
		this.endMonth = endMonth;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}

package jp.co.sss.java_spring_test.service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import jp.co.sss.java_spring_test.dto.ProductSaleDto;
import jp.co.sss.java_spring_test.entity.Products;
import jp.co.sss.java_spring_test.entity.SalesItems;
import jp.co.sss.java_spring_test.repository.ProductsRepository;
import jp.co.sss.java_spring_test.repository.SalesItemsRepository;

@Service
public class ProductService {

	private final ProductsRepository productsRepository;
	private final SalesItemsRepository salesItemsRepository;
	
	// Constructor
	public ProductService(ProductsRepository productsRepository, SalesItemsRepository salesItemsRepository) {
		
		this.productsRepository = productsRepository;
		this.salesItemsRepository = salesItemsRepository;
	}
	
	public ProductSaleDto getProductSaleDto(Integer productId) {
		Products product = productsRepository.findById(productId).orElse(null);
		if(product == null) {
			return null;
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		SalesItems saleItem = salesItemsRepository.findByProduct_ProductIdAndStartMonthLessThanEqualAndEndMonthGreaterThanEqual(productId, now, now);
		
		return new ProductSaleDto(product, saleItem);
	}
}

package jp.co.sss.java_spring_test.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.java_spring_test.dto.ProductSaleDto;
import jp.co.sss.java_spring_test.entity.Products;
import jp.co.sss.java_spring_test.entity.Reviews;
import jp.co.sss.java_spring_test.entity.SalesItems;
import jp.co.sss.java_spring_test.entity.Users;
import jp.co.sss.java_spring_test.form.CartsForm;
import jp.co.sss.java_spring_test.form.ProductSearchForm;
import jp.co.sss.java_spring_test.form.ReviewForm;
import jp.co.sss.java_spring_test.repository.CategoriesRepository;
import jp.co.sss.java_spring_test.repository.ProductsRepository;
import jp.co.sss.java_spring_test.repository.ReviewsRepository;
import jp.co.sss.java_spring_test.repository.SalesItemsRepository;
import jp.co.sss.java_spring_test.service.ProductService;

@Controller
public class ProductsController {
	
	private final CategoriesRepository categoriesRepository;
	private final SalesItemsRepository salesItemsRepository;
	private final ProductsRepository productsRepository;
	private final ReviewsRepository reviewsRepository;
	private final ProductService productService;
	
	// Constructor
	public ProductsController(SalesItemsRepository salesItemsRepository,CategoriesRepository categoriesRepository, ProductsRepository productsRepository, ReviewsRepository reviewsRepository, ProductService productService) {
		this.salesItemsRepository = salesItemsRepository;
		this.categoriesRepository = categoriesRepository;
		this.productsRepository = productsRepository;
		this.reviewsRepository = reviewsRepository;
		this.productService = productService;
	}
	
	// headerの検索機能用に常に最新の商品カテゴリの情報を取得する
	@ModelAttribute
	public void setupCommonData(ProductSearchForm form, Model model) {
		model.addAttribute("categories", categoriesRepository.findAll());
	}
	
	// TOP画面
	@GetMapping("/userTop")
	public String userTop(Model model) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<SalesItems> saleItemsList = salesItemsRepository.findByStartMonthLessThanEqualAndEndMonthGreaterThanEqual(now, now);
		model.addAttribute("saleItems", saleItemsList);
		return "user/Top";
	}
	
	// 検索機能
	@PostMapping("/products/search")
	public String productSearch(@Valid ProductSearchForm form, Model model) {
		List<Products> productList;

		// 検索フォーム空欄 全件検索
		if (!StringUtils.hasText(form.getProductName()) && form.getCategoryId() == null) {
			productList = productsRepository.findAll();

		// 商品名曖昧検索
		} else if (form.getProductName() != null && form.getCategoryId() == null) {
			productList = productsRepository.findByProductNameContaining(form.getProductName());

		// カテゴリのみ検索
		} else if (!StringUtils.hasText(form.getProductName()) && form.getCategoryId() != null) {
			productList = productsRepository.findByCategoryId(form.getCategoryId());

		// 複数条件検索
		} else {
			productList = productsRepository.findByProductNameContainingAndCategoryId(form.getProductName(), form.getCategoryId());
		}

		List<ProductSaleDto> saleDtoList = new ArrayList<>();
		for (Products product : productList) {
			saleDtoList.add(productService.getProductSaleDto(product.getProductId()));
		}

		model.addAttribute("productList", productList);
		model.addAttribute("saleDtoList", saleDtoList);

		return "user/productList";
	}
	
	// 商品詳細画面
	@GetMapping("/products/detail/{productId}")
	public String productDetile(@PathVariable("productId") Integer productId, @ModelAttribute("cartsForm") CartsForm form, Model model, RedirectAttributes redirectAttributes) {
		
		// Service経由で商品情報＋セール情報のDTO を取得
		ProductSaleDto productSaleDto = productService.getProductSaleDto(productId);
		
		// 商品が存在しない場合のイレギュラー処理
		if(productSaleDto == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "指定された商品は存在しないか、既に削除されています。");
			return "redirect:/userTop";
		}
		
		List<Reviews> reviews = reviewsRepository.findByProductIdOrderByCreatedAtDesc(productId);
		
		model.addAttribute("productSaleDto", productSaleDto);
		model.addAttribute("reviews", reviews);
		
		form.setQuantity(1);
		form.setProductId(productId);
		
		return "user/productDetile";
	}
	
	// 商品レビュー画面
	@GetMapping("/review/input/{productId}")
	public String reviewInput(@PathVariable Integer productId, @ModelAttribute ReviewForm reviewForm, Model model, HttpSession session) {
		
		reviewForm.setProductId(productId);
		Users user = (Users)session.getAttribute("user");
		if (user != null) {
			reviewForm.setUserId(user.getUserId());
			}
		return "user/review";
	}
	
	// 投稿内容の保存
	@Transactional
	@PostMapping("/review/complete")
	public String reviewCreate(@ModelAttribute ReviewForm form) {
		
		Reviews reviews = new Reviews();
		BeanUtils.copyProperties(form, reviews);
		
		reviewsRepository.save(reviews);
		
		return "redirect:/products/detail/" + form.getProductId();
		
	}
	
}
package jp.co.sss.java_spring_test.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.java_spring_test.dto.ProductSaleDto;
import jp.co.sss.java_spring_test.entity.Carts;
import jp.co.sss.java_spring_test.entity.OrderItems;
import jp.co.sss.java_spring_test.entity.Orders;
import jp.co.sss.java_spring_test.entity.Users;
import jp.co.sss.java_spring_test.form.OrderForm;
import jp.co.sss.java_spring_test.form.ProductSearchForm;
import jp.co.sss.java_spring_test.repository.CartsRepository;
import jp.co.sss.java_spring_test.repository.CategoriesRepository;
import jp.co.sss.java_spring_test.repository.OrderItemsRepository;
import jp.co.sss.java_spring_test.repository.OrdersRepository;
import jp.co.sss.java_spring_test.service.ProductService;

@Controller
public class OrderController {

	private final CartsRepository cartsRepository;
	private final CategoriesRepository categoriesRepository;
	private final OrdersRepository ordersRepository;
	private final OrderItemsRepository orderItemsRepository;
	private final ProductService productService;

	public OrderController(
			CategoriesRepository categoriesRepository,
			CartsRepository cartsRepository,
			OrdersRepository ordersRepository,
			OrderItemsRepository orderItemsRepository,
			ProductService productService) {
		this.cartsRepository = cartsRepository;
		this.categoriesRepository = categoriesRepository;
		this.ordersRepository = ordersRepository;
		this.orderItemsRepository = orderItemsRepository;
		this.productService = productService;
	}

	// headerの検索機能用に常に最新の商品カテゴリの情報を取得する
	@ModelAttribute
	public void setupCommonData(ProductSearchForm form, Model model) {
		model.addAttribute("categories", categoriesRepository.findAll());
	}

	// 購入情報入力画面
	@GetMapping("/order")
	public String order(@ModelAttribute OrderForm form, Model model, HttpSession session) {

		Users user = (Users) session.getAttribute("user");
		List<Carts> cartList = cartsRepository.findByUser(user);
		List<ProductSaleDto> saleDtoList = new ArrayList<>();

		int nowYear = java.time.LocalDate.now().getYear();
		int totalPrice = 0;
		int totalQuantity = 0;

		for (Carts cart : cartList) {
			ProductSaleDto dto = productService.getProductSaleDto(cart.getProduct().getProductId());
			saleDtoList.add(dto);

			int unitIncludeTax = (dto != null && dto.getEffectiveIncludeTax() != null)
					? dto.getEffectiveIncludeTax()
					: cart.getProduct().getIncludeTax();
			totalPrice += unitIncludeTax * cart.getQuantity();
			totalQuantity += cart.getQuantity();
		}

		model.addAttribute("cartList", cartList);
		model.addAttribute("saleDtoList", saleDtoList);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("nowYear", nowYear);

		return "user/order";
	}

	// 購入確認画面
	@PostMapping("/order/Check")
	public String orderCheck(@ModelAttribute OrderForm form, Model model, HttpSession session) {

		Users user = (Users) session.getAttribute("user");
		List<Carts> cartList = cartsRepository.findByUser(user);
		List<ProductSaleDto> saleDtoList = new ArrayList<>();

		int totalPrice = 0;
		int totalQuantity = 0;

		for (Carts cart : cartList) {
			ProductSaleDto dto = productService.getProductSaleDto(cart.getProduct().getProductId());
			saleDtoList.add(dto);

			int unitIncludeTax = (dto != null && dto.getEffectiveIncludeTax() != null)
					? dto.getEffectiveIncludeTax()
					: cart.getProduct().getIncludeTax();
			totalPrice += unitIncludeTax * cart.getQuantity();
			totalQuantity += cart.getQuantity();
		}

		model.addAttribute("cartList", cartList);
		model.addAttribute("saleDtoList", saleDtoList);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalQuantity", totalQuantity);

		return "user/orderCheck";
	}

	// 購入完了画面
	@PostMapping("/orderComplete")
	@Transactional
	public String orderComplete(@ModelAttribute OrderForm form, HttpSession session) {

		Users user = (Users) session.getAttribute("user");
		List<Carts> cartList = cartsRepository.findByUser(user);

		int totalAmount = 0;
		for (Carts cart : cartList) {
			ProductSaleDto dto = productService.getProductSaleDto(cart.getProduct().getProductId());
			int unitPrice = (dto != null && dto.getEffectivePrice() != null)
					? dto.getEffectivePrice()
					: cart.getProduct().getPrice();
			totalAmount += unitPrice * cart.getQuantity();
		}

		Orders order = new Orders();
		order.setUser(user);
		order.setDeliveryName(form.getDeliveryName());
		order.setDeliveryAddress(form.getDeliveryAddress());
		order.setDeliveryApartment(form.getDeliveryApartment());
		order.setPaymentMethod(form.getPaymentMethod());
		order.setStatus("未発送");
		order.setTotalAmount(totalAmount);

		Orders savedOrder = ordersRepository.save(order);

		for (Carts cart : cartList) {
			ProductSaleDto dto = productService.getProductSaleDto(cart.getProduct().getProductId());
			int unitPrice = (dto != null && dto.getEffectivePrice() != null)
					? dto.getEffectivePrice()
					: cart.getProduct().getPrice();

			OrderItems orderItem = new OrderItems();
			orderItem.setOrder(savedOrder);
			orderItem.setProduct(cart.getProduct());
			orderItem.setQuantity(cart.getQuantity());
			orderItem.setPrice(unitPrice * cart.getQuantity());

			orderItemsRepository.save(orderItem);
		}

		// カートの中を空にする処理
		cartsRepository.deleteAll(cartList);

		return "user/orderComplete";
	}
}
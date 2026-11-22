package jp.co.sss.java_spring_test.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.java_spring_test.dto.ProductSaleDto;
import jp.co.sss.java_spring_test.entity.Carts;
import jp.co.sss.java_spring_test.entity.Products;
import jp.co.sss.java_spring_test.entity.Users;
import jp.co.sss.java_spring_test.form.CartUpdateForm;
import jp.co.sss.java_spring_test.form.CartsForm;
import jp.co.sss.java_spring_test.form.ProductSearchForm;
import jp.co.sss.java_spring_test.repository.CartsRepository;
import jp.co.sss.java_spring_test.repository.CategoriesRepository;
import jp.co.sss.java_spring_test.repository.ProductsRepository;
import jp.co.sss.java_spring_test.service.ProductService;

@Controller
public class CartsController {

	private final CategoriesRepository categoriesRepository;
	private final CartsRepository cartsRepository;
	private final ProductsRepository productsRepository;
	private final ProductService productService;

	public CartsController(
			CategoriesRepository categoriesRepository,
			CartsRepository cartsRepository,
			ProductsRepository productsRepository,
			ProductService productService) {
		this.categoriesRepository = categoriesRepository;
		this.cartsRepository = cartsRepository;
		this.productsRepository = productsRepository;
		this.productService = productService;
	}

	// headerの検索機能用に常に最新の商品カテゴリの情報を取得する
	@ModelAttribute
	public void setupCommonData(ProductSearchForm form, Model model) {
		model.addAttribute("categories", categoriesRepository.findAll());
	}

	// カート追加処理
	@Transactional
	@PostMapping("/carts/add")
	public String cartAdd(@ModelAttribute CartsForm form, HttpSession session, Model model) {

		Users user = (Users) session.getAttribute("user");
		Products product = productsRepository.getReferenceById(form.getProductId());

		Carts existingCarts = cartsRepository.findByUserAndProduct(user, product);

		if (existingCarts != null) {
			int newQuantity = existingCarts.getQuantity() + form.getQuantity();
			existingCarts.setQuantity(newQuantity);
			cartsRepository.save(existingCarts);
		} else {
			Carts cart = new Carts();
			cart.setQuantity(form.getQuantity());
			cart.setUser(user);
			cart.setProduct(product);
			cartsRepository.save(cart);
		}

		return "redirect:/cart/list";
	}

	// カート内一覧画面
	@GetMapping("/cart/list")
	public String cartList(Model model, HttpSession session) {

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

		return "user/cartList";
	}

	// カート内詳細画面
	@GetMapping("/cart/detile/{cartId}")
	public String cartDetile(@PathVariable Integer cartId, @ModelAttribute CartUpdateForm form, Model model, HttpSession session) {

		Users user = (Users) session.getAttribute("user");
		List<Carts> cartList = cartsRepository.findByUser(user);

		int totalPrice = 0;
		int totalIncludeTax = 0;
		int totalQuantity = 0;

		int cartPrice = 0;
		int cartIncludeTax = 0;
		int cartQuantity = 0;

		Carts cartDetile = cartsRepository.findById(cartId).orElse(null);
		ProductSaleDto selectedDto = null;

		if (cartDetile != null) {
			form.setQuantity(cartDetile.getQuantity());
			form.setCartId(cartDetile.getCartId());
			selectedDto = productService.getProductSaleDto(cartDetile.getProduct().getProductId());

			int unitPrice = (selectedDto != null && selectedDto.getEffectivePrice() != null)
					? selectedDto.getEffectivePrice()
					: cartDetile.getProduct().getPrice();
			int unitIncludeTax = (selectedDto != null && selectedDto.getEffectiveIncludeTax() != null)
					? selectedDto.getEffectiveIncludeTax()
					: cartDetile.getProduct().getIncludeTax();

			totalPrice = unitPrice * cartDetile.getQuantity();
			totalIncludeTax = unitIncludeTax * cartDetile.getQuantity();
			totalQuantity = cartDetile.getQuantity();
		}

		// カート内情報
		for (Carts cart : cartList) {
			ProductSaleDto dto = productService.getProductSaleDto(cart.getProduct().getProductId());
			int unitPrice = (dto != null && dto.getEffectivePrice() != null)
					? dto.getEffectivePrice()
					: cart.getProduct().getPrice();
			int unitIncludeTax = (dto != null && dto.getEffectiveIncludeTax() != null)
					? dto.getEffectiveIncludeTax()
					: cart.getProduct().getIncludeTax();

			cartPrice += unitPrice * cart.getQuantity();
			cartIncludeTax += unitIncludeTax * cart.getQuantity();
			cartQuantity += cart.getQuantity();
		}

		model.addAttribute("cart", cartDetile);
		model.addAttribute("saleDto", selectedDto);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalIncludeTax", totalIncludeTax);
		model.addAttribute("totalQuantity", totalQuantity);

		model.addAttribute("cartPrice", cartPrice);
		model.addAttribute("cartIncludeTax", cartIncludeTax);
		model.addAttribute("cartQuantity", cartQuantity);

		return "user/cartDetile";
	}

	// カート情報更新処理
	@PostMapping("/cart/update")
	@Transactional
	public String cartUpdate(@ModelAttribute CartUpdateForm form, Model model) {

		Carts cart = cartsRepository.findById(form.getCartId()).orElse(null);

		if (cart != null) {
			cart.setQuantity(form.getQuantity());
			cartsRepository.save(cart);
		}

		return "redirect:/cart/detile/" + form.getCartId();
	}

	// カート内商品削除処理
	@GetMapping("/cart/delete/{cartId}")
	@Transactional
	public String cartDelete(@PathVariable Integer cartId) {

		cartsRepository.deleteById(cartId);

		return "redirect:/cart/list";
	}
}
package cn.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.entity.Product;
import cn.service.ProductService;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api")
public class ProductController {
 
	@Autowired ProductService productService;
	
    @RequestMapping("/products")
    public Object products(Model m) {
    	List<Product> ps = productService.listProducts();
    	m.addAttribute("ps", ps);
        return "products";
    }

    @RequestMapping(value="/greet",method = RequestMethod.GET)
    public String greet() {
        return "greet";
    }
}
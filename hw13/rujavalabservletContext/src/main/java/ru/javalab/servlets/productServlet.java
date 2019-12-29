package ru.javalab.servlets;

import ru.javalab.context.ApplicationContext;
import ru.javalab.dto.ProductDto;
import ru.javalab.services.product.AddProduct;
import ru.javalab.services.product.AddProductImpl;
import ru.javalab.services.product.AllProduct;
import ru.javalab.services.product.AllProductImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class productServlet extends HttpServlet {
    private ApplicationContext applicationContext;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AllProduct products = applicationContext.getComponent(AllProductImpl.class, "AllProduct");
        List<ProductDto> list = products.allProducts();
        req.setAttribute("products", list);

        String path = "/WEB-INF/pages/product.ftlh";
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AddProduct product = applicationContext.getComponent(AddProductImpl.class, "AddProduct");
        ProductDto productDto = new ProductDto();
        productDto.setName(req.getParameter("name"));
        productDto.setPrice(Integer.parseInt(req.getParameter("price")));
        product.save(productDto);
        resp.sendRedirect("/product");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        applicationContext = (ApplicationContext) config.getServletContext().getAttribute("context");
    }
}

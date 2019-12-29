package ru.javalab.servlets;

import ru.javalab.context.ApplicationContext;
import ru.javalab.dto.UserDto;
import ru.javalab.services.signIn.SignInService;
import ru.javalab.services.signIn.SignInServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class signUpServlet extends HttpServlet {
    private ApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        applicationContext = (ApplicationContext) config.getServletContext().getAttribute("context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/signUp.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            SignInService signInService = applicationContext.getComponent(SignInServiceImpl.class, "SignInService");
            UserDto userDto = new UserDto();
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            try {
                userDto = signInService.signIn(username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(userDto.getUsername() != null)
                resp.sendRedirect("/product");
            else
                req.getRequestDispatcher("/WEB-INF/pages/signUp.ftlh").forward(req, resp);
    }
}

package com.springapp.mvc.service;

import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 23/7/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DbUser user = userRepository.fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(user.getUserid() + " " +  user.getUsername());
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("userid", user.getUserid());
        httpSession.setAttribute("username", user.getUsername());
        httpSession.setAttribute("name", user.getName());
        setDefaultTargetUrl("/homepage");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

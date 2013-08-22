package com.springapp.mvc.interceptors;

import com.springapp.mvc.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;



public class ApiAuthenticationInterceptor implements HandlerInterceptor{

    private final UserRepository repository;

    @Autowired
    public ApiAuthenticationInterceptor(UserRepository repository) {
        this.repository = repository;
    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = request.getHeader("accessToken");
        Long userid = repository.fetchUseridByAccessToken(token);
        if (userid == null){
            System.out.println("No such token found");
            response.setContentType("application/json");
            byte[] message = "Failed in preHandle, wrong access token".getBytes();
            OutputStream out = response.getOutputStream();
            out.write(message);
            out.close();
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

package com.springapp.mvc.interceptors;

import com.springapp.mvc.data.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 19/7/13
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckAuthorizedInterceptor implements HandlerInterceptor{

    private  UserRepositry userRepositry;

    @Autowired
    public void setCheckAuthorizedInterceptor(UserRepositry userRepository) {
        this.userRepositry = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HttpSession httpSession = request.getSession(false);
        if (httpSession!=null){
            if (httpSession.getAttribute("userid")!=null)
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

package com.horizon.config;

import com.horizon.dto.UserBean;
import com.horizon.util.CnAuthConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1、不需要登录就可以访问的路径
        String requestURI = request.getRequestURI();
        if (requestURI.contains(".") || requestURI.startsWith("/" + CnAuthConstants.RESOURCE_COMMON + "/")) {
            return true;
        }

        // 2、未登录用户，直接拒绝访问
        if (null == request.getSession().getAttribute(CnAuthConstants.FLAG_CURRENTUSER)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("please login first");
            return false;
        } else {
            UserBean currentUser = (UserBean) request.getSession().getAttribute(CnAuthConstants.FLAG_CURRENTUSER);
            // 3、已登录用户，判断是否有资源访问权限
            if (requestURI.startsWith("/" + CnAuthConstants.RESOURCE_MOBILE + "/")
                    && currentUser.havaPermission(CnAuthConstants.RESOURCE_MOBILE)) {
                return true;
            } else if (requestURI.startsWith("/" + CnAuthConstants.RESOURCE_SALARY + "/")
                    && currentUser.havaPermission(CnAuthConstants.RESOURCE_SALARY)) {
                return true;
            } else if(requestURI.startsWith("/" + CnAuthConstants.RESOURCE_INDEX_PAGE)
                    && currentUser.havaPermission(CnAuthConstants.RESOURCE_INDEX_PAGE)) {
                return true;
            } else {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("no auth to visit");
                return false;
            }
        }
    }
}

package vip.bzsy.novelshop.intercepter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.bzsy.novelshop.common.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //getChapterInfo 这个接口的chapter参数  小于3的不被拦截
        String uri = request.getRequestURI();
        if (uri.indexOf("getChapterInfo") > 0 ){
            String chapter = request.getParameter("chapter");
            if (Integer.valueOf(chapter) <= 3){
                return true;
            }
        }


        final String token = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        } else {
            if (null == token || "".equals(token) ) {
                log.info("没有token信息");
                returnJson(response, CommonUtils.BeanToJson(CommonResponse.fail(CommonStatus.TOKEN_LOSE)));
                return false;
            }
        }
        Claims claims = JwtHelper.parseJWT(token, audience.getBase64Secret());
        if (claims == null) {
            log.info("信息失效");
            returnJson(response, CommonUtils.BeanToJson(CommonResponse.fail(CommonStatus.TOKEN_LOSE)));
            return false;
        }
        request.setAttribute("CLAIMS", claims);
        return true;
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            log.error("response error",e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

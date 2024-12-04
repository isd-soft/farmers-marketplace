package com.example.isdfarmersmarket.web.interceptors;

import com.example.isdfarmersmarket.business.configurations.serverinfo.services.RequestTracker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RequestCategorizingInterceptor implements HandlerInterceptor {
   RequestTracker requestTracker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod handlerMethod)  {
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            requestTracker.incrementRequestCount(controllerName);
        }
        return true;
    }
}
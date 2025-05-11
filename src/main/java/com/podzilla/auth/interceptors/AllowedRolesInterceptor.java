package com.podzilla.auth.interceptors;


import com.podzilla.auth.annotations.AllowedRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class AllowedRolesInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request,
                             @NonNull final HttpServletResponse response,
                             @NonNull final Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        AllowedRoles annotation =
                method.getMethodAnnotation(AllowedRoles.class);

        if (annotation == null) {
            annotation = method.getBeanType().getAnnotation(AllowedRoles.class);
        }

        if (annotation == null) {
            return true;
        }

        String header = request.getHeader("X-User-Roles");
        if (header == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Missing roles header");
            return false;
        }

        Set<String> userRoles = Set.of(header.split(","));
        Set<String> allowedRoles = Set.of(annotation.value());

        boolean hasAccess = userRoles.stream().anyMatch(allowedRoles::contains);
        if (!hasAccess) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Access denied");
            return false;
        }

        return true;
    }
}

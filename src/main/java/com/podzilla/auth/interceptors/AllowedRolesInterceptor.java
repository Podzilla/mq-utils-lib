package com.podzilla.auth.interceptors;


import com.podzilla.auth.annotations.AllowedRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
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
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{"
                            + "\"message\": \"Missing roles header\","
                            + "\"status\": \"FORBIDDEN\","
                            + "\"timestamp\": \"" + LocalDateTime.now() + "\""
                            + "}"
            );
            response.getWriter().flush();
            return false;
        }

        Set<String> userRoles = Set.of(header.split(","));
        Set<String> allowedRoles = Set.of(annotation.value());

        boolean hasAccess = userRoles.stream().anyMatch(allowedRoles::contains);
        if (!hasAccess) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{"
                            + "\"message\": \"Access denied\","
                            + "\"status\": \"FORBIDDEN\","
                            + "\"timestamp\": \"" + LocalDateTime.now() + "\""
                            + "}"
            );
            response.getWriter().flush();
            return false;
        }

        return true;
    }
}
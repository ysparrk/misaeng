package dgp.misaeng.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dgp.misaeng.global.exception.CustomExceptionEntity;
import dgp.misaeng.global.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;

    public CustomAccessDeniedHandler(ObjectMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorCode.HANDLE_ACCESS_DENIED.getStatus().value());
        CustomExceptionEntity error = new CustomExceptionEntity(ErrorCode.HANDLE_ACCESS_DENIED.getCode(),ErrorCode.HANDLE_ACCESS_DENIED.getMessage());
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}

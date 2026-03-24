package com.example.periodic_payment.exception;

import com.example.periodic_payment.dto.response.ApiResponse;
import com.example.periodic_payment.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionAdviceHandle {
    // 1. Lỗi Logic Nghiệp vụ
    @ExceptionHandler(ResourceException.class)
    public ApiResponse handleResourceException(ResourceException e) {
        log.error("ResourceException : {} ", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
        // Lưu ý: Nếu constructor ErrorResponse của bạn có nhận 'code', hãy truyền e.getCode() vào.
    }

    // 2. Lỗi Validation (User nhập thiếu trường, sai định dạng)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse validationForm(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {} ", e.getMessage(), e);
        // Tận dụng constructor bắt lỗi Validation bạn đã comment trước đó
        return new ErrorResponse("Dữ liệu đầu vào không hợp lệ", e.getBindingResult().getFieldErrors());
    }

    // 3. Lỗi sai URL (404 Not Found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse handlePageNotFoundException(NoHandlerFoundException e) {
        log.warn("NoHandlerFoundException : {} ", e.getMessage());
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), "Không tìm thấy đường dẫn (API) yêu cầu");
    }

    // 4. Lỗi Database mất kết nối (HikariCP không lấy được connection)
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ApiResponse handleDatabaseConnection(DataAccessResourceFailureException e) {
        log.error("DataAccessResourceFailureException : {} ", e.getMessage(), e);
        return new ApiResponse(ExceptionCode.SYSTEM_BUSY.getCode(), ExceptionCode.SYSTEM_BUSY.getMessage());
    }

    // 5. Lỗi Frontend gửi sai định dạng JSON (VD: truyền String vào cột Integer)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse handleHttpNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException : {} ", e.getMessage(), e);
        return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Định dạng JSON gửi lên không hợp lệ");
    }

//    // 6. Lỗi Quyền truy cập (Spring Security)
//    @ExceptionHandler(AccessDeniedException.class)
//    public ApiResponse handleAccessDeniedException(AccessDeniedException e) {
//        log.warn("AccessDeniedException : {} ", e.getMessage());
//        return new ApiResponse(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thực hiện chức năng này");
//    }

    // 7. Hứng toàn bộ các lỗi còn lại (Tránh lộ Stack Trace ra ngoài)
    @ExceptionHandler(Exception.class)
    public ApiResponse internalServerError(Exception e) {
        log.error("Uncaught Exception : {} ", e.getMessage(), e);
        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống không xác định");
    }

    // Xử lý lỗi 401 - Chưa đăng nhập hoặc Token sai
    @ExceptionHandler(AuthenticationInvalidException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationInvalidException(AuthenticationInvalidException e) {
        log.warn("AuthenticationInvalidException : {} ", e.getMessage());
        // Ở đây giả sử lớp ErrorResponse của bạn có constructor (code, message)
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Xử lý lỗi 403 - Đã đăng nhập nhưng không đủ quyền
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleCustomAccessDeniedException(AccessDeniedException e) {
//        log.warn("AccessDeniedException : {} ", e.getMessage());
//        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleCustomAccessDeniedException(AccessDeniedException e) {
        log.warn("AccessDeniedException : {} ", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}

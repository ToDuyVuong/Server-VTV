package hcmute.kltn.vtv.util;

import hcmute.kltn.vtv.util.exception.*;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.el.MethodNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, 404, "Thông báo", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)

    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, 403, "Thông báo", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntryException(DuplicateEntryException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, 409, "Thông báo", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(UnauthorizedAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, 401, "Thông báo", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(InternalServerErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Thông báo",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookieException(MissingRequestCookieException ex) {

        String errorMessage = "Không có cookie bắt buộc: " + ex.getCookieName();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeaderException(MissingRequestHeaderException ex) {

        String errorMessage = "Không có header bắt buộc: " + ex.getHeaderName();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingCsrfTokenException.class)
    public ResponseEntity<ErrorResponse> handleMissingCsrfTokenException(MissingCsrfTokenException ex) {

        String errorMessage = "Không có CSRF token bắt buộc.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException ex) {

        String errorMessage = "Phương thức không được phép.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, 405, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String errorMessage = "Dữ liệu không hợp lệ.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotFoundException(MethodNotFoundException ex) {

        String errorMessage = "Phương thức không tồn tại.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, 404, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedOperationException(UnsupportedOperationException ex) {

        String errorMessage = "Ngoại lệ hoạt động không được hỗ trợ.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_IMPLEMENTED, 501, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {

        String errorMessage = "Phiên đăng nhập đã hết hạn.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, 401, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {

        String errorMessage = "Phương thức không được hỗ trợ.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, 405, "Thông báo",
                errorMessage);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleDispatcherServletException(Exception ex) {
        String errorMessage = "Không tìm thấy trang.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, 404, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = "Dữ liệu không hợp lệ.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        String errorMessage = "Không tìm thấy tài nguyên.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, 404, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ErrorResponse> handleJwtExpiredException(JwtExpiredException ex) {
        String errorMessage = "Phiên đăng nhập đã hết hạn.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, 401, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Dữ liệu không hợp lệ.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        String errorMessage = "Dữ liệu không hợp lệ.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, 400, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        String errorMessage = "Tài khoản hoặc mật khẩu không chính xác.";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, 401, "Thông báo", errorMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }








}

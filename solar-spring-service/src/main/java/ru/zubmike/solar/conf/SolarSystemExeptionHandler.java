package ru.zubmike.solar.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.zubmike.solar.utils.NotFoundException;

@ControllerAdvice
public class SolarSystemExeptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolarSystemExeptionHandler.class);

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseEntity handle(Exception exception) {
		String message = exception.getMessage();
		if (exception instanceof IllegalArgumentException) {
			return createResponse(HttpStatus.BAD_REQUEST).body(message);
		} else if (exception instanceof NotFoundException
				|| exception instanceof MethodArgumentTypeMismatchException
				|| exception instanceof MethodArgumentNotValidException) {
			return createResponse(HttpStatus.NOT_FOUND).body("Not found");
		} else if (exception instanceof HttpRequestMethodNotSupportedException) {
			return createResponse(HttpStatus.METHOD_NOT_ALLOWED).body(message);
		} else if (exception instanceof HttpMediaTypeNotSupportedException) {
			return createResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported current content type");
		} else {
			LOGGER.error(message, exception);
			return createResponse(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal service error");
		}
	}

	private ResponseEntity.BodyBuilder createResponse(HttpStatus status) {
		return ResponseEntity.status(status).header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
	}
}

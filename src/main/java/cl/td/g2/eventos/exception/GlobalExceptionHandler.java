package cl.td.g2.eventos.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * GlobalExceptionHandler es una clase que maneja globalmente las excepciones
 * lanzadas en la aplicación.
 * Proporciona respuestas de error personalizadas tanto para solicitudes de API
 * como para vistas HTML.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Verifica si la solicitud es para una API.
     * 
     * @param request La solicitud HTTP.
     * @return true si la solicitud es para una API, false de lo contrario.
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }

    /**
     * Maneja excepciones globalmente, tanto para API como para vistas HTML.
     * 
     * @param exception La excepción que se lanzó.
     * @param request   La solicitud HTTP.
     * @param model     El modelo para la vista.
     * @return Un objeto que representa la respuesta, ya sea JSON o una vista.
     */
    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception exception, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            // Si es una API, retorna JSON
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // Si es una vista, retorna una página HTML personalizada
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message", exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Captura y maneja la excepción de entidad no encontrada.
     * 
     * @param exception La excepción de tipo NotFoundException que se lanzó.
     * @param request   La solicitud HTTP.
     * @param model     El modelo para la vista.
     * @return Un objeto que representa la respuesta, ya sea JSON o una vista.
     */
    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFoundException(NotFoundException exception, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(),
                    exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("message", exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Captura y maneja la excepción de solicitud incorrecta.
     * 
     * @param exception La excepción de tipo BadRequestException que se lanzó.
     * @param request   La solicitud HTTP.
     * @param model     El modelo para la vista.
     * @return Un objeto que representa la respuesta, ya sea JSON o una vista.
     */
    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequestException(BadRequestException exception, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                    exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Maneja la excepción MethodArgumentNotValidException, que se produce cuando
     * hay errores de validación
     * en los argumentos de los métodos.
     * 
     * @param exception La excepción que se lanzó.
     * @param request   La solicitud HTTP.
     * @param model     El modelo para la vista.
     * @return Un objeto que representa la respuesta, ya sea JSON o una vista.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
            HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            Map<String, Object> errorMap = new HashMap<>();
            exception.getBindingResult().getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                    errorMap.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", "Error de validación de datos: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Maneja la excepción HttpRequestMethodNotSupportedException, que se produce
     * cuando se realiza una
     * solicitud a un método no permitido.
     * 
     * @param exception La excepción que se lanzó.
     * @param request   La solicitud HTTP.
     * @param model     El modelo para la vista.
     * @return Un objeto que representa la respuesta, ya sea JSON o una vista.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
                    HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
        } else {
            model.addAttribute("statusCode", HttpStatus.METHOD_NOT_ALLOWED.value());
            model.addAttribute("message", "Método no permitido: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    /**
     * Maneja excepciones de tipo RuntimeException que pueden ocurrir durante la
     * ejecución de la aplicación.
     * 
     * @param exception La excepción de tipo RuntimeException que se lanzó.
     * @param request   La solicitud HTTP.
     * @param model     El modelo para la vista.
     * @return Un objeto que representa la respuesta, ya sea JSON o una vista.
     */
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException exception, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message", "Error interno del servidor: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Manejo de DataIntegrityViolationException.
     *
     * @param exception la excepción DataIntegrityViolationException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolationException(DataIntegrityViolationException exception,
            HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(),
                    "Conflicto de datos: " + exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } else {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.value());
            model.addAttribute("message", "Conflicto de datos: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.CONFLICT);
        }
    }

    /**
     * Manejo de AccessDeniedException.
     *
     * @param exception la excepción AccessDeniedException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request,
            Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(),
                    "Acceso denegado: " + exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } else {
            model.addAttribute("statusCode", HttpStatus.FORBIDDEN.value());
            model.addAttribute("message", "Acceso denegado: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Manejo de MethodArgumentTypeMismatchException.
     *
     * @param exception la excepción MethodArgumentTypeMismatchException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
            HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                    "Tipo de argumento no válido: " + exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", "Tipo de argumento no válido: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Manejo de HttpClientErrorException.
     *
     * @param exception la excepción HttpClientErrorException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public Object handleHttpClientErrorException(HttpClientErrorException exception, HttpServletRequest request,
            Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                    exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", "Error de cliente: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Manejo de HttpMediaTypeNotSupportedException.
     *
     * @param exception la excepción HttpMediaTypeNotSupportedException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception,
            HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Tipo de medio no soportado: " + exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else {
            model.addAttribute("statusCode", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
            model.addAttribute("message", "Tipo de medio no soportado: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    /**
     * Manejo de ConstraintViolationException.
     *
     * @param exception la excepción ConstraintViolationException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request,
            Model model) {
        if (isApiRequest(request)) {
            Map<String, Object> errorMap = new HashMap<>();
            exception.getConstraintViolations().forEach(violation -> {
                errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
            });
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                    errorMap.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", "Error de validación: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Manejo de SQLException.
     *
     * @param exception la excepción SQLException
     * @param request   la solicitud HTTP
     * @param model     el modelo para la vista
     * @return una respuesta JSON o una vista HTML según el tipo de solicitud
     */
    @ExceptionHandler(SQLException.class)
    public Object handleSQLException(SQLException exception, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error en la base de datos: " + exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message", "Error en la base de datos: " + exception.getMessage());
            return new ModelAndView("error", model.asMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Manejo de HttpMessageNotReadableException.
     * Esta excepción se lanza cuando el cuerpo de la solicitud no se puede leer
     * (por ejemplo, cuando el JSON está mal formado).
     *
     * @param exception La excepción lanzada al intentar leer el cuerpo de la
     *                  solicitud.
     * @param request   El objeto HttpServletRequest que contiene información sobre
     *                  la solicitud.
     * @param model     El modelo para pasar datos a la vista.
     * @return Un objeto que representa la respuesta de error para la API o la vista
     *         HTML.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
            HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            // Si es una API, retorna un JSON con el mensaje de error
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                    "Error en el cuerpo de la solicitud: " + exception.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            // Si es una vista, retorna una página HTML personalizada con el mensaje de
            // error
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
            model.addAttribute("message", "Error en el cuerpo de la solicitud: " + exception.getMessage());
            return new ModelAndView("error-page", model.asMap(), HttpStatus.BAD_REQUEST);
        }
    }
}
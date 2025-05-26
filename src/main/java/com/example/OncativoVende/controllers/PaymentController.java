package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.mp.PaymentRequestDTO;
import com.example.OncativoVende.dtos.mp.PaymentResponseDTO;
import com.example.OncativoVende.services.SubscriptionService;
import com.example.OncativoVende.services.implementation.MercadoPagoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Value("${mercadopago.public-key}")
    private String publicKey;

    @PostMapping("/create-preference")
    public ResponseEntity<?> createPreference(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        try {
            PaymentResponseDTO response = mercadoPagoService.createPreference(paymentRequest);
            return ResponseEntity.ok(response);

        } catch (MPApiException e) {
            logger.error("Error de API al crear preferencia: {}", e.getMessage());
            logger.error("Status Code: {}", e.getStatusCode());
            logger.error("Response: {}", e.getApiResponse());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en la API de MercadoPago");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("statusCode", e.getStatusCode());
            errorResponse.put("apiResponse", e.getApiResponse());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (MPException e) {
            logger.error("Error de MercadoPago al crear preferencia: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno de MercadoPago");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        } catch (Exception e) {
            logger.error("Error inesperado al crear preferencia: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("message", "Error inesperado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/validate-credentials")
    public ResponseEntity<?> validateCredentials() {
        try {
            // Intentar obtener información de la cuenta para validar credenciales
            com.mercadopago.client.user.UserClient client = new com.mercadopago.client.user.UserClient();
            com.mercadopago.resources.user.User user = client.get();

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("country", user.getCountryId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error validando credenciales: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("valid", "false");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/public-key")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publicKey", publicKey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> recibirWebhook(@RequestBody Map<String, Object> payload) {
        logger.info("Webhook recibido: {}", payload);

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if (data == null || data.get("id") == null) {
            return ResponseEntity.badRequest().body("Falta el campo 'data.id'");
        }

        String paymentId = String.valueOf(data.get("id"));
        String resourceUrl = "https://api.mercadopago.com/v1/payments/" + paymentId;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(resourceUrl))
                    .header("Authorization", "Bearer " + "TU_ACCESS_TOKEN")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("Respuesta de MercadoPago: {}", response.body());

            if (response.statusCode() != 200) {
                logger.warn("Fallo en la consulta del recurso. Código: {}", response.statusCode());
                return ResponseEntity.status(response.statusCode()).body("Error consultando pago en MercadoPago");
            }

            // Parsear JSON de respuesta
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> paymentData = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});

            // Verificar estado del pago
            String status = (String) paymentData.get("status");
            if ("approved".equalsIgnoreCase(status)) {
                // Aquí va la lógica para activar la suscripción
                // Por ejemplo, obtener el usuario a partir del email u otro campo
                Map<String, Object> payer = (Map<String, Object>) paymentData.get("payer");
                String email = payer != null ? (String) payer.get("email") : null;
                String tittle = (String) paymentData.get("title");
                String subscription = "";
                switch (tittle) {
                    case "Suscripción Bronce":
                        subscription = "Bronce";
                    case "Suscripción Plata":
                        subscription = "Plata";
                    case "Suscripción Oro":
                        subscription = "Oro";
                    default:
                        subscription = "Desconocida";

                }

                if (email != null) {

                    logger.info(subscription);
                    logger.info("Suscripción activada para el usuario: {}", email);
                } else {
                    logger.warn("No se encontró email del pagador en la respuesta");
                }
            } else {
                logger.info("Pago no aprobado o en otro estado: {}", status);
            }

        } catch (Exception e) {
            logger.error("Error al consultar el recurso de MercadoPago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }

        return ResponseEntity.ok().build();
    }


    @GetMapping("/test-mp-token")
    public ResponseEntity<String> testToken() {
        try {
            mercadoPagoService.validateAccessToken();
            return ResponseEntity.ok("✅ Token válido");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("❌ Token inválido: " + e.getMessage());
        }
    }
}

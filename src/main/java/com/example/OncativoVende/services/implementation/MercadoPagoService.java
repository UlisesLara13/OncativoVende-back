package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.mp.ItemDTO;
import com.example.OncativoVende.dtos.mp.PaymentRequestDTO;
import com.example.OncativoVende.dtos.mp.PaymentResponseDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MercadoPagoService {

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoService.class);

    @Value("APP_USR-5492132292908711-052522-0052e108423b28d476166cb21e51737d-156700574")
    private String accessToken;

    @Value("http://localhost:4200")
    private String baseUrl;

    @Value("https://organic-mako-curious.ngrok-free.app/payments/webhook")
    private String webhookUrl;

    @PostConstruct
    public void init() {
        try {
            // Validar que el access token tenga el formato correcto
            if (accessToken == null || accessToken.trim().isEmpty()) {
                throw new IllegalStateException("Access token no configurado");
            }

            if (!accessToken.startsWith("APP_USR-")) {
                throw new IllegalStateException("Access token tiene formato inválido. Debe empezar con 'APP_USR-'");
            }

            // Configurar el access token globalmente
            MercadoPagoConfig.setAccessToken(accessToken);
            logger.info("MercadoPago configurado correctamente con token: {}****",
                    accessToken.substring(0, Math.min(15, accessToken.length())));
        } catch (Exception e) {
            logger.error("Error al configurar MercadoPago: {}", e.getMessage());
            throw new RuntimeException("No se pudo configurar MercadoPago", e);
        }
    }

    public PaymentResponseDTO createPreference(PaymentRequestDTO paymentRequest) throws MPException, MPApiException {

        // Validar que el access token esté configurado
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new IllegalStateException("Access token de MercadoPago no configurado");
        }

        // Debug: mostrar información del token (sin exponer datos sensibles)
        logger.debug("Usando access token: {}****", accessToken.substring(0, Math.min(15, accessToken.length())));

        try {
            // Crear cliente de preferencias
            PreferenceClient client = new PreferenceClient();

            // Crear items de la preferencia
            List<PreferenceItemRequest> items = createPreferenceItems(paymentRequest);

            // URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(baseUrl + "/payment-success")
                    .failure(baseUrl + "/payment-failure")
                    .pending(baseUrl + "/payment-pending")
                    .build();

            // Crear la preferencia con Builder
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .externalReference(generateExternalReference())
                    .statementDescriptor("ONCATIVO VENDE")
                    .expires(false)
                    .notificationUrl(webhookUrl != null && !webhookUrl.trim().isEmpty() ? webhookUrl : null)
                    .build();

            logger.debug("Creando preferencia con external reference: {}", preferenceRequest.getExternalReference());

            Preference preference = client.create(preferenceRequest);

            logger.info("Preferencia creada exitosamente con ID: {}", preference.getId());
            logger.debug("External Reference: {}", preference.getExternalReference());

            return new PaymentResponseDTO(
                    preference.getId(),
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint()
            );

        } catch (MPApiException e) {
            logger.error("Error de API de MercadoPago:");
            logger.error("Mensaje: {}", e.getMessage());
            logger.error("Status Code: {}", e.getStatusCode());
            logger.error("Response: {}", e.getApiResponse());

            // Agregar información específica para el error 401
            if (e.getStatusCode() == 401) {
                logger.error("ERROR 401: Verifica que tu access token sea válido y tenga los permisos correctos");
                logger.error("Token actual (primeros 15 chars): {}****",
                        accessToken.substring(0, Math.min(15, accessToken.length())));
            }

            throw e;
        } catch (MPException e) {
            logger.error("Error de MercadoPago: {}", e.getMessage());
            if (e.getCause() != null) {
                logger.error("Causa: {}", e.getCause().getMessage());
            }
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al crear preferencia: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el pago", e);
        }
    }

    private List<PreferenceItemRequest> createPreferenceItems(PaymentRequestDTO paymentRequest) {
        List<PreferenceItemRequest> items = new ArrayList<>();

        if (paymentRequest.getItems() != null && !paymentRequest.getItems().isEmpty()) {
            for (ItemDTO item : paymentRequest.getItems()) {
                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice().setScale(2, RoundingMode.HALF_UP))
                        .currencyId("ARS")
                        .build();
                items.add(itemRequest);
            }
        } else {
            // Item por defecto si no se proporcionan items específicos
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(paymentRequest.getDescription() != null ? paymentRequest.getDescription() : "Producto")
                    .description(paymentRequest.getDescription() != null ? paymentRequest.getDescription() : "Compra en Oncativo Vende")
                    .quantity(1)
                    .unitPrice(paymentRequest.getAmount().setScale(2, RoundingMode.HALF_UP))
                    .currencyId("ARS")
                    .build();
            items.add(itemRequest);
        }

        return items;
    }

    private String generateExternalReference() {
        return "ONCATIVO-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    // Método para validar la configuración
    public boolean isConfigured() {
        return accessToken != null && !accessToken.trim().isEmpty();
    }

    // Método para debug - verificar si el token es válido
    public void validateAccessToken() {
        try {
            PreferenceClient client = new PreferenceClient();

            // Crear una preferencia mínima para probar el token
            List<PreferenceItemRequest> testItems = new ArrayList<>();
            PreferenceItemRequest testItem = PreferenceItemRequest.builder()
                    .title("Test")
                    .quantity(1)
                    .unitPrice(new BigDecimal("1.00"))
                    .currencyId("ARS")
                    .build();
            testItems.add(testItem);

            PreferenceRequest testRequest = PreferenceRequest.builder()
                    .items(testItems)
                    .externalReference("TEST-" + System.currentTimeMillis())
                    .build();

            Preference preference = client.create(testRequest);
            logger.info("Token válido - Preferencia de prueba creada: {}", preference.getId());

        } catch (MPApiException e) {
            logger.error("Token inválido - Error {}: {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Access token inválido: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error al validar token: {}", e.getMessage());
            throw new RuntimeException("Error al validar access token: " + e.getMessage());
        }
    }
}
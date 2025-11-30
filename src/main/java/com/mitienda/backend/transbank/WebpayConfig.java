package com.mitienda.backend.transbank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebpayConfig {

    // Ambiente de integración (Transbank oficial)
    public static final String API_BASE_URL = "https://webpay3gint.transbank.cl/rswebpaytransaction/api/webpay/v1.2/transactions";

    // Credenciales de integración (permitidas para pruebas)
    public static final String API_KEY_ID = "597055555532";
    public static final String API_KEY_SECRET = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C";

}

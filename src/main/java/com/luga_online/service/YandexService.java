package com.luga_online.service;

import com.yandex.money.api.methods.payment.ProcessPayment;
import com.yandex.money.api.methods.payment.RequestPayment;
import com.yandex.money.api.methods.payment.params.PhoneParams;
import com.yandex.money.api.methods.wallet.AccountInfo;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.clients.DefaultApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.luga_online.util.Properties.YANDEX_APP_ID;
import static com.luga_online.util.Properties.YANDEX_TOKEN;
import static com.luga_online.util.Utils.pauseVk;

@Service
@Slf4j
public class YandexService {
    private final ApiClient client = new DefaultApiClient.Builder()
            .setClientId(YANDEX_APP_ID)
            .create();

    public String payToUser(String phone, String money) throws Exception {

        client.setAccessToken(YANDEX_TOKEN);
        AccountInfo info = client.execute(new AccountInfo.Request());
//        log("YANDEX BALANCE ", info.balance);

        BigDecimal amount = new BigDecimal(money);
        PhoneParams phoneParams = PhoneParams.newInstance(phone, amount);
        RequestPayment requestPayment = client.execute(RequestPayment.Request.newInstance(phoneParams));


//        log("YANDEX requestPayment.error ", requestPayment.error);
//        log("YANDEX requestPayment.requestId ", requestPayment.requestId);
//        log("YANDEX requestPayment.status.code ", requestPayment.status.code);

        if (requestPayment.error != null) {
            return requestPayment.error.code;
        }
        pauseVk();
        pauseVk();

        ProcessPayment processPayment = client.execute(
                new ProcessPayment.Request(requestPayment.requestId));
        pauseVk();
//        log("YANDEX processPayment.error ", processPayment.error);
//        log("YANDEX processPayment.status.code ", processPayment.status.code);
//        log("YANDEX processPayment.balance ", processPayment.balance);
        if (processPayment.error != null) {
            return processPayment.error.code;
        }
        return processPayment.status.code;
    }
}

package com.luga_online.controller;

import com.yandex.money.api.authorization.AuthorizationData;
import com.yandex.money.api.authorization.AuthorizationParameters;
import com.yandex.money.api.methods.Token;
import com.yandex.money.api.methods.payment.ProcessPayment;
import com.yandex.money.api.methods.payment.RequestPayment;
import com.yandex.money.api.methods.payment.params.PhoneParams;
import com.yandex.money.api.methods.wallet.AccountInfo;
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.clients.DefaultApiClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

import static com.luga_online.util.Properties.*;
import static com.yandex.money.api.model.Scope.*;

@Controller
public class YandexController {

    private ApiClient client = new DefaultApiClient.Builder()
            .setClientId(YANDEX_APP_ID)
            .create();

    @GetMapping("/yaCode")
    public void payMeTest(HttpServletResponse resp) throws Exception {
        LimitedScope shopLimitedScope = createPaymentShopLimitedScope()
                .setSum(new BigDecimal(50000))
                .setDuration(1);
        AuthorizationParameters parameters = new AuthorizationParameters.Builder()
                .addScope(ACCOUNT_INFO)
                .addScope(OPERATION_DETAILS)
                .addScope(OPERATION_HISTORY)
//                .addScope(PAYMENT_SHOP)
                .addScope(shopLimitedScope)
                .addScope(Scope.createMoneySourceScope())
                .setRedirectUri(YANDEX_REDIRECT_URI)
                .create();

        AuthorizationData data = client.createAuthorizationData(parameters);
        String url = data.getUrl() + "?" + new String(data.getParameters());
        resp.sendRedirect(url);
    }

    @GetMapping("/ya")
    public String payMeTestResponse(String code) throws Exception {
//        AuthorizationCodeResponse response = AuthorizationCodeResponse.parse(data.getUrl());
//        if (response.error == null) {
        Token token = client.execute(new Token.Request(code, client.getClientId(), YANDEX_REDIRECT_URI));

        System.out.println(token.error);
        System.out.println(token.accessToken);
        System.out.println(token.toString());
        if (token.error == null) {
            client.setAccessToken(token.accessToken);
        }
//            else {handleAuthorizationError(token.error); }
//          } else { handleAuthorizationError(response.error); }

//        payMeTestPay(token.accessToken);
        return "hello";
    }

    @GetMapping("/yatestpay")
    public String payMeTestPay(String token) throws Exception {
        if (token == null) {
            client.setAccessToken(YANDEX_TOKEN);
        } else {
            client.setAccessToken(token);
        }

        AccountInfo info = client.execute(new AccountInfo.Request());
        System.out.println(info.balance);

        BigDecimal amount = new BigDecimal("2.17");
//        PhoneParams phoneParams = PhoneParams.newInstance("79219241224", amount);
        PhoneParams phoneParams = PhoneParams.newInstance("79006294991", amount);

        RequestPayment requestPayment = client.execute(RequestPayment.Request.newInstance(phoneParams));

        System.out.println(requestPayment.error);
        System.out.println(requestPayment.requestId);
        System.out.println(requestPayment.status.code);
        System.out.println(requestPayment.balance);

        ProcessPayment processPayment = client.execute(
                new ProcessPayment.Request(requestPayment.requestId));

        System.out.println(processPayment.error);
        System.out.println(processPayment.status.code);
        System.out.println(processPayment.balance);

        return "hello";
    }
}

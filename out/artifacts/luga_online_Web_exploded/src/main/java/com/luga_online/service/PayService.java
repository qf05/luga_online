package com.luga_online.service;

import com.luga_online.util.Properties;
import com.yandex.money.api.authorization.AuthorizationData;
import com.yandex.money.api.authorization.AuthorizationParameters;
import com.yandex.money.api.methods.Token;
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.AuthorizationCodeResponse;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.clients.DefaultApiClient;

import java.net.URI;

public class PayService {

    public static void pay() {

    }

//    public static void getToken() {
//        ApiClient client = new DefaultApiClient.Builder()
//                .setClientId(Properties.YANDEX_APP_ID)
//                .create();
//        AuthorizationParameters parameters = new AuthorizationParameters.Builder()
//                .addScope(Scope.ACCOUNT_INFO)
//                .addScope(Scope.PAYMENT_P2P)
//                .setRedirectUri("localhost:8080")
//                .setResponseType("code")
//                .create();
//        AuthorizationData data = client.createAuthorizationData(parameters);
//        String url = data.getUrl() + "?" + new String(data.getParameters());
//
//
//        // parse redirect uri from web browser
//        AuthorizationCodeResponse response = AuthorizationCodeResponse.parse(redirectUri);
//
//        if (response.error == null) {
//            // try to get OAuth2 access token
//            Token token = client.execute(new Token.Request(response.code, client.getClientId(),
//                    myRedirectUri, myClientSecret));
//            if (token.error == null) {
//        // store token.accessToken safely for future uses
//                System.out.println(token.accessToken);
//                // and authorize client to perform methods that require user's authentication
//                client.setAccessToken(token.accessToken);
//            } else {
////                handleAuthorizationError(token.error);
//            }
//        } else {
////            handleAuthorizationError(response.error);
//        }
//    }

    private static void initPayService() {

    }
}

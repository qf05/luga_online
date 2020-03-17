package com.luga_online.service;

import com.luga_online.model.Pay;
import com.luga_online.model.User;
import com.luga_online.repository.PayRepository;
import com.yandex.money.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PayService {

    private final UserService userService;
    private final YandexService yandexService;
    private final PayRepository payRepository;

    public PayService(UserService userService, YandexService yandexService, PayRepository payRepository) {
        this.userService = userService;
        this.yandexService = yandexService;
        this.payRepository = payRepository;
    }

    public void setPhone(User user, String phone) {
        user.setTel(phone);
        userService.updateUser(user);
    }

    public List<Pay> getHistoryPay(Integer userId) {
        List<Pay> pays = payRepository.getAllByUserVkIdOrderByTimeDesc(userId);
        return pays.stream().filter(i -> i.getResult().contains("OK!")).collect(Collectors.toList());
    }

    public String pay(Integer userId, Double moneyD) {
        Pay pay = payRepository.findTopByUserVkIdOrderByTimeDesc(userId);
        if (pay.getTime() + 10000 > Calendar.getInstance().getTimeInMillis()) {
            return "Слишком много запросов, повторите попытку через 1 минуту.";
        }
        if (moneyD == null || moneyD <= 5.0) {
            return "Не корректная сумма вывода. \n Минимальная сумма вывода 5 рублей.";
        }
        User user = userService.findUser(userId);
        long money = (long) (moneyD * 100);
        if (money < user.getMoney()) {
            return "Недостаточно средств";
        }
        String payStatus = "Error";
        String responsePay;
        try {
            responsePay = yandexService.payToUser(user.getTel(), String.valueOf((double) money / 100));
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка сервера";
        }
        if (responsePay != null)
            switch (responsePay) {
                case Constants.Status.EXT_AUTH_REQUIRED:
                case Constants.Status.REFUSED:
                    payStatus = "Ошибка вывода средств";
                    saveResultPay(user, money, responsePay);
                    break;
                case Constants.Status.IN_PROGRESS:
                    payStatus = "OK! Ожидайте, средства скоро будут зачислены. \n В случае не поступления средств в течении нескольких часов обратитесь в поддержку сайта.";
                    updateUserAfterPay(user, money, responsePay);
                    break;
                case Constants.Status.SUCCESS:
                    payStatus = "OK! Вывод средств осуществлён!";
                    updateUserAfterPay(user, money, responsePay);
                    break;
                default:
                    payStatus = "Error";
            }
        return payStatus;
    }

    @Transactional
    void updateUserAfterPay(User user, long money, String result) {
        user.setMoney(user.getMoney() - money);
        userService.updateUser(user);
        saveResultPay(user, money, result);
    }

    private void saveResultPay(User user, long money, String result) {
        payRepository.save(new Pay(user, money, result, Calendar.getInstance().getTimeInMillis()));
    }

}

package com.luga_online.service.Admin;

import com.luga_online.model.Pay;
import com.luga_online.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.luga_online.util.Utils.convertTime;

@Service
@Slf4j
public class AdminPayService {

    private final PayService payService;

    public AdminPayService(PayService payService) {
        this.payService = payService;
    }

    public List<Pay> getAllPays() {
        return payService.getAllPays();
    }

    public void removePay(Integer payId) {
        payService.removePay(payId);
    }

    public List<Pay> getFilterPays(Integer vkId,
                                   Long minMoney,
                                   Long maxMoney,
                                   String result,
                                   String excludeResult,
                                   String startTime,
                                   String endTime) {
        Long finalSTime = convertTime(startTime);
        Long finalETime = convertTime(endTime);
        return getAllPays().stream().filter(i ->
                (vkId == null || vkId.equals(i.getUser().getVkId()))
                        && (minMoney == null || minMoney < i.getMoney())
                        && (maxMoney == null || maxMoney > i.getMoney())
                        && (result == null || i.getResult().contains(result))
                        && (excludeResult == null || !i.getResult().contains(excludeResult))
                        && (finalSTime == null || finalSTime < i.getTime())
                        && (finalETime == null || finalETime > i.getTime())
        ).collect(Collectors.toList());
    }
}

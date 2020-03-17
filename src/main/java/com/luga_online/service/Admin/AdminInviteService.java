package com.luga_online.service.Admin;

import com.luga_online.model.Invite;
import com.luga_online.service.InviteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.luga_online.util.Utils.convertTime;

@Service
@Slf4j
public class AdminInviteService {

    private final InviteService inviteService;

    public AdminInviteService(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    public List<Invite> getAllInvite() {
        return inviteService.getAllInvite();
    }

    public void removeInvite(Integer inviteId) {
        inviteService.removeInvite(inviteId);
    }

    public List<Invite> getFilterInvite(Integer vkId,
                                        Integer groupId,
                                        Integer invitedId,
                                        Integer result,
                                        Integer excludeResult,
                                        String startTime,
                                        String endTime) {
        Long finalSTime = convertTime(startTime);
        Long finalETime = convertTime(endTime);
        return getAllInvite().stream().filter(i ->
                (vkId == null || vkId.equals(i.getUser().getVkId()))
                        && (groupId == null || groupId.equals(i.getGroupId()))
                        && (invitedId == null || invitedId.equals(i.getInvitedId()))
                        && (result == null || i.getResult() == result)
                        && (excludeResult == null || i.getResult() != excludeResult)
                        && (finalSTime == null || finalSTime < i.getTime())
                        && (finalETime == null || finalETime > i.getTime())
        ).collect(Collectors.toList());
    }
}

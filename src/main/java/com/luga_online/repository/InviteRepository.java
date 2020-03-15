package com.luga_online.repository;

import com.luga_online.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Integer> {

    //    @Query("SELECT i from Invite i where i.groupId=: groupId and i.user.vkId=: userId")
    List<Invite> getAllByUserVkIdAndGroupId(Integer userId, Integer groupId);

    List<Invite> getAllByUserVkId(Integer userId);
}

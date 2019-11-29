package com.luga_online.repository;

import com.luga_online.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Integer> {
}

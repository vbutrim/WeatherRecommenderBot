package com.vbutrim.bot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectedUsersRepository extends JpaRepository<ConnectedUser, Long> {

    Optional<ConnectedUser> findByChatId(Long chatId);
}
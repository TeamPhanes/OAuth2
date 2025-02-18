package com.phanes.oauth.repository;

import com.phanes.oauth.domain.User;
import com.phanes.oauth.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}

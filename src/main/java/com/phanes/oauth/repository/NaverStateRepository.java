package com.phanes.oauth.repository;

import com.phanes.oauth.domain.OAuthState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverStateRepository extends JpaRepository<OAuthState, Long> {

    OAuthState findByState(String state);
}

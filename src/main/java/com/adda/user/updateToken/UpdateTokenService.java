package com.adda.user.updateToken;

import com.adda.user.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateTokenService {

    private final Long updateTokenDuration;
    private final UserRepository userRepository;
    private final UpdateTokenRepository updateTokenRepository;

    public UpdateTokenService(@Value("${adda.app.jwtRefreshExpirationMs}") Long updateTokenDuration,
                              UserRepository userRepository, UpdateTokenRepository updateTokenRepository) {
        this.updateTokenDuration = updateTokenDuration;
        this.userRepository = userRepository;
        this.updateTokenRepository = updateTokenRepository;
    }

    public UpdateToken createUpdateToken(Long userId, String newPassword) {
        UpdateToken token = new UpdateToken();

        token.setUser(userRepository.findById(userId).get());
        token.setExpiryDate(Instant.now().plusMillis(updateTokenDuration));
        token.setToken(RandomStringUtils.randomAlphanumeric(8));
        token.setSensitiveData(newPassword);

        token = updateTokenRepository.save(token);
        return token;
    }

    public void deleteUpdateToken(String id) {
        updateTokenRepository.deleteById(id);
    }

    public UpdateToken getById(String tokenId) {
        return updateTokenRepository.getById(tokenId);
    }
}

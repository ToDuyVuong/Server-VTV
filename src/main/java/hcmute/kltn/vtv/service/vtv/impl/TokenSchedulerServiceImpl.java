package hcmute.kltn.vtv.service.vtv.impl;

import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import hcmute.kltn.vtv.authentication.service.IJwtService;
import hcmute.kltn.vtv.model.entity.user.Token;
import hcmute.kltn.vtv.repository.user.TokenRepository;
import hcmute.kltn.vtv.service.vtv.ITokenSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TokenSchedulerServiceImpl implements ITokenSchedulerService {


    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final IJwtService jwtService;



    @Override
    @Transactional
    public void checkExpirationToken() {
        List<Token> tokens = tokenRepository.findAllByExpired(false);

        checkExpiredToken(tokens);
        return;
    }


    @Override
    @Transactional
    public void deleteTokenExpiredAndRevoked() {
        List<Token> tokens = tokenRepository.findAllByExpiredAndRevoked(true, true);
        deleteAllToken(tokens);
        return;
    }

    private void deleteAllToken(List<Token> tokens) {
        try {
            tokenRepository.deleteAll(tokens);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkExpiredToken(List<Token> tokens) {
        try {
            for (Token token : tokens) {
                if (jwtService.tokenExpired(token.getToken())) {
                    token.setExpired(true);
                    tokenRepository.save(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

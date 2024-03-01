package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import org.springframework.transaction.annotation.Transactional;

public interface IMailService {


    @Transactional
    SendEmailResponse forgotPasswordSendOtpToEmail(String username);

    @Transactional
    SendEmailResponse activateAccountSendOtpToEmail(String username);

    SendEmailResponse sendOtpToEmail(String username, String mailSubject, String messageMail);
}

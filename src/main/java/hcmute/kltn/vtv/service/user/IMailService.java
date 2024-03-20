package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import org.springframework.transaction.annotation.Transactional;

public interface IMailService {



    void sendOrderConfirmationEmail(Order order, ShippingDTO shippingDTO, String message);

    @Transactional
    SendEmailResponse forgotPasswordSendOtpToEmail(String username);

    @Transactional
    SendEmailResponse activateAccountSendOtpToEmail(String username);

    SendEmailResponse sendOtpToEmail(String username, String mailSubject, String messageMail);
}

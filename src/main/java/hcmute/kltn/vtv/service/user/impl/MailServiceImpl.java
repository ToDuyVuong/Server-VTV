package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.model.dto.user.MailDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.IMailService;
import hcmute.kltn.vtv.service.user.IOtpService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class MailServiceImpl implements IMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOtpService otpService;

    private void sendEmail(MailDTO mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // Specify UTF-8 encoding
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
            mimeMessageHelper.setText(mail.getMailContent(), true); // Set text content with HTML support
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setSentDate(new Date());
            mimeMessageHelper.setValidateAddresses(true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new BadRequestException("Gửi email thất bại. " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public SendEmailResponse forgotPasswordSendOtpToEmail(String username) {
        String mailSubject = "Lấy lại mật khẩu trên hệ thống VTV.";
        String messageMail = "Bạn đã yêu cầu lấy lại mật khẩu trên hệ thống VTV.";

        try {
            return sendOtpToEmail(username, mailSubject, messageMail);
        } catch (BadRequestException e) {
            throw new InternalServerErrorException("Có lỗi xảy ra khi gửi email: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SendEmailResponse activateAccountSendOtpToEmail(String username) {
        String mailSubject = "Kích hoạt tài khoản trên hệ thống VTV.";
        String messageMail = "Bạn đã yêu cầu kích hoạt tài khoản" + username + " trên hệ thống VTV.";

        try {
            return sendOtpToEmail(username, mailSubject, messageMail);
        } catch (BadRequestException e) {
            throw new InternalServerErrorException("Có lỗi xảy ra khi gửi email: " + e.getMessage());
        }
    }


    @Override
    public SendEmailResponse sendOtpToEmail(String username, String mailSubject, String messageMail) {
        Customer customer = customerService.getCustomerByUsername(username);
        String otp = otpService.generateOtp(username);

        MailDTO mailDTO = createMailDTO(
                customer.getEmail(),
                mailSubject,
                messageMail,
                customer.getUsername(),
                otp);

        try {
            sendEmail(mailDTO);
            String message = "Thông báo: Mã OTP đã được gửi về email: "
                    + customer.getEmail()
                    + " của tài khoản "
                    + customer.getUsername()
                    + " thành công. Mã OTP có hiệu lực trong vòng 5 phút.";

            return SendEmailResponse.sendEmailResponse(customer.getUsername(), customer.getEmail(), message);
        } catch (BadRequestException e) {
            throw new InternalServerErrorException("Có lỗi xảy ra khi gửi email: " + e.getMessage());
        }
    }







    private MailDTO createMailDTO(String mailTo, String mailSubject, String message, String username, String otp) {

        MailDTO mail = new MailDTO();
        mail.setMailFrom("conc5288@gmail.com");
        mail.setMailTo(mailTo);
        mail.setMailSubject(mailSubject);

        StringBuilder mailContent = createMailContent(username, otp, message);
        mail.setMailContent(mailContent.toString());

        return mail;
    }


    private StringBuilder createMailContent(String username, String otp, String message) {
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<div style=\"background-color: #f0f0f0; padding: 20px; border-radius: 10px; margin: 20px;\">");
        mailContent.append("<h2 style=\"color: #333333;\">Xin chào ").append(username).append("!</h2>");
        mailContent.append("<p style=\"color: #555555;\">").append(message).append(" Dưới đây là mã OTP của bạn:</p>");


        // Styling for OTP digits
        mailContent.append("<div style=\"display: flex; justify-content: center;\">");
        for (char digit : otp.toCharArray()) {
            mailContent
                    .append("<span style=\"background-color: #4CAF50; " +
                            "color: white; font-weight: bold; padding: 10px; " +
                            "margin: 5px; border-radius: 5px;\">")
                    .append(digit)
                    .append("</span>");
        }
        mailContent.append("</div>");

        mailContent.append("<p style=\"color: #555555;\">Mã OTP có hiệu lực trong vòng 5 phút.</p>");
        mailContent.append("</div>");

        return mailContent;
    }



}

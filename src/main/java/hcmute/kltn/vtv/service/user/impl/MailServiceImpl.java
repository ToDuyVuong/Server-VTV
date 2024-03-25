package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.user.*;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.model.dto.user.MailDTO;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class MailServiceImpl implements IMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOtpService otpService;


    @Override
    @Async
    public void sendOrderConfirmationEmail(Order order, ShippingDTO shippingDTO, String message) {
        String mailSubject = "Xác nhận đơn hàng #" + order.getOrderId() + " - trên hệ thống VTV";

        Customer customer = order.getCustomer();
        MailDTO mailDTO = createOrderConfirmationMailDTO(customer.getEmail(), mailSubject, message, order, shippingDTO);

        try {
            sendEmail(mailDTO);
        } catch (BadRequestException e) {
            e.printStackTrace();
//            throw new InternalServerErrorException("Có lỗi xảy ra khi gửi email xác nhận đơn hàng: " + e.getMessage());
        }
    }


    private MailDTO createOrderConfirmationMailDTO(String mailTo, String mailSubject, String message, Order order, ShippingDTO shippingDTO) {
        MailDTO mail = new MailDTO();
        mail.setMailFrom("conc5288@gmail.com");
        mail.setMailTo(mailTo);
        mail.setMailSubject(mailSubject);


        StringBuilder mailContent = new StringBuilder(generateHtmlContent(order, shippingDTO, message));
        mail.setMailContent(mailContent.toString());

        return mail;
    }


    private String generateHtmlContent(Order order, ShippingDTO shippingDTO, String message) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<div style='background-color: #f0f0f0; padding: 10px;'>");
        htmlContent.append("<p>").append(message).append("</p>");
        htmlContent.append("</div>");


        htmlContent.append("<div>");
        htmlContent.append("<p>");
        htmlContent.append("Xin chào ").append(order.getCustomer().getFullName()).append(",\n\n");
        htmlContent.append("Đơn hàng #").append(order.getOrderId()).append(".\n").append(message);
        htmlContent.append("</p>");
        htmlContent.append("</div>");


        // Start HTML content
        htmlContent.append("<html>");
        htmlContent.append("<head>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif;}");
        htmlContent.append("h2 { color: #333;}");
        htmlContent.append("h3 { color: #555;}");
        htmlContent.append("p { margin: 5px 0;}");
        htmlContent.append("table { width: 100%; border-collapse: collapse;}");
        htmlContent.append("th, td { border: 1px solid #ddd; padding: 8px;}");
        htmlContent.append("th { background-color: #f2f2f2;}");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<h2 style='color: red; font-weight: bold; text-align: center;'>Chi tiết hóa đơn</h2>");
        htmlContent.append("<hr>");

        htmlContent.append("<br>");
        createHtmlContentCustomer(htmlContent, order.getCustomer());
        htmlContent.append("<br>");
        createHtmlContentOrderDetail(htmlContent, order);
        htmlContent.append("<br>");
        createHtmlContentOrderItem(htmlContent, order);
        htmlContent.append("<br>");
        createHtmlContentAddress(htmlContent, order.getAddress());
        htmlContent.append("<br>");
         createHtmlContentShopDetail(htmlContent, order);
        htmlContent.append("<br>");
        createHtmlContentShipping(htmlContent, shippingDTO);
        htmlContent.append("<br>");
        createHtmlContentVoucherOrder(htmlContent, order.getVoucherOrders());


        htmlContent.append("</body>");
        htmlContent.append("</html>");

        return htmlContent.toString();
    }

    private void createHtmlContentOrderDetail(StringBuilder htmlContent, Order order) {
        htmlContent.append("<h3>Thông tin đơn hàng:</h3>");
        htmlContent.append("<p><strong>Mã đơn hàng: #</strong>").append(order.getOrderId()).append("</p>");
        htmlContent.append("<p><strong>Ngày đặt hàng:</strong> ").append(order.getCreateAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</p>");
        htmlContent.append("<p><strong>Tổng số tiền:</strong> ").append(formatCurrency(order.getTotalPrice())).append(" VNĐ</p>");
        htmlContent.append("<p><strong>Phương thức thanh toán:</strong> ").append(order.getPaymentMethod()).append("</p>");
        htmlContent.append("<p><strong>Trạng thái đơn hàng:</strong> ").append(order.getStatus()).append("</p>");
        htmlContent.append("<p><strong>Lời nhắn:</strong> ").append(order.getNote()).append("</p>");
    }

    private void createHtmlContentShopDetail(StringBuilder htmlContent, Order order) {
        String fullAddressShop = order.getShop().getAddress() + ", " + order.getShop().getWard().getName() + ", "
                + order.getShop().getWard().getDistrict().getName() + ", " + order.getShop().getWard().getDistrict().getProvince().getName();

        htmlContent.append("<h3>Thông tin cửa hàng:</h3>");
        htmlContent.append("<p><strong>Tên cửa hàng:</strong> ").append(order.getShop().getName()).append("</p>");
        htmlContent.append("<p><strong>Email:</strong> ").append(order.getShop().getEmail()).append("</p>");
        htmlContent.append("<p><strong>Liên hệ:</strong> ").append(order.getShop().getPhone()).append("</p>");
        htmlContent.append("<p><strong>Địa chỉ cửa hàng:</strong> ").append(fullAddressShop).append("</p>");
    }

    private void createHtmlContentCustomer(StringBuilder htmlContent, Customer customer) {
        htmlContent.append("<h3>Thông tin khách hàng:</h3>");
        htmlContent.append("<p><strong>Họ và tên:</strong> ").append(customer.getFullName()).append("</p>");
        htmlContent.append("<p><strong>Email:</strong> ").append(customer.getEmail()).append("</p>");
    }


    private void createHtmlContentAddress(StringBuilder htmlContent, Address address) {

        String fullAddress = address.getFullAddress() + ", " + address.getWard().getName() + ", "
                + address.getWard().getDistrict().getName() + ", " + address.getWard().getDistrict().getProvince().getName();
        htmlContent.append("<h3>Địa chỉ giao hàng:</h3>");
        htmlContent.append("<p><strong>Địa chỉ:</strong> ").append(fullAddress).append("</p>");
        htmlContent.append("<p><strong>Số điện thoại:</strong> ").append(address.getPhone()).append("</p>");
    }

    private void createHtmlContentShipping(StringBuilder htmlContent, ShippingDTO shippingDTO) {
        // Shipping details
        htmlContent.append("<h3>Thông tin vận chuyển:</h3>");
        htmlContent.append("<p><strong>Nhà cung cấp vận chuyển:</strong> ").append(shippingDTO.getTransportProviderFullName()).append("</p>");
        htmlContent.append("<p><strong>Phương thức vận chuyển:</strong> ").append(shippingDTO.getTransportProviderShortName()).append("</p>");
        htmlContent.append("<p><strong>Phí vận chuyển:</strong> ").append(formatCurrency(shippingDTO.getShippingCost())).append(" VNĐ</p>");
        htmlContent.append("<p><strong>Thời gian dự kiến giao hàng:</strong> ").append(shippingDTO.getEstimatedDeliveryTime()).append("</p>");
    }



    private void createHtmlContentOrderItem(StringBuilder htmlContent, Order order) {
        // Order items
        List<OrderItem> orderItems = order.getOrderItems();
        htmlContent.append("<h3>Sản phẩm trong đơn hàng:</h3>");
        htmlContent.append("<table>");
        htmlContent.append("<tr>");
        htmlContent.append("<th>Hình ảnh</th>");
        htmlContent.append("<th>Loại sản phẩm</th>");
        htmlContent.append("<th>Tên sản phẩm</th>");
        htmlContent.append("<th>Số lượng</th>");
        htmlContent.append("<th>Giá</th>");
        htmlContent.append("<th>Tổng giá</th>");
        htmlContent.append("</tr>");
        for (OrderItem orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getCart().getProductVariant();
            htmlContent.append("<tr>");
            htmlContent.append("<td style='text-align: center;'><img src='").append(productVariant.getImage()).append("' width='50'></td>");
            htmlContent.append("<td style='text-align: center;'>").append(productVariant.getSku()).append("</td>");
            htmlContent.append("<td>").append(productVariant.getProduct().getName()).append("</td>");
            htmlContent.append("<td style='text-align: center;'>").append(orderItem.getCart().getQuantity()).append("</td>");
            htmlContent.append("<td style='text-align: center;'>").append(formatCurrency(orderItem.getPrice())).append(" VNĐ</td>");
            htmlContent.append("<td style='text-align: center;'>").append(formatCurrency(orderItem.getPrice() * orderItem.getCart().getQuantity())).append(" VNĐ</td>");
            htmlContent.append("</tr>");
        }

        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='text-align: right;'><strong>").append("Tổng số lượng sản phẩm").append("</td>");
        htmlContent.append("<td style='text-align: center;'><strong>").append(formatCurrency((long) order.getCount())).append(" Sản phẩm</p>").append("</td>");
        htmlContent.append("<tr>");

        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='text-align: right;'><strong>").append("Tổng tiền").append("</strong></td>");
        htmlContent.append("<td style='text-align: center;'><strong>").append(formatCurrency(order.getTotalPrice())).append(" VNĐ</p>").append("</strong></td>");
        htmlContent.append("<tr>");

        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='color: red; font-weight: bold; text-align: right;'>").append("Giảm giá của hệ thống").append("</td>");
        htmlContent.append("<td style='color: red; font-weight: bold; text-align: center;'>").append(formatCurrency(order.getDiscountSystem())).append(" VNĐ").append("</td>");
        htmlContent.append("<tr>");

        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='color: red; font-weight: bold; text-align: right;'>").append("Giảm giá của cửa hàng").append("</td>");
        htmlContent.append("<td style='color: red; font-weight: bold; text-align: center;'>").append(formatCurrency(order.getDiscountShop())).append(" VNĐ").append("</td>");
        htmlContent.append("</tr>");


        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='color: red; font-weight: bold; text-align: right;'>").append("Điềm tích lũy").append("</td>");
        htmlContent.append("<td style='color: red; font-weight: bold; text-align: center;'>").append(order.getLoyaltyPointHistory() != null ? formatCurrency(order.getLoyaltyPointHistory().getPoint()) : 0).append(" VNĐ").append("</td>");
        htmlContent.append("</tr>");

        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='text-align: right;'><strong>").append("Phí vận chuyển").append("</strong></td>");
        htmlContent.append("<td style='text-align: center;'><strong>").append(formatCurrency(order.getShippingFee())).append(" VNĐ</p>").append("</strong></td>");
        htmlContent.append("<tr>");

        htmlContent.append("<tr>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td>");
        htmlContent.append("<td style='text-align: right;'><strong>").append("Tổng số tiền phải thanh toán").append("</strong></td>");
        htmlContent.append("<td style='text-align: center;'><strong>").append(formatCurrency(order.getPaymentTotal())).append(" VNĐ</p>").append("</strong></td>");
        htmlContent.append("<tr>");


        htmlContent.append("</table>");
    }

    private void createHtmlContentVoucherOrder(StringBuilder htmlContent, List<VoucherOrder> voucherOrders) {
        if (!voucherOrders.isEmpty()) {
            htmlContent.append("<h3>Thông tin voucher:</h3>");
            htmlContent.append("<table>");
            htmlContent.append("<tr>");
            htmlContent.append("<th>Mã voucher</th>");
            htmlContent.append("<th>Mã từ</th>");
            htmlContent.append("<th>Giảm giá</th>");
            htmlContent.append("<th>Mô tả</th>");
            htmlContent.append("</tr>");
            for (VoucherOrder voucherOrder : voucherOrders) {
                htmlContent.append("<tr>");
                htmlContent.append("<style='text-align: center;>").append(voucherOrder.getVoucher().getCode()).append("</td>");
                htmlContent.append("<style='text-align: center;>");
                if (voucherOrder.isType()) {
                    htmlContent.append("Cửa hàng");
                } else {
                    htmlContent.append("Hệ thống");
                }

                htmlContent.append("<td style='text-align: center;'>");
                if (voucherOrder.getVoucher().getType() == VoucherType.PERCENTAGE_SYSTEM || voucherOrder.getVoucher().getType() == VoucherType.PERCENTAGE_SHOP) {
                    htmlContent.append(voucherOrder.getVoucher().getDiscount()).append(" %");
                } else {
                    htmlContent.append(formatCurrency((long) voucherOrder.getVoucher().getDiscount())).append(" VNĐ");
                }
                htmlContent.append("</td>");
                htmlContent.append("<td>").append(voucherOrder.getVoucher().getDescription()).append("</td>");
                htmlContent.append("</tr>");
            }
            htmlContent.append("</table>");
        }
    }


    private String formatCurrency(Long price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price);
    }


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

        MailDTO mailDTO = createOTPMailDTO(
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


    private MailDTO createOTPMailDTO(String mailTo, String mailSubject, String message, String username, String otp) {

        MailDTO mail = new MailDTO();
        mail.setMailFrom("conc5288@gmail.com");
        mail.setMailTo(mailTo);
        mail.setMailSubject(mailSubject);

        StringBuilder mailContent = createMailOTPContent(username, otp, message);
        mail.setMailContent(mailContent.toString());

        return mail;
    }


    private StringBuilder createMailOTPContent(String username, String otp, String message) {
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

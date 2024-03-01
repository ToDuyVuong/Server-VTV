package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.service.user.IOtpService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements IOtpService {

    private Map<String, OtpDetails> otpMap = new ConcurrentHashMap<>();

    @Override
    public String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public String generateOtp(String username) {
        String otp = generateRandomOtp();
        long expiryTimeMillis = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutes validity
        OtpDetails otpDetails = new OtpDetails(otp, expiryTimeMillis);
        otpMap.put(username, otpDetails);

        return otp;
    }

    @Override
    @Transactional
    public void verifyOtp(String username, String otp) {

        OtpDetails otpDetails = otpMap.get(username);
        if (otpDetails != null && !otpDetails.isExpired()) {
            if (otpDetails.isExpired()) {
                throw new BadRequestException("OTP đã hết hạn");
            } else if (!otpDetails.getOtp().equals(otp)) {
                throw new BadRequestException("OTP không hợp lệ");
            } else {
                otpMap.remove(username);
            }

        } else {
            throw new BadRequestException("OTP không tồn tại");
        }
    }

    @Override
    @Transactional
    public long getTimeValid(String username) {
        OtpDetails otpDetails = otpMap.get(username);

        if (otpDetails != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long expiryTimeMillis = otpDetails.getExpiryTimeMillis();

            if (expiryTimeMillis > currentTimeMillis) {
                return (expiryTimeMillis - currentTimeMillis) / 1000; // Convert to seconds
            }
        }

        return 0; // OTP expired or not found
    }

    @Getter
    private static class OtpDetails {
        private String otp;
        private long expiryTimeMillis;

        public OtpDetails(String otp, long expiryTimeMillis) {
            this.otp = otp;
            this.expiryTimeMillis = expiryTimeMillis;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTimeMillis;
        }
    }
}

package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@RequiredArgsConstructor
public class StatisticsRequest {

    private Date startDate;
    private Date endDate;

    public void validate() {
        if (startDate == null) {
            throw new BadRequestException("Ngày bắt đầu không được để trống.");
        }

        if (endDate == null) {
            throw new BadRequestException("Ngày kết thúc không được để trống.");
        }

        if (startDate.after(endDate)) {
            throw new BadRequestException("Ngày bắt đầu không được lớn hơn ngày kết thúc.");
        }

        // khoảng thời gian trong vòng 31
        if (endDate.getTime() - startDate.getTime() > 2678400000L) {
            throw new BadRequestException("Khoảng thời gian không được lớn hơn 31 ngày.");
        }
    }

}

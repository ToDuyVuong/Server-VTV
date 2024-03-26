package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vtv.StatisticsDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse extends ResponseAbstract {

    private int count;
    private int totalOrder;
    private Long totalMoney;
    private String username;
    private Date dateStart;
    private Date dateEnd;

    private List<StatisticsDTO> statisticsDTOs;



    public static StatisticsResponse statisticsResponse(List<StatisticsDTO> statisticsDTOs, String username,
                                                  int totalOrder, Date startDate, Date endDate, long totalMoney) {

        StatisticsResponse statisticsResponse = new StatisticsResponse();
        statisticsResponse.setUsername(username);
        statisticsResponse.setCount(statisticsDTOs.size());
        statisticsResponse.setTotalOrder(totalOrder);
        statisticsResponse.setTotalMoney(totalMoney);
        statisticsResponse.setDateStart(ofDay(startDate));
        statisticsResponse.setDateEnd(ofDay(endDate));
        statisticsResponse.setStatisticsDTOs(statisticsDTOs);
        statisticsResponse.setMessage("Thống kê doanh thu thành công.");
        statisticsResponse.setCode(200);
        statisticsResponse.setStatus("OK");

        return statisticsResponse;
    }




    private static Date ofDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}

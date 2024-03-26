package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.response.StatisticsResponse;

import java.util.Date;

public interface IRevenueService {


    StatisticsResponse statisticsByDate(Date startDate, Date endDate, String username);
}

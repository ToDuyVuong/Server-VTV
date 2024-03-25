package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.StatisticsRequest;
import hcmute.kltn.vtv.model.data.vendor.response.StatisticsResponse;
import hcmute.kltn.vtv.model.dto.vtv.StatisticsDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.vendor.IRevenueService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements IRevenueService {

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final IShopService shopService;

    @Override
    public StatisticsResponse statisticsByDate(StatisticsRequest request) {
        Shop shop = shopService.getShopByUsername(request.getUsername());
        Date startDate = startOfDay(request.getStartDate());
        Date endDate = endOfDay(request.getEndDate());
        long totalMoney = 0;

        int totalOrder = orderRepository.countAllByShopShopIdAndStatusAndOrderDateBetween(shop.getShopId(),
                OrderStatus.COMPLETED, startDate, endDate);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndStatusAndOrderDateBetween(shop.getShopId(), OrderStatus.COMPLETED, startDate, endDate)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng nào."));
        List<StatisticsDTO> statisticsDTOs = listStatisticsDTO(orders, request.getStartDate(), request.getEndDate());

        if (!orders.isEmpty()) {
            for (Order order : orders) {
                totalMoney += order.getPaymentTotal();
            }
        }

        return statisticsResponse(statisticsDTOs, request.getUsername(), totalOrder, startDate, endDate, totalMoney);
    }

    private StatisticsResponse statisticsResponse(List<StatisticsDTO> statisticsDTOs, String username,
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

    private List<StatisticsDTO> listStatisticsDTO(List<Order> orders, Date startDate, Date endDate) {

        //

        Map<Date, List<Order>> ordersByDate = getOrdersByDate(orders, startDate, endDate);
        List<StatisticsDTO> statisticsDTOs = new ArrayList<>();

        for (Map.Entry<Date, List<Order>> entry : ordersByDate.entrySet()) {
            Date orderDate = entry.getKey();
            List<Order> ordersOnDate = entry.getValue();
            StatisticsDTO statisticsDTO = getStatisticsDTO(ordersOnDate, orderDate);
            statisticsDTOs.add(statisticsDTO);
        }
        statisticsDTOs.sort(Comparator.comparing(StatisticsDTO::getDate));

        return statisticsDTOs;
    }

    private Map<Date, List<Order>> getOrdersByDate(List<Order> orders, Date startDate, Date endDate) {
        Map<Date, List<Order>> ordersByDate = new HashMap<>();
        List<Date> datesBetween = getDatesBetween(startDate, endDate);

        for (Date date : datesBetween) {
            List<Order> ordersSameDate = new ArrayList<>();
            Date start = startOfDay(date);
            Date end = endOfDay(date);
            Date ofDay = ofDay(date);

            if (!orders.isEmpty()) {
                for (Order checkOrder : orders) {
                    if (checkOrder.getOrderDate().after(start) && checkOrder.getOrderDate().before(end)) {
                        ordersSameDate.add(checkOrder);
                    }
                }
                ordersSameDate.sort(Comparator.comparing(Order::getOrderDate));
            }
            ordersByDate.put(ofDay, ordersSameDate);
        }

        return ordersByDate;
    }

    public static List<Date> getDatesBetween(Date startDate, Date endDate) {

        Date startDateWithoutTime = startOfDay(startDate);
        Date endDateWithoutTime = endOfDay(endDate);

        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDateWithoutTime);

        while (calendar.getTime().before(endDateWithoutTime)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }

    private StatisticsDTO getStatisticsDTO(List<Order> ordersOnDate, Date orderDate) {
        long totalMoney = 0;
        int totalProduct = 0;

        for (Order order : ordersOnDate) {
            totalMoney += order.getPaymentTotal();
            totalProduct += order.getOrderItems().size();
        }

        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setDate(orderDate);
        statisticsDTO.setTotalMoney(totalMoney);
        statisticsDTO.setTotalOrder(ordersOnDate.size());
        statisticsDTO.setTotalProduct(totalProduct);

        return statisticsDTO;
    }

    private static Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
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

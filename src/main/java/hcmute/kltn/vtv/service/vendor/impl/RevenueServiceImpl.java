package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.util.exception.BadRequestException;
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

    private final OrderRepository orderRepository;
    private final IShopService shopService;

    @Override
    public StatisticsResponse statisticsByDate(Date startDate, Date endDate, String username) {
        Shop shop = shopService.getShopByUsername(username);
        startDate = startOfDay(startDate);
        endDate = endOfDay(endDate);
        long totalMoney = 0;
        int totalOrder = orderRepository.countAllByShopShopIdAndStatusAndOrderDateBetween(shop.getShopId(),
                OrderStatus.COMPLETED, startDate, endDate);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndStatusAndOrderDateBetween(shop.getShopId(), OrderStatus.COMPLETED, startDate, endDate)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng nào."));
        List<StatisticsDTO> statisticsDTOs = listStatisticsDTO(orders, startDate, endDate);

        if (!orders.isEmpty()) {
            for (Order order : orders) {
                totalMoney += (long) (order.getTotalPrice() - order.getDiscountShop() - order.getTotalPrice() * 0.04);
            }
        }

        return StatisticsResponse.statisticsResponse(statisticsDTOs, username, totalOrder, startDate, endDate, totalMoney);
    }


    private List<StatisticsDTO> listStatisticsDTO(List<Order> orders, Date startDate, Date endDate) {
        List<StatisticsDTO> statisticsDTOs = new ArrayList<>();
        Map<Date, List<Order>> ordersByDate = getOrdersByDate(orders, startDate, endDate);
        for (Map.Entry<Date, List<Order>> entry : ordersByDate.entrySet()) {
            Date orderDate = entry.getKey();
            List<Order> ordersOnDate = entry.getValue();
            statisticsDTOs.add(StatisticsDTO.convertOrdersAndDateToDTO(ordersOnDate, orderDate));
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

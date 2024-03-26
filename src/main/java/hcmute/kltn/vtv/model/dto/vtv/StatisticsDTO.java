package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.user.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticsDTO {

    private Long totalMoney;
    private int totalOrder;
    private int totalProduct;
    private Date date;

    public static StatisticsDTO convertOrdersAndDateToDTO(List<Order> ordersOnDate, Date orderDate) {
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

}

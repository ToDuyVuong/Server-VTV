package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.dto.wallet.LoyaltyPointHistoryDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderDTO {

    private UUID orderId;

    private String note;

    private String paymentMethod;

    private String shippingMethod;

    private int count;

    private Long totalPrice;

    private Long discountShop;

    private Long discountSystem;

    private Long shippingFee;

    private Long paymentTotal;

    private OrderStatus status;

    private Date orderDate;

    private AddressDTO addressDTO;

    private ShopDTO shopDTO;

    private LoyaltyPointHistoryDTO loyaltyPointHistoryDTO;

    private List<VoucherOrderDTO> voucherOrderDTOs;

    private List<OrderItemDTO> orderItemDTOs;





    public static List<OrderDTO> convertEntitiesToDTOs(List<Order> orders) {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(convertEntityToDTO(order));
        }

        orderDTOs.sort(Comparator.comparing(OrderDTO::getOrderDate, Comparator.reverseOrder()));

        return orderDTOs;
    }


    public static OrderDTO convertEntityToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setNote(order.getNote());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setShippingMethod(order.getShippingMethod());
        orderDTO.setCount(order.getCount());
        orderDTO.setDiscountShop(order.getDiscountShop());
        orderDTO.setDiscountSystem(order.getDiscountSystem());
        orderDTO.setPaymentTotal(order.getPaymentTotal());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setShippingFee(order.getShippingFee());


        orderDTO.setShopDTO(ShopDTO.convertEntityToDTO(order.getShop()));
        orderDTO.setLoyaltyPointHistoryDTO(LoyaltyPointHistoryDTO.convertEntityToDTO(order.getLoyaltyPointHistory()));

        orderDTO.setOrderItemDTOs(OrderItemDTO.convertEntitiesToDTOs(order.getOrderItems()));
        orderDTO.setAddressDTO(AddressDTO.convertEntityToDTO(order.getAddress()));
        orderDTO.setVoucherOrderDTOs(VoucherOrderDTO.convertEntitiesToDTOs(order.getVoucherOrders()));

        return orderDTO;
    }

}

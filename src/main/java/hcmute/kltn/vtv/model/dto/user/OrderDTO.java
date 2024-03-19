package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
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

    private Long shopId;

    private String shopName;

    private Long totalPrice;

    private Long discountShop;

    private Long discountSystem;

    private Long shippingFee;

    private Long paymentTotal;

    private String shopWardCode;

    private OrderStatus status;

    private Date orderDate;

    private AddressDTO addressDTO;

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



//    public static OrderDTO convertEntityToDTOCreate(Order order) {
//        OrderDTO orderDTO = new OrderDTO();
//        if (order.getOrderId() != null) {
//            orderDTO.setOrderId(order.getOrderId());
//        }
//        orderDTO.setNote(order.getNote());
//        orderDTO.setPaymentMethod(order.getPaymentMethod());
//        orderDTO.setShippingMethod(order.getShippingMethod());
//        orderDTO.setCount(order.getCount());
//        orderDTO.setShopId(order.getShopId());
//        orderDTO.setShopName(order.getShopName());
//        orderDTO.setDiscountShop(order.getDiscountShop());
//        orderDTO.setDiscountSystem(order.getDiscountSystem());
//        orderDTO.setPaymentTotal(order.getPaymentTotal());
//        orderDTO.setStatus(order.getStatus());
//        orderDTO.setOrderDate(order.getOrderDate());
//        orderDTO.setTotalPrice(order.getTotalPrice());
//        orderDTO.setShippingFee(order.getShippingFee());
//        orderDTO.setShopWardCode(order.getShopWardCode().getWardCode());
//
//        orderDTO.setOrderItemDTOs(OrderItemDTO.convertListEntityToListDTO(order.getOrderItems()));
//        orderDTO.setAddressDTO(AddressDTO.convertEntityToDTO(order.getAddress()));
//        orderDTO.setVoucherOrderDTOs(VoucherOrderDTO.convertListEntityToListDTO(order.getVoucherOrders()));
//
//        return orderDTO;
//    }


    public static OrderDTO convertEntityToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setNote(order.getNote());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setShippingMethod(order.getShippingMethod());
        orderDTO.setCount(order.getCount());
        orderDTO.setShopId(order.getShopId());
        orderDTO.setShopName(order.getShopName());
        orderDTO.setDiscountShop(order.getDiscountShop());
        orderDTO.setDiscountSystem(order.getDiscountSystem());
        orderDTO.setPaymentTotal(order.getPaymentTotal());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setShippingFee(order.getShippingFee());
        orderDTO.setShopWardCode(order.getShopWardCode().getWardCode());

        orderDTO.setOrderItemDTOs(OrderItemDTO.convertListEntityToListDTO(order.getOrderItems()));
        orderDTO.setAddressDTO(AddressDTO.convertEntityToDTO(order.getAddress()));
        orderDTO.setVoucherOrderDTOs(VoucherOrderDTO.convertListEntityToListDTO(order.getVoucherOrders()));

        return orderDTO;
    }

}

package hcmute.kltn.vtv.model.extra;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(using = StatusDeserializer.class)
public enum OrderStatus {


    PENDING("Pending"),
    PROCESSING("Processing"),
    PICKUP_PENDING("Pickup Pending"),
    SHIPPING("Shipping"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    RETURNED("Returned"),
    WAITING("Waiting"),
    CANCEL("Cancel"),
    REFUNDED("Refunded"),
    PAID("Paid"),
    UNPAID("Unpaid"),
    FAIL("Fail");


    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public static boolean isValidStatus(String status) {
        for (OrderStatus validStatus : OrderStatus.values()) {
            if (validStatus.name().equalsIgnoreCase(status)) {
                return false;
            }
        }
        return true;
    }

    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}

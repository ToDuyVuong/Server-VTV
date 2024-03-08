package hcmute.kltn.vtv.model.extra;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(using = StatusDeserializer.class)
public enum CartStatus {

    CANCEL("Cancel"),
    CART("Cart"),
    ORDER("Order");


    private final String value;

    CartStatus(String value) {
        this.value = value;
    }

    public static boolean isValidStatus(String status) {
        for (CartStatus validStatus : CartStatus.values()) {
            if (validStatus.name().equalsIgnoreCase(status)) {
                return false;
            }
        }
        return true;
    }

    public static CartStatus fromValue(String value) {
        for (CartStatus status : values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}

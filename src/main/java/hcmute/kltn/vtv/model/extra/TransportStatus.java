package hcmute.kltn.vtv.model.extra;

import lombok.Getter;

@Getter
public enum TransportStatus {

    WAITING,
    PICKED_UP,
    IN_TRANSIT,
    WAREHOUSE,
    TRANSPORTING,
    DELIVERED,
    RECEIVED,
    RETURNED,
    CANCEL;



}

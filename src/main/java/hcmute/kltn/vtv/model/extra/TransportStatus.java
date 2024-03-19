package hcmute.kltn.vtv.model.extra;

import lombok.Getter;

@Getter
public enum TransportStatus {

    WAITING,
    IN_TRANSIT,
    WAREHOUSE,
    TRANSPORTING,
    DELIVERED,
    RECEIVED,
    RETURNED,
    CANCEL;



}

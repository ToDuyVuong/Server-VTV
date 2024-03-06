package hcmute.kltn.vtv.service.vtv.shippingstrategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VtvExpressShippingStrategy implements IShippingStrategy {



    @Override
    public long calculateShippingCost(Long total) {
        return 0;
    }

    @Override
    public Long calculateShippingCost(int distanceLocation) {
        return switch (distanceLocation) {
            case 0 -> 16000L;
            case 1 -> 21000L;
            case 2 -> 27000L;
            case 3 -> 32000L;
            case 4 -> 45000L;
            default -> throw new IllegalArgumentException("Khoảng cách không hợp lệ");
        };
    }
}

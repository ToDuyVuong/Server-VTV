package hcmute.kltn.vtv.service.vtv.shippingstrategy;

public class GiaoHangHoaTocShippingStrategy implements IShippingStrategy {
    @Override
    public long calculateShippingCost(Long total) {
        if (total >= 500000) {
            return 0;
        }
        return 0;
    }


    @Override
    public Long calculateShippingCost(int distanceLocation) {
        return switch (distanceLocation) {
            case 1 -> 10000L;
            case 2 -> 20000L;
            case 3 -> 30000L;
            case 4 -> 40000L;
            case 5 -> 50000L;
            default -> 0L;
        };
    }
}


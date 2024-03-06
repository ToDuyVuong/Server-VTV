package hcmute.kltn.vtv.service.vtv.shippingstrategy;

public class GiaoHangTietKiemShippingStrategy implements IShippingStrategy {
    @Override
    public long calculateShippingCost(Long total) {
        if (total >= 300000) {
            return 0;
        }
        return 0;
    }

    @Override
    public Long calculateShippingCost(int distanceLocation) {
        return switch (distanceLocation) {
            case 0 -> 14000L;
            case 1 -> 29000L;
            case 2 -> 25000L;
            case 3 -> 30000L;
            case 4 -> 40000L;
            default -> throw new IllegalArgumentException("Khoảng cách không hợp lệ");
        };
    }

}

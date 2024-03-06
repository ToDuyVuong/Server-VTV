package hcmute.kltn.vtv.service.vtv.shippingstrategy;

public interface IShippingStrategy {
    long calculateShippingCost(Long total);

    Long calculateShippingCost(int distanceLocation);


}

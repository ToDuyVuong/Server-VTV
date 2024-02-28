package hcmute.kltn.vtv.service.vtv.shippingstrategy;

public interface IShipping {
    long calculateShippingCost(Long total);

    Long calculateShippingCost(int distanceLocation);


}

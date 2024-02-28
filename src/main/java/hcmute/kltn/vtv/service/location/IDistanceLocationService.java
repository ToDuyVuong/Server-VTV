package hcmute.kltn.vtv.service.location;

public interface IDistanceLocationService {
    int calculateDistance(String wardCodeCustomer, String wardCodeShop);

    String messageByLeverRegion(int level);
}

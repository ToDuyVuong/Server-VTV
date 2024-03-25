package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ShopDTO {

    private Long shopId;

    private String name;

    private String address;

    private String provinceName;

    private String districtName;

    private String wardName;

    private String phone;

    private String email;

    private String avatar;

    private String description;

    private String openTime;

    private String closeTime;

    private Status status;

    private Long customerId;

    private String wardCode;



    public static ShopDTO convertEntityToDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopId(shop.getShopId());
        shopDTO.setName(shop.getName());
        shopDTO.setAddress(shop.getAddress());
        shopDTO.setProvinceName(shop.getWard().getDistrict().getProvince().getName());
        shopDTO.setDistrictName(shop.getWard().getDistrict().getName());
        shopDTO.setWardName(shop.getWard().getName());
        shopDTO.setPhone(shop.getPhone());
        shopDTO.setEmail(shop.getEmail());
        shopDTO.setAvatar(shop.getAvatar());
        shopDTO.setDescription(shop.getDescription());
        shopDTO.setOpenTime(shop.getOpenTime());
        shopDTO.setCloseTime(shop.getCloseTime());
        shopDTO.setStatus(shop.getStatus());
        shopDTO.setCustomerId(shop.getCustomer().getCustomerId());
        shopDTO.setWardCode(shop.getWard().getWardCode());

        return shopDTO;
    }





    public static List<ShopDTO> convertEntitiesToDTOs(List<Shop> shops) {
        List<ShopDTO> shopDTOs = new ArrayList<>();
        for (Shop shop : shops) {
            shopDTOs.add(convertEntityToDTO(shop));
        }
        // shopDTOs.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        return shopDTOs;
    }
}

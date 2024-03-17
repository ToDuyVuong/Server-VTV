package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.entity.user.Address;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long addressId;

    private String provinceName;

    private String provinceFullName;

    private String districtName;

    private String districtFullName;

    private String wardName;

    private String wardFullName;

    private String fullAddress;

    private String fullName;

    private String phone;

    private Status status;

    private String wardCode;


    public static AddressDTO convertEntityToDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setProvinceName(address.getProvinceName());
        addressDTO.setProvinceFullName(address.getWard().getDistrict().getProvince().getFullName());
        addressDTO.setDistrictName(address.getDistrictName());
        addressDTO.setDistrictFullName(address.getWard().getDistrict().getFullName());
        addressDTO.setWardName(address.getWardName());
        addressDTO.setWardFullName(address.getWard().getFullName());
        addressDTO.setFullAddress(address.getFullAddress());
        addressDTO.setFullName(address.getFullName());
        addressDTO.setPhone(address.getPhone());
        addressDTO.setStatus(address.getStatus());
        addressDTO.setWardCode(address.getWard().getWardCode());

        return addressDTO;

    }

    public static List<AddressDTO> convertEntitiesToDTOs(List<Address> addresses) {
        List<AddressDTO> addressDTOs = new ArrayList<>();
        for (Address address : addresses) {
            AddressDTO addressDTO = convertEntityToDTO(address);
            addressDTOs.add(addressDTO);
        }
        addressDTOs.sort(Comparator.comparing(AddressDTO::getStatus));

        return addressDTOs;
    }



}

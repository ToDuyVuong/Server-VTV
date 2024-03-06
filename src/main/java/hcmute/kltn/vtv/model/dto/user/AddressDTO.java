package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.entity.user.Address;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long addressId;

    private String province;

    private String district;

    private String ward;

    private String fullAddress;

    private String fullName;

    private String phone;

    private Status status;

    private String wardCode;


    public static AddressDTO convertEntityToDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setProvince(address.getProvince());
        addressDTO.setDistrict(address.getDistrict());
        addressDTO.setWard(address.getWard());
        addressDTO.setFullAddress(address.getFullAddress());
        addressDTO.setFullName(address.getFullName());
        addressDTO.setPhone(address.getPhone());
        addressDTO.setStatus(address.getStatus());
        addressDTO.setWardCode(address.getWardCode().getWardCode());
        return addressDTO;

    }

    public static List<AddressDTO> convertEntitiesToDTOs(List<Address> addresses) {
        List<AddressDTO> addressDTOs = new ArrayList<>();
        for (Address address : addresses) {
            AddressDTO addressDTO = convertEntityToDTO(address);
            addressDTOs.add(addressDTO);
        }
        addressDTOs.sort((o1, o2) -> o2.getStatus().compareTo(o1.getStatus()));

        return addressDTOs;
    }

    public static Address convertDTOToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setAddressId(addressDTO.getAddressId());
        address.setProvince(addressDTO.getProvince());
        address.setDistrict(addressDTO.getDistrict());
        address.setWard(addressDTO.getWard());
        address.setFullAddress(addressDTO.getFullAddress());
        address.setFullName(addressDTO.getFullName());
        address.setPhone(addressDTO.getPhone());
        address.setStatus(addressDTO.getStatus());
        return address;
    }

}

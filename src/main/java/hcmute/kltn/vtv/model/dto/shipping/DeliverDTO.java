package hcmute.kltn.vtv.model.dto.shipping;

import hcmute.kltn.vtv.model.dto.location.WardDTO;
import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliverDTO {

    private Long deliverId;

    private String phone;

    private String provinceName;

    private String districtName;

    private String wardName;

    private String fullAddress;

    private TypeWork typeWork;

    private String usernameAdded;

    private Status status;

    private String districtWork;

    private String wardCode;

    private List<String> wardsWork;

    private Long customerId;

    private Long transportProviderId;

    public static DeliverDTO convertEntityToDTO(Deliver deliver) {
        DeliverDTO deliverDTO = new DeliverDTO();
        deliverDTO.setDeliverId(deliver.getDeliverId());
        deliverDTO.setPhone(deliver.getPhone());
        deliverDTO.setProvinceName(deliver.getWard().getDistrict().getProvince().getName());
        deliverDTO.setDistrictName(deliver.getWard().getDistrict().getName());
        deliverDTO.setWardName(deliver.getWard().getName());
        deliverDTO.setWardCode(deliver.getWard().getWardCode());
        deliverDTO.setFullAddress(deliver.getFullAddress());
        deliverDTO.setTypeWork(deliver.getTypeWork());
        deliverDTO.setUsernameAdded(deliver.getUsernameAdded());
        deliverDTO.setStatus(deliver.getStatus());
        deliverDTO.setDistrictWork(deliver.getDistrictWork().getName());
        deliverDTO.setWardsWork(WardDTO.convertEntitiesToNames(deliver.getWardsWork()));
        deliverDTO.setCustomerId(deliver.getCustomer().getCustomerId());
        deliverDTO.setTransportProviderId(deliver.getTransportProvider().getTransportProviderId());
        return deliverDTO;
    }


    public static List<DeliverDTO> convertEntitiesToDTOs(List<Deliver> delivers) {
        List<DeliverDTO> deliverDTOs = new ArrayList<>();
        for (Deliver deliver : delivers) {
            deliverDTOs.add(convertEntityToDTO(deliver));
        }

        deliverDTOs.sort((o1, o2) -> o2.getDeliverId().compareTo(o1.getDeliverId()));
        return deliverDTOs;
    }

}

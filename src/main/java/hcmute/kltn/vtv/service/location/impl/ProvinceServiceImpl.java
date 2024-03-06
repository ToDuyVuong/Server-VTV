package hcmute.kltn.vtv.service.location.impl;

import hcmute.kltn.vtv.model.data.location.ListProvinceResponse;
import hcmute.kltn.vtv.model.dto.location.ProvinceDTO;
import hcmute.kltn.vtv.model.entity.location.Province;
import hcmute.kltn.vtv.repository.location.ProvinceRepository;
import hcmute.kltn.vtv.service.location.IProvinceService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements IProvinceService {
    private final ProvinceRepository provinceRepository;



    @Override
    public Province getProvinceByWardCode(String wardCode) {
        return provinceRepository.findProvinceByWardCode(wardCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tỉnh thành phố nào có mã là: " + wardCode));
    }


    @Override
    public ListProvinceResponse getAllProvince() {

        List<Province> provinces = provinceRepository.findAll();
        List<ProvinceDTO> provinceDTOs = new ArrayList<>();
        if (!provinces.isEmpty()) {
            provinceDTOs = ProvinceDTO.convertEntitiesToDTOs(provinces);
        }

        ListProvinceResponse response = new ListProvinceResponse();
        response.setProvinceDTOs(provinceDTOs);
        response.setCount(response.getProvinceDTOs().size());
        response.setCode(200);
        response.setMessage("Lấy danh sách tỉnh thành phố thành công.");
        response.setStatus("OK");
        return response;
    }


    @Override
    public List<Province> getPronvincesByProvinceCode(List<String> provincesCode) {
        List<Province> provinces = new ArrayList<>();
        for (String provinceCode : provincesCode) {
            Province province = provinceRepository.findByProvinceCode(provinceCode)
                    .orElseThrow(() -> new NotFoundException(
                            "Không tìm thấy tỉnh thành phố nào có mã là: " + provinceCode));
            provinces.add(province);
        }
        provinces.sort(Comparator.comparing(Province::getName));

        return provinces;
    }

}

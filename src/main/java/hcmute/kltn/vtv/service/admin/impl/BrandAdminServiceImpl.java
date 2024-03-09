package hcmute.kltn.vtv.service.admin.impl;

import hcmute.kltn.vtv.model.data.admin.request.BrandAdminRequest;
import hcmute.kltn.vtv.model.data.admin.response.AllBrandAdminResponse;
import hcmute.kltn.vtv.model.data.admin.response.BrandAdminResponse;
import hcmute.kltn.vtv.model.dto.vtv.BrandDTO;
import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.BrandRepository;
import hcmute.kltn.vtv.repository.vtv.ProductRepository;
import hcmute.kltn.vtv.service.admin.IBrandAdminService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandAdminServiceImpl implements IBrandAdminService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public BrandAdminResponse addNewBrand(BrandAdminRequest request) {

        Customer customer = customerService.getCustomerByUsername(request.getUsername());
        if (!customer.getRoles().contains(Role.ADMIN)) {
            throw new UnauthorizedAccessException("Bạn không có quyền thêm thương hiệu!");
        }

        Brand brand = brandRepository.findByName(request.getName());
        if (brand != null) {
            throw new BadRequestException("Tên thương hiệu đã tồn tại!");
        }

        brand = modelMapper.map(request, Brand.class);
        brand.setCreateAt(LocalDateTime.now());
        brand.setUpdateAt(LocalDateTime.now());
        brand.setStatus(Status.ACTIVE);

        try {
            Brand saveBrand = brandRepository.save(brand);

            BrandDTO brandDTO = modelMapper.map(saveBrand, BrandDTO.class);
            BrandAdminResponse response = new BrandAdminResponse();
            response.setBrandDTO(brandDTO);
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("Thêm thương hiệu thành công!");

            return response;
        } catch (Exception e) {
            throw new BadRequestException("Thêm thương hiệu thất bại!");
        }
    }

    @Override
    public BrandAdminResponse getBrandById(Long brandId) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy thương hiệu!"));

        BrandDTO brandDTO = modelMapper.map(brand, BrandDTO.class);

        BrandAdminResponse response = new BrandAdminResponse();
        response.setBrandDTO(brandDTO);
        response.setCode(200);
        response.setStatus("ok");
        response.setMessage("Lấy thông tin thương hiệu thành công.");

        return response;
    }

    @Override
    public AllBrandAdminResponse getAllBrandAdmin() {

//        List<Brand> brands = brandRepository.findAllByAdminOnly(true);
//        if (brands == null || brands.isEmpty()) {
//            throw new BadRequestException("Không có thương hiệu nào!");
//        }
//
//        List<BrandDTO> brandDTOs = BrandDTO.convertEntitiesToDTOs(brands);
//        brandDTOs.sort(Comparator.comparing(BrandDTO::getName));
//
//        AllBrandAdminResponse response = new AllBrandAdminResponse();
//        response.setBrandDTOs(brandDTOs);
//        response.setCode(200);
//        response.setStatus("ok");
//        response.setMessage("Lấy danh sách thương hiệu thành công.");
        return null;
    }

    @Override
    @Transactional
    public BrandAdminResponse updateBrandAdmin(BrandAdminRequest request) {

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy thương hiệu!"));
        if (!"brand.getCustomer().getUsername()".equals(request.getUsername())) {
            throw new UnauthorizedAccessException("Bạn không có quyền sửa thương hiệu này!");
        }

        if (!request.getName().equals(brand.getName())) {
            Brand brandCheck = brandRepository.findByName(request.getName());
            if (brandCheck != null) {
                throw new BadRequestException("Tên thương hiệu đã tồn tại!");
            }
        }

        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setInformation(request.getInformation());
        brand.setOrigin(request.getOrigin());
        brand.setImage(request.getImage());
        brand.setUpdateAt(LocalDateTime.now());

        try {
            brandRepository.save(brand);

            BrandDTO brandDTO = modelMapper.map(brand, BrandDTO.class);
            BrandAdminResponse response = new BrandAdminResponse();
            response.setBrandDTO(brandDTO);
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("Cập nhật thương hiệu thành công.");

            return response;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật thương hiệu thất bại!");
        }
    }

    @Override
    @Transactional
    public BrandAdminResponse updateStatusBrandAdmin(Long brandId, String username, Status status) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy thương hiệu!"));
        if (!"brand.getCustomer().getUsername()".equals(username)) {
            throw new UnauthorizedAccessException("Bạn không có quyền sửa thương hiệu này!");
        }
        if (brand.getStatus() == Status.DELETED) {
            throw new BadRequestException("Thương hiệu đã bị xóa!");
        }
        if (status == Status.DELETED && !productRepository.existsByBrandBrandId(brand.getBrandId())) {
            throw new BadRequestException("Thương hiệu đã có sản phẩm!");
        }

        brand.setStatus(status);
        brand.setUpdateAt(LocalDateTime.now());

        try {
            brandRepository.save(brand);

            BrandDTO brandDTO = modelMapper.map(brand, BrandDTO.class);
            BrandAdminResponse response = new BrandAdminResponse();
            response.setBrandDTO(brandDTO);
            response.setCode(200);
            response.setStatus("success");
            response.setMessage("Cập nhật trạng thái thương hiệu thành công.");

            return response;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật trạng thái thương hiệu thất bại!");
        }
    }

}

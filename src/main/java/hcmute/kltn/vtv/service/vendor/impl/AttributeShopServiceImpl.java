package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.repository.vendor.AttributeRepository;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.AttributeRequest;
import hcmute.kltn.vtv.model.data.vendor.response.AttributeResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListAttributeResponse;
import hcmute.kltn.vtv.model.dto.vendor.AttributeDTO;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.entity.vtv.Shop;
import hcmute.kltn.vtv.service.user.impl.CustomerServiceImpl;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeShopServiceImpl implements IAttributeShopService {

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private IShopService shopService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public AttributeResponse addNewAttribute(AttributeRequest attributeRequest) {

        Shop shop = shopService.getShopByUsername(attributeRequest.getUsername());
        boolean checkExist = existsAttribute(attributeRequest.getName(), attributeRequest.getValue(), shop.getShopId());

        if (checkExist) {
            Attribute attribute = attributeRepository.findByNameAndValueAndShop_ShopId(
                    attributeRequest.getName(),
                    attributeRequest.getValue(),
                    shop.getShopId())
                    .orElseThrow(() -> new BadRequestException("Thuộc tính không tồn tại!"));

            AttributeResponse attributeResponse = new AttributeResponse();
            attributeResponse.setStatus("error");
            attributeResponse.setMessage("Thuộc tính đã tồn tại trong cửa hàng!");
            attributeResponse.setCode(400);
            attributeResponse.setAttributeDTO(AttributeDTO.convertEntityToDTO(attribute));

            return attributeResponse;
        }

        Attribute attribute = new Attribute();
        attribute.setName(attributeRequest.getName());
        attribute.setValue(attributeRequest.getValue());
        attribute.setShop(shop);
        attribute.setActive(true);
        attribute.setCreateAt(LocalDateTime.now());
        attribute.setUpdateAt(LocalDateTime.now());

        try {
            attributeRepository.save(attribute);

            AttributeResponse attributeResponse = new AttributeResponse();
            attributeResponse.setAttributeDTO(modelMapper.map(attribute, AttributeDTO.class));
            attributeResponse.setStatus("success");
            attributeResponse.setMessage("Thêm thuộc tính trong cửa hàng thành công!");
            attributeResponse.setCode(200);

            return attributeResponse;
        } catch (Exception e) {
            throw new BadRequestException("Thêm thuộc tính thất bại!");
        }

    }

    @Override
    public AttributeResponse getAttributeById(Long attributeId, String username) {
        Shop shop = shopService.getShopByUsername(username);
        Attribute attribute = checkAttributeInShop(attributeId, shop.getShopId());

        AttributeResponse attributeResponse = new AttributeResponse();
        attributeResponse.setAttributeDTO(modelMapper.map(attribute, AttributeDTO.class));
        attributeResponse.setStatus("ok");
        attributeResponse.setMessage("Lấy thông tin thuộc tính thành công!");
        attributeResponse.setCode(200);

        return attributeResponse;
    }

    @Override
    public ListAttributeResponse getListAttributeByShopId(String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Attribute> attributes = attributeRepository.findAllByShop_ShopIdAndActive(shop.getShopId(), true);
        if (attributes.isEmpty()) {
            throw new BadRequestException("Cửa hàng không có thuộc tính!");
        }
        attributes.sort(Comparator.comparing(Attribute::getName).thenComparing(Attribute::getValue));
        List<AttributeDTO> attributeDTOs = AttributeDTO.convertToListDTO(attributes);

        ListAttributeResponse response = new ListAttributeResponse();
        response.setAttributeDTOs(attributeDTOs);
        response.setStatus("ok");
        response.setMessage("Lấy danh sách thuộc tính trong cửa hàng thành công!");
        response.setCode(200);

        return response;
    }

    @Override
    @Transactional
    public AttributeResponse updateAttribute(AttributeRequest attributeRequest) {
        Shop shop = shopService.getShopByUsername(attributeRequest.getUsername());
        Attribute attribute = checkAttributeInShop(attributeRequest.getAttributeId(), shop.getShopId());
        // existsAttribute(attributeRequest.getName(), attributeRequest.getValue(),
        // shop.getShopId());

        attribute.setName(attributeRequest.getName());
        attribute.setValue(attributeRequest.getValue());
        attribute.setUpdateAt(LocalDateTime.now());

        try {
            attributeRepository.save(attribute);

            AttributeResponse attributeResponse = new AttributeResponse();
            attributeResponse.setAttributeDTO(modelMapper.map(attribute, AttributeDTO.class));
            attributeResponse.setStatus("success");
            attributeResponse.setMessage("Cập nhật thuộc tính thành công!");
            attributeResponse.setCode(200);

            return attributeResponse;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật thuộc tính thất bại!");
        }
    }

    @Override
    @Transactional
    public AttributeResponse lockOrActiveAttribute(Long attributeId, String username, boolean active) {
        Shop shop = shopService.getShopByUsername(username);
        Attribute attribute = checkAttributeInShop(attributeId, shop.getShopId());
        if (!active && attribute.isUsedInProductVariants()) {
            throw new BadRequestException("Thuộc tính đã được sử dụng trong sản phẩm nên không thể khóa!");
        }
        attribute.setActive(active);
        attribute.setUpdateAt(LocalDateTime.now());
        String message = active ? "Mở khóa" : "Khóa";

        try {
            attributeRepository.save(attribute);

            AttributeResponse attributeResponse = new AttributeResponse();
            attributeResponse.setAttributeDTO(modelMapper.map(attribute, AttributeDTO.class));
            attributeResponse.setStatus("success");
            attributeResponse.setMessage(message + " thuộc tính trong cửa hàng thành công.");
            attributeResponse.setCode(200);

            return attributeResponse;
        } catch (Exception e) {
            throw new BadRequestException(message + " thuộc tính thất bại!");
        }
    }

    @Override
    @Transactional
    public AttributeResponse deleteAttribute(Long attributeId, String username) {
        Shop shop = shopService.getShopByUsername(username);
        Attribute attribute = checkAttributeInShop(attributeId, shop.getShopId());
        if (attribute.isUsedInProductVariants()) {
            throw new BadRequestException("Thuộc tính đã được sử dụng trong sản phẩm nên không thể xóa!");
        }

        try {
            attributeRepository.delete(attribute);

            AttributeResponse attributeResponse = new AttributeResponse();
            attributeResponse.setAttributeDTO(modelMapper.map(attribute, AttributeDTO.class));
            attributeResponse.setStatus("success");
            attributeResponse.setMessage("Xóa thuộc tính trong cửa hàng thành công.");
            attributeResponse.setCode(200);

            return attributeResponse;
        } catch (Exception e) {
            throw new BadRequestException("Xóa thuộc tính thất bại trong cửa hàng!");
        }
    }

    @Override
    public List<Attribute> getListAttributeByListId(List<Long> attributeIds, Long shopId) {

        List<Attribute> attributes = new ArrayList<>();
        if (attributeIds != null) {
            attributeIds.forEach(attributeId -> {
                Attribute attribute = attributeRepository.findById(attributeId)
                        .orElseThrow(() -> new BadRequestException("Mã thuộc tính không tồn tại!"));
                if (!attribute.isActive()) {
                    throw new BadRequestException("Mã thuộc tính đã bị khóa trong cửa hàng!");
                }
                if (!attribute.getShop().getShopId().equals(shopId)) {
                    throw new BadRequestException("Mã thuộc tính không tồn tại trong cửa hàng!");
                }

                attributes.add(attributeRepository.findByAttributeId(attributeId));
            });
        }
        attributes.sort(Comparator.comparing(Attribute::getName).thenComparing(Attribute::getValue));

        return attributes;
    }

    private Attribute checkAttributeInShop(Long attributeId, Long shopId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy thuộc tính!"));

        if (!attribute.getShop().getShopId().equals(shopId)) {
            throw new BadRequestException("Không tìm thấy thuộc tính trong cửa hàng!");
        }
        return attribute;
    }

    public boolean existsAttribute(String name, String value, Long shopId) {
        boolean existsAttribute = attributeRepository.existsByNameAndValueAndShop_ShopId(
                name,
                value,
                shopId);
        return existsAttribute;
    }

}

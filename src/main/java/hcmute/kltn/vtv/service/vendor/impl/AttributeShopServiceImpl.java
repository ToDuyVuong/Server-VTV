package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.data.vendor.request.ProductAttributeRequest;
import hcmute.kltn.vtv.repository.vendor.AttributeRepository;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.AttributeRequest;
import hcmute.kltn.vtv.model.data.vendor.response.AttributeResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListAttributeResponse;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeShopServiceImpl implements IAttributeShopService {

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private IShopService shopService;


    @Override
    @Transactional
    public List<Attribute> addNewAttributesByProductAttributeRequests(List<ProductAttributeRequest> productAttributeRequests, Shop shop) {
        try {
            List<Attribute> attributes = new ArrayList<>();
            for (ProductAttributeRequest request : productAttributeRequests) {
                attributes.add(addNewAttributeByProductAttributeRequest(request, shop));
            }
            return attributes;
        } catch (Exception e) {
            throw new BadRequestException("Thêm danh sách thuộc tính thất bại!");
        }
    }

    @Override
    @Transactional
    public Attribute addNewAttributeByProductAttributeRequest(ProductAttributeRequest request, Shop shop) {
        Attribute attribute;
        try {
            if (existsAttributeByNameAndValueAndShopId(request.getName(), request.getValue(), shop.getShopId())) {
                attribute = getAttributeByNameAndValueAndShopId(request.getName(), request.getValue(), shop.getShopId());
            } else {
                attribute = createAttributeByNameAndValueAndShop(request.getName(), request.getValue(), shop);

                attributeRepository.save(attribute);
            }
            return attribute;
        } catch (Exception e) {
            throw new BadRequestException("Thêm thuộc tính thất bại!");
        }
    }


    @Override
    @Transactional
    public AttributeResponse addNewAttribute(AttributeRequest attributeRequest) {
        Shop shop = shopService.getShopByUsername(attributeRequest.getUsername());
        Attribute attribute;
        if (existsAttributeByNameAndValueAndShopId(attributeRequest.getName(), attributeRequest.getValue(), shop.getShopId())) {
            attribute = getAttributeByNameAndValueAndShopId(attributeRequest.getName(), attributeRequest.getValue(), shop.getShopId());

            return AttributeResponse.attributeResponse(attribute, "Thuộc tính đã tồn tại trong cửa hàng!", "OK");
        }

        attribute = createAttributeByNameAndValueAndShop(attributeRequest.getName(), attributeRequest.getValue(), shop);

        try {
            attributeRepository.save(attribute);

            return AttributeResponse.attributeResponse(attribute, "Thêm thuộc tính trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Thêm thuộc tính thất bại!");
        }

    }

    @Override
    public AttributeResponse getAttributeById(Long attributeId, String username) {
        Shop shop = shopService.getShopByUsername(username);
        Attribute attribute = checkAttributeInShop(attributeId, shop.getShopId());

        return AttributeResponse.attributeResponse(attribute, "Lấy thông tin thuộc tính thành công!", "OK");
    }

    @Override
    public ListAttributeResponse getListAttributeByShopId(String username) {
        Shop shop = shopService.getShopByUsername(username);
        List<Attribute> attributes = attributeRepository.findAllByShop_ShopIdAndActive(shop.getShopId(), true);

        return ListAttributeResponse.listAttributeResponse(attributes, "Lấy danh sách thuộc tính trong cửa hàng thành công!", "OK");
    }

    @Override
    @Transactional
    public AttributeResponse updateAttribute(AttributeRequest attributeRequest) {
        Shop shop = shopService.getShopByUsername(attributeRequest.getUsername());
        Attribute attribute = checkAttributeInShop(attributeRequest.getAttributeId(), shop.getShopId());
        attribute.setName(attributeRequest.getName());
        attribute.setValue(attributeRequest.getValue());
        attribute.setUpdateAt(LocalDateTime.now());
        try {
            attributeRepository.save(attribute);

            return AttributeResponse.attributeResponse(attribute, "Cập nhật thuộc tính thành công!", "Success");
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

            return AttributeResponse.attributeResponse(attribute, message + " thuộc tính thành công!", "Success");
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

            return AttributeResponse.attributeResponse(attribute, "Xóa thuộc tính trong cửa hàng thành công.", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Xóa thuộc tính thất bại trong cửa hàng!");
        }
    }

    @Override
    public List<Attribute> getListAttributeByListId(List<Long> attributeIds, Long shopId) {

        List<Attribute> attributes = new ArrayList<>();
        if (attributeIds != null) {
            attributeIds.forEach(attributeId -> {
                Attribute attribute = getAttributeByAttributeIdAndShopId(attributeId, shopId);
                attributes.add(attributeRepository.findByAttributeId(attributeId));
            });
        }

        return attributes;
    }


    private Attribute getAttributeByAttributeIdAndShopId(Long attributeId, Long shopId) {
        Attribute attribute = attributeRepository.findByAttributeIdAndShopShopId(attributeId, shopId).orElseThrow(() -> new BadRequestException("Mã thuộc tính không tồn tại trong cửa hàng!"));
        if (!attribute.isActive()) {
            throw new BadRequestException("Mã thuộc tính đã bị khóa trong cửa hàng!");
        }
        return attribute;
    }

    private Attribute getAttributeByNameAndValueAndShopId(String name, String value, Long shopId) {
        return attributeRepository.findByNameAndValueAndShop_ShopId(name, value, shopId).orElseThrow(() -> new BadRequestException("Thuộc tính không tồn tại!"));
    }


    private Attribute createAttributeByNameAndValueAndShop(String name, String value, Shop shop) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setValue(value);
        attribute.setShop(shop);
        attribute.setActive(true);
        attribute.setCreateAt(LocalDateTime.now());
        attribute.setUpdateAt(LocalDateTime.now());

        return attribute;
    }


    private Attribute checkAttributeInShop(Long attributeId, Long shopId) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new BadRequestException("Không tìm thấy thuộc tính!"));

        if (!attribute.getShop().getShopId().equals(shopId)) {
            throw new BadRequestException("Không tìm thấy thuộc tính trong cửa hàng!");
        }
        return attribute;
    }

    public boolean existsAttributeByNameAndValueAndShopId(String name, String value, Long shopId) {
        return attributeRepository.existsByNameAndValueAndShop_ShopId(name, value, shopId);
    }

}

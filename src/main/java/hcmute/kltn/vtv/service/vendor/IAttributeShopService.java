package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.AttributeRequest;
import hcmute.kltn.vtv.model.data.vendor.request.ProductAttributeRequest;
import hcmute.kltn.vtv.model.data.vendor.response.AttributeResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListAttributeResponse;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.entity.vtv.Shop;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAttributeShopService {
    @Transactional
    List<Attribute> addNewAttributesByProductAttributeRequests(List<ProductAttributeRequest> productAttributeRequests, Shop shop);

    @Transactional
    Attribute addNewAttributeByProductAttributeRequest(ProductAttributeRequest request, Shop shop);

    AttributeResponse addNewAttribute(AttributeRequest attributeRequest);

    AttributeResponse getAttributeById(Long attributeId, String username);

    ListAttributeResponse getListAttributeByShopId(String username);

    AttributeResponse updateAttribute(AttributeRequest attributeRequest);

    AttributeResponse lockOrActiveAttribute(Long attributeId, String username, boolean active);

    AttributeResponse deleteAttribute(Long attributeId, String username);

    List<Attribute> getListAttributeByListId(List<Long> attributeIds, Long shopId);
}

package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.AttributeRequest;
import hcmute.kltn.vtv.model.data.vendor.response.AttributeResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListAttributeResponse;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;

import java.util.List;

public interface IAttributeShopService {
    AttributeResponse addNewAttribute(AttributeRequest attributeRequest);

    AttributeResponse getAttributeById(Long attributeId, String username);

    ListAttributeResponse getListAttributeByShopId(String username);

    AttributeResponse updateAttribute(AttributeRequest attributeRequest);

    AttributeResponse lockOrActiveAttribute(Long attributeId, String username, boolean active);

    AttributeResponse deleteAttribute(Long attributeId, String username);

    List<Attribute> getListAttributeByListId(List<Long> attributeIds, Long shopId);
}

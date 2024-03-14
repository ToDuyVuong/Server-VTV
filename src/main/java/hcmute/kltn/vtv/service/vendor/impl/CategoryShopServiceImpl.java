package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.repository.vendor.CategoryShopRepository;
import hcmute.kltn.vtv.service.vendor.ICategoryShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryShopServiceImpl implements ICategoryShopService {

    @Autowired
    private final CategoryShopRepository categoryShopRepository;


}

package hcmute.kltn.vtv.service.guest.impl;


import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements IPageService {



    @Override
    public void checkRequestProductPageParams(int page, int size) {
        if (page < 0) {
            throw new NotFoundException("Số trang không được nhỏ hơn 0!");
        }
        if (size < 0) {
            throw new NotFoundException("Số lượng sản phẩm không được nhỏ hơn 0!");
        }
        if (size > 200) {
            throw new NotFoundException("Số lượng sản phẩm không được lớn hơn 200!");
        }
    }



    @Override
    public void checkRequestOrderPageParams(int page, int size) {
        if (page < 0) {
            throw new NotFoundException("Số trang không được nhỏ hơn 0!");
        }
        if (size < 0) {
            throw new NotFoundException("Số lượng đơn hàng không được nhỏ hơn 0!");
        }
        if (size > 200) {
            throw new NotFoundException("Số lượng đơn hàng không được lớn hơn 200!");
        }
    }


    @Override
    public void checkRequestPriceRangeParams(Long minPrice, Long maxPrice) {
        if (minPrice < 0) {
            throw new NotFoundException("Giá lọc sản phẩm nhỏ nhất không được nhỏ hơn 0!");
        }
        if (maxPrice < 0) {
            throw new NotFoundException("Giá lọc sản phẩm lớn nhất không được nhỏ hơn 0!");
        }
        if (minPrice > maxPrice) {
            throw new NotFoundException("Giá lọc sản phẩm nhỏ nhất không được lớn hơn giá lớn nhất!");
        }
    }

    @Override
    public void checkRequestSortParams(String sort) {
        if (sort == null) {
            throw new NotFoundException("Tham số sắp xếp không được để trống!");
        }
        if (!sort.equals("best-selling") && !sort.equals("price-asc") && !sort.equals("price-desc")
                && !sort.equals("newest") && !sort.equals("random")) {
            throw new NotFoundException(
                    "Tham số sắp xếp không hợp lệ! Chỉ được sắp xếp theo: best-selling, price-asc, price-desc, newest");
        }
    }




}

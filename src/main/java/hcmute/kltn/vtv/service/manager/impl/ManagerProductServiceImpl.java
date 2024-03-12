package hcmute.kltn.vtv.service.manager.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.manager.response.ListManagerProductResponse;
import hcmute.kltn.vtv.model.data.manager.response.ManagerProductResponse;
import hcmute.kltn.vtv.model.dto.manager.ManagerProductDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.entity.manager.ManagerProduct;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.repository.vendor.ProductVariantRepository;
import hcmute.kltn.vtv.repository.manager.ManagerProductRepository;
import hcmute.kltn.vtv.service.manager.IManagerProductService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerProductServiceImpl implements IManagerProductService {

    @Autowired
    private final ManagerProductRepository managerProductRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ICustomerService customerService;
    @Autowired
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public ManagerProductResponse lockProductByProductId(Long productId, String username, String note) {
        Customer manager = customerService.getCustomerByUsername(username);
        Product product = checkProductByProductId(productId);

        ManagerProduct managerProduct = new ManagerProduct();
        if (managerProductRepository.existsByProduct_ProductId(productId)) {
            managerProduct = managerProductRepository.findByProduct_ProductId(productId)
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));
            if (managerProduct.isLock()) {
                throw new BadRequestException("Sản phẩm đã bị khóa");
            }
        } else {
            managerProduct.setProduct(product);
            managerProduct.setManager(manager);
            managerProduct.setCreateAt(LocalDateTime.now());
        }
        managerProduct.setUpdateAt(LocalDateTime.now());
        managerProduct.setLock(true);
        managerProduct.setDelete(false);
        managerProduct.setNote(note);
        product.setUpdateAt(managerProduct.getUpdateAt());
        try {
            managerProductRepository.save(managerProduct);
            lockProduct(product);

            ManagerProductResponse managerProductResponse = new ManagerProductResponse();
            managerProductResponse.setManagerProductDTO(ManagerProductDTO.convertEntityToDTO(managerProduct));
            managerProductResponse.setStatus("success");
            managerProductResponse.setMessage("Khóa sản phẩm thành công");
            managerProductResponse.setCode(200);

            return managerProductResponse;
        } catch (Exception e) {
            throw new BadRequestException("Khóa sản phẩm thất bại");
        }
    }

    @Override
    @Transactional
    public ManagerProductResponse unLockProductByProductId(Long productId, String username, String note) {

        Product product = checkProductByProductId(productId);

        ManagerProduct managerProduct = checkManagerProduct(productId, username);

        managerProduct.setUpdateAt(LocalDateTime.now());
        managerProduct.setLock(false);
        managerProduct.setDelete(true);
        managerProduct.setNote(note);

        product.setUpdateAt(managerProduct.getUpdateAt());

        try {
            managerProductRepository.save(managerProduct);

            unLockProduct(product);

            ManagerProductResponse managerProductResponse = new ManagerProductResponse();
            managerProductResponse.setManagerProductDTO(ManagerProductDTO.convertEntityToDTO(managerProduct));
            managerProductResponse.setStatus("success");
            managerProductResponse.setMessage("Mở khóa sản phẩm thành công");
            managerProductResponse.setCode(200);

            return managerProductResponse;
        } catch (Exception e) {
            throw new BadRequestException("Mở khóa sản phẩm thất bại");
        }
    }

    @Override
    public ListManagerProductResponse getListManagerProduct(int page, int size) {
        int totalManagerProduct = managerProductRepository.countAllByLock(true);
        int totalPage = (int) Math.ceil((double) totalManagerProduct / size);

        Page<ManagerProduct> managerProducts = managerProductRepository
                .findAllByLock(true, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách sản phẩm đã khóa"));
        String message = "Lấy danh sách sản phẩm đã khóa thành công";

        return listManagerProductResponse(managerProducts.getContent(), page, totalPage, size, message);
    }


    @Override
    public boolean checkExistProductUseCategory(Long categoryId) {
        return productRepository.existsByAndCategoryCategoryId(categoryId);
    }






    @Override
    public ListManagerProductResponse getListManagerProductByProductName(int page, int size, String productName) {
        int totalManagerProduct = managerProductRepository.countAllByLockAndProductNameContains(true, productName);
        int totalPage = (int) Math.ceil((double) totalManagerProduct / size);

        Page<ManagerProduct> managerProducts = managerProductRepository
                .findAllByLockAndProductNameContains(true, productName, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new BadRequestException("Không tìm thấy danh sách sản phẩm đã khóa"));
        String message = "Lấy danh sách sản phẩm đã khóa thành công";

        return listManagerProductResponse(managerProducts.getContent(), page, totalPage, size, message);
    }

    private ListManagerProductResponse listManagerProductResponse(List<ManagerProduct> managerProducts, int page,
            int totalPage, int size, String message) {
        ListManagerProductResponse listManagerProductResponse = new ListManagerProductResponse();
        listManagerProductResponse.setCount(managerProducts.size());
        listManagerProductResponse.setTotalPage(totalPage);
        listManagerProductResponse
                .setManagerProductDTOs(ManagerProductDTO.convertListEntitiesToListDTOs(managerProducts));
        listManagerProductResponse.setMessage(message);
        listManagerProductResponse.setStatus("OK");
        listManagerProductResponse.setCode(200);
        listManagerProductResponse.setPage(page);
        listManagerProductResponse.setSize(size);

        return listManagerProductResponse;
    }

    @Override
    public void checkRequestPageParams(int page, int size) {
        if (page < 0) {
            throw new NotFoundException("Trang không được nhỏ hơn 0!");
        }
        if (size < 0) {
            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
        }
        if (size > 500) {
            throw new NotFoundException("Kích thước trang không được lớn hơn 200!");
        }
    }

    public ManagerProduct checkManagerProduct(Long productId, String username) {
        ManagerProduct managerProduct = managerProductRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));
        if (!managerProduct.getManager().getUsername().equals(username)) {
            throw new BadRequestException("Bạn không có quyền thực hiện hành động này");
        }
        if (!managerProduct.isLock()) {
            throw new BadRequestException("Sản phẩm chưa bị khóa");
        }

        return managerProduct;
    }

    @Transactional
    public void unLockProduct(Product product) {
        product.setStatus(Status.ACTIVE);

        List<ProductVariant> productVariants = product.getProductVariants();
        for (ProductVariant productVariant : productVariants) {
            if (productVariant.getStatus().equals(Status.LOCKED)) {
                productVariant.setStatus(Status.ACTIVE);
                productVariant.setUpdateAt(product.getUpdateAt());
                try {
                    productVariantRepository.save(productVariant);
                } catch (Exception e) {
                    throw new BadRequestException("Mở khóa biến thể sản phẩm thất bại");
                }
            }

        }

        try {
            productRepository.save(product);
        } catch (Exception e) {
            throw new BadRequestException("Mở khóa sản phẩm thất bại");
        }
    }

    @Transactional
    public void lockProduct(Product product) {
        product.setStatus(Status.LOCKED);

        List<ProductVariant> productVariants = product.getProductVariants();
        for (ProductVariant productVariant : productVariants) {
            if (productVariant.getStatus().equals(Status.ACTIVE)) {
                productVariant.setStatus(Status.LOCKED);
                productVariant.setUpdateAt(product.getUpdateAt());
            }
            try {
                productVariantRepository.save(productVariant);
            } catch (Exception e) {
                throw new BadRequestException("Khóa biến thể sản phẩm thất bại");
            }
        }

        try {
            productRepository.save(product);
        } catch (Exception e) {
            throw new BadRequestException("Khóa sản phẩm thất bại");
        }
    }

    private Product checkProductByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy sản phẩm"));

        if (product.getStatus().equals(Status.DELETED)) {
            throw new BadRequestException("Sản phẩm đã bị xóa");
        }

        return product;
    }




}

package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.repository.vtv.AttributeRepository;
import hcmute.kltn.vtv.repository.vtv.BrandRepository;
import hcmute.kltn.vtv.repository.vtv.ProductRepository;
import hcmute.kltn.vtv.repository.vtv.ProductVariantRepository;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.model.dto.vtv.ProductDTO;
import hcmute.kltn.vtv.model.dto.vtv.ProductVariantDTO;
import hcmute.kltn.vtv.model.entity.vtv.*;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.guest.IReviewService;
import hcmute.kltn.vtv.service.vendor.IProductShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductShopServiceImpl implements IProductShopService {

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private IShopService shopService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;
    @Autowired
    private IProductVariantShopService productVariantShopService;
    @Autowired
    private final IReviewService reviewService;
    @Autowired
    private IProductVariantService productVariantService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductResponse addNewProduct(ProductRequest request) {
        Category category = categoryService.getCategoryById(request.getCategoryId());
        Brand brand = checkBrand(request.getBrandId());
        Long shopId = shopService.getShopByUsername(request.getUsername()).getShopId();


        if (productRepository.existsByNameAndStatus(
                request.getName(), Status.ACTIVE)) {
            throw new BadRequestException("Sản phẩm có tên đã tồn tại trong cửa hàng!");
        }

        List<ProductVariant> productVariants = productVariantShopService.addNewListProductVariant(
                request.getProductVariantRequests(), shopId);

        Product product = modelMapper.map(request, Product.class);
        product.setCategory(category);
        product.setBrand(brand);
        product.setSold(0L);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());
        product.setStatus(Status.ACTIVE);

        try {
            Product saveProduct = productRepository.save(product);
            updateProductVariant(saveProduct, productVariants);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(productVariants));
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductDTO(productDTO);
            productResponse.setStatus("success");
            productResponse.setMessage("Thêm sản phẩm mới trong cửa hàng thành công!");
            productResponse.setCode(200);

            return productResponse;
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm sản phẩm mới trong cửa hàng thất bại!");
        }
    }

    @Override
    public ProductResponse getProductDetail(Long productId, String username) {
        Product product = getProductByIdOnShop(productId, username);

        if (product.getStatus() == Status.DELETED) {
            List<ProductVariant> productVariants = productVariantService
                    .filterProductVariantsByStatus(product.getProductVariants(), Status.ACTIVE);
            product.setProductVariants(productVariants);
        }


        return ProductResponse.productResponse(product, "Lấy thông tin sản phẩm thành công!", "OK");
    }

    @Override
    public ListProductResponse getListProductByUsername(String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Product> products = productRepository
                .findAllByShopShopIdAndStatus(shop.getShopId(), Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đang bán!"));
        return getListProductResponseSort(products,
                "Lấy danh sách sản phẩm đang bán trong cửa hàng thành công.",
                true);
    }

    @Override
    public ListProductPageResponse getListProductByUsernamePage(String username, int page, int size) {

        Shop shop = shopService.getShopByUsername(username);

        Page<Product> pageProduct = productRepository
                .findAllByShopShopIdAndStatus(shop.getShopId(), Status.ACTIVE, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đang bán!"));

        String message = "Lấy danh sách sản phẩm đang bán trong cửa hàng thành công.";
        return ListProductPageResponse.listProductPageResponse(pageProduct, size, message);

    }


    @Override
    public ListProductResponse getListProductShopByCategoryId(Long categoryId, String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Product> products = productRepository
                .findAllByCategoryCategoryIdAndShopShopIdAndStatus(categoryId, shop.getShopId(), Status.ACTIVE)
                .orElseThrow(
                        () -> new NotFoundException("Cửa hàng không có sản phẩm đang bán nào thuộc danh mục này!"));

        return getListProductResponseSort(products,
                "Lấy danh sách sản phẩm đang bán trong danh mục thành công cửa hàng thành công.",
                true);
    }

    @Override
    public ListProductResponse searchProductsByName(String productName, String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Product> products = productRepository
                .findAllByNameContainingAndShopShopIdAndStatus(productName, shop.getShopId(), Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào có tên tương tự!"));

        return getListProductResponseSort(products, "Tìm kiếm sản phẩm theo tên trong cửa hàng thành công!",
                true);
    }

    @Override
    public ListProductResponse getBestSellingProducts(int limit, String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Product> products = productRepository
                .findByShopShopIdAndStatusOrderBySoldDescNameAsc(shop.getShopId(), Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm bán chạy!"));

        ListProductResponse response = getListProductResponseSort(products,
                "Lấy danh sách sản phẩm bán chạy trong cửa hàng thành công.",
                false);

        if (response.getCount() > limit) {
            response.setProductDTOs(response.getProductDTOs().subList(0, limit));
            response.setCount(limit);
        }

        return response;
    }

    @Override
    public ListProductResponse getListProductByPriceRange(String username, Long minPrice, Long maxPrice) {
        Shop shop = shopService.getShopByUsername(username);
        List<Product> products = productRepository.findByPriceRange(shop.getShopId(), Status.ACTIVE, minPrice, maxPrice)
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm nào trong khoảng giá này!"));
        return getListProductResponseSort(products, "Lọc sản phẩm theo giá trong cửa hàng thành công.", true);
    }

    @Override
    public ListProductResponse getListNewProduct(String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Product> products = productRepository
                .findByShopShopIdAndStatusOrderByCreateAtDesc(shop.getShopId(), Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm mới!"));

        return getListProductResponseSort(products,
                "Lấy danh sách sản phẩm mới trong cửa hàng thành công.",
                false);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest) {
        Product product = getProductByIdOnShop(productRequest.getProductId(), productRequest.getUsername());
        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
        Long shopId = shopService.getShopByUsername(productRequest.getUsername()).getShopId();

        Brand brand = checkBrand(productRequest.getBrandId());

        if (!product.getName().equals(productRequest.getName()) &&
                productRepository.existsByNameAndStatus(
                        productRequest.getName(), Status.ACTIVE)) {
            throw new BadRequestException("Sản phẩm cập nhật đã có tên đã tồn tại trong cửa hàng!");
        }

        List<ProductVariant> productVariants = productVariantShopService.getListProductVariant(
                productRequest.getProductVariantRequests(),
                shopId, product.getProductId());

        product.setName(productRequest.getName());
        product.setImage(productRequest.getImage());
        product.setDescription(productRequest.getDescription());
        product.setInformation(productRequest.getInformation());
        product.setCategory(category);
        product.setBrand(brand);
        product.setUpdateAt(LocalDateTime.now());

        try {
            productRepository.save(product);

            List<ProductVariant> activeProductVariants = productVariants.stream()
                    .filter(productVariant -> productVariant.getStatus() == Status.ACTIVE)
                    .toList();

            updateProductVariant(product, productVariants);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));

            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductDTO(productDTO);
            productResponse.setStatus("success");
            productResponse.setMessage("Cập nhật sản phẩm trong cửa hàng thành công!");
            productResponse.setCode(200);

            return productResponse;
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm sản phẩm mới trong cửa hàng thất bại!");
        }
    }

    @Override
    @Transactional
    public ProductResponse updateStatusProduct(Long productId, String username, Status status) {
        Product product = getProductByIdOnShop(productId, username);

        if (product.getStatus() == Status.DELETED) {
            throw new BadRequestException("Sản phẩm đã bị xóa trong cửa hàng!");
        }

        product.setStatus(status);
        product.setUpdateAt(LocalDateTime.now());

        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() != Status.DELETED)
                .toList();

        updateProductVariant(product, activeProductVariants);

        try {
            productRepository.save(product);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductDTO(productDTO);
            productResponse.setStatus("success");
            productResponse.setMessage("Cập nhật trạng thái sản phẩm trong cửa hàng thành công!");
            productResponse.setCode(200);

            return productResponse;
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái sản phẩm trong cửa hàng thất bại!");
        }

    }

    @Override
    @Transactional
    public ProductResponse restoreProductById(Long productId, String username) {
        Product product = getProductByIdOnShop(productId, username);

        if (product.getStatus() != Status.DELETED) {
            throw new BadRequestException("Sản phẩm chưa bị xóa trong cửa hàng!");
        }

        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getUpdateAt().equals(product.getUpdateAt()))
                .toList();

        product.setStatus(Status.ACTIVE);
        product.setUpdateAt(LocalDateTime.now());
        updateProductVariant(product, activeProductVariants);

        try {
            productRepository.save(product);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductDTO(productDTO);
            productResponse.setStatus("success");
            productResponse.setMessage("Khôi phục sản phẩm trong cửa hàng thành công!");
            productResponse.setCode(200);

            return productResponse;
        } catch (Exception e) {
            throw new InternalServerErrorException("Khôi phục sản phẩm trong cửa hàng thất bại!");
        }

    }

    @Override
    public ListProductResponse getAllDeletedProduct(String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Product> products = productRepository
                .findAllByShopShopIdAndStatus(shop.getShopId(), Status.DELETED)
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đã xóa!"));

        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = getProductDeleteToDTO(product);
            productDTOs.add(productDTO);
        }

        ListProductResponse response = new ListProductResponse();
        response.setProductDTOs(productDTOs);
        response.setCount(productDTOs.size());
        response.setStatus("ok");
        response.setMessage("Lấy danh sách sản phẩm đã xóa trong cửa hàng thành công.");
        response.setCode(200);

        return response;
    }

    @Override
    public ListProductResponse getListProductResponseSort(List<Product> products, String message, boolean isSort) {
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = getProductToDTO(product);
            productDTOs.add(productDTO);
        }

        if (isSort) {
            productDTOs.sort(Comparator.comparing(ProductDTO::getName));
        }

        ListProductResponse response = new ListProductResponse();
        response.setProductDTOs(productDTOs);
        response.setCount(productDTOs.size());
        response.setStatus("ok");
        response.setMessage(message);
        response.setCode(200);

        return response;
    }

    @Override
    public ProductDTO getProductToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() != Status.DELETED)
                .toList();

        productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
        return productDTO;
    }

    private Brand checkBrand(Long brandId) {
        if (brandId == null) {
            return null;
        }

        return brandRepository.findById(brandId)
                .orElseThrow(() -> new BadRequestException("Thương hiệu không tồn tại!"));
    }

    private ProductDTO getProductDeleteToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getUpdateAt().equals(product.getUpdateAt()))
                .toList();

        productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
        return productDTO;
    }

    private Product getProductByIdOnShop(Long productId, String username) {
        Long shopId = shopService.getShopByUsername(username).getShopId();

        checkProductInShop(productId, shopId);

        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
    }

    private void updateProductVariant(Product product, List<ProductVariant> productVariants) {
        for (ProductVariant productVariant : productVariants) {
            productVariant.setProduct(product);
            productVariant.setUpdateAt(product.getUpdateAt());
            try {
                productVariantRepository.save(productVariant);
            } catch (Exception e) {
                throw new InternalServerErrorException("Thêm biến thể sản phẩm thất bại!");
            }
        }
    }


    private void checkProductInShop(Long productId, Long shopId) {
        if (!productRepository.existsByProductIdAndShopShopId(productId, shopId)) {
            throw new NotFoundException("Sản phẩm không tồn tại trong cửa hàng!");
        }
    }

}

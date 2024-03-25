package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.repository.vtv.BrandRepository;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.service.guest.ICategoryService;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import hcmute.kltn.vtv.model.entity.vtv.*;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IProductShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductShopServiceImpl implements IProductShopService {


    @Autowired
    private IShopService shopService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IProductVariantShopService productVariantShopService;
    @Autowired
    private IImageService imageService;


    @Override
    @Transactional
    public ProductResponse addNewProduct(ProductRequest request, String username) {
        checkExistProductByName(request.getName());
        Category category = categoryService.getCategoryById(request.getCategoryId());
        categoryService.checkExistCategoryHasChild(category.getCategoryId());
        Brand brand = checkBrand(request.getBrandId());
        Shop shop = shopService.getShopByUsername(username);
        Product product = createProductByRequest(request, category, brand, shop);
        try {
            productRepository.save(product);
            List<ProductVariant> productVariants = productVariantShopService.addNewProductVariants(request.getProductVariantRequests(), product);
            product.setProductVariants(productVariants);
            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());
            return ProductResponse.productResponse(product, countOrder, "Thêm sản phẩm mới trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            imageService.deleteImageInFirebase(product.getImage());
            throw new InternalServerErrorException("Thêm sản phẩm mới trong cửa hàng thất bại!");

        }
    }


    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest request, String username) {
        checkExistProductByProductIdAndUsername(request.getProductId(), username);
        checkUpdateProductExistByName(request.getName(), request.getProductId());
        Product product = getProductByProductId(request.getProductId());
        Category category = categoryService.getCategoryById(request.getCategoryId());
        updateProductByRequest(product, request, category, checkBrand(request.getBrandId()));
        try {
            productRepository.save(product);
            List<ProductVariant> productVariants = productVariantShopService.updateProductVariants(request.getProductVariantRequests(), product);
            product.setProductVariants(productVariants);

            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());
            return ProductResponse.productResponse(product, countOrder, "Cập nhật sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật sản phẩm trong cửa hàng thất bại! " + e.getMessage());

        }
    }


    @Override
    @Transactional
    public ProductResponse updateStatusProduct(Long productId, String username, Status status) {
        checkExistProductByProductIdAndUsername(productId, username);
        Product product = getProductByProductId(productId);
        updateStatusProductByRequest(product, status);
        try {
            productRepository.save(product);
            productVariantShopService.updateStatusProductVariants(productId, product.getProductVariants(), status);
            Product productUpdate = getProductByProductId(productId);

            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());
            return ProductResponse.productResponse(productUpdate, countOrder, "Cập nhật trạng thái sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái sản phẩm trong cửa hàng thất bại!");
        }
    }



    @Override
    @Transactional
    public ProductResponse restoreProductById(Long productId, String username) {
        checkExistProductByProductIdAndUsername(productId, username);
        Product product = getProductByProductId(productId);
        restoreProduct(product);
        try {
            productRepository.save(product);
            productVariantShopService.updateStatusProductVariants(productId, product.getProductVariants(), Status.ACTIVE);
            Product productUpdate = getProductByProductId(productId);

            int countOrder = productRepository.countOrdersByProductId(product.getProductId(), OrderStatus.COMPLETED.toString());
            return ProductResponse.productResponse(productUpdate, countOrder, "Khôi phục sản phẩm trong cửa hàng thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Khôi phục sản phẩm trong cửa hàng thất bại!");
        }
    }


    @Override
    public ListProductPageResponse getListProductPageByUsernameAndStatus(String username, int page, int size, Status status) {
        Shop shop = shopService.getShopByUsername(username);
        Page<Product> products = productRepository
                .findAllByShopShopIdAndStatus(shop.getShopId(), status, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đang bán!"));
        String message = "Lấy danh sách sản phẩm theo trạng thái trong cửa hàng thành công.";

        return ListProductPageResponse.listProductPageResponse(products, size, message);
    }


    private void restoreProduct(Product product) {
        if (product.getStatus() != Status.DELETED) {
            throw new BadRequestException("Sản phẩm chưa bị xóa trong cửa hàng!");
        }
        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() == Status.DELETED)
                .toList();
        product.setStatus(Status.ACTIVE);
        product.setUpdateAt(LocalDateTime.now());
    }


    private Product getProductByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
    }


    private void checkUpdateProductExistByName(String name, Long productId) {
        if (productRepository.existsByNameAndProductIdNot(name, productId)) {
            throw new BadRequestException("Sản phẩm cập nhật đã có tên đã tồn tại trong cửa hàng!");
        }
    }


    private void checkExistProductByProductIdAndUsername(Long productId, String username) {
        if (!productRepository.existsByProductIdAndShopCustomerUsername(productId, username)) {
            throw new NotFoundException("Sản phẩm không tồn tại trong cửa hàng!");
        }
    }


    private void checkExistByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Sản phẩm không tồn tại!");
        }
    }


    private Product getProductByIdOnShop(Long productId, Long shopId) {
        checkProductInShop(productId, shopId);

        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
    }


    private void checkProductInShop(Long productId, Long shopId) {
        if (productRepository.existsByProductIdAndShopShopId(productId, shopId)) {
            throw new NotFoundException("Sản phẩm không tồn tại trong cửa hàng!");
        }
    }


    private void updateStatusProductByRequest(Product product, Status status) {
        if (product.getStatus() == Status.DELETED) {
            throw new BadRequestException("Sản phẩm đã bị xóa trong cửa hàng!");
        }
        product.setStatus(status);
        product.setUpdateAt(LocalDateTime.now());
        List<ProductVariant> productVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() != Status.DELETED && productVariant.getStatus() != Status.LOCKED)
                .collect(Collectors.toList());
        product.setProductVariants(productVariants);
    }


    private void updateProductByRequest(Product product, ProductRequest request, Category category, Brand brand) {
        product.setName(request.getName());
        product.setImage(request.isChangeImage() ? imageService.uploadImageToFirebase(request.getImage()) : product.getImage());
        product.setDescription(request.getDescription());
        product.setInformation(request.getInformation());
        product.setCategory(category);
        product.setBrand(brand);
        product.setUpdateAt(LocalDateTime.now());
    }


    private Product createProductByRequest(ProductRequest request, Category category, Brand brand, Shop shop) {
        Product product = new Product();
        product.setName(request.getName());
        product.setImage(imageService.uploadImageToFirebase(request.getImage()));
        product.setDescription(request.getDescription());
        product.setInformation(request.getInformation());
        product.setSold(0L);
        product.setShop(shop);
        product.setCategory(category);
        product.setBrand(brand);
        product.setStatus(Status.ACTIVE);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());

        return product;
    }


    private Brand checkBrand(Long brandId) {
        if (brandId == null) {
            return null;
        }

        return brandRepository.findById(brandId)
                .orElseThrow(() -> new BadRequestException("Thương hiệu không tồn tại!"));
    }


    private void checkExistProductByName(String name) {
        if (productRepository.existsByName(name)) {
            throw new BadRequestException("Sản phẩm có tên đã tồn tại trong cửa hàng!");
        }
    }


//    @Autowired
//    ModelMapper modelMapper;
//
//    @Override
//    @Transactional
//    public ProductResponse addNewProduct(ProductRequest request) {
//        Category category = categoryService.getCategoryById(request.getCategoryId());
//        categoryService.checkExistCategoryHasChild(category.getCategoryId());
//        Brand brand = checkBrand(request.getBrandId());
//        Long shopId = shopService.getShopByUsername(request.getUsername()).getShopId();
//
//
//        if (productRepository.existsByNameAndStatus(
//                request.getName(), Status.ACTIVE)) {
//            throw new BadRequestException("Sản phẩm có tên đã tồn tại trong cửa hàng!");
//        }
//
//        List<ProductVariant> productVariants = productVariantShopService.addNewListProductVariant(
//                request.getProductVariantRequests(), shopId);
//
//        Product product = modelMapper.map(request, Product.class);
//        product.setCategory(category);
//        product.setBrand(brand);
//        product.setSold(0L);
//        product.setCreateAt(LocalDateTime.now());
//        product.setUpdateAt(LocalDateTime.now());
//        product.setStatus(Status.ACTIVE);
//
//        try {
//            Product saveProduct = productRepository.save(product);
//            updateProductVariant(saveProduct, productVariants);
//
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(productVariants));
//            ProductResponse productResponse = new ProductResponse();
//            productResponse.setProductDTO(productDTO);
//            productResponse.setStatus("success");
//            productResponse.setMessage("Thêm sản phẩm mới trong cửa hàng thành công!");
//            productResponse.setCode(200);
//
//            return productResponse;
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Thêm sản phẩm mới trong cửa hàng thất bại!");
//        }
//    }
//
//    @Override
//    public ProductResponse getProductDetail(Long productId, String username) {
//        Product product = getProductByIdOnShop(productId, username);
//
//        if (product.getStatus() == Status.DELETED) {
//            List<ProductVariant> productVariants = productVariantService
//                    .filterProductVariantsByStatus(product.getProductVariants(), Status.ACTIVE);
//            product.setProductVariants(productVariants);
//        }
//
//
//        return ProductResponse.productResponse(product, "Lấy thông tin sản phẩm thành công!", "OK");
//    }
//
//    @Override
//    public ListProductResponse getListProductByUsername(String username) {
//        Shop shop = shopService.getShopByUsername(username);
//
//        List<Product> products = productRepository
//                .findAllByShopShopIdAndStatus(shop.getShopId(), Status.ACTIVE)
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đang bán!"));
//        return getListProductResponseSort(products,
//                "Lấy danh sách sản phẩm đang bán trong cửa hàng thành công.",
//                true);
//    }
//
//    @Override
//    public ListProductPageResponse getListProductByUsernamePage(String username, int page, int size) {
//
//        Shop shop = shopService.getShopByUsername(username);
//
//        Page<Product> pageProduct = productRepository
//                .findAllByShopShopIdAndStatus(shop.getShopId(), Status.ACTIVE, PageRequest.of(page - 1, size))
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đang bán!"));
//
//        String message = "Lấy danh sách sản phẩm đang bán trong cửa hàng thành công.";
//        return ListProductPageResponse.listProductPageResponse(pageProduct, size, message);
//
//    }
//
//
//    @Override
//    public ListProductResponse getListProductShopByCategoryId(Long categoryId, String username) {
//        Shop shop = shopService.getShopByUsername(username);
//
//        List<Product> products = productRepository
//                .findAllByCategoryCategoryIdAndShopShopIdAndStatus(categoryId, shop.getShopId(), Status.ACTIVE)
//                .orElseThrow(
//                        () -> new NotFoundException("Cửa hàng không có sản phẩm đang bán nào thuộc danh mục này!"));
//
//        return getListProductResponseSort(products,
//                "Lấy danh sách sản phẩm đang bán trong danh mục thành công cửa hàng thành công.",
//                true);
//    }
//
//    @Override
//    public ListProductResponse searchProductsByName(String productName, String username) {
//        Shop shop = shopService.getShopByUsername(username);
//
//        List<Product> products = productRepository
//                .findAllByNameContainingAndShopShopIdAndStatus(productName, shop.getShopId(), Status.ACTIVE)
//                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào có tên tương tự!"));
//
//        return getListProductResponseSort(products, "Tìm kiếm sản phẩm theo tên trong cửa hàng thành công!",
//                true);
//    }
//
//    @Override
//    public ListProductResponse getBestSellingProducts(int limit, String username) {
//        Shop shop = shopService.getShopByUsername(username);
//
//        List<Product> products = productRepository
//                .findByShopShopIdAndStatusOrderBySoldDescNameAsc(shop.getShopId(), Status.ACTIVE)
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm bán chạy!"));
//
//        ListProductResponse response = getListProductResponseSort(products,
//                "Lấy danh sách sản phẩm bán chạy trong cửa hàng thành công.",
//                false);
//
//        if (response.getCount() > limit) {
//            response.setProductDTOs(response.getProductDTOs().subList(0, limit));
//            response.setCount(limit);
//        }
//
//        return response;
//    }
//
//    @Override
//    public ListProductResponse getListProductByPriceRange(String username, Long minPrice, Long maxPrice) {
//        Shop shop = shopService.getShopByUsername(username);
//        List<Product> products = productRepository.findByPriceRange(shop.getShopId(), Status.ACTIVE, minPrice, maxPrice)
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm nào trong khoảng giá này!"));
//        return getListProductResponseSort(products, "Lọc sản phẩm theo giá trong cửa hàng thành công.", true);
//    }
//
//    @Override
//    public ListProductResponse getListNewProduct(String username) {
//        Shop shop = shopService.getShopByUsername(username);
//
//        List<Product> products = productRepository
//                .findByShopShopIdAndStatusOrderByCreateAtDesc(shop.getShopId(), Status.ACTIVE)
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm mới!"));
//
//        return getListProductResponseSort(products,
//                "Lấy danh sách sản phẩm mới trong cửa hàng thành công.",
//                false);
//    }
//
//    @Override
//    @Transactional
//    public ProductResponse updateProduct(ProductRequest productRequest) {
//        Product product = getProductByIdOnShop(productRequest.getProductId(), productRequest.getUsername());
//        Category category = categoryService.getCategoryById(productRequest.getCategoryId());
//        Long shopId = shopService.getShopByUsername(productRequest.getUsername()).getShopId();
//
//        Brand brand = checkBrand(productRequest.getBrandId());
//
//        if (!product.getName().equals(productRequest.getName()) &&
//                productRepository.existsByNameAndStatus(
//                        productRequest.getName(), Status.ACTIVE)) {
//            throw new BadRequestException("Sản phẩm cập nhật đã có tên đã tồn tại trong cửa hàng!");
//        }
//
//        List<ProductVariant> productVariants = productVariantShopService.getListProductVariant(
//                productRequest.getProductVariantRequests(),
//                shopId, product.getProductId());
//
//        product.setName(productRequest.getName());
//        product.setImage(productRequest.getImage());
//        product.setDescription(productRequest.getDescription());
//        product.setInformation(productRequest.getInformation());
//        product.setCategory(category);
//        product.setBrand(brand);
//        product.setUpdateAt(LocalDateTime.now());
//
//        try {
//            productRepository.save(product);
//
//            List<ProductVariant> activeProductVariants = productVariants.stream()
//                    .filter(productVariant -> productVariant.getStatus() == Status.ACTIVE)
//                    .toList();
//
//            updateProductVariant(product, productVariants);
//
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
//
//            ProductResponse productResponse = new ProductResponse();
//            productResponse.setProductDTO(productDTO);
//            productResponse.setStatus("success");
//            productResponse.setMessage("Cập nhật sản phẩm trong cửa hàng thành công!");
//            productResponse.setCode(200);
//
//            return productResponse;
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Thêm sản phẩm mới trong cửa hàng thất bại!");
//        }
//    }
//
//    @Override
//    @Transactional
//    public ProductResponse updateStatusProduct(Long productId, String username, Status status) {
//        Product product = getProductByIdOnShop(productId, username);
//
//        if (product.getStatus() == Status.DELETED) {
//            throw new BadRequestException("Sản phẩm đã bị xóa trong cửa hàng!");
//        }
//
//        product.setStatus(status);
//        product.setUpdateAt(LocalDateTime.now());
//
//        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
//                .filter(productVariant -> productVariant.getStatus() != Status.DELETED)
//                .toList();
//
//        updateProductVariant(product, activeProductVariants);
//
//        try {
//            productRepository.save(product);
//
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
//            ProductResponse productResponse = new ProductResponse();
//            productResponse.setProductDTO(productDTO);
//            productResponse.setStatus("success");
//            productResponse.setMessage("Cập nhật trạng thái sản phẩm trong cửa hàng thành công!");
//            productResponse.setCode(200);
//
//            return productResponse;
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Cập nhật trạng thái sản phẩm trong cửa hàng thất bại!");
//        }
//
//    }
//
//    @Override
//    @Transactional
//    public ProductResponse restoreProductById(Long productId, String username) {
//        Product product = getProductByIdOnShop(productId, username);
//
//        if (product.getStatus() != Status.DELETED) {
//            throw new BadRequestException("Sản phẩm chưa bị xóa trong cửa hàng!");
//        }
//
//        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
//                .filter(productVariant -> productVariant.getUpdateAt().equals(product.getUpdateAt()))
//                .toList();
//
//        product.setStatus(Status.ACTIVE);
//        product.setUpdateAt(LocalDateTime.now());
//        updateProductVariant(product, activeProductVariants);
//
//        try {
//            productRepository.save(product);
//
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
//            ProductResponse productResponse = new ProductResponse();
//            productResponse.setProductDTO(productDTO);
//            productResponse.setStatus("success");
//            productResponse.setMessage("Khôi phục sản phẩm trong cửa hàng thành công!");
//            productResponse.setCode(200);
//
//            return productResponse;
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Khôi phục sản phẩm trong cửa hàng thất bại!");
//        }
//
//    }
//
//    @Override
//    public ListProductResponse getAllDeletedProduct(String username) {
//        Shop shop = shopService.getShopByUsername(username);
//
//        List<Product> products = productRepository
//                .findAllByShopShopIdAndStatus(shop.getShopId(), Status.DELETED)
//                .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm đã xóa!"));
//
//        List<ProductDTO> productDTOs = new ArrayList<>();
//
//        for (Product product : products) {
//            ProductDTO productDTO = getProductDeleteToDTO(product);
//            productDTOs.add(productDTO);
//        }
//
//        ListProductResponse response = new ListProductResponse();
//        response.setProductDTOs(productDTOs);
//        response.setCount(productDTOs.size());
//        response.setStatus("ok");
//        response.setMessage("Lấy danh sách sản phẩm đã xóa trong cửa hàng thành công.");
//        response.setCode(200);
//
//        return response;
//    }
//
//    @Override
//    public ListProductResponse getListProductResponseSort(List<Product> products, String message, boolean isSort) {
//        List<ProductDTO> productDTOs = new ArrayList<>();
//
//        for (Product product : products) {
//            ProductDTO productDTO = getProductToDTO(product);
//            productDTOs.add(productDTO);
//        }
//
//        if (isSort) {
//            productDTOs.sort(Comparator.comparing(ProductDTO::getName));
//        }
//
//        ListProductResponse response = new ListProductResponse();
//        response.setProductDTOs(productDTOs);
//        response.setCount(productDTOs.size());
//        response.setStatus("ok");
//        response.setMessage(message);
//        response.setCode(200);
//
//        return response;
//    }
//
//    @Override
//    public ProductDTO getProductToDTO(Product product) {
//        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
//                .filter(productVariant -> productVariant.getStatus() != Status.DELETED)
//                .toList();
//
//        productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
//        return productDTO;
//    }
//
//    private Brand checkBrand(Long brandId) {
//        if (brandId == null) {
//            return null;
//        }
//
//        return brandRepository.findById(brandId)
//                .orElseThrow(() -> new BadRequestException("Thương hiệu không tồn tại!"));
//    }
//
//    private ProductDTO getProductDeleteToDTO(Product product) {
//        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
//                .filter(productVariant -> productVariant.getUpdateAt().equals(product.getUpdateAt()))
//                .toList();
//
//        productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
//        return productDTO;
//    }
//
//    private Product getProductByIdOnShop(Long productId, String username) {
//        Long shopId = shopService.getShopByUsername(username).getShopId();
//
//        checkProductInShop(productId, shopId);
//
//        return productRepository
//                .findById(productId)
//                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
//    }
//
//    private void updateProductVariant(Product product, List<ProductVariant> productVariants) {
//        for (ProductVariant productVariant : productVariants) {
//            productVariant.setProduct(product);
//            productVariant.setUpdateAt(product.getUpdateAt());
//            try {
//                productVariantRepository.save(productVariant);
//            } catch (Exception e) {
//                throw new InternalServerErrorException("Thêm biến thể sản phẩm thất bại!");
//            }
//        }
//    }
//
//
//    private void checkProductInShop(Long productId, Long shopId) {
//        if (!productRepository.existsByProductIdAndShopShopId(productId, shopId)) {
//            throw new NotFoundException("Sản phẩm không tồn tại trong cửa hàng!");
//        }
//    }

}

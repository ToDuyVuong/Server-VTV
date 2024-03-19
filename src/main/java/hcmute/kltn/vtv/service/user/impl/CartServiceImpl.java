package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.extra.CartStatus;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.model.data.user.request.CartRequest;
import hcmute.kltn.vtv.model.data.user.response.CartResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCartResponse;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.repository.user.CartRepository;
import hcmute.kltn.vtv.service.user.ICartService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final ICustomerService customerService;
    @Autowired
    private final IProductVariantService productVariantService;


    @Override
    @Transactional
    public CartResponse addNewCart(CartRequest request, String username) {
        Cart cart;
        if (checkCardExistProductVariantId(request.getProductVariantId(), username)) {
            cart = getCartByProductVariantIdAndUsername(request.getProductVariantId(), username);
            return updateCart(cart.getCartId(), request.getQuantity(), username);
        }
        ProductVariant productVariant = productVariantService.checkAndProductVariantAvailableWithQuantity(
                request.getProductVariantId(),
                request.getQuantity());
        Customer customer = customerService.getCustomerByUsername(username);
        cart = createCartByCartRequest(request, productVariant, customer);
        try {
            cartRepository.save(cart);

            return CartResponse.cartResponse(cart, "Thêm sản phẩm vào giỏ hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm sản phẩm vào giỏ hàng thất bại.");
        }
    }


    @Override
    @Transactional
    public CartResponse updateCart(UUID cartId, int quantity, String username) {
        checkCartIdExists(cartId);
        Cart cart = getCartByCartIdAndUsername(cartId, username);
        int quantityUpdate = quantity + cart.getQuantity();
        if (quantityUpdate <= 0) {
            return deleteCartById(cartId, username);
        }
        productVariantService.checkAndProductVariantAvailableWithQuantity(
                cart.getProductVariant().getProductVariantId(), quantityUpdate);
        cart.setQuantity(quantityUpdate);
        cart.setUpdateAt(LocalDateTime.now());
        try {
            cartRepository.save(cart);

            return CartResponse.cartResponse(cart, "Cập nhật giỏ hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật giỏ hàng thất bại!" + e.getMessage());
        }
    }


    @Override
    @Transactional
    public CartResponse deleteCartById(UUID cartId, String username) {
        checkCartIdExists(cartId);
        checkExistsCartIdAndUsername(cartId, username);
        try {
            cartRepository.deleteById(cartId);

            return CartResponse.cartResponseDelete("Xóa sản phẩm khỏi giỏ hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa sản phẩm khỏi giỏ hàng thất bại.");
        }
    }


    @Override
    public ListCartResponse getListCartByUsername(String username) {
        List<Cart> carts = getCartsByUsernameAndStatus(username);

        return ListCartResponse.listCartResponse(carts, "Lấy danh sách giỏ hàng thành công.", "OK");
    }


    @Override
    public ListCartResponse getListCartByUsernameAndListCartId(String username, List<UUID> cartIds) {
        List<Cart> carts = getListCartByUsernameAndIds(username, cartIds);

        return ListCartResponse.listCartResponse(carts, "Lấy danh sách giỏ hàng theo danh sách mã giỏ hàng thành công.", "OK");
    }


    @Override
    public List<Cart> getListCartByUsernameAndIds(String username, List<UUID> cartIds) {

        return cartRepository.findAllByCustomerUsernameAndStatusAndCartIdIn(username, CartStatus.CART, cartIds)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giỏ hàng theo danh sách mã giỏ hàng."));
    }


    @Override
    public Cart getCartByCartIdAndUsername(UUID cartId, String username) {

        return cartRepository.findByCartIdAndCustomerUsername(cartId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mã giỏ hàng trong tài khoản của bạn."));
    }


    @Override
    @Transactional
    public ListCartResponse deleteCartByShopId(Long shopId, String username) {
        List<Cart> carts = cartRepository
                .findAllByCustomerUsernameAndProductVariantProductShopShopIdAndStatus(username, shopId,
                        CartStatus.CART)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giỏ hàng theo cửa hàng."));
        try {
            cartRepository.deleteAll(carts);

            return ListCartResponse.listCartResponse("Xóa giỏ hàng theo cửa hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa giỏ hàng thất bại.");
        }
    }


    @Override
    public boolean checkCartsSameShop(String username, List<UUID> cartIds) {

        List<Cart> carts = getListCartByUsernameAndIds(username, cartIds);

        if (!carts.isEmpty()) {
            long distinctShopCount = carts.stream()
                    .map(cart -> cart.getProductVariant().getProduct().getShop().getShopId())
                    .distinct()
                    .count();

            return distinctShopCount <= 1;
        } else {
            throw new NotFoundException("Không tìm thấy danh sách giỏ hàng theo danh sách mã giỏ hàng.");
        }

    }


    @Override
    public Cart createCartByProductVariant(ProductVariant productVariant, int quantity, Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProductVariant(productVariant);
        cart.setQuantity(quantity);
        cart.setCreateAt(LocalDateTime.now());
        cart.setUpdateAt(LocalDateTime.now());
        cart.setStatus(CartStatus.CART);

        return cart;
    }


    @Override
    public void checkDuplicateCartIds(List<UUID> cartIds) {
        Set<UUID> set = new HashSet<>(cartIds);
        if (set.size() < cartIds.size()) {
            throw new BadRequestException("Danh giỏ hàng không được trùng lặp!");
        }
    }

    @Override
    public void checkListCartSameShop(String username, List<UUID> cartIds) {
        if (!checkCartsSameShop(username, cartIds)) {
            throw new BadRequestException("Các sản phẩm trong giỏ hàng không cùng một cửa hàng!");
        }
    }


    @Override
    @Transactional
    public Cart updateOrderCart(UUID cartId, String username, CartStatus status) {
        checkCartIdExists(cartId);
        Cart cart = getCartByCartIdAndUsername(cartId, username);
        cart.setStatus(status);
        cart.setUpdateAt(LocalDateTime.now());
        try {
            return cartRepository.save(cart);

        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái đặt hàng cho giỏ hàng thất bại!");
        }
    }


    private void checkExistsCartIdAndUsername(UUID cartId, String username) {
        checkCartIdExists(cartId);
        if (!cartRepository.existsByCartIdAndCustomerUsername(cartId, username)) {
            throw new NotFoundException("Bạn không có quyền truy cập giỏ hàng này.");
        }
    }


    private void checkCartIdExists(UUID cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new NotFoundException("Không tìm thấy giỏ hàng.");
        }
    }


    private List<Cart> getCartsByUsernameAndStatus(String username) {
        return cartRepository.findAllByCustomerUsernameAndStatus(username, CartStatus.CART)
                .orElseThrow(() -> new NotFoundException("Giỏ hàng trống."));
    }


    private Cart getCartByProductVariantIdAndUsername(Long productVariantId, String username) {
        return cartRepository.findByProductVariantProductVariantIdAndCustomerUsernameAndStatus(
                        productVariantId, username, CartStatus.CART)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong giỏ hàng."));
    }


    private boolean checkCardExistProductVariantId(Long productVariantId, String username) {
        return cartRepository.existsByProductVariantProductVariantIdAndCustomerUsernameAndStatus(
                productVariantId, username, CartStatus.CART);
    }


    private Cart createCartByCartRequest(CartRequest request, ProductVariant productVariant, Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProductVariant(productVariant);
        cart.setQuantity(request.getQuantity());
        cart.setCreateAt(LocalDateTime.now());
        cart.setUpdateAt(LocalDateTime.now());
        cart.setStatus(CartStatus.CART);

        return cart;
    }


}

package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.model.data.user.request.CartRequest;
import hcmute.kltn.vtv.model.data.user.response.CartResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCartResponse;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.vtv.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CartRepository;
import hcmute.kltn.vtv.service.user.ICartService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public CartResponse addNewCart(CartRequest request) {
        if (checkCardExistProductVariantId(request.getProductVariantId(), request.getUsername())) {
            return updateCart(request);
        }
        Cart cart = createCartByCartRequest(request);
        try {
            cartRepository.save(cart);

            return CartResponse.cartResponse(request.getUsername(), cart,
                    "Thêm sản phẩm vào giỏ hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm sản phẩm vào giỏ hàng thất bại.");
        }
    }


    @Override
    @Transactional
    public CartResponse updateCart(CartRequest request) {
        Cart cart = getCartByProductVariantIdAndUsername(request.getProductVariantId(), request.getUsername());

        if (request.getQuantity() <= 0) {
            return deleteCart(cart.getCartId(), request.getUsername());
        }
        productVariantService.checkAndProductVariantAvailableWithQuantity(request.getProductVariantId(), request.getQuantity());

        cart.setQuantity(request.getQuantity());
        cart.setUpdateAt(LocalDateTime.now());

        try {
            cartRepository.save(cart);

            return CartResponse.cartResponse(request.getUsername(), cart,
                    "Cập nhật giỏ hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật giỏ hàng thất bại." + e.getMessage());
        }
    }


    @Override
    @Transactional
    public CartResponse deleteCart(Long cartId, String username) {
        checkExistsCartIdAndUsername(cartId, username);
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giỏ hàng."));

        try {
            cartRepository.delete(cart);

            return CartResponse.cartResponse(username, null,
                    "Xóa sản phẩm khỏi giỏ hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa sản phẩm khỏi giỏ hàng thất bại.");
        }
    }


    @Override
    public ListCartResponse getListCartByUsername(String username) {
        List<Cart> carts = getCartsByUsernameAndStatus(username);

        return ListCartResponse.listCartResponse(username, carts, "Lấy danh sách giỏ hàng thành công.", "OK");
    }


    @Override
    public ListCartResponse getListCartByUsernameAndListCartId(String username, List<Long> cartIds) {
        List<Cart> carts = getListCartByUsernameAndIds(username, cartIds);

        return ListCartResponse.listCartResponse(username, carts, "Lấy danh sách giỏ hàng theo danh sách mã giỏ hàng thành công.", "OK");
    }


    @Override
    public List<Cart> getListCartByUsernameAndIds(String username, List<Long> cartIds) {

        return cartRepository.findAllByCustomerUsernameAndStatusAndCartIdIn(username, Status.CART, cartIds)
                .orElseThrow(() -> new NotFoundException("Giỏ hàng trống."));
    }


    @Override
    public Cart getCartByUserNameAndId(String username, Long cartId) {

        return cartRepository.findByCustomerUsernameAndCartId(username, cartId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giỏ hàng."));
    }


    @Override
    @Transactional
    public ListCartResponse deleteCartByShopId(Long shopId, String username) {
        List<Cart> carts = cartRepository
                .findAllByCustomerUsernameAndProductVariantProductCategoryShopShopIdAndStatus(username, shopId,
                        Status.CART)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giỏ hàng theo cửa hàng."));
        try {
            cartRepository.deleteAll(carts);

            List<Cart> cartsUpdate = getCartsByUsernameAndStatus(username);
            String message = "Xóa giỏ hàng theo cửa hàng thành công.";

            return ListCartResponse.listCartResponse(username, cartsUpdate, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa giỏ hàng thất bại.");
        }
    }


    @Override
    public boolean checkCartsSameShop(String username, List<Long> cartIds) {

        List<Cart> carts = getListCartByUsernameAndIds(username, cartIds);

        if (!carts.isEmpty()) {
            long distinctShopCount = carts.stream()
                    .map(cart -> cart.getProductVariant().getProduct().getCategory().getShop().getShopId())
                    .distinct()
                    .count();

            return distinctShopCount <= 1;
        }
        return false;
    }


    private void checkExistsCartIdAndUsername(Long cartId, String username) {
        checkCartIdExists(cartId);
        if (!cartRepository.existsByCartIdAndCustomerUsername(cartId, username)) {
            throw new NotFoundException("Bạn không có quyền truy cập giỏ hàng này.");
        }
    }


    private void checkCartIdExists(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new NotFoundException("Không tìm thấy giỏ hàng.");
        }
    }


    private List<Cart> getCartsByUsernameAndStatus(String username) {
        return cartRepository.findAllByCustomerUsernameAndStatus(username, Status.CART)
                .orElseThrow(() -> new NotFoundException("Giỏ hàng trống."));
    }


    private Cart getCartByProductVariantIdAndUsername(Long productVariantId, String username) {
        return cartRepository.findByProductVariantProductVariantIdAndCustomerUsernameAndStatus(
                        productVariantId, username, Status.CART)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong giỏ hàng."));
    }


    private boolean checkCardExistProductVariantId(Long productVariantId, String username) {
        return cartRepository.existsByProductVariantProductVariantIdAndCustomerUsernameAndStatus(
                productVariantId, username, Status.CART);
    }


    private Cart createCartByCartRequest(CartRequest request) {

        Customer customer = customerService.getCustomerByUsername(request.getUsername());
        ProductVariant productVariant = productVariantService
                .checkAndProductVariantAvailableWithQuantity(request.getProductVariantId(),
                        request.getQuantity());

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProductVariant(productVariant);
        cart.setQuantity(request.getQuantity());
        cart.setCreateAt(LocalDateTime.now());
        cart.setUpdateAt(LocalDateTime.now());
        cart.setStatus(Status.CART);

        return cart;
    }


}

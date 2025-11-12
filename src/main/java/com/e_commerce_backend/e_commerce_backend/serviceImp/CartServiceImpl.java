package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.Exception.APIException;
import com.e_commerce_backend.e_commerce_backend.Exception.ResourceNotFoundException;
import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.CartItem;
import com.e_commerce_backend.e_commerce_backend.entity.Carts;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.CartDTO;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.ProductDTO;
import com.e_commerce_backend.e_commerce_backend.entity.Product;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.repository.CartItemRepository;
import com.e_commerce_backend.e_commerce_backend.repository.CartRepository;
import com.e_commerce_backend.e_commerce_backend.repository.ProductRepository;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import com.e_commerce_backend.e_commerce_backend.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(AuthUtil authUtil, UserRepository userRepository,
                           CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartItemRepository cartItemRepository,
                           ModelMapper modelMapper) {
        this.authUtil = authUtil;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductTOcart(Long productId, Integer quantity) {

        // 1️⃣ Get or create user's cart
        Carts cart = createOrGetCart();

        // 2️⃣ Retrieve product
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId)
        );

        // 3️⃣ Check if product already in cart
        Optional<CartItem> existingItemOpt = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCart_id());
        if (existingItemOpt.isPresent()) {
            throw new APIException("Product " + product.getProductName() + " is already in your cart!");
        }

        // 4️⃣ Check stock availability
        if (product.getQuality() == 0) {
            throw new APIException("Product " + product.getProductName() + " is not available!");
        }

        if (product.getQuality() < quantity) {
            throw new APIException("Please order " + product.getProductName() + " less than or equal to available stock (" + product.getQuality() + ").");
        }

        // 5️⃣ Create new Cart Item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProduct_price(product.getSpecial_price());

        // 6️⃣ Add new item to cart
        cart.getCartItems().add(newCartItem);

        // 7️⃣ Update total price
        double updatedTotal = cart.getTotal_price() + (product.getSpecial_price() * quantity);
        cart.setTotal_price(updatedTotal);

        // 8️⃣ Save cart (Cascade will save CartItem)
        cartRepository.save(cart);



        // 9️⃣ Convert and return DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream = cartItems.stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuality(item.getQuantity());
                    return productDTO;
                });

        cartDTO.setProduct(productStream.collect(Collectors.toList()));

        return cartDTO;
    }



    private Carts createOrGetCart() {
        // Get the logged-in user's email
        String email = authUtil.loggedInEmail();

        // Try to find the cart for that user
        Optional<Carts> userCart = cartRepository.findCartByUsername(email);

        if (userCart.isPresent()) {
            return userCart.get(); // ✅ unwrap and return the actual cart
        }

        // Create a new cart for the user if not found
        Carts newCart = new Carts();
        newCart.setTotal_price(0.0);

        // ✅ Get the full UserEntity (since setUser() expects UserEntity, not String)
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        newCart.setUser(user);

        return cartRepository.save(newCart);
    }


    @Override
    public List<CartDTO> getAllCarts() {
        List<Carts> cartsList = cartRepository.findAll();
        if (cartsList.size() == 0) {
            throw new APIException("No Carts Exits");
        }
        List<CartDTO> cartDTOs = cartsList.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());

            cartDTO.setProduct(products);

            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOs;
    }

    @Override
    public CartDTO getCartByUser(String email, Long cartId) {
        Carts cart = cartRepository.findCartByEmailAndCartId(email, cartId);

        if(cart==null){
            throw new APIException("No cart Exit!!");
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> products = cart.getCartItems().stream().map(
                p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
        cartDTO.setProduct(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateCartQuantityInCart(Long productId, Integer change) {

        String emailId = authUtil.loggedInEmail();
        Carts carts = cartRepository.findCartByUsername(emailId)
                .orElseThrow(() -> new APIException("No cart exists"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, carts.getCart_id())
                .orElseThrow(() -> new APIException("Product " + product.getProductName() + " not found in cart"));

        int updatedQuantity = cartItem.getQuantity() + change;

        // Handle quantity less than or equal to zero first
        if (updatedQuantity <= 0) {
            carts.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);

            // Recalculate total after deletion
            double updatedTotal = carts.getCartItems()
                    .stream()
                    .mapToDouble(item -> item.getProduct().getSpecial_price() * item.getQuantity())
                    .sum();
            carts.setTotal_price(updatedTotal);

            cartRepository.save(carts);
            CartDTO cartDTO = modelMapper.map(carts, CartDTO.class);
            cartDTO.setProduct(carts.getCartItems().stream()
                    .map(item -> {
                        ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                        productDTO.setQuality(item.getQuantity());
                        return productDTO;
                    }).collect(Collectors.toList()));
            return cartDTO;
        }

        // If product stock is less than new quantity
        if (product.getQuality() < updatedQuantity) {
            throw new APIException("Only " + product.getQuality() + " units of " + product.getProductName() + " available");
        }

        // Update quantity
        cartItem.setQuantity(updatedQuantity);

        // Calculate new quantity for validation
        int newQuantity = cartItem.getQuantity();

        // Validation to prevent negative quantities
        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }

        // If quantity is exactly 0, delete the product from cart
        if (newQuantity == 0) {
            deleteProductFormCart(productId, carts.getCart_id()); // fixed method name
            return null; // stop execution safely
        }

        // Recalculate total price
        double updatedTotal = carts.getCartItems()
                .stream()
                .mapToDouble(item -> item.getProduct().getSpecial_price() * item.getQuantity())
                .sum();
        carts.setTotal_price(updatedTotal);

        // Save and return DTO
        cartRepository.save(carts);

        CartDTO cartDTO = modelMapper.map(carts, CartDTO.class);
        cartDTO.setProduct(carts.getCartItems().stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuality(item.getQuantity());
                    return productDTO;
                }).collect(Collectors.toList()));

        return cartDTO;
    }


    @Override
    public String deleteProductFormCart(Long productId, Long cartId) {
        Carts cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "CartId", +cartId));
        Optional<CartItem> cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);
        if(cartItem.isEmpty()) {
            throw new ResourceNotFoundException("Product" ,"ProductId" ,+productId);
        }

        cart.setTotal_price(cart.getTotal_price()-(cartItem.get().getProduct_price() * cartItem.get().getQuantity() ));
        cartItemRepository.deleteCartItemByProductIdAndCartId(productId ,cartId);
        return "Prodcut" +cartItem.get().getProduct().getProductName()+ " removed form cartSuccessfully";
    }

}

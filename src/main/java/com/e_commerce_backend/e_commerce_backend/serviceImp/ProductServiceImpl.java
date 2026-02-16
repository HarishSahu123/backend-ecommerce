package com.e_commerce_backend.e_commerce_backend.serviceImp;

import com.e_commerce_backend.e_commerce_backend.Exception.ResourceNotFoundException;
import com.e_commerce_backend.e_commerce_backend.Utility.helperClass.AuthUtil;
import com.e_commerce_backend.e_commerce_backend.entity.Category;
import com.e_commerce_backend.e_commerce_backend.entity.Product;
import com.e_commerce_backend.e_commerce_backend.entity.Dto.ProductDTO;
import com.e_commerce_backend.e_commerce_backend.entity.UserEntity;
import com.e_commerce_backend.e_commerce_backend.entity.dtoResponse.ProductResponse;
import com.e_commerce_backend.e_commerce_backend.repository.CategoryRepository;
import com.e_commerce_backend.e_commerce_backend.repository.ProductRepository;
import com.e_commerce_backend.e_commerce_backend.repository.UserRepository;
import com.e_commerce_backend.e_commerce_backend.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final FileServiceImpl fileService;

    private final AuthUtil authUtil;


    public ProductServiceImpl(CategoryRepository categoryRepository,
                              ProductRepository productRepository, UserRepository userRepository, ModelMapper modelMapper, FileServiceImpl fileService, AuthUtil authUtil
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.authUtil = authUtil;
    }


    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
       Long userId = authUtil.loggedInUserId();


        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("category ", "categoryId", categoryId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));



        Product product=modelMapper.map(productDTO ,Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        product.setUser(user);
        double SpecialPrice=product.getPrice() -
                (product.getDiscount()*0.01)* product.getPrice();
        product.setSpecial_price(SpecialPrice);
        Product savedProduct=productRepository.save(product);
        return modelMapper.map(savedProduct , ProductDTO.class);
    }

    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber, pageSize ,sortByAndOrder);

        Page<Product> pageProduct=productRepository.findAll(pageDetails);
        List<Product> products = pageProduct.getContent();
        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setPageNumber(pageProduct.getNumber());
        productResponse.setTotalElements(pageProduct.getTotalElements());
        productResponse.setPageSize(pageProduct.getSize());
        productResponse.setTotalPages(pageProduct.getTotalPages());
        productResponse.setContent(productDTOs);
        return productResponse;

    }

    @Override
    public ProductResponse getAllProductByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
        List<Product> ProductByCategoryOrderByPriceAsc = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOs = ProductByCategoryOrderByPriceAsc.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product>  productLikeIgnoreCase= productRepository.findByProductNameLikeIgnoreCase( '%' +keyword+ '%');
        List<ProductDTO> productDTOs = productLikeIgnoreCase.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product exitProductFromDb= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product" ,"ProductId" ,productId ));
       // dto-> entity
        Product product=modelMapper.map(productDTO ,Product.class);
        //update the product info with the one in request body
        exitProductFromDb.setDescription(productDTO.getDescription());
        exitProductFromDb.setProductName(productDTO.getProductName());
        exitProductFromDb.setDiscount(productDTO.getDiscount());
        exitProductFromDb.setQuality(productDTO.getQuality());
        exitProductFromDb.setSpecial_price(productDTO.getSpecial_price());
        Product savedProduct = productRepository.save(exitProductFromDb);

        return modelMapper.map(savedProduct ,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        productRepository.deleteById(productId);
        return modelMapper.map(product ,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        if (image == null || image.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "ProductId", productId));

        String path = "images/";
        String fileName = fileService.uploadImage(path, image);

        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


    @Override
    public ProductResponse getAllProductSavedByUser(Long userId) {
        List<Product> productByUserId = productRepository.findByUserId(userId);
        if(productByUserId==null){
            throw new ResourceNotFoundException();
        }
        List<ProductDTO> productDTOList = productByUserId.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOList);
        return productResponse;
    }

    @Override
    public Resource getProductImage(String fileName) {

        try {
            Path imagePath = Paths.get("images")
                    .toAbsolutePath()
                    .normalize();

            Path filePath = imagePath.resolve(fileName);

            if (!Files.exists(filePath)) {
                throw new RuntimeException("Image not found: " + fileName);
            }

            return new UrlResource(filePath.toUri());

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving image", e);
        }
    }

}



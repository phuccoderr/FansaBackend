package com.fansa.admin.product;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fansa.admin.security.FansaUserDetailsService;
import com.fansa.admin.security.jwt.JwtService;
import com.fansa.common.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@WebMvcTest(ProductApiController.class)
public class ProductControllerTests {

    private static final String END_POINT_PATH = "/auth/products";
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    @MockBean
    private FansaUserDetailsService fansaUserDetailsService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetShouldReturn204NoContent() throws Exception {
//        Mockito.when(service.listByPage(1,"asc","name",null)).thenReturn(Mockito.any());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetShouldReturn200OK() throws Exception {
        Product product = Product.builder()
                .name("Chú Thuật Hồi Chiến - Tập 21")
                .alias("Chu-Thuat-Hoi-Chien-Tap-21")
                .shortDescription("nothing")
                .fullDescription("nothing")
                .createdTime(LocalDate.parse("2019-02-3"))
                .enabled(true)
                .cost(20)
                .price(25.2f)
                .sale(15)
                .mainImage("main_image").build();

        List<Product> products = new ArrayList<>();
        products.add(product);


        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}

package com.fansa.admin.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fansa.admin.security.FansaUserDetailsService;
import com.fansa.admin.security.jwt.JwtService;
import com.fansa.admin.user.request.UserDTORequest;
import com.fansa.admin.user.request.UserDTOUpdateRequest;
import com.fansa.common.entity.Role;
import com.fansa.common.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.*;

@WebMvcTest(UserApiController.class)
public class UserControllerTests {

    private static final String END_POINT_PATH = "/auth";
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private FansaUserDetailsService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private ModelMapper modelMapper;




    private String generateValidToken() {
        return jwtService.generateToken("admin@gmail.com");
    }

    @Test
    public void testShouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/auth/users")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testShouldAllowAccessToAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/auth/users")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetAllShouldReturn204NoContent() throws Exception {
        Mockito.when(service.listByUser(1,"name","asc",null)).thenReturn(Mockito.any());

        mockMvc.perform(get(END_POINT_PATH + "/users"))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetAllShouldReturn200OK() throws Exception {
        List<User> listUser = new ArrayList<>();
        Role role = new Role();
        role.setName("STAFF");

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("admin@gmail.com");
        user1.setPassword("0123456789");
        user1.setName("Phuc");
        user1.addRole(role);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("staff@gmail.com");
        user2.setPassword("0123456789");
        user2.setName("Phi");
        user2.addRole(role);

        listUser.add(user1);
        listUser.add(user2);


        Mockito.when(service.listByUser(1,"name","asc",null)).thenReturn(Mockito.any());

        mockMvc.perform(get(END_POINT_PATH + "/users"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetShouldReturn404NotFound() throws Exception {
        Long userId = 2L;

        Mockito.when(service.getUserById(2L)).thenThrow(new UserNotFoundException("No user found"));

        mockMvc.perform(get(END_POINT_PATH + "/users/" + userId))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetShouldReturn405MethodNotAllowed() throws Exception {
        String requestURIError = "ABCDEF";

        mockMvc.perform(post(END_POINT_PATH + "/users/" + requestURIError).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testGetShouldReturn200OK() throws Exception {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail("admin@gmail.com");
        user.setPassword("0123456789");
        user.setEnabled(true);


        Mockito.when(service.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get(END_POINT_PATH + "/users/" + userId))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @WithMockUser(username = "staff@gmail.com",password = "0123456789")
    public void testAddShouldReturn400BadRequest() throws Exception {

        UserDTORequest user = new UserDTORequest();
        user.setId(5L);
        user.setEmail("admin@gmail.com");
        user.setPassword("0123456789");
        user.setName(""); // empty name

        Mockito.when(service.add(user)).thenReturn(Mockito.any());
        String bodyContent = objectMapper.writeValueAsString(user);
        mockMvc.perform(post(END_POINT_PATH + "/users").content(bodyContent)
                .contentType("application/json").with(csrf()))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testAddShouldReturn201Created() throws Exception {

        UserDTORequest user = new UserDTORequest();
        user.setId(1L);
        user.setEmail("admin@gmail.com");
        user.setPassword("0123456789");
        user.setName("Phuc");

        Mockito.when(service.add(user)).thenReturn(Mockito.any());
        String bodyContent = objectMapper.writeValueAsString(user);
        mockMvc.perform(post(END_POINT_PATH + "/users").contentType("application/json")
                .content(bodyContent).with(csrf()))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testPutShouldReturn400BadRequest() throws Exception {
        UserDTOUpdateRequest user = new UserDTOUpdateRequest();
        user.setId(5L);
        user.setEmail("admin@gmail.com");
        user.setPassword("0123456789");
        user.setName(""); // empty name


        Mockito.when(service.update(user,5L)).thenReturn(Mockito.any());

        String bodyContent = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(END_POINT_PATH + "/users" ).contentType("application/json")
                .content(bodyContent).with(csrf()))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

        @Test
        @WithMockUser(username = "admin@gmail.com",password = "0123456789")
        public void testPutShouldReturn404NotFound() throws Exception {
            UserDTOUpdateRequest user = new UserDTOUpdateRequest();
            user.setId(3L);
            user.setEmail("pp@gmail.com");
            user.setPassword("0123456789");
            user.setName("Phuc");
            user.setEnabled(true);

            Mockito.when(service.update(user,5L)).thenThrow(new UserNotFoundException("No found"));

            String bodyContent = objectMapper.writeValueAsString(user);

            mockMvc.perform(put(END_POINT_PATH + "/users" ).contentType(MediaType.APPLICATION_JSON)
                            .content(bodyContent).with(csrf()))
                    .andExpect(status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());
        }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testPutShouldReturn200OK() throws Exception {
        UserDTOUpdateRequest user = new UserDTOUpdateRequest();
        user.setId(3L);
        user.setEmail("ppot@gmail.com");
        user.setPassword("0123456789");
        user.setName("Phuc");
        user.setEnabled(true);

        Mockito.when(service.update(user,3L)).thenReturn(Mockito.any());

        String bodyContent = objectMapper.writeValueAsString(user);

        mockMvc.perform(put(END_POINT_PATH + "/users" ).contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent).with(csrf()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testDeleteShouldReturn404NotFound() throws Exception {
        Long userId = 3L;

        Mockito.doThrow(UserNotFoundException.class).when(service).deleted(userId);

        mockMvc.perform(delete(END_POINT_PATH + "/users/" + userId ).with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com",password = "0123456789")
    public void testDeleteShouldReturn204NoContent() throws Exception {
        Long userId = 3L;

        Mockito.doNothing().when(service).deleted(userId);

        mockMvc.perform(delete(END_POINT_PATH + "/users/" + userId ).with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }


}

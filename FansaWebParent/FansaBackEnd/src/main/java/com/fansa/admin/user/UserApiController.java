package com.fansa.admin.user;

import com.fansa.admin.ErrorDTO;
import com.fansa.admin.security.FansaUserDetail;
import com.fansa.admin.user.request.*;
import com.fansa.admin.security.FansaUserDetailsService;
import com.fansa.admin.security.jwt.JwtService;
import com.fansa.admin.PaginationResponse;
import com.fansa.common.entity.Role;
import com.fansa.common.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class UserApiController {
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private FansaUserDetailsService fansaUserDetailsService;
    @Autowired private ModelMapper modelMapper;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring Security tutorials";
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),loginRequest.getPassword()
        ));
        if (authenticate.isAuthenticated()) {
            FansaUserDetail fansaUserDetail = (FansaUserDetail) authenticate.getPrincipal();



            // lay danh sach authorities
            List<String> authorities = fansaUserDetail
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority).toList();

            UserDTOResponse userDTOResponse = UserDTOResponse.builder()
                    .id(fansaUserDetail.getId())
                    .name(fansaUserDetail.getName())
                    .email(fansaUserDetail.getUsername())
                    .enabled(fansaUserDetail.isEnabled())
                    .roles(fansaUserDetail.getRoles()).build();


            String token = jwtService.generateToken(loginRequest.getEmail());
            AuthResponse authResponse = AuthResponse.builder()
                    .user(userDTOResponse)
                    .accessToken(token).build();

            return ResponseEntity.ok(authResponse);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser() {
//        return listByPage(1,"name","asc",null);
        List<User> listUsers = fansaUserDetailsService.listAllUser();
        List<UserDTOResponse> userDTOResponses = listEntityToListDTO(listUsers);
        if (userDTOResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("list user is empty!");
        }
        return ResponseEntity.ok(userDTOResponses);

    }

//    @GetMapping("/users/page/{pageNum}")
//    public ResponseEntity<?> listByPage(@PathVariable("pageNum") int pageNum,
//                                         @RequestParam(value = "sortField",required = false) String sortField,
//                                         @RequestParam(value = "sortDir",required = false) String sortDir,
//                                         @RequestParam(value = "keyword",required = false) String keyword) {
//        if (sortDir == null || sortDir.isEmpty() ) {
//            sortDir = "asc";
//        }
//
//        if (sortField == null || sortField.isEmpty() ) {
//            sortField = "name";
//        }
//
//        Page<User> page = fansaUserDetailsService.listByUser(pageNum,sortField,sortDir,keyword);
//        List<User> listUsers = page.getContent();
//
//        if (listUsers.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        List<UserDTOResponse> userDTOResponses = listEntityToListDTO(listUsers);
//
//        long startCount = (pageNum - 1) * fansaUserDetailsService.USERS_PER_PAGE + 1;
//        long endCount = startCount + fansaUserDetailsService.USERS_PER_PAGE - 1;
//
//
//        PaginationResponse resultResponse = PaginationResponse.builder()
//                .currentPage(pageNum)
//                .totalPages(page.getTotalPages())
//                .totalItems(page.getTotalElements())
//                .startCount(startCount)
//                .endCount(endCount)
//                .sortField(sortField)
//                .sortDir(sortDir)
//                .keyword(keyword)
//                .results(userDTOResponses).build();
//
//
//        return ResponseEntity.ok(resultResponse);
//
//    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") @Positive(message = "user id must be greater than 0") Long userId) {
        try {
            User existingUser = fansaUserDetailsService.getUserById(userId);
            UserDTOResponse entityDTO = entityToDTO(existingUser);
            return new ResponseEntity<>(entityDTO,HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/users/roles")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = fansaUserDetailsService.getAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("roles list is empty!");
        }
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTORequest userDTORequest) {
        try {
            User saved = fansaUserDetailsService.add(userDTORequest);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saved.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (BadRequestException e) {
            ErrorDTO error = new ErrorDTO();
            error.setTimestamp(new Date());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setPath("/users");
            error.addError(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTOUpdateRequest userDTORequest, @PathVariable("id") Long userId) {
        try {
            User updateUser = fansaUserDetailsService.update(userDTORequest,userId);
            UserDTOResponse entityDTO = entityToDTO(updateUser);
            return ResponseEntity.ok(entityDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") @Positive(message = "user id must be greater than 0") Long userId) {
        try {
            fansaUserDetailsService.deleted(userId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private List<UserDTOResponse> listEntityToListDTO(List<User> users) {
        List<UserDTOResponse> userDTOResponse = new ArrayList<>();
        users.forEach(
                user -> {
                    userDTOResponse.add(entityToDTO(user));
                }
        );
        return userDTOResponse;
    }

    private UserDTOResponse entityToDTO(User user) {
        UserDTOResponse userDTOResponse = modelMapper.map(user, UserDTOResponse.class);
        return userDTOResponse;
    }
}

package com.fansa.admin.security;

import com.fansa.admin.user.RoleRepository;
import com.fansa.admin.user.UserNotFoundException;
import com.fansa.admin.user.UserRepository;
import com.fansa.admin.user.UserSpecifications;
import com.fansa.admin.user.request.UserDTORequest;
import com.fansa.admin.user.request.UserDTOUpdateRequest;
import com.fansa.common.entity.Role;
import com.fansa.common.entity.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FansaUserDetailsService implements UserDetailsService {
    public static final int USERS_PER_PAGE = 2;
    @Autowired private UserRepository repo;
    @Autowired private RoleRepository roleRepo;
    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if (user != null) {
            return new FansaUserDetail(user);
        } throw new UsernameNotFoundException("Cloud not find user with Email: " + email);
    }



    public User add(UserDTORequest userDTORequest) throws BadRequestException {
        User userInDB = repo.findByEmail(userDTORequest.getEmail());

        if (userInDB != null ) {
            throw new BadRequestException("Email has been duplicated");
        }

        String password = userDTORequest.getPassword();
        String encodePassword = encoder.encode(password);
        userInDB = User.builder()
                .name(userDTORequest.getName())
                .email(userDTORequest.getEmail())
                .password(encodePassword)
                .enabled(userDTORequest.getEnabled())
                .createdTime(LocalDate.now())
                .roles(userDTORequest.getRoles()).build();

        return repo.save(userInDB);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return repo.findById(id).orElseThrow(() -> new UserNotFoundException("No User found with the given id: " + id)) ;
    }

    public User update(UserDTOUpdateRequest userDTORequest, Long userId) throws UserNotFoundException {
        Long countById = repo.countById(userId);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("No User found with the given id: " + userId);
        }
        User userInDB = repo.findById(userId).get();
        String passwordUserInDB = userInDB.getPassword();
        if (userDTORequest.getPassword() != userInDB.getPassword() && userDTORequest.getPassword() != null) {
            String password = userDTORequest.getPassword();
            passwordUserInDB = encoder.encode(password);
        }

        userInDB = User.builder()
                .id(userId)
                .name(userDTORequest.getName())
                .email(userDTORequest.getEmail())
                .password(passwordUserInDB)
                .enabled(userDTORequest.getEnabled())
                .createdTime(userInDB.getCreatedTime())
                .roles(userDTORequest.getRoles()).build();


        return repo.save(userInDB);
    }

    public void deleted(Long id) throws UserNotFoundException {
        Optional<User> userInDB = repo.findById(id);
        if (!userInDB.isPresent()) {
            throw new UserNotFoundException("No User found with the given id: " + id);
        }
        repo.deleteById(id);
    }

    public Page<User> listByUser(Integer pageNum, String sortField,String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,USERS_PER_PAGE,sort);

        Specification<User> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(UserSpecifications.withSearchWithNameOrEmailOrId(keyword));
        }
        return repo.findAll(spec,pageable);

    }

    public List<User> listAllUser() {
        return repo.findAll();
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}

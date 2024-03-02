package com.fansa.admin.security;

import com.fansa.admin.user.UserNotFoundException;
import com.fansa.admin.user.UserRepository;
import com.fansa.admin.user.UserSpecifications;
import com.fansa.common.entity.User;
import jakarta.transaction.Transactional;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FansaUserDetailsService implements UserDetailsService {
    public static final int USERS_PER_PAGE = 12;
    @Autowired private UserRepository repo;
    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if (user != null) {
            return new FansaUserDetail(user);
        } throw new UsernameNotFoundException("Cloud not find user with Email: " + email);
    }



    public User add(User user) throws UserNotFoundException {
        User userInDB = repo.findByEmail(user.getEmail());

        if (userInDB != null ) {
            throw new UserNotFoundException("Email has been duplicated");
        }

        String password = user.getPassword();
        String encodePassword = encoder.encode(password);

        user.setPassword(encodePassword);

        repo.save(user);
        return user;
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return repo.findById(id).orElseThrow(() -> new UserNotFoundException("No User found with the given id: " + id)) ;
    }

    public User update(User user) throws UserNotFoundException {
        Optional<User> optionalUser = repo.findById(user.getId());
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("No User found with the given id: " + user.getId());
        }
        String password = user.getPassword();
        String encodePassword = encoder.encode(password);

        optionalUser.get().setName(user.getName());
        optionalUser.get().setEmail(user.getEmail());
        optionalUser.get().setPassword(encodePassword);
        optionalUser.get().setEnabled(user.getEnabled());
        optionalUser.get().setRoles(user.getRoles());
        User saved = optionalUser.get();
        return saved;
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
}

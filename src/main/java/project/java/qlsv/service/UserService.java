package project.java.qlsv.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.java.qlsv.dto.PageDTO;
import project.java.qlsv.dto.SearchDTO;
import project.java.qlsv.dto.UserDTO;
import project.java.qlsv.entity.User;
import project.java.qlsv.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    // them moi user
    @Transactional
    public void create(UserDTO userDTO) {
        User user = new ModelMapper().map(userDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepo.save(user);
    }

    // xoa user theo id
    @Transactional
    public void delete(int id) {
        userRepo.deleteById(id);
    }

    // cap nhau user
    @Transactional
    public void update(UserDTO userDTO) {
        // check
        User currentUser= userRepo.findById(userDTO.getId()).orElse(null);
        if (currentUser != null) {
            currentUser.setName(userDTO.getName());
            currentUser.setAge(userDTO.getAge());
            currentUser.setHomeAddress(userDTO.getHomeAddress());
            userRepo.save(currentUser);
        }
    }

    // cap nhat pass
    @Transactional
    public void updatePassword(UserDTO userDTO) {
        // check
        User currentUser= userRepo.findById(userDTO.getId()).orElse(null);
        if (currentUser != null) {
            currentUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            userRepo.save(currentUser);
        }
    }

    // lay User theo id
    public UserDTO getById(int id) {
        // Optional
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            return convert(user);
        }

        return null;
    }

    private UserDTO convert(User user) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setAge(user.getAge());
//        userDTO.setHomeAddress(user.getHomeAddress());
//        userDTO.setAvatarURL(user.getAvatarURL());

        return new ModelMapper().map(user, UserDTO.class);
    }

    // lay danh sach user
    public List<UserDTO> getAll() {
        List<User> userList = userRepo.findAll();
        return userList.stream().map(u -> convert(u)).collect(Collectors.toList());
    }

    // search name, tra ve danh sach user phan trang
    public PageDTO<List<UserDTO>> searchName(SearchDTO searchDTO) {
        // sap xep theo thu tu ten abc
        Sort sortBy = Sort.by("name").ascending();

        // sap xep theo thuoc tinh
        // neu rong hoac k nhap gi thi sap xep theo asc
        if (StringUtils.hasText(searchDTO.getSortedField())) {
            sortBy = Sort.by(searchDTO.getSortedField()).ascending();
        }

        if (searchDTO.getCurrentPage() == null)
            searchDTO.setCurrentPage(0);
        if (searchDTO.getSize() == null)
            searchDTO.setSize(5);

        // thiet lap tim kiem theo trang
        PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
        Page<User> page = userRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageRequest);

        // convert sang DTO de tra ve view
        PageDTO<List<UserDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<UserDTO> userDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());

        pageDTO.setData(userDTOs);
        return  pageDTO;
    }

    @Override
//	@Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepo.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        // convert userentity -> userdetails
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // chuyen vai tro ve quyen
        for (String role : userEntity.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        // tra ve user cua security
        return new org.springframework.security.core.userdetails.User(username,userEntity.getPassword(),authorities);
    }
}

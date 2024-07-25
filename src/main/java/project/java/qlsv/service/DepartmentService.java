package project.java.qlsv.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.NoResultException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.java.qlsv.dto.DepartmentDTO;
import project.java.qlsv.dto.PageDTO;
import project.java.qlsv.dto.SearchDTO;
import project.java.qlsv.entity.Department;
import project.java.qlsv.repository.DepartmentRepo;

import java.util.List;
import java.util.stream.Collectors;

public interface DepartmentService {
    void create(DepartmentDTO departmentDTO);
    DepartmentDTO update(DepartmentDTO departmentDTO);
    void delete(int id);
    DepartmentDTO getById(int id);
    PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO);
}

@Service
class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    CacheManager cacheManager; // co the dung bean nay de thay the

    @Override
    @Transactional
    @CacheEvict(cacheNames = "department-search", allEntries = true)
    public void create(DepartmentDTO departmentDTO) {
        Department department = new ModelMapper().map(departmentDTO, Department.class);
        departmentRepo.save(department);
        // co the dung cacheManager de xoa nhu nay
//        Cache cache = cacheManager.getCache("department-search");
//        cache.invalidate();
    }


    //    @CacheEvict(cacheNames = "department", key = "#departmentDTO.id")  do dùng void nên là xóa đi ban ghi trong cache,  dung getbyid de lay ban ghi moi
// có thể dùng @CachePut nhưng phải return về return convert(department); để đồng nhất vá db
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "department-search", allEntries = true)
    },
            put = {
                    @CachePut(cacheNames = "department", key = "#departmentDTO.id")
            })
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        // check da ton tai chua
        Department department = departmentRepo.findById(departmentDTO.getId()).orElse(null);
        if (department != null) {
            // set thuoc tinh
            department.setName(departmentDTO.getName());
            // update
            departmentRepo.save(department);
        }
        return convert(department);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "department", key = "#id") // khi xoa dư lieu thi xoa cache
    // neu muon xoa nhieu cache khác thì có thể dùng
    // @Caching(evict = {
    // @CacheEvict(cacheNames = "department", key = "#id"), #id xoa theo id
    // @CacheEvict(cacheNames = "department-search", allEntries = true) allEntries = true có nghĩa là xóa hết
    // }
    public void delete(int id) {
        departmentRepo.deleteById(id);
    }


    // luu vao bo nho cache dung @Cacheable
    @Override
    @Cacheable(cacheNames = "department", key = "#id", unless = "#result == null")  // result dai dien cho kq tra ve, neu null thi k luu
//    @Secured({"ROLE_ADMIN", "ROLE_SYSADMIN"})
//    @RolesAllowed({"ROLE_ADMIN", "ROLE_SYSADMIN"})
//    @PreAuthorize("hasRole('ROLE_ADMIN) or hasRole('ROLE_SYSADMIN)") // truoc khi goi vao ham
//    @PostAuthorize() // sau khi return ve
    public DepartmentDTO getById(int id) {
        // Optional
        Department department = departmentRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(department);
    }

    private DepartmentDTO convert(Department department) {
        return new ModelMapper().map(department, DepartmentDTO.class);
    }

    @Override
    @Cacheable(cacheNames = "department-search")
    public PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO) {
        // sap xep theo thu tu ten abc
        Sort sortBy = Sort.by("name").ascending();


        // sap xep theo thuoc tinh
        // neu rong hoac k nhap gi thi sap xep theo asc
        if (StringUtils.hasText(searchDTO.getSortedField())) {
            sortBy = Sort.by(searchDTO.getSortedField()).ascending();
        }

        if (searchDTO.getKeyword() == null)
            searchDTO.setKeyword("");
        if (searchDTO.getCurrentPage() == null)
            searchDTO.setCurrentPage(0);
        if (searchDTO.getSize() == null)
            searchDTO.setSize(5);

        // thiet lap tim kiem theo trang
        PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
        Page<Department> page = departmentRepo.searchName("%" + searchDTO.getKeyword() + "%", pageRequest);

        // convert sang DTO de tra ve view
        PageDTO<List<DepartmentDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<DepartmentDTO> departmentDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());

        pageDTO.setData(departmentDTOs);
        return  pageDTO;
    }
}

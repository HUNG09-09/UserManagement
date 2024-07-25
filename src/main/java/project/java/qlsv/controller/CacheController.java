package project.java.qlsv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;
import project.java.qlsv.dto.ResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    CacheManager cacheManager;

    @GetMapping("/")
    public List<String> getCaches() {
        return cacheManager.getCacheNames().stream().collect(Collectors.toList());
    }

    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("name") String name ) {
        Cache cache = cacheManager.getCache(name);
        if (cache != null)
            cache.clear();
        return ResponseDTO.<Void>builder().status(200).build();
    }
}

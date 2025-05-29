package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.CreateMenuItemFormDataDto;
import com.restaurant.backend.domains.dto.MenuItem.dto.UpdateMenuItemDto;
import com.restaurant.backend.domains.entities.ItemType;
import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.mappers.impl.MenuItemMapper;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.CloudinaryService;
import com.restaurant.backend.services.MenuItemService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final CloudinaryService cloudinaryService;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper, CloudinaryService cloudinaryService) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public MenuItemDto createMenuItem(CreateMenuItemDto dto) {
        MenuItem menuItem = menuItemMapper.mapTo(dto);
        return menuItemMapper.mapFrom(menuItemRepository.save(menuItem));
    }

    @Override
    public MenuItemDto createMenuItem(CreateMenuItemFormDataDto dto) throws IOException {
        MenuItem menuItem = MenuItem.builder()
                .itemType(com.restaurant.backend.domains.entities.ItemType.valueOf(dto.getItemType().name()))
                .itemName(dto.getItemName())
                .itemCprice(dto.getItemCprice())
                .itemSprice(dto.getItemSprice())
                .instock(dto.getInstock())
                .isdeleted(dto.getIsdeleted())
                .build();

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.uploadImage(dto.getImage());
            menuItem.setItemImg(uploadResult.get("url"));
        }
        return menuItemMapper.mapFrom(menuItemRepository.save(menuItem));
    }

    public MenuItemDto getMenuItemById(int id) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        return found.isPresent() ? menuItemMapper.mapFrom(found.get()) : null;
    }

    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .filter(item -> !Boolean.TRUE.equals(item.getIsdeleted()))
                .map(menuItemMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemDto updateMenuItem(int id, UpdateMenuItemDto dto) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return null;

        MenuItem updated = menuItemMapper.mapTo(dto);
        updated.setId(id);
        return menuItemMapper.mapFrom(menuItemRepository.save(updated));
    }

    public MenuItemDto updateMenuItem(int id, UpdateMenuItemDto dto, MultipartFile image) throws IOException {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return null;

        MenuItem item = found.get();
        if (image != null && !image.isEmpty()) {
            // Delete old image if exists
            if (item.getItemImg() != null) {
                String publicId = extractPublicId(item.getItemImg());
                if (publicId != null) {
                    cloudinaryService.deleteImage(publicId);
                }
            }
            // Upload new image
            Map<String, String> uploadResult = cloudinaryService.uploadImage(image);
            item.setItemImg(uploadResult.get("url"));
        }

        MenuItem updated = menuItemMapper.mapTo(dto);
        updated.setId(id);
        updated.setItemImg(item.getItemImg());
        return menuItemMapper.mapFrom(menuItemRepository.save(updated));
    }

    public MenuItemDto partialUpdateMenuItem(int id, UpdateMenuItemDto dto) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return null;

        MenuItem item = found.get();
        if (dto.getItemName() != null) item.setItemName(dto.getItemName());
        if (dto.getItemCprice() != null) item.setItemCprice(dto.getItemCprice());
        if (dto.getItemImg() != null) item.setItemImg(dto.getItemImg());
        if (dto.getInstock() != null) item.setInstock(dto.getInstock());
        if (dto.getIsdeleted() != null) item.setIsdeleted(dto.getIsdeleted());

        return menuItemMapper.mapFrom(menuItemRepository.save(item));
    }

    public boolean softDeleteMenuItem(int id) {
        Optional<MenuItem> found = menuItemRepository.findById(id);
        if (!found.isPresent()) return false;

        MenuItem item = found.get();
        // Delete image from Cloudinary if exists
        if (item.getItemImg() != null) {
            try {
                String publicId = extractPublicId(item.getItemImg());
                if (publicId != null) {
                    cloudinaryService.deleteImage(publicId);
                }
            } catch (IOException e) {
                // Log the error but continue with soft delete
                e.printStackTrace();
            }
        }
        item.setIsdeleted(true);
        menuItemRepository.save(item);
        return true;
    }

    private String extractPublicId(String imageUrl) {
        if (imageUrl == null) return null;
        // Extract public_id from Cloudinary URL
        // Example URL: https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/public_id.jpg
        String[] parts = imageUrl.split("/upload/");
        if (parts.length > 1) {
            String[] remainingParts = parts[1].split("/");
            if (remainingParts.length > 1) {
                return remainingParts[remainingParts.length - 1].split("\\.")[0];
            }
        }
        return null;
    }

    public MenuItem saveEntity(MenuItem entity) {
        return menuItemRepository.save(entity);
    }

    public Optional<MenuItem> findById(int id) {
        return menuItemRepository.findById(id);
    }

}


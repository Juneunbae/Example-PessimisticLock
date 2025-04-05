package com.example.locktest.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void updateItemQuantity(Long id, Integer quantity) {
        Item item = itemRepository.findByIdWithLock(id);

        item.setQuantity(quantity);

        itemRepository.save(item);
    }

    @Transactional
    public Item findItemById(Long id) {
        return itemRepository.findById(id)
            .orElse(null);
    }
}
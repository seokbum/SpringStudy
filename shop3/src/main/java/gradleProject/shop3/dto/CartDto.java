package gradleProject.shop3.dto;

import gradleProject.shop3.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class CartDto {

    private List<ItemSetDto> itemSetList = new ArrayList<>();

    public List<ItemSetDto> getItemSetList() {
        return itemSetList;
    }

    // 같은 아이템이면 수량 누적
    public void addItem(Item item, int quantity) {
        for (ItemSetDto set : itemSetList) {
            if (set.getItem().equals(item)) {
                set.setQuantity(set.getQuantity() + quantity);
                return;
            }
        }
        itemSetList.add(new ItemSetDto(item, quantity));
    }

    public void push(ItemSetDto itemSet) {
        itemSetList.add(itemSet);
    }

    public int getTotal() {
        return itemSetList.stream().mapToInt(set -> set.getItem().getPrice() * set.getQuantity()).sum();
    }
}

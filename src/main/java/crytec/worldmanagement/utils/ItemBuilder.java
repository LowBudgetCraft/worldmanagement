/*
 *
 *  * This file is part of WorldManagement, licensed under the MIT License.
 *  *
 *  *  Copyright (c) crysis992 <crysis992@gmail.com>
 *  *  Copyright (c) contributors
 *  *
 *  *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  of this software and associated documentation files (the "Software"), to deal
 *  *  in the Software without restriction, including without limitation the rights
 *  *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  copies of the Software, and to permit persons to whom the Software is
 *  *  furnished to do so, subject to the following conditions:
 *  *
 *  *  The above copyright notice and this permission notice shall be included in all
 *  *  copies or substantial portions of the Software.
 *  *
 *  *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  SOFTWARE.
 *
 */

package crytec.worldmanagement.utils;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import net.crytec.libs.commons.utils.lang.Validate;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

  private final ItemStack item;
  private final ItemMeta meta;

  public ItemBuilder(Material material) {
    item = new ItemStack(material);
    meta = item.getItemMeta();
  }

  public ItemBuilder(ItemStack item) {
    this.item = item;
    meta = item.getItemMeta();
  }


  public ItemStack build() {
    item.setItemMeta(meta);
    return item;
  }


  public ItemBuilder amount(int amount) {
    item.setAmount(amount);
    return this;
  }


  public ItemBuilder name(String name) {
    meta.setDisplayName(name);
    return this;
  }


  public ItemBuilder lore(String string) {
    List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    lore.add(string);
    meta.setLore(lore);
    return this;
  }


  public ItemBuilder lore(String string, int index) {
    List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    lore.set(index, string);
    meta.setLore(lore);
    return this;
  }


  public ItemBuilder lore(List<String> lore) {
    List<String> newLore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();
    newLore.addAll(lore);
    meta.setLore(newLore);
    return this;
  }


  public ItemBuilder setUnbreakable(boolean unbreakable) {
    meta.setUnbreakable(unbreakable);
    return this;
  }


  public ItemBuilder setDurability(int durability) {
    ((Damageable) meta).setDamage(durability);
    return this;
  }


  public ItemBuilder enchantment(Enchantment enchantment, int level) {
    if (level <= 0) {
      meta.removeEnchant(enchantment);
    } else {
      meta.addEnchant(enchantment, level, true);
    }
    return this;
  }


  public ItemBuilder enchantment(Enchantment enchantment) {
    meta.addEnchant(enchantment, 1, true);
    return this;
  }


  public ItemBuilder type(Material material) {
    item.setType(material);
    return this;
  }


  public ItemBuilder clearLore() {
    meta.setLore(Lists.newArrayList());
    return this;
  }


  public ItemBuilder clearEnchantment() {
    for (Enchantment e : item.getEnchantments().keySet()) {
      item.removeEnchantment(e);
    }
    return this;
  }


  public ItemBuilder setSkullOwner(OfflinePlayer player) {
    Validate.isTrue(item.getType() == Material.PLAYER_HEAD, "skullOwner() only applicable for skulls!");

    SkullMeta meta = (SkullMeta) this.meta;
    meta.setOwningPlayer(player);
    item.setItemMeta(meta);
    return this;
  }


  public ItemBuilder setItemFlag(ItemFlag flag) {
    meta.addItemFlags(flag);
    return this;
  }

  public ItemBuilder setAttribute(Attribute attribute, double amount, EquipmentSlot slot) {
    AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "itembuilder", amount, Operation.ADD_NUMBER, slot);
    meta.addAttributeModifier(attribute, modifier);
    return this;
  }


  public ItemBuilder removeAttribute(Attribute attribute) {
    meta.removeAttributeModifier(attribute);
    return this;
  }
}
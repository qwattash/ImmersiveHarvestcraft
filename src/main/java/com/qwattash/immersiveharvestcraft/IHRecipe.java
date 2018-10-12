/*
 * Copyright (c) 2018 qwattash
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qwattash.immersiveharvestcraft;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

class DummyRecipe
    extends IForgeRegistryEntry.Impl<IRecipe>
    implements IRecipe
{
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
	return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
	return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height)
    {
	return false;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
	return ItemStack.EMPTY;
    }
}

public class IHRecipe
{
    public void remove(String domain, String name)
    {
    	IHLogger.logger.info("Remove recipe for " + domain + ":" + name);
	IForgeRegistry<IRecipe> recipes = ForgeRegistries.RECIPES;
	ResourceLocation targetKey = new ResourceLocation(domain, name);
	Item target = Item.REGISTRY.getObject(targetKey);
	if (target == null) {
	    IHLogger.logger.warn("No such item as " + targetKey.toString());
	    return;
	}
	for (IRecipe rcp : recipes.getValuesCollection()) {
	    ItemStack output = rcp.getRecipeOutput();
	    if (output.isEmpty())
		continue;
	    if (output.getItem() == target) {
		replaceDummyRecipe(recipes, rcp);
	    }
	}
    }

    protected void replaceDummyRecipe(IForgeRegistry registry, IRecipe recipe)
    {
	IRecipe replacement = new DummyRecipe();
	replacement.setRegistryName(recipe.getRegistryName());
	registry.register(replacement);
    }
}

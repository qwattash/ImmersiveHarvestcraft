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

import com.qwattash.immersiveharvestcraft.fluids.IHFluid;
import com.qwattash.immersiveharvestcraft.fluids.IHFluid;
import com.qwattash.immersiveharvestcraft.utils.IHAssetLoader;
import com.qwattash.immersiveharvestcraft.utils.TypedAssetParser;
import com.qwattash.immersiveharvestcraft.utils.IAssetJsonConsumer;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonUtils;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;

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
    /**
     * Custom recipe parser for bottling machine recipes
     */
    public static class BottlingMachineRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	{
	    JsonObject result = JsonUtils.getJsonObject(json, "result");
	    JsonObject input = JsonUtils.getJsonObject(json, "input");
	    JsonObject fluid = JsonUtils.getJsonObject(json, "fluid_input");
	    ResourceLocation resultRes = new ResourceLocation(
		JsonUtils.getString(result, "item"));
	    ResourceLocation inputRes = new ResourceLocation(
		JsonUtils.getString(input, "item"));
	    ResourceLocation fluidRes = new ResourceLocation(
		JsonUtils.getString(fluid, "fluid"));
	    ItemStack outItem = new ItemStack(
		Item.REGISTRY.getObject(resultRes),
		JsonUtils.getInt(result, "count"),
		JsonUtils.getInt(result, "data"));
	    ItemStack inItem = new ItemStack(
		Item.REGISTRY.getObject(inputRes),
		JsonUtils.getInt(input, "count"),
		JsonUtils.getInt(input, "data"));
	    FluidStack inFluid = FluidRegistry.getFluidStack(
		fluidRes.toString(), JsonUtils.getInt(fluid, "count"));
	    BottlingMachineRecipe.addRecipe(outItem, inItem, inFluid);
	    IHLogger.logger.info("Add bottling machine recipe {}", key);
	}
    }

    /**
     * Custom recipe parser for squeezer recipes
     */
    public static class SqueezerRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	{
	    JsonObject result = JsonUtils.getJsonObject(json, "result");
	    JsonObject input = JsonUtils.getJsonObject(json, "input");
	    JsonObject fluid = JsonUtils.getJsonObject(json, "fluid_result");
	    ResourceLocation resultRes = new ResourceLocation(
		JsonUtils.getString(result, "item"));
	    ResourceLocation inputRes = new ResourceLocation(
		JsonUtils.getString(input, "item"));
	    ResourceLocation fluidRes = new ResourceLocation(
		JsonUtils.getString(fluid, "fluid"));
	    ItemStack outItem = new ItemStack(
		Item.REGISTRY.getObject(resultRes),
		JsonUtils.getInt(result, "count"),
		JsonUtils.getInt(result, "data"));
	    ItemStack inItem = new ItemStack(
		Item.REGISTRY.getObject(inputRes),
		JsonUtils.getInt(input, "count"),
		JsonUtils.getInt(input, "data"));
	    FluidStack outFluid = FluidRegistry.getFluidStack(
		fluidRes.toString(), JsonUtils.getInt(fluid, "count"));
	    int energy = JsonUtils.getInt(json, "energy");
	    SqueezerRecipe.addRecipe(outFluid, outItem, inItem, energy);
	    IHLogger.logger.info("Add squeezer machine recipe {}", key);
	}
    }

    /**
     * Custom recipe parser for crusher recipes
     */
    public static class CrusherRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	{
	    JsonObject result = JsonUtils.getJsonObject(json, "result");
	    JsonObject input = JsonUtils.getJsonObject(json, "input");
	    ResourceLocation resultRes = new ResourceLocation(
		JsonUtils.getString(result, "item"));
	    ResourceLocation inputRes = new ResourceLocation(
		JsonUtils.getString(input, "item"));
	    ItemStack outItem = new ItemStack(
		Item.REGISTRY.getObject(resultRes),
		JsonUtils.getInt(result, "count"),
		JsonUtils.getInt(result, "data"));
	    ItemStack inItem = new ItemStack(
		Item.REGISTRY.getObject(inputRes),
		JsonUtils.getInt(input, "count"),
		JsonUtils.getInt(input, "data"));
	    int energy = JsonUtils.getInt(json, "energy");
	    CrusherRecipe.addRecipe(outItem, inItem, energy);
	    IHLogger.logger.info("Add crusher machine recipe {}", key);
	}
    }

    protected static IHAssetLoader loader =
	new IHAssetLoader(
	    "assets/" + ImmersiveHarvestcraft.MODID + "/ierecipes",
	    new TypedAssetParser() {{
		registerTypeConsumer("bottlingmachine",
				     new BottlingMachineRecipeFactory());
		registerTypeConsumer("squeezer", new SqueezerRecipeFactory());
		registerTypeConsumer("crusher", new CrusherRecipeFactory());
	    }});

    @EventBusSubscriber(modid=ImmersiveHarvestcraft.MODID)
    public static class RegistrationHandler
    {
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
	    IHLogger.logger.info("Load custom recipes");
	    IHRecipe.loader.loadAssets();
	}
    }
    
    public static void remove(String domain, String name)
    {
    	IHLogger.logger.info("Remove recipe for " + domain + ":" + name);
	IForgeRegistry<IRecipe> recipes = ForgeRegistries.RECIPES;
	ResourceLocation targetKey = new ResourceLocation(domain, name);
	Item target = Item.REGISTRY.getObject(targetKey);
	if (target == null) {
	    IHLogger.logger.warn("No such item as " + targetKey.toString());
	    return;
	}
	List<IRecipe> toRemove = new ArrayList<IRecipe>();
	for (IRecipe rcp : recipes.getValuesCollection()) {
	    ItemStack output = rcp.getRecipeOutput();
	    if (output.isEmpty())
		continue;
	    if (output.getItem() == target) {
		toRemove.add(rcp);
	    }
	}
	for (IRecipe rcp : toRemove) {
	    replaceDummyRecipe(recipes, rcp);
	}
    }

    public static void remove2(String domain, String name)
    {
	IForgeRegistry<IRecipe> recipes = ForgeRegistries.RECIPES;
	ResourceLocation targetKey = new ResourceLocation(domain, name);
	Item target = Item.REGISTRY.getObject(targetKey);
	ForgeRegistry activeRecipes =
	    RegistryManager.ACTIVE.getRegistry(GameData.RECIPES);

	if (target == null) {
	    IHLogger.logger.warn("No such item as " + targetKey.toString());
	    return;
	}

	for (Map.Entry<ResourceLocation, IRecipe> entry : recipes.getEntries()) {
	    ItemStack output = entry.getValue().getRecipeOutput();
	    if (output.isEmpty())
		continue;
	    if (output.getItem() == target)
		activeRecipes.remove(entry.getKey());
	}
	// JEI
	// JEIAddonPlugin.itemRegistry.removeIngredientsAtRuntime(target);
	IHLogger.logger.info("Remove recipe for " + domain + ":" + name);
    }

    protected static void replaceDummyRecipe(IForgeRegistry registry,
					     IRecipe recipe)
    {
	IRecipe replacement = new DummyRecipe();
	replacement.setRegistryName(recipe.getRegistryName());
	registry.register(replacement);
    }


    protected static Random rand = new Random();
    @SubscribeEvent
    public static void onPlayerUseItem(LivingEntityUseItemEvent.Finish event)
    {
	/*
	 * If this is any of the harvestcraft juice bottles, we now
	 * give back the empty water bottle 90% of the time so that
	 * we don't have to needlessly make tons of glass bottles.
	 */
	if (!(event.getEntity() instanceof EntityPlayer))
	    return;
	Item item = event.getItem().getItem();
	ResourceLocation res = item.getRegistryName();
	if (res.getResourceDomain() != "harvestcraft")
	    return;
	if (res.getResourcePath().endsWith("juiceitem") && rand.nextInt(101) < 90) {
	    ItemStack bottle = new ItemStack(
		Item.getByNameOrId("minecraft:glass_bottle"));
	    event.setResultStack(bottle);
	}
	
    }
}

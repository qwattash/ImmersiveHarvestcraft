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
package com.qwattash.immersiveharvestcraft.recipes;

import com.qwattash.immersiveharvestcraft.ImmersiveHarvestcraft;
import com.qwattash.immersiveharvestcraft.IHLogger;
import com.qwattash.immersiveharvestcraft.fluids.IHFluid;
import com.qwattash.immersiveharvestcraft.utils.IHAssetLoader;
import com.qwattash.immersiveharvestcraft.utils.TypedAssetParser;
import com.qwattash.immersiveharvestcraft.utils.IAssetJsonConsumer;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;

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
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;

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
    // Add Fermenter recipes
    // bait -> ethanol
    // Bee Stuff
    // Add vanilla crafting recipe for these
    // recipes.addShapeless("immersive_harvestcraft_wax", <harvestcraft:beeswaxitem> * 2,
    // 			 [<harvestcraft:waxcombitem>]);
    // recipes.addShapeless("immersive_harvestcraft_honey", <harvestcraft:honeyitem> * 2,
    // 			 [<harvestcraft:honeycombitem>]);
    // Sugar
    // Add vanilla crafting recipe for these
    // recipes.addShapeless("immersive_harvestcraft_sugar", <minecraft:sugar> * 2,
    // 			 [<harvestcraft:beetitem>]);

    /* 
     * Items produced by the bottling machine that when used should return
     * their container to the player.
     */
    protected static Map<ResourceLocation, ItemStack> bottlingMachineReturns =
	new HashMap<ResourceLocation, ItemStack>();
    
    /**
     * Custom recipe parser for bottling machine recipes
     */
    public static class BottlingMachineRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	    throws IngredientMissingException
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
		fluidRes.getResourcePath(), JsonUtils.getInt(fluid, "count"));
	    if (outItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, resultRes);
	    if (inItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, inputRes);
	    if (inFluid == null)
	    	throw new IngredientMissingException(key, fluidRes);
	    BottlingMachineRecipe.addRecipe(outItem, inItem, inFluid);
	    if (JsonUtils.hasField(result, "return_input")) {
		boolean giveBack = JsonUtils.getBoolean(result, "return_input");
		if (giveBack) {
		    bottlingMachineReturns.put(resultRes, inItem);
		}
	    }
	    IHLogger.logger.info("Add bottling machine recipe {}", key);
	}
    }

    /**
     * Custom recipe parser for squeezer recipes
     */
    public static class SqueezerRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	    throws IngredientMissingException
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
		fluidRes.getResourcePath(), JsonUtils.getInt(fluid, "count"));
	    int energy = JsonUtils.getInt(json, "energy");
	    if (outItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, resultRes);
	    if (inItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, inputRes);
	    if (outFluid == null)
	    	throw new IngredientMissingException(key, fluidRes);
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
	    throws JsonParseException
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
	    if (outItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, resultRes);
	    if (inItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, inputRes);
	    CrusherRecipe recipe = CrusherRecipe.addRecipe(outItem, inItem, energy);

	    if (JsonUtils.hasField(json, "secondary")) {
		JsonObject secondary = JsonUtils.getJsonObject(json, "secondary");
		ResourceLocation resultRes2 = new ResourceLocation(
		    JsonUtils.getString(secondary, "item"));
		ItemStack outItem2 = new ItemStack(
		    Item.REGISTRY.getObject(resultRes2),
		    JsonUtils.getInt(secondary, "count"),
		    JsonUtils.getInt(secondary, "data"));
		float chance = JsonUtils.getFloat(secondary, "chance");
		if (outItem2 == ItemStack.EMPTY)
		    throw new IngredientMissingException(key, resultRes);
		if (chance < 0 || chance > 1)
		    throw new InvalidRecipeValueException(
			key, "chance", Float.toString(chance));
		recipe.addToSecondaryOutput(outItem2, chance);
	    }
	    IHLogger.logger.info("Add crusher machine recipe {}", key);
	}
    }

    /**
     * Custom recipe parser for mixer recipes
     */
    public static class MixerRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	    throws JsonParseException
	{
	    JsonObject result = JsonUtils.getJsonObject(json, "result");
	    JsonObject fluid_input = JsonUtils.getJsonObject(json, "fluid_input");
	    JsonArray inputs = JsonUtils.getJsonArray(json, "inputs");
	    ResourceLocation resultRes = new ResourceLocation(
		JsonUtils.getString(result, "fluid"));
	    ResourceLocation fluidRes = new ResourceLocation(
		JsonUtils.getString(fluid_input, "fluid"));
	    FluidStack outFluid = FluidRegistry.getFluidStack(
		resultRes.getResourcePath(), JsonUtils.getInt(result, "count"));
	    FluidStack inFluid = FluidRegistry.getFluidStack(
		fluidRes.getResourcePath(), JsonUtils.getInt(fluid_input, "count"));

	    List<ItemStack> inItems = new ArrayList<ItemStack>();
	    int index = 0;
	    for (JsonElement jelem : inputs) {
		if (!jelem.isJsonObject())
		    throw new InvalidRecipeValueException(
			key, "inputs[" + Integer.toString(index) + "]",
			jelem.toString());		
		JsonObject input = jelem.getAsJsonObject();
		ResourceLocation inputRes = new ResourceLocation(
		    JsonUtils.getString(input, "item"));
		ItemStack inItem = new ItemStack(
		    Item.REGISTRY.getObject(inputRes),
		    JsonUtils.getInt(input, "count"),
		    JsonUtils.getInt(input, "data"));
		if (inItem == ItemStack.EMPTY)
		    throw new IngredientMissingException(key, inputRes);
		index++;
		inItems.add(inItem);
	    }
	    int energy = JsonUtils.getInt(json, "energy");
	    if (inFluid == null)
	    	throw new IngredientMissingException(key, fluidRes);
	    if (outFluid == null)
	    	throw new IngredientMissingException(key, resultRes);
	    MixerRecipe.addRecipe(outFluid, inFluid,
		inItems.toArray(new ItemStack[inItems.size()]), energy);
	    IHLogger.logger.info("Add mixer machine recipe {}", key);
	}
    }

    /**
     * Custom recipe parser for mixer recipes
     */
    public static class RefineryRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	    throws JsonParseException
	{
	    JsonObject result = JsonUtils.getJsonObject(json, "result");
	    JsonObject fluidInput1 = JsonUtils.getJsonObject(json, "fluid_input1");
	    JsonObject fluidInput2 = JsonUtils.getJsonObject(json, "fluid_input2");
	    ResourceLocation resultRes = new ResourceLocation(
		JsonUtils.getString(result, "fluid"));
	    ResourceLocation fluidRes1 = new ResourceLocation(
		JsonUtils.getString(fluidInput1, "fluid"));
	    ResourceLocation fluidRes2 = new ResourceLocation(
		JsonUtils.getString(fluidInput2, "fluid"));
	    FluidStack outFluid = FluidRegistry.getFluidStack(
		resultRes.getResourcePath(), JsonUtils.getInt(result, "count"));
	    FluidStack inFluid1 = FluidRegistry.getFluidStack(
		fluidRes1.getResourcePath(), JsonUtils.getInt(fluidInput1, "count"));
	    FluidStack inFluid2 = FluidRegistry.getFluidStack(
		fluidRes2.getResourcePath(), JsonUtils.getInt(fluidInput2, "count"));
	    int energy = JsonUtils.getInt(json, "energy");
	    if (inFluid1 == null)
	    	throw new IngredientMissingException(key, fluidRes1);
	    if (inFluid2 == null)
	    	throw new IngredientMissingException(key, fluidRes2);
	    if (outFluid == null)
	    	throw new IngredientMissingException(key, resultRes);
	    RefineryRecipe.addRecipe(outFluid, inFluid1, inFluid2, energy);
	    IHLogger.logger.info("Add refinery machine recipe {}", key);
	}
    }

    /**
     * Custom recipe parser for fermenter recipes
     */
    public static class FermenterRecipeFactory implements IAssetJsonConsumer
    {
	public void parseJson(ResourceLocation key, JsonObject json)
	    throws JsonParseException
	{
	    JsonObject result = JsonUtils.getJsonObject(json, "result");
	    JsonObject input = JsonUtils.getJsonObject(json, "input");
	    ResourceLocation resultRes = new ResourceLocation(
		JsonUtils.getString(result, "fluid"));
	    ResourceLocation inputRes = new ResourceLocation(
		JsonUtils.getString(input, "item"));
	    FluidStack outFluid = FluidRegistry.getFluidStack(
		resultRes.getResourcePath(), JsonUtils.getInt(result, "count"));
	    ItemStack inItem = new ItemStack(
		Item.REGISTRY.getObject(inputRes),
		JsonUtils.getInt(input, "count"),
		JsonUtils.getInt(input, "data"));
	    int energy = JsonUtils.getInt(json, "energy");

	    ResourceLocation outItemRes;
	    if (JsonUtils.hasField(json, "item_result")) {
		JsonObject itemResult = JsonUtils.getJsonObject(json, "item_result");
		outItemRes = new ResourceLocation(
		    JsonUtils.getString(itemResult, "item"));
	    } else {
		outItemRes = new ResourceLocation("minecraft", "air");
	    }
	    ItemStack outItem = new ItemStack(
		    Item.REGISTRY.getObject(outItemRes),
		    JsonUtils.getInt(result, "count"),
		    JsonUtils.getInt(result, "data"));
	    
	    if (outItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, outItemRes);
	    if (inItem == ItemStack.EMPTY)
	    	throw new IngredientMissingException(key, inputRes);
	    if (outFluid == null)
	    	throw new IngredientMissingException(key, resultRes);
	    FermenterRecipe.addRecipe(outFluid, outItem, inItem, energy);
	    IHLogger.logger.info("Add fermenter machine recipe {}", key);
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
		registerTypeConsumer("mixer", new MixerRecipeFactory());
		registerTypeConsumer("refinery", new RefineryRecipeFactory());
		registerTypeConsumer("fermenter", new FermenterRecipeFactory());
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

    @EventBusSubscriber(modid=ImmersiveHarvestcraft.MODID)
    public static class RecipeEventHandler
    {
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
	    EntityPlayer player = (EntityPlayer)event.getEntity();
	    Item item = event.getItem().getItem();
	    ResourceLocation res = item.getRegistryName();
	    if (bottlingMachineReturns.containsKey(res)) {
		// && rand.nextInt(101) < 90
		ItemStack toReturn = bottlingMachineReturns.get(res);
		player.addItemStackToInventory(toReturn.copy());
		IHLogger.logger.debug(
		    "Player use item: return {} to player",
		    toReturn.getItem().getRegistryName().toString());
	    }	
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

    protected static void replaceDummyRecipe(IForgeRegistry registry,
					     IRecipe recipe)
    {
	// JEI
	// JEIAddonPlugin.itemRegistry.removeIngredientsAtRuntime(target);
	IRecipe replacement = new DummyRecipe();
	replacement.setRegistryName(recipe.getRegistryName());
	registry.register(replacement);
    }
}

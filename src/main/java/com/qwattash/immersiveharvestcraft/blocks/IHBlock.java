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
package com.qwattash.immersiveharvestcraft.blocks;

import com.qwattash.immersiveharvestcraft.ImmersiveHarvestcraft;
import com.qwattash.immersiveharvestcraft.IHLogger;
import com.qwattash.immersiveharvestcraft.fluids.IHFluid;
import com.qwattash.immersiveharvestcraft.IHConfigManager.IHConfig;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Map;
import java.util.HashMap;

public class IHBlock
{
    // Fluid blocks
    public static Map<ResourceLocation, Block> juiceBlocks =
	new HashMap<ResourceLocation, Block>();

    // Fluid item blocks
    public static Map<ResourceLocation, ItemBlock> juiceItemBlocks =
	new HashMap<ResourceLocation, ItemBlock>();

    @EventBusSubscriber(modid=ImmersiveHarvestcraft.MODID)
    public static class RegistrationHandler
    {
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
	    IHLogger.logger.info("Registering blocks");
	    IForgeRegistry<Block> registry = event.getRegistry();

	    if (IHConfig.compatPresser) {
		// register juice blocks
		IHFluid.juices.forEach((key, value) -> {
			Block block = new JuiceFluidBlock(value);
			block.setCreativeTab(ImmersiveHarvestcraft.modTab);
			juiceBlocks.put(block.getRegistryName(), block);
			registry.register(block);
		    });
	    }
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
	    IHLogger.logger.info("Registering item blocks");
	    IForgeRegistry<Item> registry = event.getRegistry();

	    if (IHConfig.compatPresser)
		// register juice blocks
		juiceBlocks.forEach((key, block) -> {
			ItemBlock iblock = new ItemBlock(block);
			setItemName(iblock, key.getResourcePath());
			juiceItemBlocks.put(iblock.getRegistryName(), iblock);
			registry.register(iblock);
		    });
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerBlockModels(ModelRegistryEvent event)
	{
	    IHLogger.logger.info("Registering block models");

	    if (IHConfig.compatPresser) {
		// Register models for juice fluid blocks
		juiceBlocks.forEach((key, block) -> {
			registerBlockModel(block, 0);
		    });
		// XXX-AM: this is probably useless
		// juiceItemBlocks.forEach((key, iblock) -> {
		// 	registerItemBlockModel(iblock, 0);
		//     });
	    }
	    
	}

	protected static void setItemName(Item iblock, String blockname)
	{
	    iblock.setRegistryName("item" + blockname);
	    iblock.setUnlocalizedName(iblock.getRegistryName().toString());
	}

	@SideOnly(Side.CLIENT)
	protected static void registerBlockModel(Block block, int meta)
	{
	    IHLogger.logger.debug("Register block model for {}[{}]",
				  block.getRegistryName().toString(), meta);
	    ModelResourceLocation mrl = new ModelResourceLocation(
	    	ImmersiveHarvestcraft.MODID + ":" + block.getRegistryName().getResourcePath(),
		"inventory");
	    ModelLoader.setCustomModelResourceLocation(
	    	Item.getItemFromBlock(block), meta, mrl);
	}

	@SideOnly(Side.CLIENT)
	protected static void registerItemBlockModel(ItemBlock iblock, int meta)
	{
	    IHLogger.logger.debug("Register item block model for {}[{}]",
				  iblock.getRegistryName().toString(), meta);
	    ModelLoader.setCustomModelResourceLocation(
	    	iblock, meta, new ModelResourceLocation(iblock.getRegistryName(),
							"inventory"));
	}
    }
}

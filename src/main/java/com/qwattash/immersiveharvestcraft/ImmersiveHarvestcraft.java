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

import com.qwattash.immersiveharvestcraft.IHLogger;
import com.qwattash.immersiveharvestcraft.IHTab;
import com.qwattash.immersiveharvestcraft.recipes.IHRecipe;
import com.qwattash.immersiveharvestcraft.IHConfigManager.IHConfig;
import com.qwattash.immersiveharvestcraft.fluids.IHFluid;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fluids.FluidRegistry;

import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;

@Mod(modid = ImmersiveHarvestcraft.MODID, name = ImmersiveHarvestcraft.NAME,
     version = ImmersiveHarvestcraft.VERSION,
     dependencies = "required-after:forge@[14.23.3.2705,);" +
     "required-after:immersiveengineering@[0.12,);" +
     "required-after:harvestcraft@[1.12,)",
     acceptedMinecraftVersions = "[1.12,1.12.2]")
public class ImmersiveHarvestcraft
{    
    public static final String MODID = "immersiveharvestcraft";
    public static final String NAME = "Immersive Harvestcraft";
    public static final String VERSION = "${version}";

    static
    {
	FluidRegistry.enableUniversalBucket();
    }

    @Mod.Instance(MODID)
    public static ImmersiveHarvestcraft instance = new ImmersiveHarvestcraft();

    public static IHTab modTab = new IHTab();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
	IHLogger.logger = event.getModLog();
	if (IHConfig.compatPresser) {
	    IHFluid.loader.loadAssets();
	}
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
	IHLogger.logger.info("ImmersiveHarvestcraft initialization");
	IHFluid.updateCreativeTabs();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
	if (IHConfig.compatPresser)
	    IHRecipe.remove("harvestcraft", "presser");
	if (IHConfig.compatGrinder)
	    IHRecipe.remove("harvestcraft", "grinder");
	if (IHConfig.disableWell)
	    IHRecipe.remove("harvestcraft", "well");
	if (IHConfig.disableMarket)
	    IHRecipe.remove("harvestcraft", "market");
	if (IHConfig.disableShippingBin)
	    IHRecipe.remove("harvestcraft", "shippingbin");

	// This is replaced by other recipes
	IHRecipe.remove("harvestcraft", "freshwateritem");
    }
}

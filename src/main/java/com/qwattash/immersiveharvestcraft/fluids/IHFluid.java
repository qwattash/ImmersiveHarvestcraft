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
package com.qwattash.immersiveharvestcraft.fluids;

import com.qwattash.immersiveharvestcraft.IHLogger;
import com.qwattash.immersiveharvestcraft.ImmersiveHarvestcraft;
import com.qwattash.immersiveharvestcraft.utils.IHAssetLoader;
import com.qwattash.immersiveharvestcraft.utils.AssetParser;

import com.google.gson.JsonObject;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Map;
import java.util.HashMap;
import java.awt.Color;

public class IHFluid extends AssetParser
{
    public static IHAssetLoader loader =
	new IHAssetLoader(
	    "assets/" + ImmersiveHarvestcraft.MODID + "/fluiddef",
	    new IHFluid());

    public static Map<ResourceLocation, Fluid> juices =
	new HashMap<ResourceLocation, Fluid>();

    public IHFluid()
    {
    	super();
    }

    @Override
    public void parseResourceJson(ResourceLocation key, JsonObject json)
    {
	String colorString = JsonUtils.getString(json, "color");
	Long colorVal = Long.decode(colorString);
	Color color = new Color((int)(long)colorVal, true);
	Fluid fluid = new FluidJuice(key.getResourcePath(), color);
	IHFluid.juices.put(key, fluid);
	FluidRegistry.registerFluid(fluid);
	FluidRegistry.addBucketForFluid(fluid);
	IHLogger.logger.info("Register fluid " + key.toString());
    }

    public static void updateCreativeTabs()
    {
	for (Fluid fluid : juices.values())
	    ImmersiveHarvestcraft.modTab.addBucketForFluid(fluid);
    }
}

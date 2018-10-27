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

import com.qwattash.immersiveharvestcraft.ImmersiveHarvestcraft;
import com.qwattash.immersiveharvestcraft.fluids.FluidJuice;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;
import java.util.ArrayList;

public class IHTab extends CreativeTabs
{
    /* Universal buckets registered by IHFluid */
    protected List<ItemStack> extraBuckets = new ArrayList<ItemStack>();

    public IHTab()
    {
	super(ImmersiveHarvestcraft.MODID);
	MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem()
    {
	FluidStack fstack = FluidRegistry.getFluidStack("fluidapplejuice",
		Fluid.BUCKET_VOLUME);
	return FluidUtil.getFilledBucket(fstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> items)
    {
	super.displayAllRelevantItems(items);
	items.addAll(extraBuckets);
    }

    @SubscribeEvent
    public void onFluidRegistration(FluidRegisterEvent evt)
    {
	FluidStack fstack = FluidRegistry.getFluidStack(evt.getFluidName(),
		Fluid.BUCKET_VOLUME);
	if (fstack.getFluid() instanceof FluidJuice) {
	    IHLogger.logger.debug("Add bucket to creative tabs for {}",
				  evt.getFluidName());
	    extraBuckets.add(FluidUtil.getFilledBucket(fstack));
	}
    }
};

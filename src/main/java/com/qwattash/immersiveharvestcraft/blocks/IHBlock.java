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

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import java.util.Map;
import java.util.HashMap;

public class IHBlock
{
    protected IHFluid fluidManager;

    // Fluid blocks
    public Map<String, Block> juiceBlocks =
	new HashMap<String, Block>();    

    public IHBlock(IHFluid fluids)
    {
	fluidManager = fluids;
	MinecraftForge.EVENT_BUS.register(this);
    }

    public Block[] allBlocks()
    {
	return juiceBlocks.values().toArray(new Block[juiceBlocks.size()]);
    }

    public void addFluidBlocks()
    {
	fluidManager.juices.forEach((key, value) -> {
		Block juiceFluidBlock = new JuiceFluidBlock(value);
		juiceFluidBlock.setCreativeTab(ImmersiveHarvestcraft.modTab);
		juiceBlocks.put(juiceFluidBlock.getRegistryName().toString(),
				juiceFluidBlock);
	    });
    }

    public void addPresserBlocks()
    {}

    public void addGrinderBlocks()
    {}

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
	event.getRegistry().registerAll(allBlocks());
    }
}

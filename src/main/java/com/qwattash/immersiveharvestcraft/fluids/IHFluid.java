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

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Map;
import java.util.HashMap;

public class IHFluid
{   
    protected static Map<String, Integer> juiceColors =
	new HashMap<String, Integer>() {{
	    put("fluidapplejuice", 0xCFC05C);
	    put("fluidcarrotjuice", 0xF17200);
	    put("fluidblackberryjuice", 0x592A2A);
	    put("fluidraspberryjuice", 0xBD3E53);
	    put("fluidblueberryjuice", 0x264474);
	    put("fluidcactusfruitjuice", 0xE03434);
	    put("fluidcherryjuice", 0xA9220A);
	    put("fluidcranberryjuice", 0xAE1E1E);
	    put("fluidgrapejuice", 0x9B26A6);
	    put("fluidkiwijuice", 0xB4EC61);
	    put("fluidlimejuice", 0x7AD47C);
	    put("fluidmangojuice", 0xDA7C7C);
	    put("fluidplumjuice", 0xC562CD);
	    put("fluidpearjuice", 0xAFC25B);
	    put("fluidapricotjuice", 0xE9B271);
	    put("fluidfigjuice", 0xC490E0);
	    put("fluidgrapefruitjuice", 0xD28633);
	    put("fluidpersimmonjuice", 0xEE9725);
	    put("fluidorangejuice", 0xF39B3B);
	    put("fluidpapayajuice", 0xDCB076);
	    put("fluidpeachjuice", 0xD39E7B);
	    put("fluidpomegranatejuice", 0xA13D3D);
	    put("fluidstarfruitjuice", 0xC1EE6C);
	    put("fluidstrawberryjuice", 0xC51A1A);
	    put("fluidoliveoil", 0xB4C561);
	    put("fluidsesameoil", 0x584028);
	    put("fluidfreshmilk", 0xE8DFD9);
	    put("fluidcoconutmilk", 0xF3F0D1);
	    put("fluidsoymilk", 0xC3D4C7);
	    put("fluidfreshwater", 0x7CB6F8);
	    put("fluidbubblywater", 0x395376);
	    put("fluidliquidtofu", 0xD1D1D1);
	}};
    
    public Map<String, Fluid> juices = new HashMap<String, Fluid>();

    public void addJuiceFluids()
    {
	juiceColors.forEach((key, value) -> {
		Fluid fluid = new FluidJuice(key, value);
		IHLogger.logger.info("Register fluid " + key);
		juices.put(key, fluid);
	    });
	registerFluids();
    }

    protected void registerFluids()
    {
	juices.forEach((key, fluid) -> {
		FluidRegistry.registerFluid(fluid);
	    });
    }
}

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
package com.qwattash.immersiveharvestcraft.utils;

import java.util.Map;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonUtils;

import net.minecraftforge.fml.common.ModContainer;

public class TypedAssetParser extends AssetParser
{
    protected Map<String, IAssetJsonConsumer> factories =
	new HashMap<String, IAssetJsonConsumer>();

    public TypedAssetParser()
    {
	super();
    }
    
    @Override
    protected void parseResourceJson(ResourceLocation key, JsonObject json)
    {
	String type = JsonUtils.getString(json, "type");
	IAssetJsonConsumer consumer = factories.get(type);
	if (type == null)
	    throw new JsonParseException("TypedAssetParser found no key 'type'");
	consumer.parseJson(key, json);
    }

    public void registerTypeConsumer(String type, IAssetJsonConsumer consumer)
    {
	factories.put(type, consumer);
    }
}

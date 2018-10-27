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

import com.qwattash.immersiveharvestcraft.IHLogger;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * Parse a custom asset file loaded by the asset loader
 * This is intended to be subclassed for different json
 * parsing.
 */
public class AssetParser
{
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected ModContainer modContainer;

    public AssetParser()
    {
	modContainer = Loader.instance().activeModContainer();
    }

    public void parseResourceFile(Path root, Path path)
    {
	String relative = root.relativize(path).toString();
	IHLogger.logger.debug("Parse asset file {}", relative);

	if (!"json".equals(FilenameUtils.getExtension(path.toString())))
	    return;

	String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
	ResourceLocation key = new ResourceLocation(modContainer.getModId(), name);

	BufferedReader reader = null;
	try {
	    reader = Files.newBufferedReader(path);
	    JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
	    parseResourceJson(key, json);
	}
	catch (JsonParseException e) {
	    IHLogger.logger.error("Parsing error loading asset {} {}",
				  key, e.toString());
	    return;
	}
	catch (IOException e) {
	    IHLogger.logger.error("Couldn't read asset {} from {} {}",
				  key, path, e.toString());
	    return;
	}
	finally {
	    IOUtils.closeQuietly(reader);
	}	
    }

    protected void parseResourceJson(ResourceLocation key, JsonObject json)
    {
	return;
    }
}

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/*
 * Load custom asset files
 */
public class IHAssetLoader
{
    protected String basePath;
    protected ModContainer mod;
    protected AssetParser parser;

    public IHAssetLoader(String base, AssetParser aParser)
    {
	basePath = base;
	parser = aParser;
	mod = Loader.instance().activeModContainer();
    }

    public void loadAssets()
    {
	FileSystem fs = null;
        try {
            File source = mod.getSource();
            Path root = null;
            if (source.isFile()) {
                try {
                    fs = FileSystems.newFileSystem(source.toPath(), null);
                    root = fs.getPath("/" + basePath);
                }
                catch (IOException e)
                {
                    IHLogger.logger.error("Error loading FileSystem from jar: ", e);
                    return;
                }
            }
            else if (source.isDirectory()) {
                root = source.toPath().resolve(basePath);
            }

            if (root == null || !Files.exists(root)) {
		IHLogger.logger.error("Error FileSystem root not found in jar");
		return;
	    }

	    Iterator<Path> itr = null;
	    try {
		itr = Files.walk(root).iterator();
	    }
	    catch (IOException e) {
		IHLogger.logger.error("Error iterating filesystem for: {}", mod.getModId(), e);
		return;
	    }

	    while (itr != null && itr.hasNext()) {
		parser.parseResourceFile(root, itr.next());
            }
        }
        finally
        {
            IOUtils.closeQuietly(fs);
        }
    }
    
}

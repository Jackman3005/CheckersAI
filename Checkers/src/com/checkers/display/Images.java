package com.checkers.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	public static Images singleton = new Images();

	public BufferedImage crown = null;

	private Images() {
		try {
			this.crown = ImageIO.read(new File("crown.png"));
		} catch (IOException e) {
			System.out.println("Failed to load Image");
		}
	}

}

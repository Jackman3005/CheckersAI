package com.checkers.display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	public static Images singleton = new Images();

	public BufferedImage crown = null;
	public BufferedImage blackPiece = null;
	public BufferedImage redPiece = null;
	public BufferedImage blackKingPiece = null;
	public BufferedImage redKingPiece = null;

	private Images() {
		try {
			this.crown = ImageIO.read(new File("crown.png"));
			this.blackPiece = ImageIO.read(new File("blackPiece.png"));
			this.redPiece = ImageIO.read(new File("redPiece.png"));
			this.blackKingPiece = ImageIO.read(new File("blackKingPiece.png"));
			this.redKingPiece = ImageIO.read(new File("redKingPiece.png"));
		} catch (IOException e) {
			System.out.println("Failed to load Image");
		}
	}

}

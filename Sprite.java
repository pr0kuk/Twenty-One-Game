// package org.tides.tutorial;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
public class Sprite {
	private Image image;
	private int _width;
    private int _height;
	public Sprite(String path) {
        BufferedImage sourceImage = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
		this.image = Toolkit.getDefaultToolkit().createImage(sourceImage.getSource());
	}


    public Sprite width(int _width)
    {
        this._width = _width;
        return this;
    }
       public Sprite height(int _height)
    {
        this._height = _height;
        return this;
    }


	public int getWidth() {
		return image.getWidth(null);
	}

	public int getHeight() {
		return image.getHeight(null);
	}
	
	public void draw(Graphics g,int x,int y) {
		g.drawImage(image,x,y,_width,_height,null);
	}
	public void draw(Graphics g,int x,int y, int w, int h) {
		g.drawImage(image,x,y,w,h,null);
	}
    public void center_draw(Graphics g, int x,int y) {
        g.drawImage(image, x-(int)(_width/2), y-(int)(_width/2), _width, _height, null);
    }
}
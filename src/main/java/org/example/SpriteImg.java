package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteImg {

    public int h;
    public int w;
    private int posX;
    private int posY;
    public Image img;
    public String type;
    public boolean isDead;

    public int getH() {
        return h;
    }
    public int getW() {
        return w;
    }
    public int getPosX() {  // alt + insert   or  alt + fn + insert
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }


    public SpriteImg(String type, int w, int h, int posX, int posY, String imgPath){
        this.w = w;
        this.h = h;
        this.posX = posX;
        this.posY = posY;
        this.type = type;

        this.img = new Image(imgPath, getW(), getH(), false,false);
                                    // w, h

        this.isDead = false;
    }
    public void draw(GraphicsContext context){
        context.save();
        context.translate(this.posX, this.posY);
        if (this.type.equals("enemy")){
            context.translate((double)(this.w)/2, (double)(this.h)/2);
            context.rotate(180);
            context.translate(-(double)(this.w)/2, -(double)(this.h)/2);
        }
        context.drawImage(this.img, 0, 0);
        context.restore();

    }
    public Boolean collides(SpriteImg anotherSprite){
        Boolean collisionX = (this.getPosX() < anotherSprite.getPosX() + anotherSprite.getW()) &
                             (this.getPosX() > anotherSprite.getPosX()) ||
                             (this.getPosX() + this.getW() < anotherSprite.getPosX() + anotherSprite.getW()) &
                             (this.getPosX() + this.getW() > anotherSprite.getPosX());

        Boolean collisionY = (this.getPosY() < anotherSprite.getPosY() + anotherSprite.getH()) &
                             (this.getPosY() > anotherSprite.getPosY()) ||
                             (this.getPosY() + this.getH() < anotherSprite.getPosY() + anotherSprite.getH()) &
                             (this.getPosY() + this.getH() > anotherSprite.getPosY());


        return collisionX && collisionY;
    }

    public void moveUp()
    {
      this.posY -= 5;
    }

    public void moveDown()
    {
        this.posY += 5;
    }

    public void moveLeft()
    {
        this.posX -= 5;
    }

    public void moveRight()
    {
        this.posX += 5;
    }
}



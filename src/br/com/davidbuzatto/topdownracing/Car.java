package br.com.davidbuzatto.topdownracing;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.math.Vector2;

/**
 * A racing car.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Car {
    
    private static final double ANGLE_SPEED = 100;
    private static final double MAX_SPEED = 600;
    private static final double MOVE_ACCL = 600;
    
    private Vector2 pos;
    private Vector2 dim;
    private Vector2 vel;
    private double rotation;
    private double speed;
    private Image image;
    
    private EngineFrame e;
    
    public Car( double x, double y, Image image, EngineFrame e ) {
        this.pos = new Vector2( x, y );
        this.image = image;
        this.dim = new Vector2( image.getWidth(), image.getHeight() );
        this.vel = new Vector2();
        this.rotation = -90;
        this.e = e;
    }
    
    public void update( double delta ) {
        
        if ( e.isKeyDown( EngineFrame.KEY_RIGHT ) ) {
            rotation += delta * ANGLE_SPEED;
        }
        
        if ( e.isKeyDown( EngineFrame.KEY_LEFT ) ) {
            rotation -= delta * ANGLE_SPEED;
        }
        
        boolean moving = false;
        
        if ( e.isKeyDown( EngineFrame.KEY_UP ) ) {
            speed += MOVE_ACCL * delta;
            moving = true;
            if ( speed > MAX_SPEED ) {
                speed = MAX_SPEED;
            }
        } else if ( e.isKeyDown( EngineFrame.KEY_DOWN ) ) {
            speed -= MOVE_ACCL * delta;
            moving = true;
            if ( speed < -MAX_SPEED ) {
                speed = -MAX_SPEED;
            }
        }
        
        if ( !moving ) {
            if ( speed < 0 ) {
                speed += MOVE_ACCL * delta;
                if ( speed > 0 ) {
                    speed = 0;
                }
            } else if ( speed > 0 ) {
                speed -= MOVE_ACCL * delta;
                if ( speed < 0 ) {
                    speed = 0;
                }
            }
        }
        
        vel.x = Math.cos( Math.toRadians( rotation ) ) * speed;
        vel.y = Math.sin( Math.toRadians( rotation ) ) * speed;
            
        pos.x += vel.x * delta;
        pos.y += vel.y * delta;
        
    }
    
    public void draw() {
        e.drawImage( image, pos.x, pos.y, dim.x / 2, dim.y / 2, rotation );
    }
    
    public double getCenterX() {
        return pos.x + dim.x / 2;
    }
    
    public double getCenterY() {
        return pos.y + dim.y / 2;
    }
    
}

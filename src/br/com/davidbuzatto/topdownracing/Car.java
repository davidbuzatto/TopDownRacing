package br.com.davidbuzatto.topdownracing;

import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.math.MathUtils;
import br.com.davidbuzatto.jsge.math.Vector2;

/**
 * A racing car.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Car {
    
    private static final double ANGLE_SPEED = 200;
    //private static final double MAX_SPEED = 800;
    private static final double MAX_SPEED = 600;
    private static final double MOVE_ACCL = 600;
    
    private Vector2 pos;
    private Vector2 dim;
    private Vector2 vel;
    private double rotation;
    private double speed;
    private Image image;
    
    private Vector2[] targets;
    private int currentTarget;
    
    private boolean controlled;
    private EngineFrame e;
    
    public Car( double x, double y, Image image, boolean controlled, EngineFrame e ) {
        this.pos = new Vector2( x, y );
        this.image = image;
        this.dim = new Vector2( image.getWidth(), image.getHeight() );
        this.vel = new Vector2();
        this.rotation = -90;
        this.targets = Main.createTargets();
        this.controlled = controlled;
        this.e = e;
    }
    
    public void update( double delta ) {
        
        boolean moving = false;
        double distance = pos.distanceSquare( targets[currentTarget] );
        double changeTargetDistance = 10000;
        
        if ( controlled ) {
            
            if ( e.isKeyDown( EngineFrame.KEY_RIGHT ) ) {
                rotation += delta * ANGLE_SPEED;
            }

            if ( e.isKeyDown( EngineFrame.KEY_LEFT ) ) {
                rotation -= delta * ANGLE_SPEED;
            }

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
            
        } else {
            
            Vector2 t = targets[currentTarget];
            double angleToTarget = Math.atan2( pos.y - t.y, pos.x - t.x );
            double beta = rotation - Math.toDegrees( angleToTarget ) - 180;
            
            if ( Math.sin( Math.toRadians( beta ) ) < 0 ) {
                rotation += 4;
            } else {
                rotation -= 4;
            }
            
            if ( MathUtils.getRandomValue( 0, 100 ) > 30 ) {
                speed += MOVE_ACCL * delta / 2;
            } else {
                speed -= MOVE_ACCL * delta / 2;
            }
            
            moving = true;
            
            if ( speed > MAX_SPEED ) {
                speed = MAX_SPEED;
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
        
        if ( distance < changeTargetDistance ) {
            currentTarget = ( currentTarget + 1 ) % targets.length;
            if ( currentTarget == 0 ) {
                targets = Main.createTargets();
            }
        }
        
    }
    
    public void draw() {
        
        e.drawImage( image, pos.x - dim.x / 2, pos.y - dim.y / 2, dim.x / 2, dim.y / 2, rotation );
        
        //e.fillCircle( pos, 50, EngineFrame.WHITE );
        //e.drawLine( pos, targets[currentTarget], EngineFrame.WHITE );
        
    }
    
    public double getCenterX() {
        return pos.x + dim.x / 2;
    }
    
    public double getCenterY() {
        return pos.y + dim.y / 2;
    }
    
    public double getX() {
        return pos.x;
    }
    
    public double getY() {
        return pos.y;
    }
    
    public void setPos( double x, double y ) {
        pos.x = x;
        pos.y = y;
    }
    
    public boolean checkCollision( Car car ) {
        return CollisionUtils.checkCollisionCircles( pos, 50, car.pos, 50 );
    }
    
}

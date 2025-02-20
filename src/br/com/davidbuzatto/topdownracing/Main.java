package br.com.davidbuzatto.topdownracing;

import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;

/**
 * Top-Down Racing game main class.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
    
    private Car car;
    private Image trackImage;
    private Camera2D camera;
    
    public Main() {
        super ( 800, 800, "Top-Down Racing", 60, false );
    }
    
    @Override
    public void create() {
        setDefaultFontSize( 20 );
        car = new Car( 330, 1000, loadImage( "resources/images/car01.png" ), this );
        trackImage = loadImage( "resources/images/track.png" );
        camera = new Camera2D();
    }
    
    @Override
    public void update( double delta ) {
        car.update( delta );
        if ( isKeyDown( KEY_PAGE_UP ) ) {
            camera.zoom += 0.01;
        } else if ( isKeyDown( KEY_PAGE_DOWN ) ) {
            camera.zoom -= 0.01;
            if ( camera.zoom < 0.1 ) {
                camera.zoom = 0.1;
            }
        }
        updateCamera();
    }
    
    public void updateCamera() {
        
        if ( car.getCenterX() <= getScreenWidth() / 2 ) {
            camera.target.x = getScreenWidth() / 2;
        } else if ( car.getCenterX() >= trackImage.getWidth() - getScreenWidth() / 2 ) {
            camera.target.x = trackImage.getWidth() - getScreenWidth() / 2 ;
        } else {
            camera.target.x = car.getCenterX();
        }
        
        if ( car.getCenterY() <= getScreenHeight() / 2 ) {
            camera.target.y = getScreenHeight() / 2;
        } else if ( car.getCenterY() >= trackImage.getHeight() - getScreenHeight() / 2 ) {
            camera.target.y = trackImage.getHeight() - getScreenHeight() / 2 ;
        } else {
            camera.target.y = car.getCenterY();
        }
        
        camera.offset.x = getScreenWidth() / 2;
        camera.offset.y = getScreenHeight() / 2;
        
    }
    
    @Override
    public void draw() {
        clearBackground( WHITE );
        beginMode2D( camera );
        drawImage( trackImage, 0, 0 );
        car.draw();
        endMode2D();
        drawFPS( 20, 20 );
    }
    
    public static void main( String[] args ) {
        new Main();
    }
    
}

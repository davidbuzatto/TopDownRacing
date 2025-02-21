package br.com.davidbuzatto.topdownracing;

import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.math.MathUtils;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 * Top-Down Racing game main class.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
    
    private Car mainCar;
    private List<Car> cars;
    
    private Image trackImage;
    private Camera2D camera;
    
    public Main() {
        super ( 800, 800, "Top-Down Racing", 60, false );
    }
    
    @Override
    public void create() {
        
        setDefaultFontSize( 20 );
        
        mainCar = new Car( 390, 1000, loadImage( "resources/images/car01.png" ).resize( 0.5 ), true, this );
        
        cars = new ArrayList<>();
        cars.add( new Car( 270, 1000, loadImage( "resources/images/car02.png" ).resize( 0.5 ), false, this ) );
        cars.add( new Car( 330, 1000, loadImage( "resources/images/car03.png" ).resize( 0.5 ), false, this ) );
        cars.add( new Car( 450, 1000, loadImage( "resources/images/car04.png" ).resize( 0.5 ), false, this ) );
        cars.add(  mainCar );
        
        trackImage = loadImage( "resources/images/track.png" );
        
        camera = new Camera2D();
        camera.zoom = 1;
        
    }
    
    @Override
    public void update( double delta ) {
        
        for ( Car c : cars ) {
            c.update( delta );
        }
        
        resolveCarCollision();
        updateCamera();
        
    }
    
    public void updateCamera() {
        
        if ( isKeyDown( KEY_PAGE_UP ) ) {
            camera.zoom += 0.01;
        } else if ( isKeyDown( KEY_PAGE_DOWN ) ) {
            camera.zoom -= 0.01;
            if ( camera.zoom < 0.1 ) {
                camera.zoom = 0.1;
            }
        }
        
        if ( mainCar.getCenterX() <= getScreenWidth() / 2 ) {
            camera.target.x = getScreenWidth() / 2;
        } else if ( mainCar.getCenterX() >= trackImage.getWidth() - getScreenWidth() / 2 ) {
            camera.target.x = trackImage.getWidth() - getScreenWidth() / 2 ;
        } else {
            camera.target.x = mainCar.getCenterX();
        }
        
        if ( mainCar.getCenterY() <= getScreenHeight() / 2 ) {
            camera.target.y = getScreenHeight() / 2;
        } else if ( mainCar.getCenterY() >= trackImage.getHeight() - getScreenHeight() / 2 ) {
            camera.target.y = trackImage.getHeight() - getScreenHeight() / 2 ;
        } else {
            camera.target.y = mainCar.getCenterY();
        }
        
        camera.offset.x = getScreenWidth() / 2;
        camera.offset.y = getScreenHeight() / 2;
        
    }
    
    @Override
    public void draw() {
        
        clearBackground( WHITE );
        
        beginMode2D( camera );
        
        drawImage( trackImage, 0, 0 );
        for ( Car c : cars ) {
            c.draw();
        }
        
        endMode2D();
        
        drawFPS( 20, 20 );
        
    }
    
    private void resolveCarCollision() {
        
        for ( int i = 0; i < cars.size(); i++ ) {
            for ( int j = i+1; j < cars.size(); j++ ) {
                Car c1 = cars.get( i );
                Car c2 = cars.get( j );
                if ( c1.checkCollision( c2 ) ) {
                    double a = Math.atan2( c1.getCenterY() - c2.getCenterY(), c1.getCenterX() - c2.getCenterX() );
                    c1.setPos( c1.getX() + 2 * Math.cos( a ), c1.getY() + 2 * Math.sin( a ) );
                    c2.setPos( c2.getX() + 2 * Math.cos( a + Math.PI ), c2.getY() + 2 * Math.sin( a + Math.PI ) );
                }
            }
        }
        
    }
    
    public static Vector2[] createTargets() {
        int rMin = -100;
        int rMax = 100;
        return new Vector2[]{
            new Vector2( 400 + MathUtils.getRandomValue( rMin, rMax ), 400 + MathUtils.getRandomValue( rMin, rMax ) ),
            new Vector2( 1650 + MathUtils.getRandomValue( rMin, rMax ), 400 + MathUtils.getRandomValue( rMin, rMax ) ),
            new Vector2( 1650 + MathUtils.getRandomValue( rMin, rMax ), 1650 + MathUtils.getRandomValue( rMin, rMax ) ),
            new Vector2( 400 + MathUtils.getRandomValue( rMin, rMax ), 1650 + MathUtils.getRandomValue( rMin, rMax ) ),
            new Vector2( 400 + MathUtils.getRandomValue( rMin, rMax ), 1000 ),
        };
    }
    
    public static void main( String[] args ) {
        new Main();
    }
    
}

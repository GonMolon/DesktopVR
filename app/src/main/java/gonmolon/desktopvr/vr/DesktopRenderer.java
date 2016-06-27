package gonmolon.desktopvr.vr;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;

import gonmolon.desktopvr.R;

public class DesktopRenderer extends VRRenderer {

    private GazePointer pointer;
    private Window window;
    private Sphere bola;

    public Vector3 position;
    public Vector3 leftEyePos;
    public Vector3 rightEyePos;

    public DesktopRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        position = new Vector3(0, 0, 0);
        leftEyePos = new Vector3(position);
        rightEyePos = new Vector3(position);

        getCurrentCamera().setFarPlane(1000);
        try {
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        DirectionalLight light = new DirectionalLight(0f, -1f, -0.5f);
        light.setPower(2f);
        getCurrentScene().addLight(light);

        FloorGenerator.generate(this);
        pointer = new GazePointer(this);
        window = new Window(this, 8, 5f);
        window.setAngularPosition(90, 0, 10);

        bola = new Sphere(1f, 12, 12);
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        bola.setMaterial(material);
        bola.setColor(Color.YELLOW);
        getCurrentScene().addChild(bola);
        bola.setPosition(0, 0, -6);

        Plane plane = new Plane(10, 10, 1, 1);
        material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        plane.setMaterial(material);
        plane.setColor(Color.GREEN);
        plane.setPosition(0, 0, -10);
        //getCurrentScene().addChild(plane);
    }

    public Vector3 getCameraDir() {
        /*
        Vector3 cameraDir = new Vector3();
        mHeadViewQuaternion.fromMatrix(mHeadViewMatrix);
        mHeadViewQuaternion.inverse();
        cameraDir.setAll(0, 0, 1);
        cameraDir.transform(mHeadViewQuaternion);
        cameraDir.inverse();
        return cameraDir;
        */
        Vector3 cameraDir = new Vector3(pointer.getPosition());
        cameraDir.subtract(position);
        return cameraDir;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        super.onNewFrame(headTransform);
        position = new Vector3(rightEyePos);
        position.add(leftEyePos);
        position.divide(2.0f);
        if(pointer != null) {
            pointer.refresh();
            pointer.setClickable(window.isLookingAt());
        }
    }

    @Override
    public void onDrawEye(Eye eye) {
        super.onDrawEye(eye);
        if(eye.getType() == 1) {
            leftEyePos = getCurrentCamera().getPosition();
        } else {
            rightEyePos = getCurrentCamera().getPosition();
        }
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        //getCurrentCamera().setPosition(position);
        super.onRender(elapsedTime, deltaTime);
    }


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void onCardboardTrigger() {

    }
}

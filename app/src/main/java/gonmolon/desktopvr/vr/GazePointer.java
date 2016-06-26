package gonmolon.desktopvr.vr;

import android.graphics.Color;
import android.opengl.Matrix;

import org.rajawali3d.materials.Material;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.primitives.Sphere;

public class GazePointer extends Sphere {

    private DesktopRenderer renderer;
    private float[] absolutePos = new float[4];
    private float[] relativePos = {0, 0, -2, 1.0f};
    private float[] headViewMatrix = new float[16];

    public GazePointer(DesktopRenderer renderer) {
        super(0.01f, 12, 12);
        this.renderer = renderer;
        Material material = new Material();
        material.setColor(Color.GRAY);
        setMaterial(material);
        renderer.getCurrentScene().addChild(this);
    }

    public void refresh(boolean clickable) {
        if(clickable) {
            setScale(2f);
        } else {
            setScale(1.0f);
        }

        Matrix4 matrix = new Matrix4();
        matrix.setAll(renderer.mHeadViewMatrix);
        matrix.inverse().toFloatArray(headViewMatrix);
        Matrix.multiplyMV(absolutePos, 0, headViewMatrix, 0, relativePos, 0);
        setPosition(absolutePos[0], absolutePos[1], absolutePos[2]);
        setLookAt(renderer.getCurrentCamera().getPosition());
    }
}

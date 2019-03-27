package com.cky.a3dtetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.cky.a3dtetris.shape.BaseBlock;
import com.cky.a3dtetris.shape.BlockFactory;
import com.cky.a3dtetris.shape.BlockType;
import com.cky.a3dtetris.shape.Floor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.Matrix.orthoM;


public class GameRenderer implements GLSurfaceView.Renderer {

    private int screenHeight;
    private int screenWidth;
    private float[] projectionMatrix = new float[16];
    private int program;
    private int vPosition;
    private int vNormalPosition;
    private int uLightPosition;
    private int Kd;
    private int Ld;
    private int ModelViewMatrix;
    private int NormalMatrix;
    private int uTextureUnitLocation;
    private int aTextureCoordinatesLocation;
    private int uMatrixLocation;
    private int uColor;

    private static final String verticesShader
            = "attribute vec3 vPosition;            \n"
            + "attribute vec3 vNormalPosition;            \n"
            + "attribute vec2 a_TextureCoordinates; \n"
            + "varying vec2 v_TextureCoordinates; \n"
            + "varying vec3 LightIntensity;  \n"
            + "uniform vec4 LightPosition;  \n"
            + "uniform vec3 Kd;             \n"
            + "uniform vec3 Ld;            \n"
            + "uniform mat4 ModelViewMatrix;\n"
            + "uniform mat3 NormalMatrix;\n"
            + "uniform mat4 uMatrix; \n"
            + "void main(){                         \n"
            + "vec3 tnorm = normalize( NormalMatrix * vNormalPosition); \n"
            + "vec4 eyeCoords = ModelViewMatrix * vec4(vPosition,1.0);\n"
            + "vec3 s = normalize(vec3(LightPosition - eyeCoords));\n"
            + "LightIntensity = Ld * Kd * max( dot( s, tnorm ), 0.5); "
            + " v_TextureCoordinates = a_TextureCoordinates;\n"
            + "   gl_Position = uMatrix * vec4(vPosition,1.0);\n"
            + "}";

    private static final String fragmentShader
            = "precision mediump float;         \n"
            + "uniform vec4 uColor;             \n"
            + "varying vec3 LightIntensity;   \n"
            + "uniform sampler2D u_TextureUnit;\n"
            + "varying vec2 v_TextureCoordinates;"
            + "void main(){                     \n"
            + "   gl_FragColor = vec4(LightIntensity, 1.0) *texture2D(u_TextureUnit, v_TextureCoordinates)* uColor " +
            "+ vec4(0.3, 0.3, 0.3, 1.0) * texture2D(u_TextureUnit, v_TextureCoordinates);        \n"
            + "}";

    private Context context;
    private int blockTexture;
    private int floorTexture;


    public GameRenderer(Context context, int screenHeight, int screenWidth) {
        this.context = context;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        program = createProgram(verticesShader, fragmentShader);
        GLES20.glUseProgram(program);

        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        uMatrixLocation = GLES20.glGetUniformLocation(program, "uMatrix");
        vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        vNormalPosition = GLES20.glGetAttribLocation(program, "vNormalPosition");
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, "a_TextureCoordinates");
        uLightPosition = GLES20.glGetUniformLocation(program, "LightPosition");
        Kd = GLES20.glGetUniformLocation(program, "Kd");
        Ld = GLES20.glGetUniformLocation(program, "Ld");
        ModelViewMatrix = GLES20.glGetUniformLocation(program, "ModelViewMatrix");
        NormalMatrix = GLES20.glGetUniformLocation(program, "NormalMatrix");
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, "u_TextureUnit");
        uColor = GLES20.glGetUniformLocation(program, "uColor");

        // Light
        GLES20.glUniform3f(Kd, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(Ld, 1.0f, 1f, 1.0f);
        GLES20.glUniform4f(uLightPosition, 0.5f, 0.8f, 1f, 1.0f);

//        GLES20.glUniform4f(uColor, 1f, 0f, 0f, 1.0f);
//
//        // set normal line
//        GLES20.glVertexAttribPointer(vNormalPosition, 3, GLES20.GL_FLOAT, false, 0, Utils.getFBVertices(CubeUtil.getCubeNormalPosition()));
//        GLES20.glEnableVertexAttribArray(vNormalPosition);

        // Texture
        blockTexture = loadTexture(context, R.drawable.basic_square);
        floorTexture = loadTexture(context, R.drawable.floor_texture);

//        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation, 2, GLES20.GL_FLOAT, false, 0,
//                Utils.getFBVertices(CubeUtil.getCubeTexturePosition()));
//        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);
//
//        // set shapes` location
//        GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 0,
//                Utils.getFBVertices(CubeUtil.getCubePosition(0.2f, 0, 0,0)));
//        GLES20.glEnableVertexAttribArray(vPosition);
        fallingBlock = BlockFactory.createBlock(BlockType.F, NormalMatrix, ModelViewMatrix, uMatrixLocation, projectionMatrix);

        floor = new Floor(NormalMatrix, ModelViewMatrix, uMatrixLocation, projectionMatrix);

    }

    // todo test code
    private BaseBlock fallingBlock;
    private Floor floor;

    public BaseBlock getFallingBlock() { // todo used to test
        return fallingBlock;
    }

    public Floor getFloor() {
        return floor;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
        if (width > height) {
            // Landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1, 1);
        } else {
            // Portrait or square
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1, 1);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        fallingBlock.refresh(vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, blockTexture);
        GLES20.glUniform1i(uTextureUnitLocation, 0);
        fallingBlock.draw();

        floor.drawFixedBlocks(vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);

        floor.refresh(vPosition, aTextureCoordinatesLocation, vNormalPosition, uColor);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, floorTexture);
        GLES20.glUniform1i(uTextureUnitLocation, 1);
        floor.draw();

    }

    @Deprecated
    private void demoDraw() {
        float[] MVPM = new float[16];
        float[] MM = new float[16];
        float[] VM = new float[16];
        float[] MVM = new float[16];
        Matrix.setIdentityM(MM, 0);
        Matrix.translateM(MM, 0, 0.2f, 0, 0);
        Matrix.rotateM(MM, 0, 45f, 1, 0, 0);
        Matrix.rotateM(MM, 0, 10f, 0, 1, 0);
        Matrix.setLookAtM(VM, 0, 0, 0, 0, -1, -0.5f, -1, 0, 1, 0);
        Matrix.multiplyMM(MVM, 0, VM, 0, MM, 0);
        Matrix.multiplyMM(MVPM, 0, projectionMatrix, 0, MVM, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MVPM, 0);
        GLES20.glUniformMatrix4fv(ModelViewMatrix, 1, false, MM, 0);
        GLES20.glUniformMatrix3fv(NormalMatrix, 1, false, Utils.mat4ToMat3(MM), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        // 加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        // 创建程序
        int program = GLES20.glCreateProgram();
        // 若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            // 向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShader);
            // 向程序中加入片元着色器
            GLES20.glAttachShader(program, pixelShader);
            // 链接程序
            GLES20.glLinkProgram(program);
        }

        return program;
    }

    private int loadShader(int shaderType, String sourceCode) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, sourceCode);
            GLES20.glCompileShader(shader);
        }
        return shader;
    }

    public int loadTexture(Context context, int resourceId) {
        //textureObjectIds用于存储OpenGL生成纹理对象的ID，我们只需要一个纹理
        final int[] textureObjectIds = new int[1];
        //1代表生成一个纹理
        GLES20.glGenTextures(1, textureObjectIds, 0);
        //判断是否生成成功
        if (textureObjectIds[0] == 0) {
            return 0;
        }
        //加载纹理资源，解码成bitmap形式
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            //删除指定的纹理对象
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        //第一个参数代表这是一个2D纹理，第二个参数就是OpenGL要绑定的纹理对象ID，也就是让OpenGL后面的纹理调用都使用此纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        //设置纹理过滤参数，GL_TEXTURE_MIN_FILTER代表纹理缩写的情况，GL_LINEAR_MIPMAP_LINEAR代表缩小时使用三线性过滤的方式，至于过滤方式以后再详解
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //GL_TEXTURE_MAG_FILTER代表纹理放大，GL_LINEAR代表双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //加载实际纹理图像数据到OpenGL ES的纹理对象中，这个函数是Android封装好的，可以直接加载bitmap格式，
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glGenerateMipmap(GL_TEXTURE_2D);
        //现在OpenGL已经完成了纹理的加载，不需要再绑定此纹理了，后面使用此纹理时通过纹理对象的ID即可
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        bitmap.recycle();
        //返回OpenGL生成的纹理对象ID
        return textureObjectIds[0];
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }
}

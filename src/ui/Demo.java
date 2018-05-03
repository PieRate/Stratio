package ui;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.event.MouseAdapter;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Demo {

	// The window handle
	private long window;
	private int mouseX;
	private int mouseY;
	private float unit_center_x;
	private float unit_center_y;
	private float ratio;
	private int squareSpeed;
	private float unit_target_x;
	private float unit_target_y;
	private int screenWidth;
	private int screenHeight;
	private float unit_square_x;
	private float unit_square_y;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1280, 720, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		//set the coordinate of the square
		unit_center_x = 0;
		unit_center_y = 0;
		
		try (MemoryStack stack = stackPush()){
			DoubleBuffer xBuff = stack.mallocDouble(1);
			DoubleBuffer yBuff = stack.mallocDouble(1);
			glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
				if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS){
					glfwGetCursorPos(window, xBuff, yBuff);
					mouseX = (int) Math.floor(xBuff.get(0));
					mouseY = (int) Math.floor(yBuff.get(0));
					float width = ((float) screenWidth)/2f;
					float height = ((float) screenHeight)/2f;
					unit_center_x = (((float)mouseX) - width)/width/unit_square_x;
					unit_center_y = -(((float)mouseY) - height)/height/unit_square_y;
				}
			});
		}
		

		glfwSetWindowSizeCallback(window, (window, width, height) ->{
			screenWidth = width;
			screenHeight = height;
			ratio = ((float) screenWidth) / ((float) screenHeight);
			unit_square_x = 1f / 10f;
			unit_square_y = unit_square_x * ratio;
		});
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);
			
			screenWidth = pWidth.get(0);
			screenHeight = pHeight.get(0);
			ratio = ((float) screenWidth) / ((float) screenHeight);
			
			/* Set the size of the grid coordinate unit
			 * The grid coordinate unit is used to record the location of the objects in game
			 */
			unit_square_x = 1f / 10f;
			unit_square_y = unit_square_x * ratio;
			

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - screenWidth) / 2,
				(vidmode.height() - screenHeight) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
		

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			float square_size = 1;

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			glBegin(GL_QUADS);
			glColor3f(1f,0f,0f);
			glVertex2f(unit_square_x * (unit_center_x + square_size / 2f) , unit_square_y * (unit_center_y + square_size / 2f));
			glColor3f(0f,1f,0f);
			glVertex2f(unit_square_x * (unit_center_x - square_size / 2f) , unit_square_y * (unit_center_y + square_size / 2f));
			glColor3f(0f,0f,1f);
			glVertex2f(unit_square_x * (unit_center_x - square_size / 2f) , unit_square_y * (unit_center_y - square_size / 2f));
			glColor3f(0f,0f,0f);
			glVertex2f(unit_square_x * (unit_center_x + square_size / 2f) , unit_square_y * (unit_center_y - square_size / 2f));
			glEnd();

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	public static void main(String[] args) {
		new Demo().run();
	}

}
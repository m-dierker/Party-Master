package com.partyrock.tools.test;

import java.io.IOException;
import java.net.BindException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.partyrock.comm.serial.SerialCommunicator;

/**
 * Controls a laser with an iPhone
 * @author Matthew
 * 
 */
public class IPhoneDemo extends HttpServlet {
	private Server server;
	private SerialCommunicator comm;

	public IPhoneDemo() {
		String port = "/dev/tty.usbmodemfa131";
		comm = new SerialCommunicator(port);
		comm.connect();

		server = new Server(5000);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(this), "/*");

		try {
			server.start();
			server.join();
		} catch (BindException e) {
			System.out.println("Address already in use - Is another server already running?");
			System.exit(0);
		} catch (Exception e) {
			System.err.println("Error starting the server");
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Favicon check
		if (req.getRequestURI().contains("favicon")) {
			return;
		}

		String degString;
		String msg = "";
		int motor1 = 0, motor2 = 0;

		if ((degString = req.getParameter("motor1")) != null) {
			motor1 = Integer.parseInt(degString);
			if (motor1 < 0) {
				motor1 = 0;
			} else if (motor1 > 180) {
				motor1 = 180;
			}
			msg += getMessage(1, motor1);
		}

		if ((degString = req.getParameter("motor2")) != null) {
			motor2 = Integer.parseInt(degString);
			if (motor2 < 0) {
				motor2 = 0;
			} else if (motor2 > 180) {
				motor2 = 180;
			}
			msg += getMessage(2, motor2);
		}

		System.out.println(motor1 + ", " + motor2);

		if (!msg.trim().equals("")) {
			comm.sendMsg(msg);
		}

		resp.getWriter().print(motor1 + ", " + motor2 + "\n" + msg);

	}

	public String getMessage(int motor, int deg) {
		String msg = "." + motor;
		int tens = deg / 10;
		msg += (char) (tens + 'A');
		msg += "" + deg % 10;
		return msg;
	}

	public static void main(String... args) {
		new IPhoneDemo();
	}
}

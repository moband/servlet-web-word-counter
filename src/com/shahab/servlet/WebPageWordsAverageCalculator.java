package com.shahab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.asynchttpclient.uri.Uri;

import com.shahab.service.WebPageFetcher;
import com.shahab.service.WordAverageLengthCalcService;

import static org.asynchttpclient.Dsl.*;

/**
 * Servlet implementation class WebPageWordsAverageCalculator
 */
@WebServlet(urlPatterns = "/v1/avg-word-len", asyncSupported = true)
public class WebPageWordsAverageCalculator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final int TIMEOUT_VALUE = 60000;
	public static final int THREAD_POOL_SIZE = 10;

	private ExecutorService exec;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WebPageWordsAverageCalculator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		exec = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}

	public void destroy() {
		exec.shutdown();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		URI url = URI.create(request.getParameter("url"));
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
        
		PrintWriter writer = response.getWriter();
		writer.println("Hello ! thread name is : " + Thread.currentThread().getName());
		System.out.println("Hello ! thread name is : " + Thread.currentThread().getName());
		writer.flush();
		AsyncContext asyncContext = request.startAsync();
		asyncContext.addListener(new WebPageWordsAverageListener());
		asyncContext.setTimeout(TIMEOUT_VALUE);

		WebPageFetcher fetcher = new WebPageFetcher();
		WordAverageLengthCalcService calcService = new WordAverageLengthCalcService();
		fetcher.getPageContent(exec, url)
				.thenComposeAsync(content -> calcService.getStringAverageWordsLengthValue(exec, content))
				.thenAccept(v -> {
					try {
						PrintWriter out = asyncContext.getResponse().getWriter();
						out.printf("Average WebPage length is: %f thread: %s", v, Thread.currentThread());
						out.println("");
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();

					} finally {
						asyncContext.complete();
					}
				});

		// asyncContext.complete();
		// asyncContext.start();
		writer.println("Bye ! thread name is : " + Thread.currentThread().getName());
		System.out.println("Bye ! thread name is : " + Thread.currentThread().getName());
		response.getWriter().append("Served at: ").append(request.getContextPath());
		long stopTime = System.currentTimeMillis();
		System.out.printf("Execution Time: %d\n",stopTime - startTime);
		System.out.println("---------------------------");
	}

}

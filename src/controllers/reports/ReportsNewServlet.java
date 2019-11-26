package controllers.reports;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;

@WebServlet("/reports/new")
public class ReportsNewServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public ReportsNewServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("_token", request.getSession().getId());    //セッションスコープの@Idをgetして、リクエストスコープの属性_tokenに代入する
		
		Report r = new Report();                                                //Report型オブジェクトr を生成
		r.setReport_date(new Date(System.currentTimeMillis()));         //r のｾｯﾀｰを通して、本日の日付を取得して格納
		request.setAttribute("report", r);                                       //リクエストスコープの属性reportにオブジェクトrを格納
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp"); //new.jspにフォワード
		rd.forward(request, response);
	}

}
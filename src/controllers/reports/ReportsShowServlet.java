package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;


@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public ReportsShowServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManager em = DBUtil.createEntityManager();                                            //★EntityManager生成
		
		Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));        //リクエストからid パラメータをgetしてint化し、クラスReportの中からidが一致するレコードを探し出す。
		
		em.close();                                                                                              //★EntityManager破棄
		
		request.setAttribute("report", r);                                                                   //属性reportにr を代入
		request.setAttribute("_token", request.getSession().getId());                                //属性_tokenにセッションIdを代入
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp"); //show.jspにフォワード
		rd.forward(request, response);
	}
	
}
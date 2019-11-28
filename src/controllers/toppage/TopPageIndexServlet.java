package controllers.toppage;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public TopPageIndexServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManager em = DBUtil.createEntityManager(); //★EntityManager生成
		
		Employee login_employee = (Employee)request.getSession().getAttribute("login_employee"); //セッション属性login_employeeをEmployee化してlogin_employeeに代入？
		
		int page;                                                                                                       //数 pageを宣言
		
		try{
			page = Integer.parseInt(request.getParameter("page"));                                       //ﾘｸｴｽﾄｽｺｰﾌﾟのﾊﾟﾗﾒｰﾀpageをint化してpageに代入
			
		} catch(Exception e) {                                                                                      //例外が起きた場合は
			page = 1;                                                                                                  //pageに1を代入
			
		}
		
		List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)            //ｸｴﾘgetMyAllReportsを実行し、Report.class型に格納する→代入
			.setParameter("employee", login_employee)      //ﾊﾟﾗﾒｰﾀemployeeにlogin_employeeを代入
			.setFirstResult(15 * (page - 1))                     //ﾊﾟﾗﾒｰﾀFirstResultに、1ページ前までに何件レコードがあったかを代入
			.setMaxResults(15)                                     //MaxResultsに15を代入
			.getResultList();                                         //ResultListを取得
		
		long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)  //ｸｴﾘgeｔMyReportsCountを実行し、Long.class型に格納する→代入
			.setParameter("employee", login_employee)      //ﾊﾟﾗﾒｰﾀemployeeにlogin_employeeを代入
			.getSingleResult();                                      //SingleResultを取得
		
		em.close();                                   //★EntityManager破棄
		
		request.setAttribute("reports", reports);                 //属性reportsにreportsを代入
		request.setAttribute("reports_count", reports_count);//属性reports_countにreports_countを代入
		request.setAttribute("page", page);                        //属性pageにpageを代入
		
		if(request.getSession().getAttribute("flush") != null) { //もしリクエスト属性flushが存在していれば
			request.setAttribute("flush", request.getSession().getAttribute("flush")); //属性flushにセッション属性のflushを上書きする？
			request.getSession().removeAttribute("flush");    //セッション属性flushを削除
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp"); //index.jspにフォワード
		rd.forward(request, response);
	}

}
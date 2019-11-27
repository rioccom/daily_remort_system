package controllers.reports;

import java.io.IOException;

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


@WebServlet("/reports/edit")
public class ReportsEditServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public ReportsEditServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManager em = DBUtil.createEntityManager();               //★EntityManager生成
		
		Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id"))); //idが一致するレコードを探し出してr に代入
		
		em.close();                                                                 //★EntityManager破棄
		
		Employee login_employee = (Employee)request.getSession().getAttribute("login_employee"); //セッションスコープから属性login_employeeをgetしてEmployee化してlogin_employeeに代入
		
		if(login_employee.getId() == r.getEmployee().getId()) {                                //もしlogin_employeeのidが r のIdと同一ならば =>ログイン者とrpoert作成者が同じならば
			request.setAttribute("report", r);                                                    //リクエストの属性reportにr を代入
			request.setAttribute("_token", request.getSession().getId());                 //    〃      属性_tokenにセッションIDを代入
			request.getSession().setAttribute("report_id", r.getId());                       //セッションの属性report_id に r のidを代入
		}
	
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp"); 
		rd.forward(request, response);                                                             //edit.jspにフォワード
	}
	
}
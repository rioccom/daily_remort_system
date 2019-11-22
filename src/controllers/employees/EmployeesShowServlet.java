package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;


@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	public EmployeesShowServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManager em = DBUtil.createEntityManager(); //EntityManager生成★
		
		Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id"))); //Employee型ﾃﾞｰﾀeからidﾊﾟﾗﾒｰﾀを1件取り出す？
		
		em.close();//EntityManager破棄★
		
		request.setAttribute("employee", e); //リクエストスコープの属性employeeにeをセット
		request.setAttribute("_token", request.getSession().getId()); //属性_tokenにidをセット？
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp"); //show.jspにフォワード
		rd.forward(request, response);
	}
	
}

package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;


@WebServlet("/employees/destroy")
public class EmployeesDestroyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public EmployeesDestroyServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String _token = (String)request.getParameter("_token");
		
		if(_token != null && _token.equals(request.getSession().getId())) { //トークンチェック
			
			EntityManager em = DBUtil.createEntityManager(); //★生成
			
			Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id"))); //employee_idが一致するレコードを探す
			e.setDelete_flag(1);
			e.setUpdated_at(new Timestamp(System.currentTimeMillis())); //最終更新時間の上書き
			
			em.getTransaction().begin();
			em.getTransaction().commit(); //保存
			em.close(); //                                                  ★破棄
			request.getSession().setAttribute("flush", "削除が完了しました。");
				
			response.sendRedirect(request.getContextPath() + "/employees/index"); //indexにリダイレクト
		}
		
	}
	
}


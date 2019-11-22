package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;


@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public EmployeesUpdateServlet() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String _token = (String)request.getParameter("_token");

		if(_token != null && _token.equals(request.getSession().getId())) {
			
			EntityManager em = DBUtil.createEntityManager(); //★生成
			
			Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id"))); //employee_idが同一のレコードを探す
			
			//____現在の値と異なる社員番号が入力されていたら重複チェックを行う指定をする_______________
			Boolean code_duplicate_check = true; 
			
			if(e.getCode().equals(request.getParameter("code"))) { //レコードeのCodeがリクエストスコープのcodeと等しければ
				code_duplicate_check = false;
			} else { //. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ..//そうでなければ
				e.setCode(request.getParameter("code"));
			}
		
			//____パスワード欄に入力があったらパスワードの入力値チェックを行う指定をする____________________
			Boolean password_check_flag = true;
			String password = request.getParameter("password");
			
			if(password == null || password.equals("")) { //パスワードを入力していない時は
				password_check_flag = false;
			} else { //. . . . . . . . . . . . . . . . . . . . . . . . . . . .//入力していたら
				e.setPassword(
					EncryptUtil.getPasswordEncrypt(    //暗号化
						password,
						(String)this.getServletContext().getAttribute("salt")
					)
				);
			}
			
			e.setName(request.getParameter("name"));  //各種データをset
			e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
			e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
			e.setDelete_flag(0);
			
			List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag); //エラーチェック
			
			if(errors.size() > 0) { //エラーが一つでもあれば
				em.close(); //破棄★
				
				request.setAttribute("_token", request.getSession().getId());
				request.setAttribute("employee", e);
				request.setAttribute("errors", errors);
				
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
				rd.forward(request, response);
				
			} else {                   //エラーがなければ
				em.getTransaction().begin();
				em.getTransaction().commit();
				em.close(); //破棄★
				request.getSession().setAttribute("flush", "更新が完了しました。");
				
				request.getSession().removeAttribute("employee_id");
				
				response.sendRedirect(request.getContextPath() + "/employees/index");
			}
			
		}
		
	}
	
}
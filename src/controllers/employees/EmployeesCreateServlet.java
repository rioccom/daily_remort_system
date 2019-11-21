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

import models.Employee;                         // [models]------------Employee.java
import models.validators.EmployeeValidator;  // [models]--[validators]--EmployeeValidator.java
import utils.DBUtil;                                 // [utils]---------------DBUtils.java
import utils.EncryptUtil;                            // [utils]---------------EncryptUtils.java


@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
private static final long serialVersionUID = 1L;


public EmployeesCreateServlet() {
	super();
}


protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String _token = (String)request.getParameter("_token");
	if(_token != null && _token.equals(request.getSession().getId())) { //------------ｾｷｭﾘﾃｨ対策用
		EntityManager em = DBUtil.createEntityManager(); //★utils/DBUtil.javaのEntityManager生成
		
		Employee e = new Employee(); //-------------------------------models/Employee型ﾃﾞｰﾀのeを生成
		
		e.setCode(request.getParameter("code")); //----------------------Employeeｸﾗｽﾞで定義したsetterのﾒｿｯﾄﾞなど。
		e.setName(request.getParameter("name")); //                                    ※request.getParameter("HTMLのname属性の値");
		e.setPassword(                                     //                                    ※属性は、オブジェクトなどの多数の要素で、要素の名前を指定します。
			EncryptUtil.getPasswordEncrypt(         //---------------------EncryptUtilｸﾗｽで定義した暗号化ﾒｿｯﾄﾞ
				request.getParameter("password"), //--------------------リクエストスコープからﾃﾞｰﾀを取得
				(String)this.getServletContext().getAttribute("salt") //-------属性値saltを取得
				)
			);
		e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag"))); //eのﾊﾟﾗﾒｰﾀadmin_flagを取得
		
		Timestamp currentTime = new Timestamp(System.currentTimeMillis()); //Timestamp型ﾃﾞｰﾀのcurrentTimeを生成
		e.setCreated_at(currentTime); //---------------------------------eの各ﾊﾟﾗﾒｰﾀをset。
		e.setUpdated_at(currentTime);
		e.setDelete_flag(0);
		
		List<String> errors = EmployeeValidator.validate(e, true, true); //------models/validators/Employeevalidato.javaのvalidateﾒｿｯﾄﾞ
		if(errors.size() > 0) { //validateエラーあれば
			em.close(); //★EntityManager破棄
			request.setAttribute("_token", request.getSession().getId());  //getId()...セッションIDを取得
			request.setAttribute("employee", e);                               //setAttribute...リクエストスコープに各値をset
			request.setAttribute("errors", errors);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp"); 
			rd.forward(request, response); //                                       new.jspにフォワード
			
		} else { //valdateエラーなければ
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit(); //保存
			em.close();//★EntityManager破棄
			request.getSession().setAttribute("flush", "登録が完了しました。");  //リクエストの属性flushに文言をset
			response.sendRedirect(request.getContextPath() + "/employees/index"); //indexにリダイレクト
		}
	}
}

}